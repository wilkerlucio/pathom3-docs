(ns com.wsscode.pathom3.docs.demos.core.async-extend-core-async
  (:require
    [clojure.core.async :as async :refer [go <!]]
    [clojure.core.async.impl.channels :as async-ch]
    [com.wsscode.pathom3.connect.indexes :as pci]
    [com.wsscode.pathom3.connect.operation :as pco]
    [com.wsscode.pathom3.interface.async.eql :as p.a.eql]
    [promesa.core :as p]
    [promesa.protocols]))

; region core.async extension

(defn error?
  "Returns true if err is an error object."
  [err]
  #?(:clj
     (instance? Throwable err)

     :cljs
     (instance? js/Error err)))

(defn chan->promise [c]
  (p/create
    (fn [resolve reject]
      (go
        (let [v (<! c)]
          (if (error? v)
            (reject v)
            (resolve v)))))))

(extend-type async-ch/ManyToManyChannel
  promesa.protocols/IPromiseFactory
  (-promise [this]
    (chan->promise this)))

; endregion

(pco/defresolver slow-resolver []
  {::pco/output [::slow-response]}
  (go
    (<! (async/timeout 400))
    {::slow-response "done"}))

(def env (pci/register slow-resolver))

(comment
  (p/let [res (p.a.eql/process env [::slow-response])]
    (cljs.pprint/pprint res)))
