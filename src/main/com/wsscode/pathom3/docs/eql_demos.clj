(ns com.wsscode.pathom3.docs.eql-demos
  (:require [com.wsscode.pathom3.connect.built-in.resolvers :as pbir]
            [com.wsscode.pathom3.connect.indexes :as pci]
            [com.wsscode.pathom3.interface.eql :as p.eql]
            [com.wsscode.pathom3.connect.operation :as pco]
            [clojure.java.io :as io])
  (:import (java.io File)))

(def indexes
  (pci/register
    [(pbir/constantly-resolver ::pi 3.1415)
     (pbir/single-attr-resolver ::pi ::tau #(* % 2))]))

(p.eql/process indexes [::tau ::pi
                        :com.wsscode.pathom3.connect.runner/run-stats])
; => {::pi 3.1415 ::tau 6.283}

(p.eql/process indexes [{[::pi 2.3] [::tau]} :com.wsscode.pathom3.connect.runner/run-stats])

; (p.eql/process [{(:>/wilker {::first-name "Wilker" ::last-name "Silva"}) [::full-name]}])

(-> (p.eql/process indexes [::tau])
    meta)

(def indexes
  (pci/register
    [(pbir/constantly-resolver ::pi 3.1415)
     (pbir/single-attr-resolver ::pi ::tau #(* % 2))
     ; define a resolver to provide a collection of items
     (pbir/constantly-resolver ::pi-worlds
       [{::pi 3.14}
        {::pi 3.14159}
        {::pi 6.8}
        {::tau 20}
        {::pi 10 ::tau 50}])]))

(p.eql/process indexes
  ; using a map we are able to specify nested requirements from some attribute
  [{::pi-worlds [::tau ::pi]}])
; => {::pi-worlds
;      [{::tau 6.28
;        ::pi  3.14}
;       {::tau 6.28318
;        ::pi  3.14159}
;       {::tau 13.6
;        ::pi  6.8}
;       {::tau 20
;        ::pi  3.1415}
;       {::tau 50
;        ::pi  10}]}

(def union-env
  (pci/register
    [(pbir/static-table-resolver `posts :acme.post/id
       {1 {:acme.post/text "Foo"}})
     (pbir/static-table-resolver `ads :acme.ad/id
       {1 {:acme.ad/backlink "http://marketing.is-bad.com"
           :acme.ad/title    "Promotion thing"}})
     (pbir/static-table-resolver `videos :acme.video/id
       {1 {:acme.video/title "Some video"}})
     (pbir/constantly-resolver :acme/feed
       [{:acme.post/id 1}
        {:acme.ad/id 1}
        {:acme.video/id 1}])]))

(p.eql/process union-env
  [{:acme/feed
    {:acme.post/id  [:acme.post/text]
     :acme.ad/id    [:acme.ad/backlink
                     :acme.ad/title]
     :acme.video/id [:acme.video/title]}}])
; => {:acme/feed
;     [{:acme.post/text "Foo"}
;      {:acme.ad/backlink "http://marketing.site.com",
;       :acme.ad/title "Promotion thing"}
;      {:acme.video/title "Some video"}]}

(pco/defresolver file-from-path [{:keys [path]}]
  {:file (io/file path)})

(pco/defresolver file-name [{:keys [^File file]}]
  {:file-name (.getName file)})

(pco/defresolver directory? [{:keys [^File file]}]
  {:directory? (.isDirectory file)})

(pco/defresolver directory-files [{:keys [^File file directory?]}]
  {:files
   (if directory?
     (mapv #(hash-map :file %) (.listFiles file))
     ::pco/unknown-value)})

(def file-env
  (pci/register
    [file-from-path
     file-name
     directory?
     directory-files]))

(comment
  (p.eql/process file-env
    {:path "src"}
    [:file-name
     {:files [:file-name
              {:files [:file-name
                       {:files []}]}]}]))

(comment
  (p.eql/process file-env
    {:path "src"}
    [:file-name
     {:files '...}]))

(comment
  (p.eql/process file-env
    {:path "src"}
    [:file-name
     {:files 2}]))
