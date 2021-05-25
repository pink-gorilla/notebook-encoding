(ns pinkgorilla.encoding.encode
  (:require
   [clojure.string :as str]
   [pinkgorilla.encoding.protocols :refer [encode]]
   [pinkgorilla.encoding.helper :refer [make-clojure-comment create-writer to-json]]))

(defmulti to-clojure :type)

(defmethod to-clojure :md
  [md-segment]
  (let [s (str ";; **\n"
               (make-clojure-comment (get-in md-segment [:data]))
               "\n;; **\n")]
    ;(println "encoded md:" s)
    s))

(def output-start ";; =>\n")
(def output-end "\n;; <=\n")

(defn encode-output [code-segment]
  (let [w (create-writer)]
    (if-let [ot (get-in code-segment [:state :picasso])]
      (str output-start (make-clojure-comment (to-json w ot)) output-end)
      "")))

(def console-start ";; ->\n")
(def console-end "\n;; <-\n")

(defn encode-console [code-segment]
  (if-let [ct (get-in code-segment [:state :out])]
    (str console-start (make-clojure-comment ct) console-end)
    ""))

;(def start-tag ";; @@\n")
(def start-tag ";; @@ [")
(def start-end-tag "]\n")
(def end-tag "\n;; @@\n")

(defn encode-code [code-segment]

  (let [kernel (get-in code-segment [:data :kernel])
        code (get-in code-segment [:data :code])
        ;_  (println "encode-code: kernel:" kernel " code: " code)
        s (str start-tag
               (name kernel)
               start-end-tag
               code
               end-tag)]
    ;(println "encoded code: " s)
    s))

(defmethod to-clojure :code
  [code-segment]
  (str
   (encode-code code-segment)
   (encode-console code-segment)
   (encode-output code-segment)))


;; meta


(defn meta-to-segment [meta]
  {:type :code
   :data {:kernel :meta
          :code (prn-str meta)}})

(defn- encode-notebook
  "encodes a dehydrated notebook"
  [notebook]
  (let [meta (meta-to-segment (or (:meta notebook) {}))
        segments (into [meta] (:segments notebook))]
    ;(println "encoding " segments)
    (str ";; gorilla-repl.fileformat = 2\n\n"
         (->> (map to-clojure segments)
              (str/join "\n")))))

(defmethod encode :gorilla [_ notebook]
  (encode-notebook notebook))





