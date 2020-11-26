(ns com.wsscode.pathom3.docs.mutations
  (:require [clojure.java.io :as io]
            [com.wsscode.pathom3.connect.indexes :as pci]
            [com.wsscode.pathom3.connect.operation :as pco]
            [com.wsscode.pathom3.interface.eql :as p.eql]))

(pco/defmutation save-file [{::keys [file-path file-content] :as file}]
  {::pco/op-name 'io/save-my-file}
  (spit file-path file-content)
  file)

(pco/defmutation error [] (throw (ex-info "Error" {:data true})))

(pco/defresolver file-size [{::keys [file-path]}]
  {::file-size (.length (io/file file-path))})

(def env (pci/register [save-file file-size error]))

(comment

  (p.eql/process env
    ['(io/save-my-file {::file-path "./file.txt" ::file-content "contents here"})])

  (p.eql/process env
    [{`(save-file {::file-path "./file.txt" ::file-content "contents here"})
      [::file-path
       ::file-size]}]))

(p.eql/process env
  [`(error {::file-path "./file.txt" ::file-content "contents here"})])

