(ns pinkgorilla.encoding.decode
  (:require
   [clojure.string :as str]
   [instaparse.core :as insta]
   [pinkgorilla.encoding.helper :refer [unmake-clojure-comment from-json]]))

(def parse-notebook
  "parse-notebook gets passed in a string and returns an intermediary output format.
   It is based on InstaParse.
   Notes: 
      SEGMENTS=SEGMENT (<N> SEGMENT)*
      This basically decodes one or more segments, that are separated by newline.

      LINE = #'.*'
      This is an regex that spans maximum one line and can contain one or more
      characters of any type.

      One of multiple keywords:
      keyword = 'cond' | 'defn'
   "
  (insta/parser
   "NOTEBOOK = HEADER SEGMENTS
     SEGMENT = MD | CODE
     SEGMENTS = SEGMENT (<N> SEGMENT)*

     HEADER = <F> VERSION <N> <N>
     F = ';; gorilla-repl.fileformat = '
     VERSION = #'[1-9]'
     
     N = '\n'
     LINE = #'.*'
     DATA = #'[.\\w\\d\\s\\-\\+()]'
     LINES = LINE (<N> LINE)*

     MD = <MD-B> LINES <MD-E>
     MD-B =   ';; **' N
     MD-E =  N ';; **' N 

     CODE = INP CON? VAL?

     INP = <INP-B> LINES <INP-E>
     INP-B =   ';; @@' N
     INP-E =  N ';; @@' N 

     CON = <CON-B> LINES <CON-E>
     CON-B =   ';; ->' N
     CON-E =  N ';; <-' N 

     VAL = <VAL-B> LINES <VAL-E>
     VAL-B =   ';; =>' N
     VAL-E =  N ';; <=' N 

     <IN>   = #'[a-zA-Z0-9]+[\r\n]'
     <OUT>  = #'[a-zA-Z0-9]+[\r\n]'  "))


(defn get-lines [lines]
  (let [;_ (println "lines:    " lines)
       ; lines-with-wrapper (second lines)
        rlines (rest lines)
        slines (map second rlines)
        ;_ (println "rlines " rlines)
        ]
    (str/join "\n" slines)))



(defn process-md [seg]
  {:type :free
   :markup-visible false
   :content
   {:value (or (unmake-clojure-comment (get-lines seg)) "")
    :type  "text/x-markdown"}})


(defn create-code-segment [inp]
  {:type :code
   :kernel :clj
   :content    {:value (or (get-lines  (second inp)) "")
                :type  "text/x-clojure"}})

(defn add-console-response [segment con]
  (assoc segment
         :console-response
         (or (unmake-clojure-comment (get-lines (second con))) "")))

(defn add-value-response [segment val]
  (assoc segment
         :value-response
         (from-json (or (unmake-clojure-comment (get-lines (second val))) ""))))


(defn add-addon [segment addon]
  (let [addon-type (first addon)
        ;_ (println "processing code-addon type: " addon-type)
        ]
    (case addon-type
      :CON (add-console-response segment addon)
      :VAL (add-value-response segment addon)
      (do (println "unknwn code-addon type: " addon-type)
          segment))))


(defn process-code [seg]
  (let [;_ (println "code is: " seg)
        inp (first seg)
        segment (create-code-segment inp)
        ;_ (println "code segment base: " segment)
        addons (rest seg)
        ;_ (println "code segment addons: " addons)
        ]
    (reduce add-addon segment addons)))


(defn process-segment [seg]
  (let [seg-with-wrapper (second seg)
        type (first seg-with-wrapper)
        data (rest seg-with-wrapper)]
    ;(get-lines (first data))
    (case type
      :MD (process-md (first data))
      :CODE (process-code data)
      nil)))


(def vector-type
  #?(:clj clojure.lang.PersistentVector
     :cljs cljs.core/PersistentVector))

(defn decode [s]
  (let [nb (parse-notebook s)
        ;_ (println "parse result type is: " (type nb))
        ]
    ; awb99: a case would be good here, however it does not work
    ; cheshire has condp - but only for cloure, nut we also need cljs
    (if (= (type nb) vector-type)
      (let [segments (rest (nth nb 2))]
        {:segments (vec (map process-segment segments))})
      (do (when (not (nil? nb))
            ; ;instaparse.gll.Failure
            (println "notebook format is invalid. error:" nb))
          nil))))


; (process-segment (first segments))
; (process-segment (nth segments 3))

