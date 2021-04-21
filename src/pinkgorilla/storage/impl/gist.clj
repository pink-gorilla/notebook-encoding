(ns pinkgorilla.storage.impl.gist
  (:require
   [clojure.string]
   [taoensso.timbre :refer [info error]]
   [pinkgorilla.storage.impl.github :refer [save-gist load-gist]]
   [pinkgorilla.storage.protocols :refer [Save Load]])
  (:import
   [pinkgorilla.storage.gist StorageGist]))

(extend-type StorageGist
  Save
  (storage-save [self notebook tokens]
    (if (nil? notebook)
      (throw (Exception. (str "NOT Saving EMPTY Notebook to file: " (:filename self))))
      (do
        (info "Saving Notebook to gist: " (:filename self) " size:" (count notebook))
        (save-gist (:id self) (:description self) (:is-public self) (:filename self) notebook tokens))))
  Load
  (storage-load [self tokens]
    (info "Loading Notebook from gist id: " (:id self))
    (if (nil? tokens)
      (load-gist (:id self) (:filename self))
      (load-gist (:id self) (:filename self) tokens))))