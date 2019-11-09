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

     CODE = INP CON VAL

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

(defn process-code [seg]
  (let [;_ (println "code is: " seg)
        inp (first seg)
        ;_ (println "code inp: " inp)
        ;_ (println "inp lines: " (second inp))
        con (nth seg 1)
        val (nth seg 2)]
    {:type :code
     :kernel :clj
     :content    {:value (or (get-lines  (second inp)) "")
                  :type  "text/x-clojure"}
     :console-response (or (unmake-clojure-comment (get-lines (second con))) "")
     :value-response (from-json (or (unmake-clojure-comment (get-lines (second val))) ""))}))


(defn process-segment [seg]
  (let [seg-with-wrapper (second seg)
        type (first seg-with-wrapper)
        data (rest seg-with-wrapper)]
    ;(get-lines (first data))
    (case type
      :MD (process-md (first data))
      :CODE (process-code data)
      nil)))

(defn decode [s]
   (let [nb (parse-notebook s)
         segments (rest (nth nb 2))]
  {:segments (vec (map process-segment segments))})
  )


; (process-segment (first segments))
; (process-segment (nth segments 3))

(defn decode2 [s]
  {:meta {:format 3
          :author "awb99"
          :id "abcd-1234-5678"
          :date "2019-11-08 09:40:00Z"
          :tags [:super :vega :ui :sample]}
   :widget-state  {:a 1
                   :b "2"}
   :segments [{:type :free :input "#hello"}
              {:type :code :kernel :clj :input "(+ 1 2)" :output "3"}
              {:type :code :kernel :cljs :input "(+ 1 2)" :output "3"}
              {:type :code :kernel :mock :input "(+ 1 2)" :output "Everything is wunderbar"}]})