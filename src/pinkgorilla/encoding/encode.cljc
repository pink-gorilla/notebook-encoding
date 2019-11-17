(ns pinkgorilla.encoding.encode
  (:require
   [clojure.string :as str]
   [pinkgorilla.encoding.helper :refer [make-clojure-comment create-writer to-json]]))

(defmulti to-clojure :type)

(defmethod to-clojure :free
  [free-segment]
  (str ";; **\n"
       (make-clojure-comment (get-in free-segment [:content :value]))
       "\n;; **\n"))

(def output-start ";; =>\n")
(def output-end "\n;; <=\n")

(defn encode-output [code-segment]
  (let [w (create-writer)]
    (if-let [ot (:value-response code-segment)]
      (str output-start (make-clojure-comment (to-json w ot)) output-end)
      "")))

(def console-start ";; ->\n")
(def console-end "\n;; <-\n")

(defn encode-console [code-segment]
  (if-let [ct (:console-response code-segment)]
    (str console-start (make-clojure-comment ct) console-end)
    ""))

;(def start-tag ";; @@\n")
(def start-tag ";; @@ [")
(def start-end-tag "]\n")
(def end-tag "\n;; @@\n")

(defn encode-code [code-segment]
  ;(println "encode-code: " code-segment)
  (str
   start-tag
   (name (:kernel code-segment))
   start-end-tag
   (get-in code-segment [:content :value])
   end-tag))

(defmethod to-clojure :code
  [code-segment]
  (str
   (encode-code code-segment)
   (encode-console code-segment)
   (encode-output code-segment)))


;; meta

(defn meta-to-segment [meta]
  {:type :code
   :kernel :meta
   :content  {
              :value (prn-str meta) 
              ;s:type "text/x-clojure"
              }}
  )


(defn encode-notebook
  [notebook]
  (let [;segments (:segments notebook)
        meta (meta-to-segment (or (:meta notebook) {}))
        segments (into [meta] (:segments notebook))
        ;_ (println "encoding " segments)
        ]
    (str ";; gorilla-repl.fileformat = 2\n\n"
         (->> (map to-clojure segments)
              (str/join "\n")))))

(defn encode-notebook-ui
  "todo: move to notebook"
  [notebook]
  (let [segments-unsorted (:segments notebook)
        segment-ids (:segment-order notebook)
        segments (map #(get segments-unsorted %) segment-ids)]
    (encode-notebook {:segments segments})))



