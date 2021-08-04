(ns com.wsscode.pathom3.docs.demos.core.foreign
  (:require
    [com.wsscode.pathom3.connect.foreign :as pcf]
    [com.wsscode.pathom3.connect.indexes :as pci]
    [com.wsscode.pathom3.connect.operation :as pco]
    [com.wsscode.pathom3.connect.operation.transit :as pcot]
    [com.wsscode.pathom3.interface.eql :as p.eql]
    [com.wsscode.transito :as transito]
    [org.httpkit.client :as http]))

(def todo-db
  {1 {:todo/id    1
      :todo/title "Write foreign docs"
      :todo/done? true}
   2 {:todo/id    2
      :todo/title "Integrate the whole internet"
      :todo/done? false}})

(pco/defresolver todo-items []
  {::pco/output
   [{:app/all-todos
     [:todo/id]}]}
  ; export only the ids to force the usage of another resolver for
  ; the details
  {:app/all-todos
   [{:todo/id 1}
    {:todo/id 2}]})

(pco/defresolver todo-by-id [{:todo/keys [id]}]
  {::pco/output
   [:todo/id
    :todo/title
    :todo/done?]}
  (get todo-db id))

(def foreign-env
  (pci/register
    {:com.wsscode.pathom3.connect.planner/plan-cache* (atom {})}
    [todo-items
     todo-by-id]))

(def foreign-request
  (p.eql/boundary-interface foreign-env))

(comment
  (foreign-request
    [{:app/all-todos
      [:todo/title
       :todo/done?]}]))

(def canceled-todos
  #{2})

(pco/defresolver todo-canceled? [{:todo/keys [id]}]
  {:todo/cancelled? (contains? canceled-todos id)})

(defn tread [s]
  (transito/read-str s {:handlers pcot/read-handlers}))

(defn twrite [x]
  (transito/write-str x {:handlers pcot/write-handlers}))

(defn request-remote-pathom [url request]
  (-> @(http/request
         {:url     url
          :method  :post
          :headers {"Content-Type" "application/transit+json"
                    "Accept"       "application/transit+json"}
          :body    (twrite request)})
      :body
      tread))

(def url "https://southamerica-east1-pathomdemos.cloudfunctions.net/development-pathom-server-demo")

(def env
  (pci/register
    {:com.wsscode.pathom3.connect.planner/plan-cache* (atom {})}
    [; pull the remote instance
     (pcf/foreign-register
       #(request-remote-pathom url %))
     ; add our custom resolver on top
     todo-canceled?]))

(comment
  (p.eql/process
    env
    [{:app/all-todos
      [:todo/title
       :todo/done?
       :todo/cancelled?]}]))
