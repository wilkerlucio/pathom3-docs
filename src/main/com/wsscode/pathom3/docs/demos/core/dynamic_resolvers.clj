(ns com.wsscode.pathom3.docs.demos.core.dynamic-resolvers
  (:require
    [clojure.data.json :as json]
    [com.wsscode.misc.coll :as coll]
    [com.wsscode.pathom3.connect.indexes :as pci]
    [com.wsscode.pathom3.connect.operation :as pco]
    [com.wsscode.pathom3.graphql :as p.gql]
    [com.wsscode.pathom3.interface.eql :as p.eql]
    [org.httpkit.client :as http]
    [com.wsscode.pathom3.connect.planner :as pcp]
    [edn-query-language.core :as eql]))

; helper function to go from URL to map data
(defn request-api [url]
  (-> @(http/request {:url url})
      :body json/read-str))

(defn adapt-person [{:strs [name films]}]
  {:swapi.person/name  name
   :swapi.person/films (mapv #(array-map :swapi.film/url %) films)})

(pco/defresolver all-people []
  {::pco/output
   [{:swapi/all-people
     [:swapi.person/name
      {:swapi.person/films
       [:swapi.film/url]}]}]}
  {:swapi/all-people
   (let [results (-> (request-api "https://swapi.dev/api/people")
                     (get "results"))]
     (mapv adapt-person results))})

(defn adapt-film [film]
  (coll/map-keys #(keyword "swapi.film" %) film))

(pco/defresolver film-by-url [{:keys [swapi.film/url]}]
  {::pco/output
   [:swapi.film/title]}
  (-> (request-api url)
      (adapt-film)))

(def env
  (pci/register
    [all-people
     film-by-url]))

(comment
  (p.eql/process
    env
    [{:swapi/all-people
      [:swapi.person/name]}])

  (p.eql/process
    env
    [{:swapi/all-people
      [:swapi.person/name
       {:swapi.person/films
        [:swapi.film/title]}]}]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn request-swapi-graphql [query]
  (-> @(http/request
         {:url     "https://swapi-graphql.netlify.app/.netlify/functions/index"
          :method  :post
          :headers {"Content-Type" "application/json"
                    "Accept"       "*/*"}
          :body    (json/write-str {:query query})})
      :body
      json/read-str))

(defn adapt-person [{:strs [name filmConnection]}]
  {:swapi.person/name  name
   :swapi.person/films (mapv #(array-map :swapi.film/id (get % "id"))
                         (get filmConnection "films"))})

(pco/defresolver all-people []
  {::pco/output
   [{:swapi/all-people
     [:swapi.person/name
      {:swapi.person/films
       [:swapi.film/id]}]}]}
  {:swapi/all-people
   (let [results (-> (request-swapi-graphql "query { allPeople { people { name filmConnection { films { id } } } } }")
                     (get-in ["data" "allPeople" "people"]))]
     (mapv adapt-person results))})

(defn adapt-film [film]
  (coll/map-keys #(keyword "swapi.film" %) film))

(pco/defresolver film-by-id [{:keys [swapi.film/id]}]
  {::pco/output
   [:swapi.film/title]}
  (-> (request-swapi-graphql (str "query { film(id: \"" id "\") { title } }"))
      (get-in ["data" "film"])
      adapt-film))

(def gql-env
  (-> (pci/register
        [all-people
         film-by-id])
      ((requiring-resolve 'com.wsscode.pathom.viz.ws-connector.pathom3/connect-env)
       "swapi")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def env
  (-> {}
      (p.gql/connect-graphql
        {::p.gql/namespace "swapi"}
        request-swapi-graphql)))


(comment
  (p.eql/process
    env
    [{:swapi.gql.Root/allPeople
      [{:swapi.gql.PeopleConnection/people
        [:swapi.gql.Person/name
         {:swapi.gql.Person/filmConnection
          [{:swapi.gql.PersonFilmsConnection/films
            [:swapi.gql.Film/title]}]}]}]}]))

(comment
  (tap> (::pci/index-resolvers gql-env2))



  (pcp/compute-plan-snapshots
    (assoc gql-env2
      :edn-query-language.ast/node (eql/query->ast
                                     [{:swapi/all-people
                                       [:swapi.Person/name]}]))
    )

  (let [id "ZmlsbXM6MQ=="]
    (request-swapi-graphql (str "query { film(id: \"" id "\") { title } }")))

  (p.eql/process
    gql-env
    [{:swapi/all-people
      [:swapi.person/name
       {:swapi.person/films
        [:swapi.film/title]}]}])

  (p.eql/process
    gql-env
    [{:swapi.gql.Root/allPeople
      [{:swapi.gql.PeopleConnection/people
        [:swapi.gql.Person/name
         {:swapi.gql.Person/filmConnection
          [{:swapi.gql.PersonFilmsConnection/films
            [:swapi.gql.Film/title]}]}]}]}

     {:swapi/all-people
      [:swapi.person/name
       {:swapi.person/films
        [:swapi.film/title]}]}])

  (-> gql-env
      ((requiring-resolve 'com.wsscode.pathom.viz.ws-connector.pathom3/connect-env)
       "swapi2"))

  (keys (adapt-film (request-api "https://swapi.dev/api/films/1/")))

  (adapt-person (request-api "https://swapi.dev/api/people/1")))
