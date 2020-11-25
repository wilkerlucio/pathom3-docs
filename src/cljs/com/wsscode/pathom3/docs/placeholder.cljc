(ns com.wsscode.pathom3.docs.placeholder
  (:require [com.wsscode.pathom3.connect.operation :as pco]
            [com.wsscode.pathom3.interface.eql :as p.eql]
            [com.wsscode.pathom3.connect.indexes :as pci]
            [com.wsscode.pathom3.entity-tree :as p.ent]))

(pco/defresolver full-name [{::keys [first-name last-name]}]
  {::full-name (str first-name " " last-name)})

(def env (pci/register full-name))

(p.eql/process env
  [{'(:>/bret {::first-name "Bret" ::last-name "Victor"})
    [::full-name]}])

(p.eql/process env
  [{'(:>/bret {::first-name "Bret" ::last-name "Victor"})
    [::full-name
     {'(:>/bard {::first-name "Bard"})
      [::full-name]}]}])
; {:>/bret
;   {:com.wsscode.pathom3.docs.placeholder/full-name "Bret Victor",
;    :>/bard
;    {:com.wsscode.pathom3.docs.placeholder/full-name "Bard Victor"}}}

(p.eql/process
  (p.ent/with-entity env {::first-name "Brod" ::last-name "Victor"})
  [{'(:>/bret {::first-name "Bret" ::last-name "Victor"})
    [::full-name]}
   ::full-name])
