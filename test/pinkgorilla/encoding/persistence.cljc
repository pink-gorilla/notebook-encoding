(ns pinkgorilla.encoding.persistence
  " clojure persistence loads/saves from disk.
    clojurescript persistence loads/saves to an atom.
    TODO: inject file content to this atom via macros, so that at test
    time the same test notebooks as for clojure are available in
    clojurescript
    "
  (:require
   [pinkgorilla.encoding.decode :refer [decode]]
   [pinkgorilla.encoding.encode :refer [encode-notebook]])
  #?(:cljs (:require-macros [pinkgorilla.macros :refer [inline-resource]])))

#?(:cljs
   (def content
     (atom
      {"test/notebooks/bad-format.cljg" (inline-resource "notebooks/bad-format.cljg")
       "test/notebooks/diff.cljg" (inline-resource "notebooks/diff.cljg")
       "test/notebooks/reagent-manipulate.cljg" (inline-resource "notebooks/reagent-manipulate.cljg")})))

(defn load-str [f]
  #?(:clj    (let [s (slurp f)]
               s)
     :cljs   (let [s (get @content f)]
               s)))

(defn load-notebook [f]
  #?(:clj    (let [s (slurp f)]
               (decode :gorilla s))
     :cljs   (let [s (get @content f)]
               (decode :gorilla s))))

(defn save-notebook [f notebook]
  #?(:clj (let [s (encode-notebook notebook)]
            (spit f s))
     :cljs (let [s (encode-notebook notebook)]
             (swap! content assoc f s))))

