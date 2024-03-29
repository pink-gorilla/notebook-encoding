(ns pinkgorilla.storage.impl.file
  (:require
   [taoensso.timbre :refer [debug info error]]
   [pinkgorilla.storage.protocols :refer [Save Load]])
  (:import
   [pinkgorilla.storage.file StorageFile]))

(extend-type StorageFile
  Save
  (storage-save [self notebook tokens]
    (if (nil? notebook)
      {:success false :error-message "NOT Saving EMPTY Notebook "}
      (do
        (debug "Saving Notebook to file: " (:filename self) " size:" (count notebook))
        (spit (:filename self) notebook)
        {:success true :filename (:filename self)})))
  Load
  (storage-load [self tokens]
    (debug "Loading Notebook from file: " (:filename self))
    (slurp (:filename self))))