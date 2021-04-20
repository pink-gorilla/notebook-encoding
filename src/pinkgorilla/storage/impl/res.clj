(ns pinkgorilla.storage.impl.res
  (:require
   [taoensso.timbre :refer [info error]]
   [clojure.java.io :as io]
   [pinkgorilla.storage.protocols :refer [FromFilename Storage create-storage Save Load]])
  (:import
   [pinkgorilla.storage.res  StorageRes]))

(extend-type StorageRes
     ;Save
  #_(storage-save [self notebook tokens]
                  (if (nil? notebook)
                    (throw (Exception. (str "NOT Saving EMPTY Notebook to file: " (:filename self))))
                    (do
                      (info "Saving Notebook to file: " (:filename self) " size:" (count notebook))
                      (spit (:filename self) notebook)
                      {:filename (:filename self)})))
  Load
  (storage-load [self tokens]
    (let [res-name (str "notebooks/" (:filename self))
          r (io/resource res-name)]
      (info "Loading Notebook from resource: ")
      (if r
        (slurp r)
        (do (error "error: resource not found: " res-name)
            nil)))))