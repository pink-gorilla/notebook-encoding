(ns pinkgorilla.import.convert-main
  (:require
   [me.raynes.fs :as fs]
   [clojure.java.io :as io]
   [pinkgorilla.document.default-config] ; side effects
   [pinkgorilla.import.marginalia :refer [marginalia-convert]]
   [pinkgorilla.import.jupyter :refer [jupyter-convert]])
  (:gen-class))

(def jupyter-extensions #{"ipynb"})
(def clj-extensions #{"clj"})

(defn file-extension
  [file]
  (some-> (.getPath file) fs/extension (subs 1)))

(defn jupyter-file?
  "Returns whether or not a file ends in a jupyter extension."
  [file]
  (contains? jupyter-extensions (file-extension file)))

(defn clj-file?
  "Returns whether or not a file ends in a jupyter extension."
  [file]
  (contains? clj-extensions (file-extension file)))

(defn to-gorilla [file-name]
  (let [file (io/file file-name)]
    (cond
      (jupyter-file? file)
      (jupyter-convert file-name)

      (clj-file? file)
      (marginalia-convert file-name)

      :else
      (println "file is neither clj nor jupyter format: " file-name))))

(defn -main [& args]
  (println "nbconvert args: " args)
  (let [file-name (last args)]
    (println "converting clj file: " file-name)
    (to-gorilla file-name)))
