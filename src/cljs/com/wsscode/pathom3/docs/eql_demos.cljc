(ns com.wsscode.pathom3.docs.eql-demos
  (:require [com.wsscode.pathom3.connect.built-in.resolvers :as pbir]
            [com.wsscode.pathom3.connect.indexes :as pci]
            [com.wsscode.pathom3.interface.eql :as p.eql]))

(def indexes
  (pci/register
    [(pbir/constantly-resolver ::pi 3.1415)
     (pbir/single-attr-resolver ::pi ::tau #(* % 2))]))

(p.eql/process indexes [::tau ::pi
                        :com.wsscode.pathom3.connect.runner/run-stats])
; => {::pi 3.1415 ::tau 6.283}

(p.eql/process indexes [{[::pi 2.3] [::tau]} :com.wsscode.pathom3.connect.runner/run-stats])

; (p.eql/process [{(:>/wilker {::first-name "Wilker" ::last-name "Silva"}) [::full-name]}])

(p.eql/process indexes [{^:pathom/spread [::name {::pi 1 ::tau 3}] [::pi ::tau]}])

(p.eql/process indexes [::tau :com.wsscode.pathom3.connect.runner/run-stats])

;{:com.wsscode.pathom.docs.eql-demos/tau
; 6.283
;
; :com.wsscode.pathom3.connect.runner/run-stats
; {:com.wsscode.pathom3.connect.planner/index-attrs                           #:com.wsscode.pathom.docs.eql-demos{:tau 1,
;                                                                                                                 :pi  2},
;  :com.wsscode.pathom3.connect.planner/unreachable-attrs                     #{:com.wsscode.pathom3.connect.runner/run-stats},
;  :com.wsscode.pathom3.connect.runner/graph-process-duration-ms              0.10610496997833252,
;  :com.wsscode.pathom3.connect.runner.stats/overhead-duration-ms             0.09002596139907837,
;  :com.wsscode.pathom3.connect.planner/root                                  2,
;  :com.wsscode.pathom3.connect.runner.stats/overhead-duration-percentage     0.8484613059827677,
;  :com.wsscode.pathom3.connect.runner/node-run-stats                         {1 #:com.wsscode.pathom3.connect.runner{:run-duration-ms 0.00959700345993042,
;                                                                                                                     :node-run-input  #:com.wsscode.pathom.docs.eql-demos{:pi 3.1415}},
;                                                                              2 #:com.wsscode.pathom3.connect.runner{:run-duration-ms 0.0064820051193237305,
;                                                                                                                     :node-run-input  {}}},
;  :com.wsscode.pathom3.connect.planner/index-ast                             {:com.wsscode.pathom3.connect.runner/run-stats {:key          :com.wsscode.pathom3.connect.runner/run-stats,
;                                                                                                                             :type         :prop,
;                                                                                                                             :dispatch-key :com.wsscode.pathom3.connect.runner/run-stats},
;                                                                              :com.wsscode.pathom.docs.eql-demos/tau        {:key          :com.wsscode.pathom.docs.eql-demos/tau,
;                                                                                                                             :type         :prop,
;                                                                                                                             :dispatch-key :com.wsscode.pathom.docs.eql-demos/tau}},
;  :com.wsscode.pathom3.connect.planner/unreachable-resolvers                 #{},
;  :com.wsscode.pathom3.connect.planner/index-resolver->nodes                 {com.wsscode.pathom.docs.eql_demos_SLASH_pi-constant                                                           #{2},
;                                                                              com.wsscode.pathom.docs.eql_demos_SLASH_pi->com.wsscode.pathom.docs.eql_demos_SLASH_tau-single-attr-transform #{1}},
;  :com.wsscode.pathom3.connect.planner/nodes                                 {1 {:com.wsscode.pathom3.connect.planner/after-nodes      #{2},
;                                                                                 :com.wsscode.pathom3.connect.planner/requires         #:com.wsscode.pathom.docs.eql-demos{:tau {}},
;                                                                                 :com.wsscode.pathom3.connect.operation/op-name        com.wsscode.pathom.docs.eql_demos_SLASH_pi->com.wsscode.pathom.docs.eql_demos_SLASH_tau-single-attr-transform,
;                                                                                 :com.wsscode.pathom3.connect.planner/source-for-attrs #{:com.wsscode.pathom.docs.eql-demos/tau},
;                                                                                 :com.wsscode.pathom3.connect.planner/input            #:com.wsscode.pathom.docs.eql-demos{:pi {}},
;                                                                                 :com.wsscode.pathom3.connect.planner/node-id          1},
;                                                                              2 {:com.wsscode.pathom3.connect.planner/requires         #:com.wsscode.pathom.docs.eql-demos{:pi {}},
;                                                                                 :com.wsscode.pathom3.connect.operation/op-name        com.wsscode.pathom.docs.eql_demos_SLASH_pi-constant,
;                                                                                 :com.wsscode.pathom3.connect.planner/source-for-attrs #{:com.wsscode.pathom.docs.eql-demos/pi},
;                                                                                 :com.wsscode.pathom3.connect.planner/input            {},
;                                                                                 :com.wsscode.pathom3.connect.planner/run-next         1,
;                                                                                 :com.wsscode.pathom3.connect.planner/node-id          2}},
;  :com.wsscode.pathom3.connect.runner.stats/resolver-accumulated-duration-ms 0.01607900857925415}}

(def indexes
  (pci/register
    [(pbir/constantly-resolver ::pi 3.1415)
     (pbir/single-attr-resolver ::pi ::tau #(* % 2))
     ; define a resolver to provide a collection of items
     (pbir/constantly-resolver ::pi-worlds
       [{::pi 3.14}
        {::pi 3.14159}
        {::pi 6.8}
        {::tau 20}
        {::pi 10 ::tau 50}])]))

(p.eql/process indexes
  ; using a map we are able to specify nested requirements from some attribute
  [{::pi-worlds [::tau ::pi]}])
; => {::pi-worlds
;      [{::tau 6.28
;        ::pi  3.14}
;       {::tau 6.28318
;        ::pi  3.14159}
;       {::tau 13.6
;        ::pi  6.8}
;       {::tau 20
;        ::pi  3.1415}
;       {::tau 50
;        ::pi  10}]}
