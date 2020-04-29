(ns pinkgorilla.import.clj-import
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
  ; dependencies needed to be in cljs bundle:  
   [pinkgorilla.storage.storage :refer [create-storage] :as storage]
   [pinkgorilla.storage.file]
   ; pinkgorilla
   [pinkgorilla.encoding.persistence :refer [save-notebook]])
  (:import [java.io PushbackReader]))

(def empty-notebook
  "empty persisted notebook"
  {:version 2
   :meta {}
   :segments []})

(def empty-notebook-imported
  "empty persisted notebook"
  {:version 2
   :meta {:msg "imported from clj"}
   :segments
   [{:type :free
     :content {:value "# Imported from clj" :type "text/x-markdown"}
     :markup-visible false}]})

(defn code->segment [code]
  {:type :code
   :kernel :clj
   :content  {:value (pr-str code)
              :type "text/x-clojure"}})

(defn add-segments [notebook segments]
  (let [segments (into []
                       (concat (:segments notebook) segments))]
    (assoc notebook :segments segments)))

;; adapted from: http://stackoverflow.com/a/24922859/6264 

(defn read-forms [file-name]
  (let [rdr (-> file-name io/file io/reader PushbackReader.) sentinel (Object.)]
    (loop [forms []]
      (let [form (edn/read {:eof sentinel} rdr)]
        (if (= sentinel form) forms (recur (conj forms form)))))))

(defn clj->notebook [file-name]
  (let [forms (read-forms file-name)
        segment-forms (map code->segment forms)
        notebook empty-notebook-imported]
    (add-segments notebook segment-forms)))

(defn clj->convert [file-name]
  (let [notebook (clj->notebook file-name)
        filename-notebook (clojure.string/replace file-name #".clj" ".cljg")]
    (save-notebook filename-notebook notebook)))


