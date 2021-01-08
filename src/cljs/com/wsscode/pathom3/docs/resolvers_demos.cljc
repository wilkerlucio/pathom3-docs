(ns com.wsscode.pathom3.docs.resolvers-demos
  (:require [clojure.string :as str]
            [com.wsscode.pathom3.connect.built-in.resolvers :as pbir]
            [com.wsscode.pathom3.connect.indexes :as pci]
            [com.wsscode.pathom3.connect.operation :as pco]
            [com.wsscode.pathom3.interface.eql :as p.eql]
            [com.wsscode.pathom3.interface.smart-map :as psm]
            [com.wsscode.pathom3.entity-tree :as p.ent]
            [clojure.data.json :as json]
            [com.fulcrologic.guardrails.core :refer [<- => >def >defn >fdef ? |]]
            [com.wsscode.pathom3.format.eql :as pf.eql]))

;; what is a resolver?

(def user-birthdays-map
  {1 1969
   2 1954
   3 1986})

(pco/defresolver user-birthday [{:keys [acme.user/id]}]
  {:acme.user/birth-year (get user-birthdays-map id)})

; next

; define a map for indexed access to user data
(def users-db
  {1 #:acme.user{:name     "Usuario 1"
                 :email    "user@provider.com"
                 :birthday "1989-10-25"}
   2 #:acme.user{:name     "Usuario 2"
                 :email    "anuser@provider.com"
                 :birthday "1975-09-11"}})

; pull stored user info from id
(pco/defresolver user-by-id [{:keys [acme.user/id]}]
  {::pco/output
   [:acme.user/name
    :acme.user/email
    :acme.user/birthday]}
  (get users-db id))

