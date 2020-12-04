(ns com.wsscode.pathom3.docs.components
  (:require [helix.core :as h]
            [cljs.tools.reader :refer [read-string]]
            [com.wsscode.pathom3.viz.plan :as p.plan]
            [com.wsscode.pathom3.connect.indexes :as pci]
            [edn-query-language.core :as eql]
            [com.wsscode.pathom3.viz.ui :as ui]
            [helix.dom :as dom]
            [helix.hooks :as hooks]
            [cljs.spec.alpha :as s]))

(h/defnc ^:export PlanCytoscapeJS [{:keys [oir query displayType available]}]
  (dom/div {:style {:height "500px"}}
    (h/$ p.plan/PlanCytoscape
      {:frames
       (->> (p.plan/compute-frames {::pci/index-oir                                     (read-string oir)
                                    ::eql/query                                         (read-string query)
                                    :com.wsscode.pathom3.connect.planner/available-data (or (some-> available read-string) {})})
            (mapv (juxt identity p.plan/c-nodes-edges)))

       :display
       (some-> (or displayType "display-type-label") (#(keyword "com.wsscode.pathom3.viz.plan" %)))})))

(def area-style
  {:height  "200px"
   :width   "100%"
   :outline "0"
   :resize  "none"})

(defn safe-read-string [s]
  (try (read-string s) (catch :default _ nil)))

(h/defnc ^:export PlannerExplorer []
  (let [[oir :as oir-state] (hooks/use-state "{:a {#{} #{a}}\n :b {#{:g} #{b}}\n :c {#{} #{c}}\n :e {#{} #{e e1}}\n :f {#{:e} #{f}}\n :g {#{:c :f} #{g}}\n :h {#{:a :b} #{h}}}")
        [query :as query-state] (hooks/use-state "[:h]")
        [gd set-gd!] (hooks/use-state nil)
        valid-oir?   (s/valid? ::pci/index-oir (safe-read-string oir))
        valid-query? (s/valid? ::eql/query (safe-read-string query))]
    (dom/div
      (dom/div {:style {:display "flex"}}
        (dom/div {:style {:flex "1"}}
          (dom/div "Index OIR")
          (dom/textarea {:style (cond-> area-style
                                  (not valid-oir?)
                                  (assoc :border-color "#f00"))
                         :&     (ui/dom-props {::ui/state oir-state})}))
        (dom/div {:style {:flex "1"}}
          (dom/div "Query")
          (dom/textarea {:style (cond-> area-style
                                  (not valid-query?)
                                  (assoc :border-color "#f00"))
                         :&     (ui/dom-props {::ui/state query-state})})))

      (dom/div {:style {:text-align "center"}}
        (dom/button {:on-click #(let [config {::pci/index-oir (safe-read-string oir)
                                              ::eql/query     (safe-read-string query)}]
                                  (if (s/valid? (s/keys) config)
                                    (set-gd! config)))
                     :disabled (not (and valid-oir? valid-query?))
                     :style    {:margin-bottom "12px"}}
          "Render Graph"))

      (if gd
        (dom/div {:style {:height "500px"}}
          (h/$ p.plan/PlanCytoscape
            {:frames
             (->> (p.plan/compute-frames gd)
                  (mapv (juxt identity p.plan/c-nodes-edges)))

             :display
             ::p.plan/display-type-label}))))))
