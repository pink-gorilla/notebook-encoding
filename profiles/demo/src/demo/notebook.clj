(ns demo.notebook
  (:require
   [taoensso.timbre :refer [debug info error]]
   [pinkgorilla.encoding.default-config] ; side-effects
   [pinkgorilla.storage.impl.gist :refer [save-gist]]
   [pinkgorilla.storage.impl.repo :refer [save-repo]]
   [pinkgorilla.creds :refer [creds]]
   [pinkgorilla.storage.protocols :refer [create-storage]]
   [pinkgorilla.notebook.persistence :refer [load-notebook save-notebook]]

   ; picasso
   [picasso.document.transactor :refer [exec notebook]]
   [picasso.default-config] ; side-effects
   [picasso.data.document :as data] ; sample-data
   )
  (:gen-class))



(defn -main [mode]
  (let [s (create-storage {:type :file
                           :filename "demo-nb.cljg"
                           ;:filename "resources/notebooks/document/specs-doc.cljg"
                           })
        s2 (create-storage {:type :file
                            :filename "demo2-nb.cljg"})
        tokens {}]
    (info "storage: " s)
    (case mode
      "save" (info (save-notebook s tokens data/document))
      "load" (let [nb (load-notebook s tokens)]
               (info "loaded:" nb)
               (save-notebook s2 tokens data/document)))))



