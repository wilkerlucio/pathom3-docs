(ns com.wsscode.pathom3.docs.demos.core.async
  (:require [com.wsscode.pathom3.connect.indexes :as pci]
            [com.wsscode.pathom3.connect.operation :as pco]
            [com.wsscode.pathom3.interface.async.eql :as p.a.eql]
            [com.wsscode.pathom3.connect.operation.transit :as pcot]
            [com.wsscode.transito :as tt]
            [promesa.core :as p]
            [com.wsscode.pathom.viz.ws-connector.pathom3 :as pvc]
            [com.wsscode.pathom3.connect.foreign :as pcf]
            [com.wsscode.pathom3.connect.built-in.resolvers :as pbir]))

(defn json-get [url]
  (p/let [resp (js/fetch url)
          json (.json resp)]
    (js->clj json :keywordize-keys true)))

(pco/defresolver age-from-name [{::keys [first-name]}]
  {::pco/output [::age]}
  (p/let [{:keys [age]} (json-get (str "https://api.agify.io/?name=" first-name))]
    {::age age}))

(def env
  (pci/register
    age-from-name))

(comment
  (p/let [res (json-get "https://api.jikan.moe/v3/search/anime?q=death note")]
    (def last-res res)
    (js/console.log "!! res" res))

  (p/let [res (json-get "https://api.jikan.moe/v3/anime/11741")]
    (js/console.log "!! res" res))

  (com.wsscode.pathom3.format.eql/data->query
    last-res))

(comment
  (p/let [res (p.a.eql/process env
                {::first-name "Ada"}
                [::first-name ::age])]
    (cljs.pprint/pprint res)))

(pco/defresolver random-dog-image []
  {::pco/output [::random-dog-image-url]
   ::pco/cache? false}
  (p/let [{:keys [message]} (json-get "https://dog.ceo/api/breeds/image/random")]
    {::random-dog-image-url message}))

(pco/defresolver random-cat-image []
  {::pco/output [::random-cat-image-url]
   ::pco/cache? false}
  (p/let [{:keys [file]} (json-get "https://aws.random.cat/meow")]
    {::random-cat-image-url file}))

(pco/defresolver random-pets [{::keys [random-dog-image-url
                                       random-cat-image-url]}]
  {::random-pets [random-dog-image-url
                  random-cat-image-url]})

(pco/defresolver search-animes [{:jikan.anime/keys [search-title]}]
  {::pco/output [{:jikan.search/animes
                  [:jikan.mal/id
                   :jikan.anime/airing
                   :jikan.anime/end_date
                   :jikan.anime/episodes
                   :jikan.anime/image_url
                   :jikan.anime/members
                   :jikan.anime/rated
                   :jikan.anime/score
                   :jikan.anime/start_date
                   :jikan.anime/synopsis
                   :jikan.anime/title
                   :jikan.anime/type
                   :jikan.anime/url]}]})

(defn transit-request [url body]
  (p/let [resp (js/fetch url
                 #js {:method "POST"
                      :body   (tt/write-str body {:handlers pcot/write-handlers})})
          text (.text resp)]
    (tt/read-str text {:handlers pcot/read-handlers})))

(defn pathom-remote [request]
  (transit-request "https://europe-west2-pathomdemos.cloudfunctions.net/development-pathom-server-demo" request))

(def users
  {1 {:person/first-name "Wilker"
      :person/last-name  "Silva"}
   2 {:person/first-name "Denise"
      :person/last-name  "Mascarenhas"}})

(def async-env
  (-> {::p.a.eql/parallel? false}
      (pci/register
        [random-dog-image
         random-cat-image
         random-pets
         (pbir/static-table-resolver :person/id users)])
      (pvc/connect-env "js")))

(comment
  (p/let [res (p.a.eql/process async-env
                {:person/id 2}
                [:person/full-name])]
    (js/console.log "!! " res)))

; region parallel

(comment
  (p/let [res (p.a.eql/process async-env
                {:items [{} {} {} {} {}]}
                [{:items
                  [::random-pets]}])]
    (js/console.log "!! " res)))

; endregion
