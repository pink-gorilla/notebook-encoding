(ns pinkgorilla.notebook.rules
  (:require
   #?(:clj [clojure.tools.logging :refer [info]]
      :cljs [taoensso.timbre :refer-macros [info]])
   [clojure.string :as str]
   [pinkgorilla.storage.storage :refer [Storage query-params-to-storage Save Load]]))

(defn extension->encoding [extension]
  (case (str/lower-case extension)
    "cljg" :gorilla
    "ipynb" :jupyter
    "clj" :clojure
    :unknown))

(defn split-filename
  "extracts the only the name of the file, without extension and path"
  [file-name]
  (when-not (str/blank? file-name)
    (when-let [; the regex returns [full-hit name-only]
               match (re-find #"(?i)(.+?)([\w-]*).(cljg|ipynb)*$" file-name)]
      (let [[full path name ext] match]
        {:full full
         :path path
         :name name
         :ext ext
         :encoding (extension->encoding ext)}))))

(comment

  (split-filename "/home/andreas/demo1.cljg")
  (split-filename "/home/andreas/demo1.Cljg"))

