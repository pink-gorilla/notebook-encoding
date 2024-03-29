(ns pinkgorilla.notebook.persistence
  (:require
   #?(:clj [taoensso.timbre :refer [debug info error]]
      :cljs [taoensso.timbre :refer-macros [debug info error]])
   [pinkgorilla.encoding.protocols :refer [decode encode]]
   [pinkgorilla.storage.protocols :refer [determine-encoding storage-load storage-save]]))

(defn save-nb [storage tokens nb]
  (storage-save storage nb tokens))

(defn save-notebook [storage tokens notebook]
  (if-let [format (determine-encoding storage)]
    (do (debug "saving notebook with format: " format)
        (->> notebook
             (encode format)
             ;(println "encoded: ")
             (save-nb storage tokens))) ;save-nb returns :success true/false
    (do (error "could not save notebook, because encoding cannot be determined: " storage)
        {:success false :error "could not determine storage-format!"})))

(defn load-notebook [storage tokens]
  (if-let [format (determine-encoding storage)]
    (do (debug "loading notebook with format: " format)
        (if-let [content (storage-load storage tokens)]
          (decode format content)
          (do (error "cannot decode. content is nil")
              nil)))
    (do
      (error "cannot load notebook - format cannot be determined! " storage)
      nil)))