(ns pinkgorilla.document.default-config
  (:require

    ; encoding  string <-> notebook
   [pinkgorilla.encoding.decode]  ; gorilla format decoding
   [pinkgorilla.encoding.encode] ; gorilla format encoding
   [pinkgorilla.encoding.jupyter] ; ipynb jupyter notebook    

   ; temporary deactivated marginalia, as it causes issues in lein plugin
   ;#?(:clj [pinkgorilla.encoding.marginalia]) ; clj with comments and output annotations
   #?(:clj [pinkgorilla.encoding.clj]) ; clj without comments

   ; storage map <-> storage
   [pinkgorilla.storage.file]
   [pinkgorilla.storage.res]
   [pinkgorilla.storage.gist]
   [pinkgorilla.storage.repo]
   [pinkgorilla.storage.bitbucket]

   #?(:clj [pinkgorilla.storage.impl.file])
   #?(:clj [pinkgorilla.storage.impl.res])
   #?(:clj [pinkgorilla.storage.impl.gist])
   #?(:clj [pinkgorilla.storage.impl.repo])

   ;[pinkgorilla.storage.direct.file]
   ;[pinkgorilla.storage.direct.gist]
   ;[pinkgorilla.storage.direct.repo]
   ;[pinkgorilla.storage.direct.bitbucket]
   ))