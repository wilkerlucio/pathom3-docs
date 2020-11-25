(ns com.wsscode.pathom3.docs.placeholder
  (:require [com.wsscode.pathom3.connect.operation :as pco]
            [com.wsscode.pathom3.interface.eql :as p.eql]
            [com.wsscode.pathom3.connect.indexes :as pci]
            [com.wsscode.pathom3.entity-tree :as p.ent]))

(pco/defresolver full-name [{::keys [first-name last-name]}]
  {::full-name (str first-name " " last-name)})

(def env (pci/register full-name))

(p.eql/process env
  [{'(:>/bret {::first-name "Bret" ::last-name "Victor"})
    [::full-name]}])

(p.eql/process env
  [{'(:>/bret {::first-name "Bret" ::last-name "Victor"})
    [::full-name
     {'(:>/bard {::first-name "Bard"})
      [::full-name]}]}])
; {:>/bret
;   {:com.wsscode.pathom3.docs.placeholder/full-name "Bret Victor",
;    :>/bard
;    {:com.wsscode.pathom3.docs.placeholder/full-name "Bard Victor"}}}

; display user name at header
(def header-view-eql
  [:acme.customer/full-name])

(def latest-orders-eql
  [{:acme.customer/orders
    [:acme.order/id
     :acme.order/description]}])

(def address-eql
  [:acme.address/street
   :acme.address/number
   :acme.address/zipcode])

(into [] (concat header-view-eql latest-orders-eql addresses-eql))
; [:acme.customer/full-name
;  {:acme.customer/orders [:acme.order/id :acme.order/description]}
;  {:acme.customer/addresses
;   [:acme.address/street :acme.address/number :acme.address/zipcode]}]
(def expensive-orders-eql
  [{'(:acme.customer/orders {::filter-price-gt 100})
    [:acme.order/id
     :acme.order/description]}])

(defn render-address [{:acme.address/keys [street number zipcode]}]
  (str "Send to: " street ", " number ", " zipcode))

(defn render-address [{:acme.address/keys  [street number zipcode]
                       :acme.customer/keys [full-name]}]
  (str "Send to " full-name " at: " street ", " number ", " zipcode))

[{:>/header header-view-eql}
 {:>/latest latest-orders-eql}
 {:>/address address-eql}
 {:>/expensive expensive-orders-eql}]
