(ns pinkgorilla.encoding.encode
  (:require
   [clojure.string :as str]
   [cognitect.transit :as t]))

(defn make-clojure-comment
  [code]
  (->> (str/split-lines code)
       (map #(str ";;; " %))
       (str/join "\n")))

;; pegjs calls
(defn unmake-clojure-comment
  [code]
  (->> (str/split-lines code)
       (map #(.slice % 4))
       (str/join "\n")))

(defmulti to-clojure :type)

(defmethod to-clojure :code
  [code-segment]
  (let [w (t/writer :json)
        start-tag ";; @@\n"
        end-tag "\n;; @@\n"
        output-start ";; =>\n"
        output-end "\n;; <=\n"
        console-start ";; ->\n"
        console-end "\n;; <-\n"
        console-text (if-let [ct (:console-response code-segment)]
                       (str console-start (make-clojure-comment ct) console-end)
                       "")
        output-text (if-let [ot (:value-response code-segment)]
                      (str output-start (make-clojure-comment (t/write w ot)) output-end)
                      "")]
    (str start-tag
         (get-in code-segment [:content :value])
         end-tag
         console-text
         output-text)))

(defmethod to-clojure :free
  [free-segment]
  (str ";; **\n"
       (make-clojure-comment (get-in free-segment [:content :value]))
       "\n;; **\n"))

(defn encode-worksheet
  [worksheet]
  (let [segments-unsorted (:segments worksheet)
        segment-ids (:segment-order worksheet)
        segments (map #(get segments-unsorted %) segment-ids)]
    (str ";; gorilla-repl.fileformat = 2\n\n"
         (->> (map to-clojure segments)
              (str/join "\n")))))