(ns pinkgorilla.encoding.protocols)

#?(:clj (defmulti decode (fn [t p] t))
   :cljs (defmulti decode identity))

#?(:clj (defmulti encode (fn [t p] t))
   :cljs (defmulti encode identity))
