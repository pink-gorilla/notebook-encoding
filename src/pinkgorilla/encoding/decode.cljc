(ns pinkgorilla.encoding.decode
  (:require
   [clojure.string :as str]
   [clojure.edn :as edn]
   [instaparse.core :as insta]
   [pinkgorilla.notebook.uuid :refer [guuid]]
   [pinkgorilla.encoding.protocols :refer [decode]]
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

      <IN>   = #'[a-zA-Z0-9]+[\r\n]'
      <OUT>  = #'[a-zA-Z0-9]+[\r\n]'
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

     KERNEL = ' [clj]' | ' [cljs]' | ' [mock]' | ' [meta]'

     INP = <INP-B> KERNEL? <N> LINES <INP-E>
     INP-B =   ';; @@' 
     INP-E =  N ';; @@' N 

     CON = <CON-B> LINES <CON-E>
     CON-B =   ';; ->' N
     CON-E =  N ';; <-' N 

     VAL = <VAL-B> LINES <VAL-E>
     VAL-B =   ';; =>' N
     VAL-E =  N ';; <=' N 

     "))

(defn get-lines [lines]
  (let [;_ (println "lines:    " lines)
       ; lines-with-wrapper (second lines)
        rlines (rest lines)
        slines (map second rlines)
        ;_ (println "rlines " rlines)
        ]
    (str/join "\n" slines)))

(defn process-md [seg]
  {:type :md
   :data (or (unmake-clojure-comment (get-lines seg)) "")})

(defn is-type [kw data]
  (let [t (first data)
        ;_ (println "checking type: " t)
        ]
    (= t kw)))

(defn find-element [data kw]
  (first (filter (partial is-type kw) (rest data))))

(defn kernel-s-to-kw [skernel]
  ; " [clj]" " [cljs]"
  (let [r (re-matches #".*\[(.*)\]" skernel) ;["abcxyz" "xyz"]
        kernel (if r
                 (keyword (second r))
                 :unknown)]
    ;(println "kernel: " kernel " str: " skernel)
    kernel))

(defn create-code-segment [inp]
  ;(println "code input is: " inp)  
  (let [lines (find-element inp :LINES)
        kernel (find-element inp :KERNEL)
        kernel-kw (if (nil? kernel) :clj (kernel-s-to-kw (second kernel)))
        ;_ (println "k is:" kernel)
        ]
    {:type :code
     :data {:kernel kernel-kw
            :code (or (get-lines lines) "")}
     :state {}}))

(defn add-console-response [segment con]
  (let [lines (get-lines (second con))
        lines-no-comment (unmake-clojure-comment lines)]
    (if (or (nil? lines-no-comment) (str/blank? lines-no-comment))
      segment
      (assoc-in segment [:state :out] lines-no-comment))))

(defn add-value-response [segment val]
  (let [lines (get-lines (second val))
        lines-no-comment (unmake-clojure-comment lines)
        picasso (when lines-no-comment
                  (from-json lines-no-comment))]
    ;(println "val lines: " lines)
    (assoc-in segment [:state :picasso] picasso)))

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

; meta

(defn meta? [segment]
  (and (= (:type segment) :code)
       (= (get-in segment [:data :kernel]) :meta)))

(defn get-meta [segments]
  (let [meta-segment (first (filter meta? segments))
        meta (if (nil? meta-segment)
               {}
               (edn/read-string (get-in meta-segment [:data :code])))
        id (:id meta)]
    (if id
      meta
      (assoc meta :id (guuid)))))

(def vector-type
  #?(:clj clojure.lang.PersistentVector
     :cljs cljs.core/PersistentVector))

(defmethod decode :gorilla [_ s]
  (let [nb (parse-notebook s)
        ;_ (println "parse result type is: " (type nb))
        ;_ (println "notebook: " nb)
        el-header (find-element nb :HEADER)
        el-version (find-element el-header :VERSION)
        version (get el-version 1)
        version (case version "1" 1 "2" 2 nil)
        ;_ (println "version: " version)
        ]
    ; awb99: a case would be good here, however it does not work
    ; cheshire has condp - but only for clojure, nut we also need cljs
    (if (= (type nb) vector-type)
      (let [segments (rest (nth nb 2))
            segments (vec (map process-segment segments))
            segments-no-meta (remove meta? segments)
            segments-no-meta-id (vec (map #(assoc % :id (guuid)) segments-no-meta))
            meta (get-meta segments)]
        {;:version version
         :meta meta
         :segments segments-no-meta-id})
      (do (when (not (nil? nb))
            ; ;instaparse.gll.Failure
            (println "notebook format is invalid. error:" nb))
          nil))))




