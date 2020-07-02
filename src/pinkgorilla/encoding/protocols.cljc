(ns pinkgorilla.encoding.protocols)

#?(:clj (defmulti decode (fn [type notebook-str] type))
   :cljs (defmulti decode identity))

#?(:clj (defmulti encode (fn [type notebook] type))
   :cljs (defmulti encode identity))
