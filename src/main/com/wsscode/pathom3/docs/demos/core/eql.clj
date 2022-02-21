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

;; strict error demo

(comment

  (pathom [:wrong]))

;;;;;;;;;; env extension demo

(def users-db
  {1 {:user/login "bunny"}
   2 {:user/login "fox"}})

(pco/defresolver current-user [{:keys [app/current-user-id]} _]
  {::pco/output [:user/login]}
  (get users-db current-user-id))

(def env (pci/register current-user))

(def pathom (p.eql/boundary-interface env))

(pathom {:app/current-user-id 1} [:user/login])

(pathom
  (fn [env]
    (-> env
        (assoc :app/current-user-id 2)
        (pci/register (pbir/single-attr-resolver :user/login :user/greet #(str "Hello " %)))))
  [:user/greet])

;; union demo

(def union-env
  (pci/register
    [(pbir/single-attr-resolver :acme.post/title :acme.post/link #(str % "-link"))
     (pbir/single-attr-resolver :acme.ad/x :acme.ad/y inc)
     (pbir/constantly-resolver :acme/feed
       [^{:com.wsscode.pathom3.format.eql/union-entry-key :acme.post/id}
        {:acme.post/title "TITLE"}
        ^{:com.wsscode.pathom3.format.eql/union-entry-key :acme.ad/id}
        {:acme.ad/x 1}])]))

(p.eql/process union-env
  [{:acme/feed
    {:acme.post/id [:acme.post/link]
     :acme.ad/id   [:acme.ad/y]}}])
; => {:acme/feed
;     [{:acme.post/link "TITLE-link"}
;      {:acme.ad/y 2}]}
