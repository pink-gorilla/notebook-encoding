(ns pinkgorilla.encoding.helper
  (:require
   [clojure.string :as str]))


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