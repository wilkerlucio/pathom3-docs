(ns com.wsscode.pathom.docs.smart-maps-demo
  (:require [com.wsscode.pathom3.connect.indexes :as pci]
            [com.wsscode.pathom3.connect.operation :as pco]
            [com.wsscode.pathom3.interface.smart-map :as psm]))

(pco/defresolver full-name [{::keys [first-name last-name]}]
  ::full-name (str first-name " " last-name))

(def indexes (pci/register full-name))

(def person-data {::first-name "Anne" ::last-name "Frank"})

(def smart-map (psm/smart-map indexes person-data))

(::full-name smart-map) ; => "Anne Frank"

;; demo 2

(pco/defresolver full-name [{::keys [first-name last-name]}]
  ::full-name (str first-name " " last-name))

(pco/defresolver anne []
  ::anne {::first-name "Anne" ::last-name "Frank"})

(def indexes (pci/register [full-name anne]))

(def smart-map (psm/smart-map indexes))

(::anne smart-map) ; => {::first-name "Anne" ::last-name "Frank"}

; nested access
(-> smart-map ::anne ::full-name) ; => "Anne Frank"

;; demo 3

(pco/defresolver full-name [{::keys [first-name last-name]}]
  ::full-name (str first-name " " last-name))

(pco/defresolver stars []
  ::start-wars-characters
  [{::first-name "Luke" ::last-name "Skywalker"}
   {::first-name "Darth" ::last-name "Vader"}
   {::first-name "Han" ::last-name "Solo"}])

(def indexes (pci/register [full-name stars]))

(def smart-map (psm/smart-map indexes))

; nested access on sequences
(mapv ::full-name (::start-wars-characters smart-map))
; => ["Luke Skywalker"
;     "Darth Vader"
;     "Han Solo"]

;; changes demo

(pco/defresolver full-name [{::keys [first-name last-name]}]
  ::full-name (str first-name " " last-name))

(def indexes (pci/register full-name))

(def person-data {::first-name "John" ::last-name "Lock"})

(def smart-map (psm/smart-map indexes person-data))

(::full-name smart-map) ; => "John Lock"

(-> smart-map
    (assoc ::last-name "Oliver")
    ::full-name)
; => "John Oliver", the full-name gets re-computed due to the change
