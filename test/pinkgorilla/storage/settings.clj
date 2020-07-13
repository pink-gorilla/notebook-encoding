(ns pinkgorilla.storage.settings)

(def storage-data
  {:load  ["pink-junkjard" "unittest-notebooks" "unittest-load.txt"]

   :write ["pink-junkjard" "unittest-notebooks" "unittest-dynamic.txt"]

   :gist-load {:type :gist
               :user "awb99"
               :id "5e5fb05046d3510745bd3285adb42715"
               :filename "meta1.cljg"}

   :core {:type :repo
          :user "pink-junkjard" ; "pink-gorilla"
          :repo "unittest-notebooks"
          :filename "unittest-load.txt"}})


