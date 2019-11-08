(ns pinkgorilla.encoding.a
  (:require
   ;[cljs.test :refer-macros [async deftest is testing]]
   [instaparse.core :as insta]
   [pinkgorilla.encoding.decode :refer [decode]]))

; (def nb (slurp "resources/notebook1.cljw"))
;(def nb (slurp "resources/demo.cljw"))
(def nb (slurp "resources/reagent-manipulate.clj"))
nb

(def token
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

; keyword = 'cond' | 'defn'
; HEADER = #';; gorilla-repl.fileformat = \n'

; SEGMENT = IN OUT

; codeSegmentOpenTag = 
; codeSegmentCloseTag = lineEnd ";; @@" lineEnd
;      A = 'a'+


; segment = freeSegment / codeSegment



; outputSection = outputOpenTag output:stringNoDelim outputCloseTag {return output;}
; outputOpenTag = ";; =>" lineEnd
; outputCloseTag = lineEnd ";; <=" lineEnd



(token nb)



