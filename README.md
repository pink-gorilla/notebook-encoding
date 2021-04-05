# PinkGorilla Notebook Encoding [![GitHub Actions status |pink-gorilla/notebook-encoding](https://github.com/pink-gorilla/notebook-encoding/workflows/CI/badge.svg)](https://github.com/pink-gorilla/notebook-encoding/actions?workflow=CI)[![Clojars Project](https://img.shields.io/clojars/v/org.pinkgorilla/notebook-encoding.svg)](https://clojars.org/org.pinkgorilla/notebook-encoding)

- This project is used in [Notebook](https://github.com/pink-gorilla/gorilla-notebook) ,
  but the library can be used independently.
- **Encoding** Converts a notebook data-format to a notebook datastructure in memory.
- **Storage** The string gets read/written to the .cljg notebook files.

# Features
- Storage Backends: 
  - file
  - github repo 
  - github gist

- Formats: 
  - pinkgorilla (v1/v2)
  - jupyter
  - clj (marginalia format)
- parsing works on clj and cljs

# Unit Tests

Tests going to github need credential file: `test/creds.edn`. There is is
a `sample-creds.edn` to copy for convenience.

clj:

```
lein test
lein test :only pinkgorilla.notebook.new-notebook
```

If you want to run tests, but don't want to run tests that need creds, then
you can run `lein test :no-creds`

cljs:
```
npm install
lein test-js
```

# Notebook Converter

For development purpose use:

```
lein convert /tmp/import-test.clj
```
clj code in /tmp/import-test.clj will be saved as a pink-gorilla notebook to /tmp/import-test.cljg

If you are a user, please use lein-pinkgorilla with the same syntax.

# Todo
- add spec from notebook here
- extract clojure / clojurescript routine move here from notebook
- cli utility to test all notebooks ?
- github api tentacles does not support cljs!


# marginalia
marginalia import makes problems with lein pinkgorilla.
enabled only clj import for now.
aot issue ??