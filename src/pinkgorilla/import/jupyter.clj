(ns pinkgorilla.import.jupyter
  (:require
   [clojure.string]
   [pinkgorilla.encoding.protocols :refer [decode encode]]))

(defn save-notebook [f notebook]
  (let [s (encode :gorilla notebook)]
    (spit f s)))

(defn jupyter-convert [file-name]
  (let [notebook (decode :jupyter (slurp file-name))
        filename-notebook (clojure.string/replace file-name #".ipynb" ".cljg")]
    (save-notebook filename-notebook notebook)))

