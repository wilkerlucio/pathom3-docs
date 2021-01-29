(ns com.wsscode.pathom3.docs.built-in-plugins
  (:require [com.wsscode.pathom3.connect.built-in.plugins :as pbip]
            [com.wsscode.pathom3.connect.built-in.resolvers :as pbir]
            [com.wsscode.pathom3.connect.indexes :as pci]
            [com.wsscode.pathom3.interface.eql :as p.eql]
            [com.wsscode.pathom3.plugin :as p.plugin]))

(def env
  (-> (pci/register (pbir/single-attr-resolver :x :y inc))
      (p.plugin/register
        [pbip/remove-stats-plugin
         (pbip/attribute-errors-plugin)])))

(p.eql/process env
  {:x "foo"}
  [:y])
=>
{:com.wsscode.pathom3.connect.runner/attribute-errors
 {:y #error{:cause "class java.lang.String cannot be cast to class java.lang.Number (java.lang.String and java.lang.Number are in module java.base of loader 'bootstrap')",
            :via   [{:type    java.lang.ClassCastException,
                     :message "class java.lang.String cannot be cast to class java.lang.Number (java.lang.String and java.lang.Number are in module java.base of loader 'bootstrap')",
                     :at      [clojure.lang.Numbers
                               inc
                               "Numbers.java"
                               137]}],
            :trace ...}}}
