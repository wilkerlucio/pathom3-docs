(ns com.wsscode.pathom3.docs.demos.core.error-handling
  (:require
    [com.wsscode.pathom3.connect.operation :as pco]
    [com.wsscode.pathom3.interface.eql :as p.eql]
    [com.wsscode.pathom3.connect.indexes :as pci]
    [com.wsscode.pathom3.error :as p.error]
    [com.wsscode.pathom3.connect.built-in.resolvers :as pbir]
    [com.wsscode.pathom3.plugin :as p.plugin]))

(def identity-db
  {1 {:user/name "Martin"}})

(pco/defresolver user-identity
  [{:keys [user/id]}]
  {::pco/output [:user/name]}
  (get identity-db id))

(def movies-db
  {1 {:movie/title "Bacurau"}})

(pco/defresolver movie-details
  [{:keys [movie/id]}]
  {::pco/output [:movie/title]}
  (get movies-db id))

(def env
  (pci/register
    [user-identity movie-details]))

(p.eql/process env {:movie/id 1}
  [:user/name :movie/title])
; => #:movie{:title "Bacurau"}

(let [response (p.eql/process env {:movie/id 1}
                 [:user/name :movie/title])]
  (p.error/attribute-error response :user/name))
; => #:com.wsscode.pathom3.error{:error-type :com.wsscode.pathom3.error/attribute-unreachable}


(let [response (p.eql/process env {:movie/id 1}
                 [:movie/title])]
  (p.error/attribute-error response :user/name))
; => #:com.wsscode.pathom3.error{:error-type :com.wsscode.pathom3.error/attribute-not-requested}

;; node exception

(let [response (p.eql/process
                 (pci/register
                   (pbir/constantly-fn-resolver :error-demo
                     (fn [_] (throw (ex-info "Example Error" {})))))
                 [:error-demo])]
  (p.error/attribute-error response :error-demo))
; =>
'{:com.wsscode.pathom3.error/error-type
  :com.wsscode.pathom3.error/node-errors,

  :com.wsscode.pathom3.error/node-error-details
  {1
   {:com.wsscode.pathom3.error/error-type
    :com.wsscode.pathom3.error/node-exception,

    :com.wsscode.pathom3.error/exception
    {:cause "Example Error"
     :data  {}
     :via   [{:type    clojure.lang.ExceptionInfo
              :message "Example Error"
              :data    {}
              :at      [...]}]
     :trace ...}}}}

;; output missing

(let [response (p.eql/process
                 (pci/register
                   (pco/resolver 'x
                     {::pco/output [:a :b :c]}
                     (fn [_ _]
                       {:b 1 :c 2})))
                 [:a])]
  (p.error/attribute-error response :a))
; =>
{:com.wsscode.pathom3.error/error-type
 :com.wsscode.pathom3.error/node-errors,

 :com.wsscode.pathom3.error/node-error-details
 {1
  {:com.wsscode.pathom3.error/error-type
   :com.wsscode.pathom3.error/attribute-missing}}}

;; ancestor error

(let [response (p.eql/process
                 (pci/register
                   [(pbir/constantly-fn-resolver :error-demo
                      (fn [_] (throw (ex-info "Example Error" {}))))
                    (pbir/single-attr-resolver :error-demo :dep-on-error str)])
                 [:dep-on-error])]
  (p.error/attribute-error response :dep-on-error))
; =>
'{:com.wsscode.pathom3.error/error-type
  :com.wsscode.pathom3.error/node-errors,

  :com.wsscode.pathom3.error/node-error-details
  {1
   {:com.wsscode.pathom3.error/error-type
    :com.wsscode.pathom3.error/ancestor-error,

    :com.wsscode.pathom3.error/error-ancestor-id
    2,

    :com.wsscode.pathom3.error/exception
    {:cause "Example Error"
     :data  {}
     :via   [{:type    clojure.lang.ExceptionInfo
              :message "Example Error"
              :data    {}
              :at      [...]}]
     :trace [...]}}}}

