(ns com.wsscode.pathom3.docs.components
  (:require [helix.core :as h]
            [helix.dom :as dom]
            [helix.hooks :as hooks]
            [com.wsscode.misc.coll :as coll]
            [cljs.reader :refer [read-string]]))

(defn post-message [^js window message]
  (.postMessage window (pr-str message)))

(h/defnc EmbedComponent [{:keys [args height]}]
  (let [iframe-ref (hooks/use-ref nil)]
    (hooks/use-effect [iframe-ref]
      (when @iframe-ref
        (-> @iframe-ref
            (.addEventListener "load"
              (fn []
                (-> @iframe-ref
                    (.-contentWindow)
                    (post-message args)))))))

    (dom/iframe {:src   "/embed.html"
                 :ref   iframe-ref
                 :style {:width "100%" :height height :border "0"}})))

(h/defnc ^:export PlanCytoscapeJS [{:keys [oir query displayType available] :as args}]
  (h/$ EmbedComponent {:args   (-> args
                                   (assoc :message "pathom-start-embed" :component "plan-cytoscape")
                                   (coll/update-if :oir read-string)
                                   (coll/update-if :available read-string)
                                   (coll/update-if :query read-string))
                       :height "500px"}))

(h/defnc ^:export PlannerExplorer [args]
  (h/$ EmbedComponent {:args   (assoc args :message "pathom-start-embed" :component "planner-explorer")
                       :height "760px"}))
