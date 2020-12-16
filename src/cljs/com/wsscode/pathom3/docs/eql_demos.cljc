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

(-> (p.eql/process indexes [::tau])
    meta)

{:com.wsscode.pathom3.connect.runner/run-stats
 {:com.wsscode.pathom3.connect.planner/index-attrs
  {:com.wsscode.pathom3.docs.eql-demos/pi  2,
   :com.wsscode.pathom3.docs.eql-demos/tau 1},
  :com.wsscode.pathom3.connect.planner/unreachable-attrs
  #{},
  :com.wsscode.pathom3.connect.runner/compute-plan-run-finish-ms
  1.632031796479513E9,
  :com.wsscode.pathom3.connect.runner/graph-run-finish-ms
  1.632031796589152E9,
  :com.wsscode.pathom3.connect.runner/compute-plan-run-start-ms
  1.6320317961298E9,
  :com.wsscode.pathom3.connect.planner/root
  2,
  :com.wsscode.pathom3.connect.runner/node-run-stats
  {2
   {:com.wsscode.pathom3.connect.runner/run-start-ms
    1.632031796514809E9,
    :com.wsscode.pathom3.connect.runner/run-finish-ms
    1.632031796520259E9,
    :com.wsscode.pathom3.connect.runner/node-run-input
    {},
    :com.wsscode.pathom3.connect.runner/node-run-output
    {:com.wsscode.pathom3.docs.eql-demos/pi 3.1415}},
   1
   {:com.wsscode.pathom3.connect.runner/run-start-ms
    1.632031796555155E9,
    :com.wsscode.pathom3.connect.runner/run-finish-ms
    1.632031796567302E9,
    :com.wsscode.pathom3.connect.runner/node-run-input
    {:com.wsscode.pathom3.docs.eql-demos/pi 3.1415},
    :com.wsscode.pathom3.connect.runner/node-run-output
    {:com.wsscode.pathom3.docs.eql-demos/tau 6.283}}},
  :com.wsscode.pathom3.connect.planner/index-ast
  {:com.wsscode.pathom3.docs.eql-demos/tau
   {:type         :prop,
    :dispatch-key :com.wsscode.pathom3.docs.eql-demos/tau,
    :key          :com.wsscode.pathom3.docs.eql-demos/tau}},
  :com.wsscode.pathom3.connect.planner/unreachable-resolvers
  #{},
  :com.wsscode.pathom3.connect.runner/graph-run-start-ms
  1.632031796081473E9,
  :com.wsscode.pathom3.connect.planner/index-resolver->nodes
  {com.wsscode.pathom3.docs.eql_demos_SLASH_pi->com.wsscode.pathom3.docs.eql_demos_SLASH_tau-single-attr-transform
   #{1},
   com.wsscode.pathom3.docs.eql_demos_SLASH_pi-constant
   #{2}},
  :com.wsscode.pathom3.connect.planner/nodes
  {1
   {:com.wsscode.pathom3.connect.operation/op-name
    com.wsscode.pathom3.docs.eql_demos_SLASH_pi->com.wsscode.pathom3.docs.eql_demos_SLASH_tau-single-attr-transform,
    :com.wsscode.pathom3.connect.planner/node-id
    1,
    :com.wsscode.pathom3.connect.planner/expects
    {:com.wsscode.pathom3.docs.eql-demos/tau {}},
    :com.wsscode.pathom3.connect.planner/input
    {:com.wsscode.pathom3.docs.eql-demos/pi {}},
    :com.wsscode.pathom3.connect.planner/node-parents
    #{2},
    :com.wsscode.pathom3.connect.planner/source-for-attrs
    #{:com.wsscode.pathom3.docs.eql-demos/tau}},
   2
   {:com.wsscode.pathom3.connect.operation/op-name
    com.wsscode.pathom3.docs.eql_demos_SLASH_pi-constant,
    :com.wsscode.pathom3.connect.planner/node-id
    2,
    :com.wsscode.pathom3.connect.planner/expects
    {:com.wsscode.pathom3.docs.eql-demos/pi {}},
    :com.wsscode.pathom3.connect.planner/input
    {},
    :com.wsscode.pathom3.connect.planner/source-for-attrs
    #{:com.wsscode.pathom3.docs.eql-demos/pi},
    :com.wsscode.pathom3.connect.planner/run-next
    1}}}}

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

(def union-env
  (pci/register
    [(pbir/static-table-resolver `posts :acme.post/id
       {1 {:acme.post/text "Foo"}})
     (pbir/static-table-resolver `ads :acme.ad/id
       {1 {:acme.ad/backlink "http://marketing.is-bad.com"
           :acme.ad/title    "Promotion thing"}})
     (pbir/static-table-resolver `videos :acme.video/id
       {1 {:acme.video/title "Some video"}})
     (pbir/constantly-resolver :acme/feed
       [{:acme.post/id 1}
        {:acme.ad/id 1}
        {:acme.video/id 1}])]))

(p.eql/process union-env
  [{:acme/feed
    {:acme.post/id  [:acme.post/text]
     :acme.ad/id    [:acme.ad/backlink
                     :acme.ad/title]
     :acme.video/id [:acme.video/title]}}])
; => {:acme/feed
;     [{:acme.post/text "Foo"}
;      {:acme.ad/backlink "http://marketing.site.com",
;       :acme.ad/title "Promotion thing"}
;      {:acme.video/title "Some video"}]}
