(ns com.wsscode.pathom3.docs.importer
  (:require [helix.core :as h]))

(h/defnc Importer [{:keys [module] :as props}]
  (let [standard-props (dissoc props :module)]
    ))
