(ns pinkgorilla.storage.file
  (:require
   #?(:clj [clojure.tools.logging :refer (info)]
      :cljs [taoensso.timbre :refer-macros (info)])
   [clojure.string]
   [pinkgorilla.storage.storage :refer [Storage query-params-to-storage Save Load]]))

(defn filename-format [filename]
  (->> filename
       (clojure.string/lower-case)
       (re-find #"\.([a-z]*)$")
       (last)
       (#(case %
           "cljg" :gorilla
           "ipynb" :jupyter
           :gorilla))))

(defrecord StorageFile [filename])

(defmethod query-params-to-storage :file [_ params]
  (StorageFile. (:filename params)))

(extend-type StorageFile
  Storage

  (storagetype [self] :file)

  (storageformat [self]
    (filename-format (:filename self)))

  (external-url [self]
    (info "file-storage.external-url")
    (str "file://" (:filename self)))

  (gorilla-path [self]
    (info "file-storage.gorilla-path")
    (str "/edit?source=file&filename=" (:filename self))))

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
       (info "Loading Notebook from file: " (:filename self))
       (slurp (:filename self)))))

(comment

  (re-find #"\.([a-z]*)$" "../../hhh/bongo.ipynb")

  (filename-format "demo.cljg")
  (filename-format "../../quant/trateg/notebooks/basic-concepts.ipynb")

  ; comment end
  )