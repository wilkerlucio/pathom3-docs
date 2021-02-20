(ns com.wsscode.pathom3.docs.components
  (:require [cljs.reader :refer [read-string]]
            [com.wsscode.misc.coll :as coll]
            [helix.core :as h]
            [helix.dom :as dom]
            [helix.hooks :as hooks]))

(defn post-message [^js window message]
  (.postMessage window (pr-str message)))

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

    (dom/iframe {:src     "/embed.html"
                 :ref     iframe-ref
                 :loading "lazy"
                 :style   {:width "100%" :height height :border "0"}})))

(h/defnc ^:export PlanCytoscapeJS [{:keys [oir query displayType available] :as message}]
  (h/$ EmbedComponent {:message   (-> message
                                   (assoc :message "pathom-start-embed" :component "plan-history")
                                   (coll/update-if :oir read-string)
                                   (coll/update-if :available read-string)
                                   (coll/update-if :query read-string))
                       :height "500px"}))

(h/defnc ^:export PlannerExplorer [message]
  (h/$ EmbedComponent {:message   (assoc message :message "pathom-start-embed" :component "planner-explorer")
                       :height "760px"}))
