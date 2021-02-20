(ns com.wsscode.pathom3.docs.demos.core.debugging
  (:require [com.wsscode.pathom3.connect.operation :as pco]
            [com.wsscode.pathom3.connect.indexes :as pci]
            [com.wsscode.pathom3.interface.eql :as p.eql]
            [com.wsscode.pathom3.connect.runner.stats :as pcrs]
            [com.wsscode.pathom3.connect.runner :as pcr]
            [com.wsscode.pathom3.interface.smart-map :as psm]))

(pco/defresolver area [{:keys [width height]}]
  {:area (* width height)})

(pco/defresolver width [{:keys [x x2]}]
  {:width (- x2 x)})

(def env
  (pci/register
    [area width]))

(comment
  (-> (p.eql/process
        env
        {:x 10 :x2 30 :height 40}
        [:area])

      (meta)
      ::pcr/run-stats
      (psm/smart-run-stats)

      clojure.datafy/datafy
      keys)

  (p.eql/process
    env
    {:x 10 :x2 "foo" :height 40}
    [:area])

  (-> (p.eql/process
        env
        {:x 10 :x2 "foo" :height 40}
        [:area])

      (meta)
      ::pcr/run-stats))

(-> (p.eql/process
      env
      {:x 10 :x2 "foo" :height 40}
      [:area])

    (meta)
    ::pcr/run-stats
    psm/smart-run-stats
    (pcrs/get-attribute-error :area))
=>
{::pcrs/node-error-type
 ::pcrs/node-error-type-ancestor

 ::pcrs/node-error-id
 2,

 ::pcr/node-error
 #error{:cause "class java.lang.String cannot be cast to class java.lang.Number (java.lang.String and java.lang.Number are in module java.base of loader 'bootstrap')",
        :via   [{:type    java.lang.ClassCastException,
                 :message "class java.lang.String cannot be cast to class java.lang.Number (java.lang.String and java.lang.Number are in module java.base of loader 'bootstrap')",
                 :at      [clojure.lang.Numbers
                           minus
                           "Numbers.java"
                           162]}],
        :trace ...}}
