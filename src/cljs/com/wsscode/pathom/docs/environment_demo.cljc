(ns com.wsscode.pathom.docs.environment-demo
  (:require [com.wsscode.pathom3.connect.built-in.resolvers :as pbir]
            [com.wsscode.pathom3.connect.indexes :as pci]
            [com.wsscode.pathom3.connect.operation :as pco]
            [com.wsscode.pathom3.interface.eql :as p.eql]
            [com.wsscode.pathom3.path :as p.path]))

(pco/defresolver env-path [{::p.path/keys [path]} _]
  {::p.path/path path})

(p.eql/process (pci/register [env-path
                              (pbir/constantly-resolver ::data
                                {::nested [{} {}]})])
  [::p.path/path
   {::data [::p.path/path
            {::nested [::p.path/path]}]}])
; => {::p.path/path
;     []
;
;     ::data
;     {::p.path/path
;      [::data]
;
;      ::nested
;      [{::p.path/path [::data ::nested]}
;       {::p.path/path [::data ::nested]}]}}
