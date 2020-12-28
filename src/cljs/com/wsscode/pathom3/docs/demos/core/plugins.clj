(ns com.wsscode.pathom3.docs.demos.core.plugins
  (:require [com.wsscode.misc.coll :as coll]
            [com.wsscode.pathom3.connect.built-in.resolvers :as pbir]
            [com.wsscode.pathom3.connect.indexes :as pci]
            [com.wsscode.pathom3.interface.eql :as p.eql]
            [com.wsscode.pathom3.plugin :as p.plugin]))

(def protected-attributes
  #{:user/password})

(defn protect-attributes-wrapper [mse]
  (fn [env source {:keys [key] :as ast}]
    (if (and (contains? source key)
             (contains? protected-attributes key))
      ; the output of this extension must be a map entry or nil
      ; a vector with two elements would also work, but creating a map entry is
      ; more efficient
      (coll/make-map-entry key ::protected-value)
      (mse env source ast))))

(p.plugin/defplugin protect-attributes-plugin
  {:com.wsscode.pathom3.format.eql/wrap-map-select-entry
   protect-attributes-wrapper})

(def users
  {1 {:user/id       1
      :user/name     "User Name"
      :user/password "12345"}})

(def env
  (-> (p.plugin/register protect-attributes-plugin)
      (pci/register [(pbir/static-table-resolver :user/id users)])))

(p.eql/process env
  '[{[:user/id 1]
     [:user/name :user/password]}])

;;;;

(defn protect-attributes-plugin [protected-attributes]
  {::p.plugin/id
   `protect-attributes-plugin

   :com.wsscode.pathom3.format.eql/wrap-map-select-entry
   (fn [mst]
     (fn [env source {:keys [key] :as ast}]
       (if (and (contains? source key)
                (contains? protected-attributes key))
         ; the output of this extension must be a map entry or nil
         ; a vector with two elements would also work, but creating a map entry is
         ; more efficient
         (coll/make-map-entry key ::protected-value)
         (mst env source ast))))})

(def env
  ; create plugin to protect specific attributes and add it
  (-> (p.plugin/register (protect-attributes-plugin #{:user/password}))
      (pci/register [(pbir/static-table-resolver :user/id users)])))
