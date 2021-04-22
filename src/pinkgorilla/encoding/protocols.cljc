(ns pinkgorilla.encoding.protocols)

#?(:clj (defmulti decode (fn [type _] type)) ; notebook-str
   :cljs (defmulti decode identity))

#?(:clj (defmulti encode (fn [type _] type)) ; notebook
   :cljs (defmulti encode identity))
