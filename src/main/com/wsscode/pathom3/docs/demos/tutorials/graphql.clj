(ns com.wsscode.pathom3.docs.demos.tutorials.graphql
  (:require
    [clojure.data.json :as json]
    [com.wsscode.pathom3.graphql :as p.gql]
    [com.wsscode.pathom3.interface.eql :as p.eql]
    [org.httpkit.client :as http]))

; first a helper function to request GraphQL queries to the Star Wars API
(defn request-swapi-graphql [query]
  (-> @(http/request
         {:url     "https://swapi-graphql.netlify.app/.netlify/functions/index"
          :method  :post
          :headers {"Content-Type" "application/json"
                    "Accept"       "*/*"}
          :body    (json/write-str {:query query})})
      :body
      json/read-str))

; lets create the environment
(def env
  (-> {}
      ; this helper will pull the schema and register it in the environment
      (p.gql/connect-graphql
        {::p.gql/namespace "swapi"}
        request-swapi-graphql)))

(comment
  ; request all people and the title of the films they participate
  (p.eql/process
    env
    [{:swapi.Root/allPeople
      [{:swapi.PeopleConnection/people
        [:swapi.Person/name
         {:swapi.Person/filmConnection
          [{:swapi.PersonFilmsConnection/films
            [:swapi.Film/title]}]}]}]}]))
