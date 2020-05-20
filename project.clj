(defproject org.pinkgorilla/notebook-encoding "0.0.34-SNAPSHOT"
  :description "Encoding of Pink Gorilla Notebook."
  :url "https://github.com/pink-gorilla/notebook-encoding"
  :license {:name "MIT"}
  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :username :env/release_username
                                     :password :env/release_password
                                     :sign-releases false}]]
  :release-tasks [["vcs" "assert-committed"]
                  ["bump-version" "release"]
                  ["vcs" "commit" "Release %s"]
                  ["vcs" "tag" "v" "--no-sign"]
                  ["deploy"]
                  ["bump-version"]
                  ["vcs" "commit" "Begin %s"]
                  ["vcs" "push"]]
  
  ;; TODO: prep tasks breaks alias???
  ;; :prep-tasks ["build-shadow-ci"]
  
  :source-paths ["src"]
  :test-paths ["test"]

  :plugins [[lein-shell "0.5.0"]]

  :dependencies  [[org.clojure/clojure "1.10.1"]
                  ;; [org.clojure/clojurescript "1.10.520"] ; adding this fucks up shadow-build
                  [org.clojure/tools.logging "0.5.0"]
                  [instaparse "1.4.10"] ; used in decoding
                  [com.cognitect/transit-clj "0.8.319"] ; used in encoding - clojure
                  [com.cognitect/transit-js "0.8.861"] ;transit-cljs has old dependency that does not work with meta-data, see: https://github.com/cognitect/transit-cljs/issues/48
                  [com.cognitect/transit-cljs "0.8.256"] ; used in encoding - clojurescript
                  [com.lucasbradstreet/cljs-uuid-utils "1.0.2"] ; uuid - clojurescript
                  [com.taoensso/timbre "4.10.0"] ; cljs logging
                  [org.clojure/tools.logging "0.5.0"] ; clj logging
                  [irresponsible/tentacles "0.6.6"] ; github api  https://github.com/clj-commons/tentacles
                  [cheshire "5.7.1"] ; tentacles dependency, JSON and JSON SMILE (binary json format) encoding/decoding
                  [me.raynes/fs "1.4.6"]
                  [marginalia "0.9.1"] ; clj parser
                  ]

  :profiles {:convert {; converts clj file to notebook
                       :main ^:skip-aot pinkgorilla.import.convert-main}
             :dev {:dependencies [[thheller/shadow-cljs "2.8.80"]
                                  [clj-kondo "2019.11.23"]]
                   :plugins      [[lein-cljfmt "0.6.6"]
                                  [lein-cloverage "1.1.2"]]
                   :aliases      {"clj-kondo" ["run" "-m" "clj-kondo.main"]}
                   :cloverage    {:codecov? true
                                  ;; In case we want to exclude stuff
                                  ;; :ns-exclude-regex [#".*util.instrument"]
                                  ;; :test-ns-regex [#"^((?!debug-integration-test).)*$$"]
                                  }
                   ;; TODO : Make cljfmt really nice : https://devhub.io/repos/bbatsov-cljfmt
                   :cljfmt       {:indents {as->                [[:inner 0]]
                                            with-debug-bindings [[:inner 0]]
                                            merge-meta          [[:inner 0]]
                                            try-if-let          [[:block 1]]}}}}


  :aliases {"build-shadow-ci"
            ["run" "-m" "shadow.cljs.devtools.cli" "compile" ":ci"]
            "bump-version"
            ["change" "version" "leiningen.release/bump-version"]
            "test-js" ^{:doc "Test compiled JavaScript."}
            ["do" "build-shadow-ci" ["shell" "./node_modules/karma/bin/karma" "start" "--single-run"]]
            "convert" ^{:doc "Converts clj file to notebook. Needs filename parameter"}
            ["with-profile" "convert" "run"]})


