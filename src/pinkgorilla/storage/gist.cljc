(ns pinkgorilla.storage.gist
  (:require

   #?(:clj [clojure.tools.logging :refer (info)]
      :cljs [taoensso.timbre :refer-macros (info)])

   #?(:clj [pinkgorilla.storage.github :refer [save-gist]])

   [pinkgorilla.storage.storage :refer [Storage query-params-to-storage Save]]))


(defrecord StorageGist [id filename user is-public description])

(defn hack-null
  "hack: on server query params in post-body arrive as null as string "
  [id]
  (if (nil? id)
    nil
    (if (= "null" id)
      nil
      id)))

(defmethod query-params-to-storage :gist [_ params]
  (info "gist params: " params)
  (StorageGist.
   (or (hack-null (:id params)) nil)
   (:filename params)
   (or (:user params) "")
   (or (:is-public params) true)
   (or (:description params) "")))


(extend-type StorageGist
  Storage

  (storagetype [self] :gist)

  (external-url [self]
    (info "local-storage.external-url")
    ;https://gist.github.com/awb99/55b101d84d9b3814c46a4f9fbadcf2f8
    (str "https://gist.github.com/" (:user self) "/" (:id self)))

  (gorilla-path [self]
    (info "local-storage.gorilla-path")
    (str "/edit?source=gist&filename=" (:filename self) "&id=" (:id self))))



#?(:clj
   (extend-type StorageGist
     Save
     (storage-save [self notebook tokens]
       (if (nil? notebook)
         (do
           (info "NOT Saving EMPTY Notebook to file: " (:filename self))
           {:success false :error-message "Notebook is empty"})
         (do
           (info "Saving Notebook to gist: " (:filename self) " size:" (count notebook))
           (info "tokens: " tokens)
           (save-gist (:id self) (:description self) (:is-public self) (:filename self) notebook (:github-token tokens)))))))

