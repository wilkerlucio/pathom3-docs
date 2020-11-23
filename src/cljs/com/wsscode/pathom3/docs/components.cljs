(ns com.wsscode.pathom3.docs.components
  (:require [helix.core :as h]
            [cljs.tools.reader :refer [read-string]]
            [com.wsscode.pathom3.viz.plan :as p.plan]
            [com.wsscode.pathom3.connect.indexes :as pci]
            [edn-query-language.core :as eql]))

(h/defnc ^:export PlanCytoscapeJS [{:keys [oir query displayType available]}]
  (h/$ p.plan/PlanCytoscape
    {:frames
     (->> (p.plan/compute-frames {::pci/index-oir                                     (read-string oir)
                                  ::eql/query                                         (read-string query)
                                  :com.wsscode.pathom3.connect.planner/available-data (or (some-> available read-string) {})})
          (mapv (juxt identity p.plan/c-nodes-edges)))

     :display
     (some-> (or displayType "display-type-label") (#(keyword "com.wsscode.pathom3.viz.plan" %)))}))

