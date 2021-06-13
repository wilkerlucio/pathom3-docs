(ns com.wsscode.pathom3.docs.demos.core.eql
  (:require
    [com.wsscode.pathom3.interface.eql :as p.eql]
    [com.wsscode.pathom3.connect.indexes :as pci]
    [com.wsscode.pathom3.connect.built-in.resolvers :as pbir]
    [com.wsscode.pathom3.connect.operation :as pco])
  (:import (java.util Date)))

(pco/defresolver area [{:geo/keys [width height]}]
  {:geo/area (* width height)})

(def env
  (pci/register
    [(pbir/constantly-fn-resolver ::now (fn [_] (Date.)))
     area]))

(def pathom (p.eql/boundary-interface env))

; request EQL
(pathom [::now])

; send root data with entity
(pathom {:pathom/entity {:geo/width 10 :geo/height 8}
         :pathom/eql    [:geo/area]})

; use AST, this way Pathom doesn't have to decode the EQL
(pathom
  {:pathom/ast {:type     :root
                :children [{:type         :prop
                            :key          ::now
                            :dispatch-key ::now}]}})
