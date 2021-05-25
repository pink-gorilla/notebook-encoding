(ns pinkgorilla.encoding.simple-test
  (:require
   #?(:clj [clojure.test :refer :all]
      :cljs  [cljs.test :refer-macros [async deftest is testing]])
   [pinkgorilla.encoding.default-config] ; side-effects
   [pinkgorilla.encoding.persistence-helper :refer [load-notebook save-notebook]]))

(defn nb-no-seg-id [nb]
  (let [segs (:segments nb)
        segs-no-id (into []
                         (map #(dissoc % :id) segs))]
    (assoc nb :segments segs-no-id)))

(def notebook-simple
  {;:version 2
   :meta {:id 17
          :a 17
          :msg "simple-notebook"
          :experimental :on}
   :segments
   [{:type :md
     :data "#test"}
    {:type :md
     :data ""} ; test for empty code segment
    {:type :code
     :data {:kernel :clj
            :code "(+ 7 7)"}
     :state {:out "adding numbers.."
             :picasso {:type "html" :content [:span "14"]}}}
    {:type :code
     :data {:kernel :clj
            :code "" ; test for empty code segment
            }
     :state {}}]})

(deftest encode-simple
  (let [f "/tmp/notebook-simple.cljg"
        _ (save-notebook f notebook-simple)
        nb (load-notebook f)
        nb (nb-no-seg-id nb)]

    (is (= notebook-simple nb))))

(def notebook-meta-value
  {;:version 2
   :meta {:id 77}
   :segments
   [{:type :code
     :data {:kernel :clj
            :code "(+ 7 7)"}
     :state {:out "adding numbers.."
             :picasso {:type "html" :content ^{:p/render-as true} [:span "14"]}}}]})

(deftest encode-meta-value
  (let [f "/tmp/notebook-meta-value.cljg"
        _ (save-notebook f notebook-meta-value)
        loadback (load-notebook f)
        loadback (nb-no-seg-id loadback)]
    (is (= notebook-meta-value loadback))
    (is (= {:p/render-as true} (-> loadback :segments first  :state :picasso :content meta))))) ;