(comment
  (meta
    (p.eql/process
      (pci/register
        [(pbir/constantly-fn-resolver :error-demo
           (fn [_] (throw (ex-info "Example Error" {}))))
         (pbir/single-attr-resolver :error-demo :dep-on-error str)])
      [:dep-on-error])))


(let [response (p.eql/process
                 (pci/register
                   [(pco/resolver 'err1
                      {::pco/output [:error-demo]}
                      (fn [_ _] (throw (ex-info "One Error" {}))))
                    (pco/resolver 'err2
                      {::pco/output [:error-demo]}
                      (fn [_ _] (throw (ex-info "Other Error" {}))))])
                 [:error-demo])]
  (p.error/attribute-error response :error-demo))
; =>
'{:com.wsscode.pathom3.error/error-type
  :com.wsscode.pathom3.error/node-errors,

  :com.wsscode.pathom3.error/node-error-details
  {1
   {:com.wsscode.pathom3.error/error-type
    :com.wsscode.pathom3.error/node-exception,

    :com.wsscode.pathom3.error/exception
    {:cause "Other Error"
     :data  {}
     :via   [{:type    clojure.lang.ExceptionInfo
              :message "Other Error"
              :data    {}
              :at      [...]}]
     :trace
            [...]}},
   2
   {:com.wsscode.pathom3.error/error-type
    :com.wsscode.pathom3.error/node-exception,

    :com.wsscode.pathom3.error/exception
    {:cause "One Error"
     :data  {}
     :via   [{:type    clojure.lang.ExceptionInfo
              :message "One Error"
              :data    {}
              :at      [...]}]
     :trace [...]}}}}

(comment
  (spit "static/viz/error-handling/multiple-errors.edn"
    (with-out-str
      (clojure.pprint/pprint
        (meta
          (p.eql/process
            (pci/register
              [(pco/resolver 'err1
                 {::pco/output [:error-demo]}
                 (fn [_ _] (throw (ex-info "One Error" {}))))
               (pco/resolver 'err2
                 {::pco/output [:error-demo]}
                 (fn [_ _] (throw (ex-info "Other Error" {}))))])
            [:error-demo]))))))


(p.eql/process
  (pci/register
    (pco/mutation 'doit
      {}
      (fn [_ _]
        (throw (ex-info "Mutation error" {})))))
  ['(doit)])
; =>
'{doit {:com.wsscode.pathom3.connect.runner/mutation-error
        {:cause "Mutation error"
         :data  {}
         :via   [{:type    clojure.lang.ExceptionInfo
                  :message "Mutation error"
                  :data    {}
                  :at      [...]}]
         :trace [...]}}}


(p.eql/process
  (-> (pci/register
        [(pco/resolver 'err1
           {::pco/output [:error-demo]}
           (fn [_ _] (throw (ex-info "One Error" {}))))
         (pco/resolver 'err2
           {::pco/output [:error-demo]}
           (fn [_ _] (throw (ex-info "Other Error" {}))))])
      (p.plugin/register
        {::p.plugin/id 'err
         :com.wsscode.pathom3.connect.runner/wrap-resolver-error
                       (fn [_]
                         (fn [env node error]
                           (println "Error: " (ex-message error))))}))
  [:error-demo])

(p.eql/process
  (-> (pci/register
        (pco/mutation 'doit
          {}
          (fn [_ _]
            (throw (ex-info "Mutation error" {})))))
      (p.plugin/register
        {::p.plugin/id 'err
         :com.wsscode.pathom3.connect.runner/wrap-mutation-error
                       (fn [_]
                         (fn [env ast error]
                           (println "Error on" (:key ast) "-" (ex-message error))))}))
  ['(doit)])
; =>
'{doit {:com.wsscode.pathom3.connect.runner/mutation-error
        #error {:cause "Mutation error"
                :data  {}
                :via   [{:type    clojure.lang.ExceptionInfo
                         :message "Mutation error"
                         :data    {}
                         :at      [...]}]
                :trace [...]}}}
