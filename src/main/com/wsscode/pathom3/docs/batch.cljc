(ns com.wsscode.pathom3.docs.batch
  (:require [com.wsscode.pathom3.connect.operation :as pco]
            [com.wsscode.pathom3.connect.indexes :as pci]
            [com.wsscode.pathom3.interface.eql :as p.eql]
            [com.wsscode.pathom3.connect.built-in.resolvers :as pbir]
            [com.wsscode.pathom3.entity-tree :as p.ent]
            [com.wsscode.misc.coll :as coll]))

(pco/defresolver fetch-v [{:keys [id]}]
  (Thread/sleep 300)
  {:v (* 10 id)})

(pco/defresolver batch-fetch-v [items]
  {::pco/input  [:id]
   ::pco/output [:v]
   ::pco/batch? true}
  (Thread/sleep 300)
  (mapv #(hash-map :v (* 10 (:id %))) items))

(pco/defresolver batch-fetch-v-reorder [items]
  {::pco/input  [:id]
   ::pco/output [:v]
   ::pco/batch? true}
  (Thread/sleep 300)
  (let [results
        (->> items
             ; note we add the id again so the re-order function can match this item with the source
             (mapv #(hash-map :id (:id %) :v (* 10 (:id %))))
             ; make the order random
             (shuffle)
             ; drop some item
             (drop 1))]
    results))

(pco/defresolver batch-fetch-v-reorder-fixed [items]
  {::pco/input  [:id]
   ::pco/output [:v]
   ::pco/batch? true}
  (Thread/sleep 300)
  (let [results
        (->> items
             ; note we add the id again so the re-order function can match this item with the source
             (mapv #(hash-map :id (:id %) :v (* 10 (:id %))))
             ; make the order random
             (shuffle)
             (drop 1))]
    (coll/restore-order items :id results)))

(time
  (p.eql/process
    (-> (pci/register fetch-v)
        (p.ent/with-entity
          {:list [{:id 1}
                  {:id 2}
                  {:id 3}]}))
    [{:list [:v]}]))
; "Elapsed time: 903.389738 msecs"
; => {:list [{:v 10} {:v 20} {:v 30}]}

(p.eql/process
  (-> (pci/register batch-fetch-v-reorder)
      (p.ent/with-entity
        {:list [{:id 1}
                {:id 2}
                {:id 3}]}))
  [{:list [:id :v]}])
; => {:list [{:id 1, :v 20} {:id 2, :v 30} {:id 3, :v 10}]}
(comment

  (p.eql/process
    (-> (pci/register batch-fetch-v-reorder)
        (p.ent/with-entity
          {:list [{:id 1}
                  {:id 2}
                  {:id 3}]}))
    [{:list [:id :v]}])

  (time
    (p.eql/process
      (-> (pci/register batch-fetch-v-reorder-fixed)
          (p.ent/with-entity
            {:list [{:id 1}
                    {:id 2}
                    {:id 3}]}))
      [{:list [:id :v]}])))

(time
  (p.eql/process
    (-> (pci/register batch-fetch-v)
        (p.ent/with-entity
          {:list [{:id 1}
                  {:id 2}
                  {:id 3}]}))
    [{:list [:v]}]))
; "Elapsed time: 302.606045 msecs"
; => {:list [{:v 10} {:v 20} {:v 30}]}

(def env
  (pci/register batch-fetch-v))

(p.eql/process
  (p.ent/with-entity env
    {:list [{:id 1}
            {:id 2}
            {:id 3}]})
  [{:list [:v]}])

(time
  (p.eql/process
    (p.ent/with-entity env
      {:id   500
       :list [{:items [{:id 1}
                       {:id 2}]}
              {:items [{:id 3}
                       {:id 4}]}
              {:items [{:id 5}
                       {:id 6}]}]})
    [{:list [{:items [:v]}]}
     :v]))
; "Elapsed time: 303.566392 msecs"
; =>
; {:list [{:items [{:v 10} {:v 20}]}
;         {:items [{:v 30} {:v 40}]}
;         {:items [{:v 50} {:v 60}]}]}
