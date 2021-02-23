(ns pinkgorilla.notebook.persistence
  (:require
   #?(:clj [taoensso.timbre :refer [info error]]
      :cljs [taoensso.timbre :refer-macros [info error]])
   [pinkgorilla.encoding.protocols :refer [decode encode]]
   [pinkgorilla.storage.protocols :refer [determine-encoding storage-load storage-save]]))

(defn save-nb [storage tokens nb]
  (storage-save storage nb tokens))

(defn save-notebook [storage tokens notebook]
  (if-let [format (determine-encoding storage)]
    (do (info "saving notebook with format: " format)
        (->> notebook
             ;(tap "dehydrated nb:")
             (encode format)
             ;(tap "encoded nb: ")
             (save-nb storage tokens))
        {:success "notebook saved!"})
    (do (error "could not save notebook, because encoding cannot be determined: " storage)
        {:error "could not determine storage-format!"})))

(defn load-notebook [storage tokens]
  (if-let [encoding-type (determine-encoding storage)]
    (->> (storage-load storage tokens)
         (decode encoding-type))
    (do
      (error "cannot load notebook - format cannot be determined! " storage)
      nil)))