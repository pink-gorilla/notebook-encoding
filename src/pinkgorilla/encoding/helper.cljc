(ns pinkgorilla.encoding.helper
  (:require
   [clojure.string :as str]
   [cognitect.transit :as t]))

;; Cognitec TRANSIT WRITER
;; make json encoding indepenent of clj/cljs

#?(:clj (import '[java.io ByteArrayInputStream ByteArrayOutputStream]))

(defn create-writer []
  #?(:clj  (let [out (ByteArrayOutputStream. (* 4096 1024))
                 writer (t/writer out :json)]
             {:writer writer :out out})
     :cljs (t/writer :json)))


(defn to-json [writer o]
  #?(:clj  (do (t/write (:writer writer) o)
               (String. (.toByteArray (:out writer))))
     :cljs (t/write writer o)))


(defn from-json [s]
  #?(:clj
     (do
       (def in (ByteArrayInputStream. (.getBytes s "UTF-8")))
       (def reader (t/reader in :json))
       (t/read reader))
     :cljs (t/read (t/reader :json) s)))



;; COMMENT ENCODING / DECODING

(defn make-clojure-comment
  [code]
  #_(println "make-clojure-comment for: " code)
  (if (nil? code)
    ";;; "
    (->> (str/split-lines code)
         (map #(str ";;; " %))
         (str/join "\n"))))


(defn unmake-line [l]
  (if (nil? l)
    (do (println "unmake-line called with nil.")
        "")
    (let [c (count l)]
      (if (< c 4)
        (do (println "unmake-line called with less than 4 characters: " l)
            l)
        (subs l 4 c)))))

(defn unmake-clojure-comment
  [code]
  (if (nil? code)
    ""
    (->> (str/split-lines code)
         (map unmake-line)
         (str/join "\n"))))

;(defn unmake-clojure-comment
;  [code]
;  (try
;    (unmake-clojure-comment-x code)
;    (catch Exception e code)))
