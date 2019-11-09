(ns pinkgorilla.notebook.uuid
  (:require
   [clojure.string :as str] ; here because otherwise the ns fails in clj
   #?(:cljs [cljs-uuid-utils.core :as uuid-cljs])))


(defn uuid []
  #?(:clj (-> (.toString (java.util.UUID/randomUUID)) keyword)
     :cljs  (-> (uuid-cljs/make-random-uuid) uuid/uuid-string keyword)))