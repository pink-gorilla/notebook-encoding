(ns pinkgorilla.storage.file
  (:require
   #?(:clj [taoensso.timbre :refer [info error]]
      :cljs [taoensso.timbre :refer-macros [info]])
   [clojure.string]
   [pinkgorilla.storage.filename-encoding :refer [filename->encoding filename->name]]
   [pinkgorilla.storage.protocols :refer [FromFilename Storage query-params-to-storage Save Load]]))

(defrecord StorageFile [filename])

(defmethod query-params-to-storage :file [_ params]
  (StorageFile. (:filename params)))

(extend-type StorageFile
  Storage

  (storagetype [self] :file)

  (external-url [self]
    (str "file://" (:filename self)))

  (gorilla-path [self]
    (str "?source=file"
         "&filename=" (:filename self))))

#?(:clj

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
       (slurp (:filename self)))))

(extend-type StorageFile
  FromFilename
  (determine-encoding [this]
    (filename->encoding this :filename))
  (determine-name [this]
    (filename->name this :filename)))
