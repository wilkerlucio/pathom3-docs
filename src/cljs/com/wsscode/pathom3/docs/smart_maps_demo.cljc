(ns com.wsscode3.pathom.docs.smart-maps-demo
  (:require [com.wsscode.pathom3.connect.indexes :as pci]
            [com.wsscode.pathom3.connect.operation :as pco]
            [com.wsscode.pathom3.interface.smart-map :as psm]
            [com.wsscode.pathom3.connect.built-in.resolvers :as pbir]))

(pco/defresolver full-name [{::keys [first-name last-name]}]
  {::full-name (str first-name " " last-name)})

(def indexes (pci/register full-name))

(def person-data {::first-name "Anne" ::last-name "Frank"})

(def smart-map (psm/smart-map indexes person-data))

(::full-name smart-map) ; => "Anne Frank"

;; demo 2

(pco/defresolver full-name [{::keys [first-name last-name]}]
  {::full-name (str first-name " " last-name)})

(pco/defresolver anne []
  {::anne {::first-name "Anne" ::last-name "Frank"}})

(def indexes (pci/register [full-name anne]))

(def smart-map (psm/smart-map indexes))

(::anne smart-map) ; => {::first-name "Anne" ::last-name "Frank"}

; nested access
(-> smart-map ::anne ::full-name) ; => "Anne Frank"

;; demo 3

(pco/defresolver full-name [{::keys [first-name last-name]}]
  {::full-name (str first-name " " last-name)})

(pco/defresolver stars []
  {::start-wars-characters
   [{::first-name "Luke" ::last-name "Skywalker"}
    {::first-name "Darth" ::last-name "Vader"}
    {::first-name "Han" ::last-name "Solo"}]})

(def indexes (pci/register [full-name stars]))

(def smart-map (psm/smart-map indexes))

; nested access on sequences
(mapv ::full-name (::start-wars-characters smart-map))
; => ["Luke Skywalker"
;     "Darth Vader"
;     "Han Solo"]

;; changes demo

(pco/defresolver full-name [{::keys [first-name last-name]}]
  {::full-name (str first-name " " last-name)})

(def indexes (pci/register full-name))

(def person-data {::first-name "John" ::last-name "Lock"})

(def smart-map (psm/smart-map indexes person-data))

(::full-name smart-map) ; => "John Lock"

(-> smart-map
    (assoc ::last-name "Oliver")
    ::full-name)
; => "John Oliver", the full-name gets re-computed due to the change

;; pre load

(pco/defresolver right [{::keys [left width]}]
  {::right (+ left width)})

(pco/defresolver bottom [{::keys [top height]}]
  {::bottom (+ top height)})

(def indexes (pci/register [right bottom]))

(def square {::left  10 ::top 30
             ::width 23 ::height 35})

(def smart-map
  (-> (psm/smart-map indexes square)
      (psm/sm-touch! [::right ::bottom])))

(::right smart-map) ; => 33, read from cache
(::bottom smart-map) ; => 65, read from cache

;; debug reads

