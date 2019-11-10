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

(def start-tag ";; @@\n")
(def end-tag "\n;; @@\n")

(defn encode-code [code-segment]
  (str
   start-tag
   (get-in code-segment [:content :value])
   end-tag))

(defmethod to-clojure :code
  [code-segment]
  (str
   (encode-code code-segment)
   (encode-console code-segment)
   (encode-output code-segment)))


(defn encode-notebook
  [notebook]
  (let [segments (:segments notebook)]
    (str ";; gorilla-repl.fileformat = 2\n\n"
         (->> (map to-clojure segments)
              (str/join "\n")))))

(defn encode-notebook-ui
  "todo: move to notebook"
  [worksheet]
  (let [segments-unsorted (:segments worksheet)
        segment-ids (:segment-order worksheet)
        segments (map #(get segments-unsorted %) segment-ids)]
    (encode-notebook {:segments segments})))



