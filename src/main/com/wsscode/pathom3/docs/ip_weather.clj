(ns com.wsscode.pathom3.docs.ip-weather
  (:require
    [cheshire.core :as json]
    [com.wsscode.pathom3.connect.indexes :as pci]
    [com.wsscode.pathom3.connect.operation :as pco]
    [com.wsscode.pathom3.interface.eql :as p.eql]
    [org.httpkit.client :as http]))

(pco/defresolver ip->lat-long
  [{:keys [ip]}]
  {::pco/output [:latitude :longitude]}
  (-> (slurp (str "https://get.geojs.io/v1/ip/geo/" ip ".json"))
      (json/parse-string keyword)
      (select-keys [:latitude :longitude])))

(pco/defresolver latlong->temperature
  [{:keys [latitude longitude]}]
  {:temperature
   (-> @(http/request
          {:url (str "http://www.7timer.info/bin/api.pl?lon=" longitude
                     "&lat=" latitude
                     "&product=astro&output=json")})
       :body
       (json/parse-string keyword)
       :dataseries first :temp2m)})

(def env
  (pci/register [ip->lat-long
                 latlong->temperature]))

(defn main [args]
  (let [temp (p.eql/process-one env args :temperature)]
    (println (str "It's currently " temp "C at " (pr-str args)))))
'
{:com.wsscode.pathom3.connect.indexes/index-resolvers
 {...}

 :com.wsscode.pathom3.connect.indexes/index-attributes
 {...}

 :com.wsscode.pathom3.connect.indexes/index-io
 {...}

 :com.wsscode.pathom3.connect.indexes/index-oir
 {:latitude    {{:ip {}} #{com.wsscode.pathom3.docs.ip-weather/ip->lat-long}},
  :longitude   {{:ip {}} #{com.wsscode.pathom3.docs.ip-weather/ip->lat-long}},
  :temperature {{:latitude  {}, :longitude {}} #{com.wsscode.pathom3.docs.ip-weather/latlong->temperature}}}}

; available context
{:ip "198.29.213.3"}

; request
:temperature
(comment
  env

  (-> {:ip "198.29.213.3"}
      ip->lat-long
      latlong->temperature))
