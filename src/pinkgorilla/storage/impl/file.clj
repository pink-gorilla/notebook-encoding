(ns pinkgorilla.storage.impl.file
  (:require
   [taoensso.timbre :refer [info error]]
   [pinkgorilla.storage.protocols :refer [Save Load]])
  (:import
   [pinkgorilla.storage.file StorageFile]))

(extend-type StorageFile
  Save
  (storage-save [self notebook tokens]
    (if (nil? notebook)
      (throw (Exception. (str "NOT Saving EMPTY Notebook to file: " (:filename self))))
      (do
        (info "Saving Notebook to file: " (:filename self) " size:" (count notebook))
        (spit (:filename self) notebook)
        {:filename (:filename self)})))
  Load
  (storage-load [self tokens]
       ;(info "Loading Notebook from file: " (:filename self))
    (slurp (:filename self))))