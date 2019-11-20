PinkGorilla Notebook - Encoding


Convert the notebook datastructure in memory to/from string.
The string gets read/written to the .cljg notebook files.

# Unit Tests

clj:
´´´
lein test
lein test :only pinkgorilla.notebook.new-notebook
´´´

Tests going to github need credential file: `test/creds.edn`. There is is
a `sample-creds.edn` to copy for convenience.

cljs:
´´´
lein doo
´´´


## Todo:
- unit test clojurescript - inject notebook strings via macro to code
- add spec from notebook here
- extract clojure / clojurescript routine move here from notebook
- add kernel type 
- add meta
- cli utility to test all notebooks ?

