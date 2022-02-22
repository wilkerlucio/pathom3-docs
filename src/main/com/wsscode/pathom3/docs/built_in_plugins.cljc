(ns com.wsscode.pathom3.docs.built-in-plugins
  (:require [com.wsscode.pathom3.connect.built-in.plugins :as pbip]
            [com.wsscode.pathom3.connect.built-in.resolvers :as pbir]
            [com.wsscode.pathom3.connect.indexes :as pci]
            [com.wsscode.pathom3.interface.eql :as p.eql]
            [com.wsscode.pathom3.plugin :as p.plugin]))

(def env
  (-> (pci/register (pbir/constantly-fn-resolver :time :now))
      (p.plugin/register
        [(pbip/env-wrap-plugin #(assoc % :now (java.util.Date.)))])))

(comment
  (p.eql/process env [:time]))