(def indexes
  (pci/register
    [(pbir/constantly-resolver ::n 10)
     (pbir/single-attr-resolver ::n ::x inc)
     (pbir/single-attr-resolver ::x ::y #(* % 2))
     (pbir/single-attr-resolver ::y ::z #(- % 10))]))

(-> (psm/smart-map indexes)
    (psm/sm-get-debug ::y))
;{:com.wsscode.pathom3.connect.planner/index-attrs #:com.wsscode.pathom.docs.smart-maps-demo{:n 3,
;                                                                                            :y 1,
;                                                                                            :x 2},
; :com.wsscode.pathom3.connect.planner/unreachable-attrs #{},
; :com.wsscode.pathom3.connect.runner/graph-process-duration-ms 0.11225199699401855,
; :com.wsscode.pathom3.connect.runner.stats/overhead-duration-ms 0.09637504816055298,
; :com.wsscode.pathom3.connect.planner/root 3,
; :com.wsscode.pathom3.connect.runner.stats/overhead-duration-percentage 0.8585597650052356,
; :com.wsscode.pathom3.connect.runner/node-run-stats {1 #:com.wsscode.pathom3.connect.runner{:run-duration-ms 0.006672978401184082,
;                                                                                            :node-run-input #:com.wsscode.pathom.docs.smart-maps-demo{:x 11}},
;                                                     3 #:com.wsscode.pathom3.connect.runner{:run-duration-ms 0.004477977752685547,
;                                                                                            :node-run-input {}},
;                                                     2 #:com.wsscode.pathom3.connect.runner{:run-duration-ms 0.004725992679595947,
;                                                                                            :node-run-input #:com.wsscode.pathom.docs.smart-maps-demo{:n 10}}},
; :com.wsscode.pathom3.connect.planner/index-ast #:com.wsscode.pathom.docs.smart-maps-demo{:y {:key :com.wsscode.pathom.docs.smart-maps-demo/y,
;                                                                                              :type :prop,
;                                                                                              :dispatch-key :com.wsscode.pathom.docs.smart-maps-demo/y}},
; :com.wsscode.pathom3.connect.planner/unreachable-resolvers #{},
; :com.wsscode.pathom3.interface.smart-map/value 22,
; :com.wsscode.pathom3.connect.planner/index-resolver->nodes {com.wsscode.pathom.docs.smart_maps_demo_SLASH_x->com.wsscode.pathom.docs.smart_maps_demo_SLASH_y-single-attr-transform #{1},
;                                                             com.wsscode.pathom.docs.smart_maps_demo_SLASH_n-constant #{3},
;                                                             com.wsscode.pathom.docs.smart_maps_demo_SLASH_n->com.wsscode.pathom.docs.smart_maps_demo_SLASH_x-single-attr-transform #{2}},
; :com.wsscode.pathom3.connect.planner/nodes {1 {:com.wsscode.pathom3.connect.planner/after-nodes #{2},
;                                                :com.wsscode.pathom3.connect.planner/requires #:com.wsscode.pathom.docs.smart-maps-demo{:y {}},
;                                                :com.wsscode.pathom3.connect.operation/op-name com.wsscode.pathom.docs.smart_maps_demo_SLASH_x->com.wsscode.pathom.docs.smart_maps_demo_SLASH_y-single-attr-transform,
;                                                :com.wsscode.pathom3.connect.planner/source-for-attrs #{:com.wsscode.pathom.docs.smart-maps-demo/y},
;                                                :com.wsscode.pathom3.connect.planner/input #:com.wsscode.pathom.docs.smart-maps-demo{:x {}},
;                                                :com.wsscode.pathom3.connect.planner/node-id 1},
;                                             3 {:com.wsscode.pathom3.connect.planner/requires #:com.wsscode.pathom.docs.smart-maps-demo{:n {}},
;                                                :com.wsscode.pathom3.connect.operation/op-name com.wsscode.pathom.docs.smart_maps_demo_SLASH_n-constant,
;                                                :com.wsscode.pathom3.connect.planner/source-for-attrs #{:com.wsscode.pathom.docs.smart-maps-demo/n},
;                                                :com.wsscode.pathom3.connect.planner/input {},
;                                                :com.wsscode.pathom3.connect.planner/run-next 2,
;                                                :com.wsscode.pathom3.connect.planner/node-id 3},
;                                             2 {:com.wsscode.pathom3.connect.planner/after-nodes #{3},
;                                                :com.wsscode.pathom3.connect.planner/requires #:com.wsscode.pathom.docs.smart-maps-demo{:x {}},
;                                                :com.wsscode.pathom3.connect.operation/op-name com.wsscode.pathom.docs.smart_maps_demo_SLASH_n->com.wsscode.pathom.docs.smart_maps_demo_SLASH_x-single-attr-transform,
;                                                :com.wsscode.pathom3.connect.planner/source-for-attrs #{:com.wsscode.pathom.docs.smart-maps-demo/x},
;                                                :com.wsscode.pathom3.connect.planner/input #:com.wsscode.pathom.docs.smart-maps-demo{:n {}},
;                                                :com.wsscode.pathom3.connect.planner/run-next 1,
;                                                :com.wsscode.pathom3.connect.planner/node-id 2}},
; :com.wsscode.pathom3.connect.runner.stats/resolver-accumulated-duration-ms 0.015876948833465576}
