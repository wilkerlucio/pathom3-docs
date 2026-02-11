(ns com.wsscode.pathom3.docs.demos.tutorials.hacker-news-scrapper
  (:require [clojure.string :as str]
            [com.wsscode.misc.coll :as coll]
            [com.wsscode.pathom3.connect.built-in.resolvers :as pbir]
            [com.wsscode.pathom3.connect.indexes :as pci]
            [com.wsscode.pathom3.connect.operation :as pco]
            [com.wsscode.pathom3.interface.eql :as p.eql]
            [com.wsscode.pathom3.interface.smart-map :as psm]
            [hickory.core :as hc]
            [hickory.select :as hs]))

; region helpers

(defn find-text
  "Given an element, traverse the contents until it reaches some text."
  [el]
  (loop [item (first (:content el))]
    (if (string? item)
      item
      (if-let [next (some-> item :content first)]
        (recur next)
        nil))))

(defn class-text
  "Get the text for a given element that matches a css class."
  [el class]
  (->> (hs/select (hs/class class) el)
       first
       (find-text)))

(defn select-number
  "Extract the first integer from a string."
  [x]
  (if-let [[_ n] (re-find #"(\d+)" (str x))]
    (Integer/parseInt n)
    0))

; endregion

; region extractors

(defn extract-item-from-hickory [el]
  {:hacker-news.item/age            (class-text el "age")
   :hacker-news.item/author-name    (class-text el "hnuser")
   :hacker-news.item/comments-count (->> (hs/select (hs/find-in-text #"comments$") el)
                                         first
                                         (find-text)
                                         (select-number))
   :hacker-news.item/score          (select-number (class-text el "score"))
   :hacker-news.item/id             (->> el :content first :attrs :id)
   :hacker-news.item/rank-in-page   (select-number (class-text el "rank"))
   :hacker-news.item/source         (class-text el "sitestr")
   :hacker-news.item/title          (class-text el "titleline")
   :hacker-news.item/url            (->> (hs/select (hs/class "titleline") el)
                                         first :content first :attrs :href)})

; endregion

; region news page

(pco/defresolver news-page-html-string [{:hacker-news.page/keys [news-page-url]}]
  {::pco/cache-store ::durable-cache*
   ::pco/input       [(pco/? :hacker-news.page/news-page-url)]}
  {:hacker-news.page/news-raw-html
   (slurp (or news-page-url "https://news.ycombinator.com/news"))})

(pco/defresolver news-page-hickory [{:hacker-news.page/keys [news-raw-html]}]
  {:hacker-news.page/news-hickory
   (-> news-raw-html
       (hc/parse)
       (hc/as-hickory))})

(pco/defresolver news-page [{:hacker-news.page/keys [news-hickory]}]
  {::pco/output
   [{:hacker-news.page/news
     [:hacker-news.item/age
      :hacker-news.item/author-name
      :hacker-news.item/id
      :hacker-news.item/comments-count
      :hacker-news.item/score
      :hacker-news.item/rank-in-page
      :hacker-news.item/source
      :hacker-news.item/title
      :hacker-news.item/url]}]}
  {:hacker-news.page/news
   (->> news-hickory
        (hs/select (hs/id "hnmain"))
        first
        (hs/select (hs/and
                     (hs/tag "tr")
                     (hs/not (hs/or
                               (hs/class "spacer")
                               (hs/class "morespace")))))
        (partition 2)
        (mapv #(hash-map :type :element :tag :tbody :content (vec %)))
        (mapv extract-item-from-hickory))})

(pco/defresolver news-next-page [{:hacker-news.page/keys [news-hickory]}]
  {::pco/output
   [{:hacker-news.page/news-next-page
     [:hacker-news.page/news-page-url]}]}

  (let [link (some->> news-hickory
               (hs/select (hs/class "morelink"))
               first :attrs :href)]
    (if link
      {:hacker-news.page/news-next-page
       {:hacker-news.page/news-page-url
        (str "https://news.ycombinator.com/" link)}})))

(pco/defresolver all-news-pages [input]
  {::pco/input  [{:hacker-news.page/news
                  [:hacker-news.item/age
                   :hacker-news.item/author-name
                   :hacker-news.item/id
                   :hacker-news.item/comments-count
                   :hacker-news.item/score
                   :hacker-news.item/rank-in-page
                   :hacker-news.item/source
                   :hacker-news.item/title
                   :hacker-news.item/url]}
                 {:hacker-news.page/news-next-page '...}]
   ::pco/output [{:hacker-news.page/news-all-pages
                  [:hacker-news.item/age
                   :hacker-news.item/author-name
                   :hacker-news.item/id
                   :hacker-news.item/comments-count
                   :hacker-news.item/score
                   :hacker-news.item/rank-in-page
                   :hacker-news.item/source
                   :hacker-news.item/title
                   :hacker-news.item/url]}]}
  (clojure.pprint/pprint input)
  {:hacker-news.page/news-all-pages
   (->> input
        (tree-seq :hacker-news.page/news-next-page
          (comp vector :hacker-news.page/news-next-page))
        (into [] (mapcat :hacker-news.page/news)))})

(comment
  ; WARNING: this can take really long time to finish, but you effectively scrape all
  ; the news, with comments and user details with this one:
  (time
    (p.eql/process env
      {:hacker-news.page/news-page-url "https://news.ycombinator.com/news?p=18"}
      [{:hacker-news.page/news
        [:hacker-news.item/age
         :hacker-news.item/author-name
         :hacker-news.item/id
         :hacker-news.item/comments-count
         :hacker-news.item/score
         :hacker-news.item/rank-in-page
         :hacker-news.item/source
         :hacker-news.item/title
         :hacker-news.item/url

         :hacker-news.user/id
         :hacker-news.user/karma
         :hacker-news.user/join-date

         {:hacker-news.item/comments
          [:hacker-news.comment/author-name
           :hacker-news.comment/age
           :hacker-news.comment/content
           :hacker-news.comment/id

           ;:hacker-news.user/id
           ;:hacker-news.user/karma
           ;:hacker-news.user/join-date

           {:hacker-news.comment/responses '...}]}]}])))

; endregion

; region past page

(defn extract-items-from-list [hickory]
  (let [tbody (->> hickory
                   (hs/select (hs/id "hnmain"))
                   first
                   (hs/select (hs/tag "tbody"))
                   first)
        tbody (update tbody :content
                #(into []
                       (drop-while
                         (fn [e]
                           (let [cls (some-> e :attrs :class)]
                             (not= cls "athing"))))
                       %))]

    (->> tbody
         (hs/select (hs/and
                      (hs/tag "tr")
                      (hs/not (hs/or
                                (hs/class "spacer")
                                (hs/class "morespace")))))
         (partition 2)
         (mapv #(hash-map :type :element :tag :tbody :content (vec %)))
         (mapv extract-item-from-hickory))))

(defn extract-more-link [hickory]
  (some->> hickory
    (hs/select (hs/class "morelink"))
    first :attrs :href))

(defn hn-page-resolvers [page-name base-url]
  (let [hickory-sym   (symbol "hacker-news.resolvers.page" (str page-name "-hickory"))
        page-sym      (symbol "hacker-news.resolvers.page" page-name)
        next-page-sym (symbol "hacker-news.resolvers.page" (str page-name "-next-page"))

        hickory-kw    (keyword "hacker-news.page" (str page-name "-hickory"))
        page-kw       (keyword "hacker-news.page" page-name)
        next-page-kw  (keyword "hacker-news.page" (str page-name "-next-page"))
        page-url-kw   (keyword "hacker-news.page" (str page-name "-page-url"))]
    [(pco/resolver hickory-sym
       {::pco/cache-store ::durable-cache*
        ::pco/input       [(pco/? page-url-kw)]
        ::pco/output      [hickory-kw]}
       (fn [_ input]
         {hickory-kw
          (-> (or (get input page-url-kw) base-url)
              slurp
              (hc/parse)
              (hc/as-hickory))}))

     (pco/resolver page-sym
       {::pco/input
        [hickory-kw]

        ::pco/output
        [{page-kw
          [:hacker-news.item/age
           :hacker-news.item/author-name
           :hacker-news.item/id
           :hacker-news.item/comments-count
           :hacker-news.item/score
           :hacker-news.item/rank-in-page
           :hacker-news.item/source
           :hacker-news.item/title
           :hacker-news.item/url]}]}
       (fn [_ input]
         {page-kw
          (extract-items-from-list (get input hickory-kw))}))

     (pco/resolver next-page-sym
       {::pco/input
        [hickory-kw]

        ::pco/output
        [{next-page-kw
          [page-url-kw]}]}
       (fn [_ input]

         (if-let [link (extract-more-link (get input hickory-kw))]
           {next-page-kw
            {page-url-kw
             (str "https://news.ycombinator.com/" link)}})))]))

(comment
  (hn-page-resolvers "past" "https://news.ycombinator.com/front"))

(comment
  (tap> (psm/smart-map env))

  (p.eql/process env
    [{:hacker-news.page/past
      [:hacker-news.item/id
       :hacker-news.item/title]}]))

; endregion

; region comments resolvers

(pco/defresolver item-page-hickory [{:hacker-news.item/keys [id]}]
  {::pco/cache-store ::durable-cache*}
  {:hacker-news.page/item-hickory
   (->> (slurp (str "https://news.ycombinator.com/item?id=" id))
        hc/parse hc/as-hickory)})

(pco/defresolver item-data [{:hacker-news.page/keys [item-hickory]}]
  {::pco/output
   [:hacker-news.item/age
    :hacker-news.item/author-name
    :hacker-news.item/comments-count
    :hacker-news.item/score
    :hacker-news.item/source
    :hacker-news.item/title
    :hacker-news.item/url]}
  (extract-item-from-hickory (->> (hs/select (hs/class "fatitem") item-hickory)
                                  first)))

(defn comment-ident-level [el]
  (-> (hs/select (hs/and (hs/tag "img") (hs/attr "src" #{"s.gif"})) el)
      first :attrs :width select-number (/ 40)))

(defn extract-comment [el]
  {:hacker-news.comment/id          (-> el :attrs :id)
   :hacker-news.comment/age         (class-text el "age")
   :hacker-news.comment/author-name (class-text el "hnuser")
   :hacker-news.comment/indent      (comment-ident-level el)
   :hacker-news.comment/content     (->> (hs/select (hs/class "comment") el)
                                         first :content (keep find-text) (str/join "\n"))})

(pco/defresolver item-comments-flat [{:hacker-news.page/keys [item-hickory]}]
  {::pco/output [{:hacker-news.item/comments-flat
                  [:hacker-news.comment/author-name
                   :hacker-news.comment/age
                   :hacker-news.comment/content
                   :hacker-news.comment/id
                   :hacker-news.comment/indent]}]}
  {:hacker-news.item/comments-flat
   (->> item-hickory
        (hs/select (hs/class "comtr"))
        (mapv extract-comment))})

(comment
  (p.eql/process env
    {:hacker-news.item/id "25733200"}
    [{:hacker-news.item/comments-flat
      [:hacker-news.comment/author-name]}]))

(pco/defresolver item-comments [{:hacker-news.item/keys [comments-flat]}]
  {::pco/output
   [{:hacker-news.item/comments
     [:hacker-news.comment/author-name
      :hacker-news.comment/age
      :hacker-news.comment/content
      :hacker-news.comment/id
      {:hacker-news.comment/responses '...}]}]}
  (let [{:keys [roots index]}
        (->> comments-flat
             (reduce
               (fn [{:keys [level prev] :as acc} {:hacker-news.comment/keys [id indent] :as comment}]
                 (let [{:keys [stack] :as acc} (cond-> acc
                                                 (> indent level)
                                                 (update :stack conj prev)

                                                 (< indent level)
                                                 (update :stack pop))
                       stack-head (peek stack)]
                   (cond-> acc
                     true
                     (-> (update :index assoc id comment)
                         (assoc :level indent)
                         (assoc :prev id))

                     (zero? indent)
                     (update :roots conj comment)

                     (pos? indent)
                     (update-in [:index stack-head :hacker-news.comment/responses]
                       coll/vconj {:hacker-news.comment/id id}))))
               {:stack []
                :roots []
                :index {}
                :level 0
                :prev  nil}))]
    (if (seq index)
      (->> (p.eql/process
             (pci/register [(pbir/static-table-resolver :hacker-news.comment/id index)
                            (pbir/constantly-resolver :roots roots)])
             [{:roots [:hacker-news.comment/author-name
                       :hacker-news.comment/age
                       :hacker-news.comment/content
                       :hacker-news.comment/id
                       {:hacker-news.comment/responses '...}]}])
           :roots
           (hash-map :hacker-news.item/comments))
      {:hacker-news.item/comments []})))

(comment
  (p.eql/process env
    {:hacker-news.item/id "25733200"}
    [{:hacker-news.item/comments
      [:hacker-news.comment/author-name]}]))

; endregion

; region user resolvers

(pco/defresolver user-data-hickory [{:keys [hacker-news.user/id]}]
  {::pco/cache-store ::durable-cache*}
  {:hacker-news.page/user-hickory
   (try
     (some-> (slurp (str "https://news.ycombinator.com/user?id=" id))
       hc/parse hc/as-hickory)
     (catch Throwable _ nil))})

(pco/defresolver user-data [{:hacker-news.page/keys [user-hickory]}]
  {::pco/output [:hacker-news.user/karma :hacker-news.user/join-date]}
  (if user-hickory
    {:hacker-news.user/karma
     (->> user-hickory
          (hs/select
            (hs/and
              (hs/tag "tr")
              (hs/has-child (hs/find-in-text #"karma:"))))
          first :content second :content select-number)

     :hacker-news.user/join-date
     (let [str (->> user-hickory
                    (hs/select
                      (hs/and
                        (hs/tag "tr")
                        (hs/has-child (hs/find-in-text #"created:"))))
                    first :content second :content first :attrs :href)
           [_ date] (re-find #"(\d{4}-\d{2}-\d{2})" str)]
       date)}))

; endregion

(defonce cache* (atom {}))

(comment
  (keys @cache*))

(def env
  (-> {::durable-cache* cache*}
      (pci/register
        [(pbir/alias-resolver :hacker-news.item/author-name :hacker-news.user/id)
         (pbir/alias-resolver :hacker-news.comment/author-name :hacker-news.user/id)
         news-page-html-string
         news-page-hickory
         news-page
         news-next-page
         all-news-pages

         ; this is different than news, news is the home, newest are the most recent
         (hn-page-resolvers "newest" "https://news.ycombinator.com/newest")
         (hn-page-resolvers "past" "https://news.ycombinator.com/front")
         (hn-page-resolvers "ask" "https://news.ycombinator.com/ask")
         (hn-page-resolvers "show" "https://news.ycombinator.com/show")

         user-data-hickory
         user-data

         item-page-hickory
         item-data
         item-comments-flat
         item-comments])))

(comment
  (keys @cache*)
  (reset! cache* {})

  (p.eql/process env
    {:hacker-news.user/id "wilkerlucios"}
    [:hacker-news.user/karma
     :hacker-news.user/join-date]))

(comment
  (com.wsscode.pathom3.interface.smart-map/smart-map
    com.wsscode.pathom3.docs.demos.tutorials.hacker-news-scrapper/env
    *v)
  ; get the title of all the news
  (p.eql/process env
    [{:hacker-news.page/news
      [:hacker-news.item/id
       :hacker-news.item/title]}
     {:hacker-news.page/news-next-page
      3}]))

(comment
  (->> (p.eql/process env
         [{:hacker-news.page/news
           [:hacker-news.item/author-name
            :hacker-news.user/karma]}])
       :hacker-news.page/news
       (sort-by :hacker-news.user/karma #(compare %2 %))))

(comment
  ; get titles from first and second page
  (p.eql/process env
    [{:hacker-news.page/news
      [:hacker-news.item/title]}
     {:hacker-news.page/news-next-page
      [{:hacker-news.page/news
        [:hacker-news.item/title]}]}]))

(comment
  (p.eql/process env
    [{:hacker-news.page/news
      [:hacker-news.item/title]}
     ; recurse bounded to 3 steps
     {:hacker-news.page/news-next-page 3}]))

(comment
  (->> (p.eql/process env
         [{:hacker-news.page/news
           [:hacker-news.item/title]}
          ; recurse bounded to 3 steps
          {:hacker-news.page/news-next-page 3}])
       (tree-seq :hacker-news.page/news-next-page
         ; we need vector at the end because tree-seq expects children to be a collection
         (comp vector :hacker-news.page/news-next-page))
       ; mapcat the news to have a single flat list
       (into [] (mapcat :hacker-news.page/news))))

(comment
  (psm/smart-map env {})

  (reset! cache* {})

  (->> (p.eql/process env
         [{:hacker-news.page/news
           [:hacker-news.item/author-name
            :hacker-news.user/karma]}])
       :hacker-news.page/news
       (sort-by :hacker-news.user/karma #(compare %2 %)))

  (p.eql/process env
    [{:hacker-news.page/news
      [:hacker-news.item/author-name]}])

  (->> (p.eql/process env
         [{:hacker-news.page/news
           [:hacker-news.item/title]}
          {:hacker-news.page/news-next-page '...}])
       (tree-seq :hacker-news.page/news-next-page
         (comp vector :hacker-news.page/news-next-page))
       (into [] (mapcat :hacker-news.page/news)))

  (->> (p.eql/process env
         #_{:hacker-news.page/news-page-url "https://news.ycombinator.com/news?p=18"}
         [{:hacker-news.page/news
           [#_:hacker-news.item/title
            :hacker-news.item/author-name
            :hacker-news.author/karma]}
          {:hacker-news.page/news-next-page 3}])
       (tree-seq :hacker-news.page/news-next-page
         (comp vector :hacker-news.page/news-next-page))
       (into [] (mapcat :hacker-news.page/news)))

  (->> (p.eql/process env
         [{:hacker-news.page/news-all-pages
           [:hacker-news.item/title]}]))

  (-> (p.eql/process env
        {:hacker-news.item/id "25713842"}
        [:hacker-news.item/title
         {:hacker-news.item/comments
          [:hacker-news.comment/author-name]}])))

(comment
  (->> (p.eql/process env
         [{:hacker-news.page/news-all-pages
           [:hacker-news.item/title]}])))

(comment
  (-> (psm/smart-map env {:hacker-news.item/id "25733200"})
      :hacker-news.item/title)

  (-> (psm/smart-map env {:hacker-news.item/id "25733200"})
      :hacker-news.user/karma)

  (p.eql/process env
    {:hacker-news.comment/author-name "wilkerlucios"}
    [:hacker-news.user/join-date])

  (-> (psm/smart-map env {:hacker-news.item/id "25733200"})
      :hacker-news.item/comments
      first
      (select-keys [:hacker-news.comment/author-name
                    :hacker-news.comment/content
                    :hacker-news.user/join-date]))

  )
