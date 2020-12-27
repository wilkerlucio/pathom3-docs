(ns com.wsscode.pathom3.docs.demos.core.cache
  (:require [clojure.core.cache.wrapped :as cc]
            [com.wsscode.pathom3.cache :as p.cache]
            [com.wsscode.pathom3.connect.indexes :as pci]
            [com.wsscode.pathom3.connect.operation :as pco]
            [com.wsscode.pathom3.connect.planner :as pcp]
            [com.wsscode.pathom3.interface.eql :as p.eql]))

(pco/defresolver full-name [{::keys [first-name last-name]}]
  {::full-name (str first-name " " last-name)})

; create a new record type to use core.cache implementation
(defrecord CoreCacheStore [cache-atom]
  p.cache/CacheStore
  (-cache-lookup-or-miss [_ cache-key f]
    (cc/lookup-or-miss cache-atom cache-key (fn [_] (f)))))

; helper to start a new LRU cache
(defn lru-cache [base threshold]
  (-> (cc/lru-cache-factory base :threshold threshold)
      (->CoreCacheStore)))

; create a cache holds only the latest 100 read plans
(def plan-cache* (lru-cache {} 100))

(def env
  (-> (pci/register full-name)
      (pcp/with-plan-cache plan-cache*)))

(defn handle [tx]
  (p.eql/process env tx))
