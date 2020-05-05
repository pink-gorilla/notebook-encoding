(ns pinkgorilla.import.clj-import
  (:require
   [clojure.tools.reader :as tr]
   [clojure.tools.reader.reader-types :as rts]
   [clojure.edn :as edn]
   [clojure.java.io :as io]
  ; dependencies needed to be in cljs bundle:  
   [pinkgorilla.storage.storage :refer [create-storage] :as storage]
   [pinkgorilla.storage.file]
   ; pinkgorilla
   [pinkgorilla.encoding.persistence :refer [save-notebook]])
  (:import [java.io PushbackReader]))

;; options for clojure reader:
;; https://github.com/cgrand/sjacket
;; There are also other clojure parsers,
;;  I'm using https://github.com/xsc/rewrite-clj
;;  and I've heard good things about https://github.com/carocad/parcera (b

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
   :content  {:value code
              :type "text/x-clojure"}})

(defn add-segments [notebook segments]
  (let [segments (into []
                       (concat (:segments notebook) segments))]
    (assoc notebook :segments segments)))

(defn read-forms-clj
  [file-name]
  (let [rdr (-> file-name io/file io/reader PushbackReader.)]
    (loop [forms []]
      (let [form (try (read rdr) (catch Exception e nil))]
        (if form
          (recur (conj forms form))
          forms)))))

(defn file->topforms-with-metadata [path]
  (->> path
       slurp
       rts/source-logging-push-back-reader
       repeat
       ;(map #(tr/read {:read-cond :preserve} %))
       (map #(tr/read  % false :EOF))
       (take-while (partial not= :EOF))))

(defn read-forms [file-name]
  (map #(-> % meta :source)
       (file->topforms-with-metadata file-name)))

(defn clj->notebook [file-name]
  (let [forms (read-forms file-name)
        segment-forms (map code->segment forms)
        notebook empty-notebook-imported]
    (add-segments notebook segment-forms)))

(defn clj->convert [file-name]
  (let [notebook (clj->notebook file-name)
        filename-notebook (clojure.string/replace file-name #".clj" ".cljg")]
    (save-notebook filename-notebook notebook)))


