(ns com.wsscode.pathom3.docs.demos.core.dynamic-resolvers
  (:require
    [com.wsscode.pathom3.connect.built-in.resolvers :as pbir]
    [com.wsscode.pathom3.connect.foreign :as pcf]
    [com.wsscode.pathom3.connect.indexes :as pci]
    [com.wsscode.pathom3.connect.operation :as pco]
    [com.wsscode.pathom3.connect.operation.transit :as pcot]
    [com.wsscode.pathom3.connect.planner :as pcp]
    [com.wsscode.pathom3.interface.eql :as p.eql]
    [com.wsscode.transito :as transito]
    [org.httpkit.client :as http]
    [org.httpkit.server :as server]))

(defonce servers* (atom {}))

(defn make-server [port env]
  (if-let [s (get @servers* port)]
    (s))

  (let [request (p.eql/boundary-interface env)
        handler (fn [{:keys [body]}]
                  (let [req (transito/read-str (slurp body)
                              {:handlers pcot/read-handlers})]
                    {:status 200
                     :body   (transito/write-str
                               (request req)
                               {:handlers pcot/write-handlers})}))
        server  (server/run-server handler
                  {:port port})]
    (swap! servers* assoc port server)
    server))

(defn http-req [port req]
  (-> @(http/request
         {:url    (str "http://localhost:" port)
          :method :post
          :body   (transito/write-str req {:handlers pcot/write-handlers})})
      :body
      slurp
      (transito/read-str {:handlers pcot/read-handlers})))

(defn http-interface [port env]
  (make-server port env)
  #(http-req port %))

(def users-data
  {1 {:user/id    1
      :user/name  "Christop Rippin"
      :company/id 1}
   2 {:user/id    2
      :user/name  "Miss Annabell Kessler"
      :company/id 1}
   3 {:user/id    3
      :user/name  "Demarco Padberg"
      :company/id 1}
   4 {:user/id    4
      :user/name  "Daren Wolff Jr."
      :company/id 1}
   5 {:user/id    5
      :user/name  "Carlo Schmitt"
      :company/id 2}
   6 {:user/id    6
      :user/name  "Meda Hegmann"
      :company/id 2}
   7 {:user/id    7
      :user/name  "Onie Schimmel"
      :company/id 3}
   8 {:user/id    8
      :user/name  "Mayra Raynor"
      :company/id 3}
   9 {:user/id    9
      :user/name  "Bobbie Grant"
      :company/id 3}})

(def company-data
  {1 {:company/id   1
      :company/name "Gladys King Inc"}
   2 {:company/id   2
      :company/name "Funk-Stamm"}
   3 {:company/id   3
      :company/name "Carter, Harber and Jacobi"}})

(pco/defresolver all-users []
  {::pco/output
   [{:user/all
     [:user/id
      :user/name
      :company/id]}]}
  {:user/all
   (vec (vals users-data))})

(pco/defresolver user-by-id [{:user/keys [id]}]
  {::pco/output
   [:user/name
    :company/id]}
  (get users-data id))

(pco/defresolver company-by-id [{:company/keys [id]}]
  {::pco/output
   [:company/name]}
  (get company-data id))

(def env
  (-> (pci/register
        [all-users
         user-by-id
         company-by-id])
      (pcp/with-plan-cache (atom {}))))

(def request
  (http-interface 8087 env))

(def ips
  {1 "82949-5679"
   2 "39359-0412"
   3 "40703-7676"
   4 "85822-1129"
   5 "03074-6343"
   6 "09986-9393"
   7 "74750-0040"
   8 "82239"
   9 "81444-2468"})

(pco/defresolver remote-users
  "Forward user list request to remote server"
  []
  {::pco/output
   [{:user/all
     [:user/id
      :user/name
      :company/id]}]}
  (request
    [{:user/all
      [:user/id
       :user/name
       :company/id]}]))

(pco/defresolver remote-company
  "Forward company data request to remote server"
  [{:keys [company/id]}]
  {::pco/output
   [:company/name]}
  (request
    {:pathom/entity
     {:company/id id}

     :pathom/eql
     [:company/name]}))

(def client-env
  (-> (pci/register
        [(pcf/foreign-register request)
         (pbir/static-attribute-map-resolver
           :user/id :user/ip ips)])
      (pcp/with-plan-cache (atom {}))
      ((requiring-resolve 'com.wsscode.pathom.viz.ws-connector.pathom3/connect-env)
       "debug")))

(def client-request
  (p.eql/boundary-interface client-env))

(comment
  (request
    [:user/all])

  (client-request
    [{:user/all
      [:user/name
       :user/ip
       :company/name]}])

  (take 10 ((requiring-resolve 'faker.name/names)))
  (take 10 ((requiring-resolve 'faker.name/names)))
  (take 10 (repeatedly #((requiring-resolve 'faker.address/zip-code))))
  (require-res 'faker.name))
