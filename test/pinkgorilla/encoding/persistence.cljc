(ns pinkgorilla.encoding.persistence
  " clojure persistence loads/saves from disk.
    clojurescript persistence loads/saves to an atom.
    TODO: inject file content to this atom via macros, so that at test
    time the same test notebooks as for clojure are available in
    clojurescript
    "
  (:require
   [pinkgorilla.encoding.decode :refer [decode]]
   [pinkgorilla.encoding.encode :refer [encode-notebook]]))

#?(:cljs (def content (atom "")))

(defn load-notebook [f]
  #?(:clj    (let [s (slurp f)]
               (decode s))
     :cljs   (let [s @content]
               (decode s))))

(defn save-notebook [f notebook]
  #?(:clj (let [s (encode-notebook notebook)]
            (spit f s))
     :cljs (let [s (encode-notebook notebook)]
             (reset! content s))))
