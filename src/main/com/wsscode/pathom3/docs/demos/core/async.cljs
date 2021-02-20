(ns com.wsscode.pathom3.docs.demos.core.async
  (:require [com.wsscode.pathom3.connect.indexes :as pci]
            [com.wsscode.pathom3.connect.operation :as pco]
            [com.wsscode.pathom3.interface.async.eql :as p.a.eql]
            [promesa.core :as p]))

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
  {::pco/output [::random-dog-image-url]}
  (p/let [{:keys [message]} (json-get "https://dog.ceo/api/breeds/image/random")]
    {::random-dog-image-url message}))

(pco/defresolver random-cat-image []
  {::pco/output [::random-cat-image-url]}
  (p/let [{:keys [file]} (json-get "https://aws.random.cat/meow")]
    {::random-cat-image-url file}))

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