; extract birth year from birthday
(pco/defresolver birth-year [{:keys [acme.user/birthday]}]
  {:acme.user/birth-year (first (str/split birthday #"-"))})

;; defresolver

; this resolver computes a slug to use on URL from some article title
(pco/defresolver article-slug [env {:acme.article/keys [title]}]
  {::pco/input  [:acme.article/title]
   ::pco/output [:acme.article/slug]}
  {:acme.article/slug (str/replace title #"[^a-z0-9A-Z]" "-")})

;; invoking resolvers

(def user-from-id
  (pbir/static-table-resolver `user-db :acme.user/id
    {1 #:acme.user{:name  "Trey Parker"
                   :email "trey@provider.com"}
     2 #:acme.user{:name  "Matt Stone"
                   :email "matt@provider.com"}}))

; avatar slug is a version of email, converting every non letter character into dashes
(pco/defresolver user-avatar-slug [{:acme.user/keys [email]}]
  {:acme.user/avatar-slug (str/replace email #"[^a-z0-9A-Z]" "-")})

(pco/defresolver user-avatar-url [{:acme.user/keys [avatar-slug]}]
  {:acme.user/avatar-url (str "http://avatar-images-host/for-id/" avatar-slug)})

(-> {:acme.user/id 1}
    (user-from-id)
    (user-avatar-slug)
    (user-avatar-url)
    :acme.user/avatar-url)
; => "http://avatar-images-host/for-id/trey-provider-com"

(def indexes
  (pci/register [user-from-id
                 user-avatar-slug
                 user-avatar-url]))

; now instead of reference the functions, we let Pathom figure them out using the indexes
(->> {:acme.user/id 2}
     (psm/smart-map indexes)
     :acme.user/avatar-url)
; => "http://avatar-images-host/for-id/matt-provider-com"

; to highlight the fact that we disregard the function, other ways where we can change
; the initial data and reach the same result:
(->> {:acme.user/email "other@provider.com"}
     (psm/smart-map indexes)
     :acme.user/avatar-url)
; => "http://avatar-images-host/for-id/other-provider-com"

(->> {:acme.user/avatar-slug "some-slogan"}
     (psm/smart-map indexes)
     :acme.user/avatar-url)
; => "http://avatar-images-host/for-id/some-slogan"

(-> {:acme.user/id 1}
    (->> (psm/smart-map indexes))
    (select-keys [:acme.user/email :acme.user/avatar-slug]))

(-> {:acme.user/id 1}
    (->> (p.ent/with-entity indexes))
    (p.eql/process [:acme.user/email :acme.user/avatar-slug]))

; demo meter to feet

(defn meter<->feet-resolver
  [attribute]
  (let [foot-kw  (keyword (namespace attribute) (str (name attribute) "-ft"))
        meter-kw (keyword (namespace attribute) (str (name attribute) "-m"))]
    [(pbir/single-attr-resolver meter-kw foot-kw #(* % 3.281))
     (pbir/single-attr-resolver foot-kw meter-kw #(/ % 3.281))]))

(let [sm (psm/smart-map (pci/register (meter<->feet-resolver :foo)))]
  [(-> sm (assoc :foo-m 169) :foo-ft)
   (-> sm (assoc :foo-ft 358) :foo-m)])
; => [554.489 109.11307528192624]

; params

; helper fn to filter coll based on the params for the current context
(defn map->query-params [m]
  (str/join "&" (map (fn [[k v]] (str (name k) "=" v)) m)))

(>def :public-apis.entry/auth
  #{"" "apiKey" "OAuth" "X-Mashape-Key" "No" "null"})

(>def :public-apis.entry/cors
  #{"no" "unknown" "yes"})

(>def :public-apis.entry/https boolean?)

(pco/defresolver public-api-entries [env _]
  {::pco/output
   [{:public.apis/entries
     [:public-apis.entry/api
      :public-apis.entry/auth
      :public-apis.entry/category
      :public-apis.entry/cors
      :public-apis.entry/description
      :public-apis.entry/https
      :public-apis.entry/link]}]

   ::pco/params
   [:public-apis.entry/auth
    :public-apis.entry/category
    :public-apis.entry/cors
    :public-apis.entry/description
    :public-apis.entry/https
    :public-apis.entry/title]}
  {:public.apis/entries
   (let [params (pco/params env)]
     (-> (slurp (str "https://api.publicapis.org/entries?" (map->query-params params)))
         (json/read-str :key-fn #(keyword "public-apis.entry" (str/lower-case %)))
         (:public-apis.entry/entries)))})

(def params-env (pci/register public-api-entries))

(def mock-todos-db
  [{::todo-message "Write demo on params"
    ::todo-done?   true}
   {::todo-message "Pathom in Rust"
    ::todo-done?   false}])

(defn filter-matches [match coll]
  (let [match-keys (keys match)]
    (if (seq match)
      (filter
        #(= match
            (select-keys % match-keys))
        coll)
      coll)))

(pco/defresolver todos-resolver [env _]
  {::pco/output
   [{::todos
     [::todo-message
      ::todo-done?]}]}

  {::todos
   (filter-matches (pco/params env) mock-todos-db)})

(def env (pci/register todos-resolver))

; list all todos
(p.eql/process env
  [::todos])

; list undone todos
(p.eql/process env
  '[(::todos {::todo-done? false})])

(comment
  (->> (p.eql/process params-env
         ['(:public.apis/entries)])
       :public.apis/entries
       (map :public-apis.entry/https)
       distinct)
  (pf.eql/data->query
    (public-api-entries (pco/with-node-params {:category "animals"}) {}))

  (-> (slurp "https://api.publicapis.org/entries?auth=null")
      (json/read-str :key-fn #(keyword "public-apis.entry" (str/lower-case %)))
      (:public-apis.entry/entries)))

; region optional inputs

(pco/defresolver user-display-name
  [{:user/keys [email name]}]
  {::pco/input [:user/email (pco/? :user/name)]}
  {:user/display-name (or name email)})

(def opt-env
  (pci/register
    [(pbir/static-table-resolver :user/id
       {1 {:user/email "user@example.com"}
        2 {:user/email "another@example.com"
           :user/name  "Sam"}})
     (pbir/constantly-resolver :all-users
       [{:user/id 1}
        {:user/id 2}
        {:user/id 3}])
     user-display-name]))

(p.eql/process opt-env
  [{:all-users [:user/display-name]}])
; => {:all-users
;     [{:user/display-name "user@example.com"} ; no name, use email
;      {:user/display-name "Sam"} ; has name (the optional), use it
;      {} ; user 3 doesn't exist, so resolver didn't get called
;     ]}

; endregion

; region nested inputs demo

(pco/defresolver top-players
  "Return maps with ids for the top players in a given game."
  []
  {:game/top-players
   [{:player/id 1}
    {:player/id 20}
    {:player/id 8}
    {:player/id 2}]})

(pco/defresolver player-by-id
  "Get player by id, in our case just generate some data based on the id."
  [{:keys [player/id]}]
  {:player/name  (str "Player " id)
   :player/score (* id 50)})

(pco/defresolver top-players-avg
  "Compute the average score for the top players using nested inputs."
  [{:keys [game/top-players]}]
  ; we have to make the nested input explicit
  {::pco/input [{:game/top-players [:player/score]}]}
  {:game/top-players-avg-score
   (let [score-sum (transduce (map :player/score) + 0 top-players)]
     (double (/ score-sum (count top-players))))})

(p.eql/process (pci/register [top-players player-by-id top-players-avg])
  [:game/top-players-avg-score])
; => #:game{:top-players-avg-score 387.5}

; endregion

; region prioritization

(pco/defresolver db-user-by-id [{:user/keys [id]}]
  {:user/name (str "Name from DB " id)})

(pco/defresolver cache-server-user-by-id [{:user/keys [id]}]
  ; here we increase the priority of the external cache resolver
  {::pco/priority 1}
  {:user/name
   (if (even? id)
     (str "Name from Cache " id)
     ::pco/unknown-value)})

(p.eql/process (pci/register [db-user-by-id cache-server-user-by-id])
  [{[:user/id 1] [:user/name]}])
; => {[:user/id 1] #:user{:name "Name from DB 1"}}

(p.eql/process (pci/register [db-user-by-id cache-server-user-by-id])
  [{[:user/id 2] [:user/name]}])
; => {[:user/id 2] #:user{:name "Name from Cache 2"}}

; endregion
