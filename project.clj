(defproject org.pinkgorilla/encoding "0.0.4"
  :description "The encoding of PinkGorilla Notebook."
  :url "https://github.com/pink-gorilla/notebook-encoding"
  :license {:name "MIT"}
  ;:deploy-repositories [["releases" :clojars]]
  :repositories [["clojars" {:url "https://clojars.org/repo"
                             :username "pinkgorillawb"
                             :sign-releases false}]]
  :dependencies
  [[org.clojure/clojure "1.10.1"]
   [org.clojure/clojurescript "1.10.520"]
   [instaparse "1.4.10"] ; used in decoding
   [com.cognitect/transit-clj "0.8.319"] ; used in encoding - clojure
   [com.cognitect/transit-cljs "0.8.256"] ; used in encoding - clojurescript
   [com.lucasbradstreet/cljs-uuid-utils "1.0.2"] ; uuid - clojurescript
   ] 

  :source-paths ["src"]
  :test-paths ["test"]
  :plugins [[lein-doo "0.1.11"]]
  :doo {:build "test"
        :default  [#_:chrome #_:phantom :karma-phantom]
        :browsers [:chrome #_:firefox]
        :alias {:default [:chrome-headless]}
        :paths
        {;; :phantom "phantomjs --web-security=false"
         :karma "./node_modules/karma/bin/karma --port=9881 --no-colors"}}
  :cljsbuild
  {:builds [{:id "test"
             :source-paths ["src" "test"]
             :compiler {:output-to "resources/public/js/testable.js"
                        :main pinkgorilla.doo-runner
                        :optimizations :none}}]})


