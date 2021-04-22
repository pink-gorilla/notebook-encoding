(ns pinkgorilla.storage.filename-encoding
  (:require
   [clojure.string :as str]))

(defn encoding->extension [encoding]
  (case encoding
    :gorilla "cljg"
    :jupyter "ipynb"
    :clj "clj"
    "xxx"))

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

(defn filename->encoding [this k]
  ;(debug "filename->encoding: " this)
  (:encoding (split-filename (k this))))

(defn filename->name [this k]
  (:name (split-filename (k this))))

(comment
  (re-find #"\.([a-z]*)$" "../../hhh/bongo.ipynb")

  (split-filename "/home/andreas/demo1.cljg")
  (split-filename "/home/andreas/demo1.Cljg")
  (split-filename "../../quant/trateg/notebooks/basic-concepts.ipynb")
;  
  )

