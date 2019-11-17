(ns pinkgorilla.storage.file
  (:require

   #?(:clj [clojure.tools.logging :refer (info)]
      :cljs [taoensso.timbre :refer-macros (info)])

   [pinkgorilla.storage.storage :refer [Storage query-params-to-storage Save]]))


(defrecord StorageFile [filename])

(defmethod query-params-to-storage :file [_ params]
  (StorageFile. (:filename params)))


(extend-type StorageFile
  Storage

  (storagetype [self] :file)

  (external-url [self]
    (info "file-storage.external-url")
    nil)

  (gorilla-path [self]
    (info "file-storage.gorilla-path")
    (str "/edit?source=file?filename=" (:filename self))))



#?(:clj
   (extend-type StorageFile
     Save

     (storage-save [self notebook tokens]
       (if (nil? notebook)
         (info "NOT Saving EMPTY Notebook to file: " (:filename self))
         (do
           (info "Saving Notebook to file: " (:filename self) " size:" (count notebook))
           (spit (:filename self) notebook))))))