(ns pinkgorilla.storage.gist
  (:require
   [clojure.string]
   #?(:clj [clojure.tools.logging :refer [debug info]]
      :cljs [taoensso.timbre :refer-macros [debug info]])
   [pinkgorilla.storage.filename-encoding :refer [filename->encoding filename->name]]
   #?(:clj [pinkgorilla.storage.github :refer [save-gist load-gist]])
   [pinkgorilla.storage.protocols :refer [FromFilename Storage query-params-to-storage Save Load]]))

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
  (debug "gist params: " params)
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
    ;https://gist.github.com/awb99/55b101d84d9b3814c46a4f9fbadcf2f8
    (str "https://gist.github.com/" (:user self) "/" (:id self)))

  ; depreciated
  (gorilla-path [self]
    (str "?source=gist "
         "&filename=" (:filename self)
         "&id=" (:id self))))

#?(:clj

   (extend-type StorageGist
     Save
     (storage-save [self notebook tokens]
       (if (nil? notebook)
         (throw (Exception. (str "NOT Saving EMPTY Notebook to file: " (:filename self))))
         (do
           (info "Saving Notebook to gist: " (:filename self) " size:" (count notebook))
           (save-gist (:id self) (:description self) (:is-public self) (:filename self) notebook (:github-token tokens)))))
     Load
     (storage-load [self tokens]
       (let [token (:github-token tokens)]
         (info "Loading Notebook from gist id: " (:id self))
         (if (or (nil? token) (clojure.string/blank? token))
           (load-gist (:id self) (:filename self))
           (load-gist (:id self) (:filename self) token))))))

(extend-type StorageGist
  FromFilename
  (determine-encoding [this]
    (filename->encoding this :filename))
  (determine-name [this]
    (filename->name this :filename)))

