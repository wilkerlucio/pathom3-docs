(ns com.wsscode.pathom3.docs.components
  (:require [cljs.reader :refer [read-string]]
            [com.wsscode.misc.coll :as coll]
            [helix.core :as h]
            [helix.dom :as dom]
            [helix.hooks :as hooks]
            [com.wsscode.pathom3.connect.indexes :as pci]
            [com.wsscode.pathom3.connect.planner :as pcp]
            [edn-query-language.core :as eql]))

(defn post-message [^js window message]
  (.postMessage window #js {:event "pathom-viz-embed" :payload (pr-str message)} "*"))

(h/defnc EmbedComponent [{:keys [message height]}]
  (let [iframe-ref (hooks/use-ref nil)]
    (hooks/use-effect [iframe-ref]
      (when @iframe-ref
        (-> @iframe-ref
            (.addEventListener "load"
              (fn []
                (-> @iframe-ref
                    (.-contentWindow)
                    (post-message message)))))))

    (dom/iframe {:src     "https://pathom-viz.wsscode.com/embed.html"
                 :ref     iframe-ref
                 :loading "lazy"
                 :style   {:width "100%" :height height :border "0"}})))

(h/defnc ^:export PlanCytoscapeJS [{:keys [oir query displayType available] :as message}]
  (h/$ EmbedComponent
    {:message {:pathom.viz.embed/component-name
               :pathom.viz.embed.component/plan-stepper-demo

               :pathom.viz.embed/component-props
               {::pci/index-oir      (read-string oir)
                ::pcp/available-data (read-string available)
                ::eql/query          (read-string query)}}
     :height  "500px"}))

(h/defnc ^:export PlannerExplorer [{:keys [oir query displayType available] :as message}]
  (h/$ EmbedComponent
    {:message {:pathom.viz.embed/component-name
               :pathom.viz.embed.component/planner-explorer

               :pathom.viz.embed/component-props
               {::pci/index-oir (read-string oir)
                ::eql/query     (read-string query)}}
     :height  "760px"}))
