(ns pinkgorilla.storage.file
  (:require
   [clojure.string]
   [pinkgorilla.storage.filename-encoding :refer [filename->encoding filename->name]]
   [pinkgorilla.storage.protocols :refer [FromFilename Storage create-storage]]))

(defrecord StorageFile [filename])

(defmethod create-storage :file [params]
  (StorageFile. (:filename params)))

(extend-type StorageFile
  Storage

  (storagetype [self] :file)

  (external-url [self]
    (str "file://" (:filename self))))

(extend-type StorageFile
  FromFilename
  (determine-encoding [this]
    (filename->encoding this :filename))
  (determine-name [this]
    (filename->name this :filename)))
