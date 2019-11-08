(defproject org.pinkgorilla/encoding "2.0.15"
  :description "The encoding of PinkGgorilla Notebook."
  :url "https://github.com/pink-gorilla/notebook-encoding"
  :license {:name "MIT"}
  ;:deploy-repositories [["releases" :clojars]]
  :repositories [["clojars" {:url "https://clojars.org/repo"
                             :username "pinkgorillawb"
                             :sign-releases false}]]
  :dependencies
  [[org.clojure/clojure "1.10.1"]
   [org.clojure/clojurescript "1.10.520"]
   [org.clojure/data.json "0.2.6"] ; used by old vega renderer
   [instaparse "1.4.10"]] 


  :source-paths ["src"]
  :test-paths ["test"]
  :plugins [[lein-doo "0.1.10"]]
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
                        :main pinkgorilla.encoding.doo-runner
                        :optimizations :none}}]})


