(ns com.wsscode.pathom.docs.smart-maps-demo
  (:require [com.wsscode.pathom3.connect.indexes :as pci]
            [com.wsscode.pathom3.connect.operation :as pco]
            [com.wsscode.pathom3.interface.smart-map :as psm]))

(pco/defresolver full-name [{::keys [first-name last-name]}]
  ::full-name (str first-name " " last-name))

(def indexes (pci/register full-name))

(def person-data {::first-name "David" ::last-name "Nolan"})

(def smart-map (psm/smart-map indexes person-data))

(::full-name smart-map) ; => "David Nolan"
