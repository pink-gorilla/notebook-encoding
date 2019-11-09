(ns pinkgorilla.encoding.helper
  (:require
   [clojure.string :as str]
   [cognitect.transit :as t]))

;; Coginitec TRANSIT WRITER

#?(:clj (import '[java.io ByteArrayInputStream ByteArrayOutputStream]))

(defn create-writer []
  #?(:clj  (do (def out (ByteArrayOutputStream. 4096))
               (t/writer out :json))
     :cljs (t/writer :json)))

;; COMMENT ENCODING / DECODING

(defn make-clojure-comment
  [code]
  (->> (str/split-lines code)
       (map #(str ";;; " %))
       (str/join "\n")))


(defn unmake-clojure-comment
  [code]
  (if (nil? code)
    nil
    (->> (str/split-lines code)
         (map #(subs % 4 (count code)))
         (str/join "\n"))))