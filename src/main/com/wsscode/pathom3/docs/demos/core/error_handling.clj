(ns com.wsscode.pathom3.docs.demos.core.error-handling
  (:require
    [com.wsscode.pathom3.connect.operation :as pco]
    [com.wsscode.pathom3.interface.eql :as p.eql]
    [com.wsscode.pathom3.connect.indexes :as pci]
    [com.wsscode.pathom3.error :as p.error]
    [com.wsscode.pathom3.connect.built-in.resolvers :as pbir]
    [com.wsscode.pathom3.plugin :as p.plugin]))

; region strict mode

; Attribute doesn't exist in the indexes
(comment
  (p.eql/process
    (pci/register
      (pbir/constantly-resolver :a "foo"))
    [:b]))
; => EXCEPTION: Pathom can't find a path for the following elements in the query: [:b] at path []

; Attribute exists in the indexes, but the available data isn't enough to reach it
(comment
  (p.eql/process
    (pci/register
      (pbir/single-attr-resolver :a :b "foo"))
    [:b]))
; => EXCEPTION: Pathom can't find a path for the following elements in the query: [:b] at path []

; An extended version of this is also true in case of nested inputs, if Pathom can't see
; a Pathom for a nested input requirement, it will consider it a plan failure.
(comment
  (p.eql/process
    (pci/register
      [(pco/resolver 'nested-provider
         {::pco/output [{:a [:b]}]}
         (fn [_ _]
           {:a {:b 1}}))
       (pco/resolver 'nested-requires
         {::pco/input  [{:a [:c]}]
          ::pco/output [:d]}
         (fn [_ _]
           {:d 10}))])
    [:d]))
; => EXCEPTION: Pathom can't find a path for the following elements in the query: [:c] at path [:a]

; A resolver throw an exception*
(comment
  (p.eql/process
    (pci/register
      (pco/resolver 'error
        {::pco/output [:error]}
        (fn [_ _]
          (throw (ex-info "Deu ruim." {})))))
    [:error]))
; => EXCEPTION: Deu ruim.

; Attribute wasn't in the final output after all the process
(comment
  (p.eql/process
    (pci/register
      (pco/resolver 'data
        {::pco/output [:a :b :c]}
        (fn [_ _]
          {:a 10})))
    [:c]))
; => EXCEPTION: Required attributes missing: [:c] at path []

(comment
  (p.eql/process
    (pci/register
      (pco/resolver 'data
        {::pco/output [:a :b :c]}
        (fn [_ _]
          {:a 10})))
    [:a (pco/? :c) (pco/? :not-on-index)]))
; => {:a 10} - no exception, ignore the absence of :c and :not-on-index

; endregion

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
    {::p.error/lenient-mode? true}
    [user-identity movie-details]))

(p.eql/process env {:movie/id 1}
  [:user/name :movie/title])
; => {:movie/title "Bacurau",
;     :com.wsscode.pathom3.connect.runner/attribute-errors {:user/name {:com.wsscode.pathom3.error/error-type :com.wsscode.pathom3.error/attribute-unreachable}}}

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
                   {::p.error/lenient-mode? true}
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
                   {::p.error/lenient-mode? true}
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
                   {::p.error/lenient-mode? true}
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
        {::p.error/lenient-mode? true}
        [(pbir/constantly-fn-resolver :error-demo
           (fn [_] (throw (ex-info "Example Error" {}))))
         (pbir/single-attr-resolver :error-demo :dep-on-error str)])
      [:dep-on-error])))


(let [response (p.eql/process
                 (pci/register
                   {::p.error/lenient-mode? true}
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
    {::p.error/lenient-mode? true}
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
        {::p.error/lenient-mode? true}
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
                         (fn [_env _node error]
                           (println "Error: " (ex-message error))))}))
  [:error-demo])

(p.eql/process
  (-> (pci/register
        {::p.error/lenient-mode? true}
        (pco/mutation 'doit
          {}
          (fn [_ _]
            (throw (ex-info "Mutation error" {})))))
      (p.plugin/register
        {::p.plugin/id 'err
         :com.wsscode.pathom3.connect.runner/wrap-mutation-error
                       (fn [_]
                         (fn [_env ast error]
                           (println "Error on" (:key ast) "-" (ex-message error))))}))
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
