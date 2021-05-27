(ns com.wsscode.pathom3.docs.components
  (:require [com.wsscode.misc.coll :as coll]
            [com.wsscode.pathom3.connect.indexes :as pci]
            [com.wsscode.pathom3.connect.planner :as pcp]
            [edn-query-language.core :as eql]
            [promesa.core :as p]
            [cljs.tools.reader.edn :refer [read-string]]
            [helix.core :as h]
            [helix.dom :as dom]
            [helix.hooks :as hooks]
            [clojure.string :as str]))

(def DEV-VIZ? false)

(def embed-url
  (if DEV-VIZ?
    "http://localhost:8087/embed.html"
    "https://pathom-viz.wsscode.com/embed.html"))

(defn post-message [^js window message]
  (.postMessage window #js {:event "pathom-viz-embed" :payload (pr-str message)} "*"))

(h/defnc EmbedComponent [{:keys [message height]}]
  (let [iframe-ref (hooks/use-ref nil)
        [full? sf!] (hooks/use-state false)]
    (hooks/use-effect [iframe-ref]
      (when @iframe-ref
        (-> @iframe-ref
            (.addEventListener "load"
              (fn []
                (-> @iframe-ref
                    (.-contentWindow)
                    (post-message message)))))))
    (dom/div {:& {:style (cond-> {:display       "flex"
                                  :flexDirection "column"}
                           full?
                           (merge {:position      "fixed" :inset "0"
                                   :zIndex        "1000"}))}}
      (dom/button {:on-click #(sf! (not full?))}
        (if full? "Leave Full Screen" "Full Screen"))
      (dom/iframe {:src     embed-url
                   :ref     iframe-ref
                   :loading "lazy"
                   :style   {:flex "1" :width "100%" :height (if full? "auto" height) :border "0"}}))))

(defn use-edn-file [path]
  (let [[contents c!] (hooks/use-state ::loading)]
    (hooks/use-effect [path]
      (-> (p/let [res (js/fetch path)
                  txt (.text res)]
            (c! (read-string txt)))
          (p/catch #(c! {::error %}))))
    contents))

(defn loader [x f]
  (cond
    (= ::loading x)
    (dom/div {:class "loading-container"}
      "Loading viz data...")

    (try
      (get x ::error)
      (catch :default _))
    (do
      (js/console.error (get x ::error))
      (dom/div "Error"))

    :else
    (f x)))

(h/defnc ^:export PlanStepperFile [{:keys [path]}]
  (loader (use-edn-file path)
    #(h/$ EmbedComponent
       {:message {:pathom.viz.embed/component-name
                  :pathom.viz.embed.component/plan-snapshots

                  :pathom.viz.embed/component-props
                  %}
        :height  "500px"})))

(h/defnc ^:export PlanStepperDemo [{:keys [oir query displayType available] :as message}]
  (h/$ EmbedComponent
    {:message {:pathom.viz.embed/component-name
               :pathom.viz.embed.component/plan-stepper-demo

               :pathom.viz.embed/component-props
               {::pci/index-oir      (read-string oir)
                ::pcp/available-data (or (some-> available read-string) {})
                ::eql/query          (read-string query)}}
     :height  "500px"}))

(h/defnc ^:export PlanGraphFile [{:keys [path]}]
  (loader (use-edn-file path)
    #(h/$ EmbedComponent
       {:message {:pathom.viz.embed/component-name
                  :pathom.viz.embed.component/plan-view

                  :pathom.viz.embed/component-props
                  %}
        :height  "500px"})))

(h/defnc ^:export PlanGraphFileWithDetails [{:keys [path]}]
  (loader (use-edn-file path)
    #(h/$ EmbedComponent
       {:message {:pathom.viz.embed/component-name
                  :pathom.viz.embed.component/plan-view-with-details

                  :pathom.viz.embed/component-props
                  %}
        :height  "500px"})))

(h/defnc ^:export PlannerExplorer [{:keys [oir query displayType available] :as message}]
  (h/$ EmbedComponent
    {:message {:pathom.viz.embed/component-name
               :pathom.viz.embed.component/planner-explorer

               :pathom.viz.embed/component-props
               {::pci/index-oir (read-string oir)
                ::eql/query     (read-string query)}}
     :height  "760px"}))

(h/defnc ^:export PlanView [message]
  (h/$ EmbedComponent {:message (-> message
                                    (assoc :message "pathom-start-embed" :component "plan-view")
                                    (coll/update-if :plan read-string))
                       :height  "500px"}))

; region kroki

(def extension->type
  {"puml" "plantuml"})

(defn path-extension [path]
  (last (str/split path ".")))

(defn request-graph [graph-type source]
  (p/let [^js response (js/fetch (str "https://kroki.io/" graph-type "/svg")
                         #js {:method  "post"
                              :headers #js {"Content-Type" "text/plain"}
                              :body    source})]
    (.text response)))

(defn use-kroki-diagram [{:keys [type source]}]
  (let [[diagram-svg ds!] (hooks/use-state nil)]
    (hooks/use-effect [type source]
      (if (and type source)
        (-> (request-graph type source)
            (p/then ds!))))
    diagram-svg))

(h/defnc ^:export KrokiDiagram [diagram]
  (let [diagram-svg (use-kroki-diagram diagram)]
    (if diagram-svg
      (dom/div {:dangerouslySetInnerHTML #js {:__html diagram-svg}})
      (dom/noscript))))

(defn use-diagram-file [path]
  (let [[contents c!] (hooks/use-state ::loading)]
    (hooks/use-effect [path]
      (let [type (-> path path-extension extension->type)]
        (-> (p/let [res (js/fetch path)
                    txt (.text res)]
              (c! {:type type :source txt}))
            (p/catch #(c! {::error %})))))
    contents))

(h/defnc ^:export KrokiDiagramFile [{:keys [path]}]
  (let [diagram-data (use-diagram-file path)]
    (loader diagram-data
      (fn [diagram]
        (h/$ KrokiDiagram {:& diagram})))))

; endregion
