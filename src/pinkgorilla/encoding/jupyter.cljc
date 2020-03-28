(ns pinkgorilla.encoding.jupyter
  "parse contents (string) of a Jupyter notebook
   TODO: parse output, console"
  (:require
   [pinkgorilla.encoding.helper :refer [from-json]]
   [pinkgorilla.encoding.decode :refer [decode]]))

(defn- convert-md [source]
  {:type :free
   :markup-visible false
   :content
   {:value source
    :type  "text/x-markdown"}})

(defn- convert-code [source]
  {:type    :code
   :kernel  :clj
   :content {:value source
             :type  "text/x-clojure"}})

(defn- parse-cell [cell]
  (let [cell-type (get cell "cell_type")
        source (get cell "source")
        source-multiline (apply str source)]
     ;:cell_type "markdown", :metadata {}, :source ["#
    (case cell-type
      "markdown" {:markdown (convert-md source-multiline)}
      "code" {:code (convert-code source-multiline)}
      nil)))

(defn- parse-cells [notebook]
  (let [cells (get notebook "cells")]
    (->> cells
         (map parse-cell)
         (into []))))

(defmethod decode :jupyter [_ s]
  (let [notebook (-> s (from-json))]
    {:version "J"
     :meta {:tagline "Jupyter Notebook"}
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

  (decode :jupyter (slurp "test/notebooks/basic-concepts.ipynb"))

; comment end
  )