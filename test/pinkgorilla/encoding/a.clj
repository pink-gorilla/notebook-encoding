(ns pinkgorilla.encoding.a
  (:require
   ;[cljs.test :refer-macros [async deftest is testing]]
   [instaparse.core :as insta]
   [pinkgorilla.encoding.decode :refer [decode]]))

; (def nb (slurp "resources/notebook1.cljw"))
(def nb (slurp "resources/demo.cljw"))
nb

(def token
  (insta/parser
   "NOTEBOOK = HEADER SEGMENT*
     HEADER = F VERSION NEWLINE NEWLINE
     F = ';; gorilla-repl.fileformat = '
     <VERSION> = #'[1-9]'
     NEWLINE = '\n'   

     SEGMENT = CODE | MD | OUT-VAL | OUT-CON

     CODE = CODE-B DATA* CODE-E
     CODE-B =  ';; @@' NEWLINE
     CODE-E =  ';; @@' NEWLINE NEWLINE

     MD = MD-B DATA* MD-E
     MD-B =  ';; **' NEWLINE
     MD-E =  ';; ****' NEWLINE NEWLINE

     OUT-VAL = VAL-B DATA VAL-E
     VAL-B =  ';; =>' NEWLINE
     VAL-E =  ';; >=' NEWLINE NEWLINE

     OUT-CON = CON-B DATA CON-E
     CON-B =  ';; ->' NEWLINE
     CON-E =  ';; >-' NEWLINE NEWLINE

     DATA2 = #'/.'
     DATA = #'[.\\w\\d\\s\\-\\+()]'
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



