(ns pinkgorilla.encoding.jupyter
  "parse contents (string) of a Jupyter notebook
   TODO: parse output, console"
  (:require
   [pinkgorilla.encoding.helper :refer [from-json]]
   [pinkgorilla.notebook.core :refer [md->segment code->segment]]
   [pinkgorilla.encoding.protocols :refer [decode]]))

(defn- parse-cell [cell]
  (let [cell-type (get cell "cell_type")
        source (get cell "source")
        source-multiline (apply str source)]
     ;:cell_type "markdown", :metadata {}, :source ["#
    (case cell-type
      "markdown" (md->segment source-multiline)
      "code" (code->segment :clj source-multiline)
      nil)))

(defn- parse-cells [notebook]
  (let [cells (get notebook "cells")]
    (->> cells
         (map parse-cell)
         (into []))))

(defmethod decode :jupyter [_ s]
  (let [notebook (-> s (from-json))]
    {:version "J"
     :meta {:tags "jupyter"
            :tagline "Jupyter Notebook"}
     :segments (parse-cells notebook)}))

(comment

  ; :metadata {:kernelspec {:display_name "Clojure (clojupyter-v0.2.2)", 
  ;                         :language "clojure", 
  ;                         :name "clojupyter"}, 
  ;           :language_info {:file_extension ".clj", 
  ;                           :mimetype "text/x-clojure", 
  ;                           :name "clojure", 
  ;                           :version "1.10.0"}}, 
  ; :nbformat 4, 
  ; :nbformat_minor 2}

  ;(decode :jupyter (slurp "test/notebooks/basic-concepts.ipynb"))
  ;(decode :gorilla (slurp "test/notebooks/reagent-manipulate.cljg"))


; comment end
  )