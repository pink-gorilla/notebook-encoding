(ns pinkgorilla.storage.res
  (:require
   [clojure.string]
   [pinkgorilla.storage.filename-encoding :refer [filename->encoding filename->name]]
   [pinkgorilla.storage.protocols :refer [FromFilename Storage create-storage]]))

(defrecord StorageRes [filename])

(defmethod create-storage :res [params]
  (StorageRes. (:filename params)))

(extend-type StorageRes
  Storage
  (storagetype [self] :res)
  (external-url [self]
    nil))

(extend-type StorageRes
  FromFilename
  (determine-encoding [this]
    (filename->encoding this :filename))
  (determine-name [this]
    (filename->name this :filename)))