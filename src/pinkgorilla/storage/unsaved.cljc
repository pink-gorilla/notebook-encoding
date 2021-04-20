(ns pinkgorilla.storage.unsaved
  (:require
   [clojure.string]
   [pinkgorilla.storage.protocols :refer [Storage create-storage FromFilename]]))

(defrecord StorageUnsaved [id])

(defmethod create-storage :unsaved [params]
  (StorageUnsaved. (:id params)))

(extend-type StorageUnsaved
  Storage
  (storagetype [self] :unsaved)
  (external-url [self]
    nil))

(extend-type StorageUnsaved
  FromFilename
  (determine-encoding [this]
    :gorilla)
  (determine-name [this]
    (:id this)))


