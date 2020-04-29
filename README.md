# PinkGorilla Notebook Encoding [![GitHub Actions status |pink-gorilla/notebook-encoding](https://github.com/pink-gorilla/notebook-encoding/workflows/CI/badge.svg)](https://github.com/pink-gorilla/notebook-encoding/actions?workflow=CI)[![Clojars Project](https://img.shields.io/clojars/v/org.pinkgorilla/notebook-encoding.svg)](https://clojars.org/org.pinkgorilla/notebook-encoding)


Convert the notebook datastructure in memory to/from string.
The string gets read/written to the .cljg notebook files.

# Unit Tests

clj:

```
lein test
lein test :only pinkgorilla.notebook.new-notebook
```

Tests going to github need credential file: `test/creds.edn`. There is is
a `sample-creds.edn` to copy for convenience.

cljs:
```
lein test-js
```

# .clj -> Notebook Converter
```
lein convert /tmp/import-test.clj
```
clj code in /tmp/import-test.clj will be saved as a pink-gorilla notebook to /tmp/import-test.cljg

You might want to add an alias to ~/.bashrc

```
alias nbconvert="cd /home/andreas/Documents/gorilla/notebook-encoding && lein convert"
```

## Todo:
- add spec from notebook here
- extract clojure / clojurescript routine move here from notebook
- cli utility to test all notebooks ?

