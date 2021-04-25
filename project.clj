(defproject org.pinkgorilla/notebook-encoding "0.1.32-SNAPSHOT"
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

  :source-paths ["src"]
  :test-paths ["test"]
  :target-path  "target/jar"
  :resource-paths ["resources"]
  :plugins []
  :dependencies  [[org.clojure/clojure "1.10.3"]
                  [instaparse "1.4.10"] ; used in decoding
                  [com.cognitect/transit-clj "1.0.324"] ; used in encoding - clojure
                  [com.cognitect/transit-js "0.8.867"] ;transit-cljs has old dependency that does not work with meta-data, see: https://github.com/cognitect/transit-cljs/issues/48
                  [com.cognitect/transit-cljs "0.8.264"] ; used in encoding - clojurescript
                  [com.lucasbradstreet/cljs-uuid-utils "1.0.2"] ; uuid - clojurescript
                  [com.taoensso/timbre "5.1.2"] ; clj/cljs logging
                  [cheshire "5.10.0"] ; tentacles dependency
                  [irresponsible/tentacles "0.6.8" ; github api  https://github.com/clj-commons/tentacles
                   :exclusions [cheshire]]
                  [me.raynes/fs "1.4.6"]
                  ;[org.clojure/clojurescript "1.10.844"]  ; for marginalia
                  [org.clojure/tools.cli "1.0.206"] ; for marginalia
                  [marginalia "0.9.1"
                   :exclusions [org.clojure/clojure
                                org.clojure/clojurescript
                                org.clojure/tools.cli]] ; clj parser
                  ]

  :profiles {:convert {; converts clj file to notebook
                       :main ^:skip-aot pinkgorilla.import.convert-main}

             :demo {:main  demo.main
                    :source-paths ["profiles/demo/src"
                                   "test"]}

             :dev {:resource-paths ["test/resources"]
                   :dependencies [ ;[org.pinkgorilla/webly "0.2.5"]
                                  [org.clojure/data.json "2.1.0"] ; https://github.com/thheller/shadow-cljs/issues/872
                                  [thheller/shadow-cljs "2.12.5"
                                   :exclusions [org.clojure/data.json]]
                                  [clj-kondo "2021.03.31"]]
                   :plugins      [[lein-cljfmt "0.6.6"]
                                  [lein-cloverage "1.1.2"]
                                  [lein-ancient "0.6.15"]
                                  [lein-shell "0.5.0"]]
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

   ; no-creds runs only subset of lein test
  :test-selectors {:no-creds (fn [m]
                               (not (clojure.string/includes? (str (:name m))
                                                              "-creds")))}


  :aliases {"bump-version"
            ["change" "version" "leiningen.release/bump-version"]

            "build-shadow-ci"
            ["run" "-m" "shadow.cljs.devtools.cli" "compile" ":ci"]

            "test-js" ^{:doc "Test compiled JavaScript."}
            ["do" 
             "build-shadow-ci" 
             ["shell" "./node_modules/karma/bin/karma" "start" "--single-run"]]

            "lint"  ^{:doc "Lint for dummies"}
            ["clj-kondo"
             "--config" "clj-kondo.edn"
             "--lint" "src"]

            "convert" ^{:doc "Converts clj file to notebook. Needs filename parameter"}
            ["with-profile" "convert" "run"]

            "demo"
            ["with-profile" "demo" "run"]})


