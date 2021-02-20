(ns com.wsscode.pathom3.docs.introduction
  (:require [com.wsscode.pathom3.connect.operation :as pco]
            [com.wsscode.pathom3.connect.indexes :as pci]
            [com.wsscode.pathom3.interface.eql :as p.eql]
            [com.wsscode.pathom3.interface.smart-map :as psm]
            [com.wsscode.pathom3.entity-tree :as p.ent]
            [com.wsscode.pathom3.connect.built-in.resolvers :as pbir]
            [clojure.string :as str]))

(def temperatures
  {"São Paulo" 23})

(pco/defresolver temperature-from-city [{:keys [city]}]
  {:temperature (get temperatures city)})

(pco/defresolver cold? [{:keys [temperature cold-threshold]}]
  {:cold? (< temperature cold-threshold)})

(pco/defresolver cold? [{:keys [temperature]}]
  {:cold? (< temperature 20)})

(def users
  {1 {:first-name "Jessica"
      :last-name  "Miyazaki"}})

(pco/defresolver full-name [{:keys [first-name last-name]}]
  {:full-name (str first-name " " last-name)})

(pco/defresolver country-from-ip [{:keys [ip]}]
  {:country (str/trim (slurp (str "https://get.geojs.io/v1/ip/country/" ip)))})

(pco/defresolver user-by-id [{:keys [user-id]}]
  {::pco/output [:first-name :last-name]}
  (get users user-id))

#_
(def indexes
  (pci/register [temperature-from-city its-cold?
                 (pbir/constantly-resolver :cold-threshold 20)]))

(def indexes
  (pci/register [full-name user-by-id country-from-ip
                 (pbir/single-attr-resolver :full-name :greet-full-name
                   #(str "Hello " % "!!!"))]))

(p.eql/process
  indexes
  {:city           "São Paulo"
   :cold-threshold 25}
  [:is-cold?])

(p.eql/process
  indexes
  {:ip "197.46.189.5"}
  [:country ])
