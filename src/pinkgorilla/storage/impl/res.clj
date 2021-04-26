(ns pinkgorilla.storage.impl.res
  (:require
   [taoensso.timbre :refer [info debug error]]
   [clojure.java.io :as io]
   [pinkgorilla.storage.protocols :refer [Save Load]])
  (:import
   [pinkgorilla.storage.res  StorageRes]))

(extend-type StorageRes
  Save
  (storage-save [self notebook tokens]
    {:success :false
     :error-message "resources cannot be saved"})
  Load
  (storage-load [self tokens]
    (let [res-name (str "notebooks/" (:filename self))
          r (io/resource res-name)]
      (debug "Loading Notebook from resource: ")
      (if r
        (slurp r)
        (do (error "error: resource not found: " res-name)
            nil)))))