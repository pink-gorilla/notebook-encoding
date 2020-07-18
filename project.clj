(defproject org.pinkgorilla/notebook-encoding "0.1.10-SNAPSHOT"
  :description "Encoding of Pink Gorilla Notebook."
  :url "https://github.com/pink-gorilla/notebook-encoding"
  :license {:name "MIT"}
  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :username :env/release_username
                                     :password :env/release_password
                                     :sign-releases false}]]


    ;; TODO: prep tasks breaks alias???
  ;; :prep-tasks ["build-shadow-ci"]

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

  :plugins []

  :managed-dependencies [; to avoid a :exclusion mess, we define certain versions numbers centrally

                         ; clojure
                         [org.clojure/clojure "1.10.1"]
                         [org.clojure/core.async "1.2.603"]
                         [org.clojure/clojurescript "1.10.773"]
                         [org.clojure/tools.analyzer "1.0.0"]
                         [com.google.javascript/closure-compiler-unshaded "v20200406"]
                         [com.google.code.findbugs/jsr305 "3.0.2"]
                         [org.clojure/tools.logging "1.1.0"]

                         ; shadow-cljs
                         [thheller/shadow-cljs "2.8.81"]
                         [org.jboss.logging/jboss-logging "3.4.1.Final"]

                         ; serialization libraries are dependencies of many libraries,
                         [org.clojure/core.memoize "1.0.236"]
                         [org.clojure/data.json "1.0.0"]
                         [org.clojure/data.fressian "1.0.0"]
                         [org.clojure/data.xml "0.0.8"]
                         [org.clojure/data.csv "1.0.0"]
                         [org.clojure/core.match "1.0.0"]
                         [com.cognitect/transit-clj "1.0.324"]
                         [com.cognitect/transit-cljs "0.8.264"]
                         [com.cognitect/transit-java "1.0.343"]

                         [com.fasterxml.jackson.core/jackson-core "2.11.1"]
                         [com.fasterxml.jackson.dataformat/jackson-dataformat-cbor "2.11.1"]
                         [com.fasterxml.jackson.dataformat/jackson-dataformat-smile "2.11.1"]

                         [cheshire "5.10.0"]
                         [com.taoensso/encore "2.122.0"]
                         ; patches to get most uptodate version for certain conflicts:
                         [commons-codec "1.14"] ; selmer and clj-http (via gorilla-explore)
                         [ring/ring-codec "1.1.2"] ; ring and compojure
                         [org.flatland/useful "0.11.6"] ; clojail and ring-middleware-format

                         [tigris "0.1.2"]]


  :dependencies  [[org.clojure/clojure "1.10.1"]
                  [instaparse "1.4.10"] ; used in decoding
                  [com.cognitect/transit-clj "1.0.324"] ; used in encoding - clojure
                  [com.cognitect/transit-js "0.8.861"] ;transit-cljs has old dependency that does not work with meta-data, see: https://github.com/cognitect/transit-cljs/issues/48
                  [com.cognitect/transit-cljs "0.8.264"] ; used in encoding - clojurescript
                  [com.lucasbradstreet/cljs-uuid-utils "1.0.2"] ; uuid - clojurescript
                  [com.taoensso/timbre "4.10.0"] ; cljs logging
                  ;[org.clojure/tools.logging "0.5.0"] ; clj logging

                  [cheshire "5.10.0"] ; tentacles dependency
                  [irresponsible/tentacles "0.6.6" ; github api  https://github.com/clj-commons/tentacles
                   :exclusions [cheshire]]
                  ;[cheshire "5.7.1"] ; tentacles dependency, JSON and JSON SMILE (binary json format) encoding/decoding
                  [me.raynes/fs "1.4.6"]
                  [org.clojure/clojurescript "1.10.773"]  ; for marginalia
                  [org.clojure/tools.cli "1.0.194"] ; for marginalia
                  [marginalia "0.9.1"
                   :exclusions [org.clojure/clojure
                                org.clojure/clojurescript
                                org.clojure/tools.cli]] ; clj parser
                  ]

  :profiles {:convert {; converts clj file to notebook
                       :main ^:skip-aot pinkgorilla.import.convert-main}
             :dev {:dependencies [[thheller/shadow-cljs "2.8.81"]
                                  [clj-kondo "2020.06.21"]]
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


  :aliases {"build-shadow-ci"
            ["run" "-m" "shadow.cljs.devtools.cli" "compile" ":ci"]

            "bump-version"
            ["change" "version" "leiningen.release/bump-version"]

            "test-js" ^{:doc "Test compiled JavaScript."}
            ["do" "build-shadow-ci" ["shell" "./node_modules/karma/bin/karma" "start" "--single-run"]]

            "convert" ^{:doc "Converts clj file to notebook. Needs filename parameter"}
            ["with-profile" "convert" "run"]})


