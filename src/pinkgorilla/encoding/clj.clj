(ns pinkgorilla.encoding.clj
  (:require
   [clojure.tools.reader :as tr]
   [clojure.tools.reader.reader-types :as rts]
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.string :as str]
  ; dependencies needed to be in cljs bundle:  
   [pinkgorilla.notebook.helper :refer [add-segments empty-notebook assoc-meta code->segment]]
   [pinkgorilla.encoding.protocols :refer [decode]])
  (:import [java.io PushbackReader]))


;; options for clojure reader:
;; https://github.com/cgrand/sjacket
;; There are also other clojure parsers,
;;  I'm using https://github.com/xsc/rewrite-clj
;;  and I've heard good things about https://github.com/carocad/parcera (b


;; CLJ READER


(defn read-forms-clj
  [source-str]
  (let [rdr (-> source-str PushbackReader.)]
    (loop [forms []]
      (let [form (try (read rdr) (catch Exception e nil))]
        (if form
          (recur (conj forms form))
          forms)))))

(defn str->topforms-with-metadata [str]
  (->> str
       rts/source-logging-push-back-reader
       repeat
       ;(map #(tr/read {:read-cond :preserve} %))
       (map #(tr/read  % false :EOF))
       (take-while (partial not= :EOF))))

(defn read-forms [str]
  (map #(-> % meta :source)
       (str->topforms-with-metadata str)))

(defmethod decode :clj [_ source-str]
  (let [notebook (assoc-meta empty-notebook :msg "imported from clj")
        forms (read-forms source-str)
        segment-forms (map (partial code->segment :clj) forms)]
    (add-segments notebook segment-forms)))
