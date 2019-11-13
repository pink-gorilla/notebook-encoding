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
      {"resources/bad-format.cljw" (inline-resource "resources/bad-format.cljw")
       "resources/diff.cljw" (inline-resource "resources/diff.cljw")
       "resources/reagent-manipulate.cljw" (inline-resource "resources/reagent-manipulate.cljw")})))

(defn load-str [f]
  #?(:clj    (let [s (slurp f)]
               s)
     :cljs   (let [s (get @content f)]
               s)))


(defn load-notebook [f]
  #?(:clj    (let [s (slurp f)]
               (decode s))
     :cljs   (let [s (get @content f)]
               (decode s))))

(defn save-notebook [f notebook]
  #?(:clj (let [s (encode-notebook notebook)]
            (spit f s))
     :cljs (let [s (encode-notebook notebook)]
             (swap! content assoc f s))))

