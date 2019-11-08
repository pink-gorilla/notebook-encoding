

code-segment
   :type             :code
   :kernel           :clj
   :content          {:value (get-in free-segment [:content :value])
                      :type  "text/x-clojure"}
   :console-response nil
   :value-response   nil
   :error-text       nil
   :exception        nil

free-segment
  {:id             (-> (uuid/make-random-uuid) uuid/uuid-string keyword)
   :type           :free
   :markup-visible false
   :content        {:value (or content "")
                    :type  "text/x-markdown"}})



free = **
code = ;; @@
output ;; =>  ;; <=
console ;;->  ;; <-













worksheet = worksheetHeader seg:segmentWithBlankLine* {return seg;}
lineEnd = "\n" / "\r\n"
worksheetHeader = ";; gorilla-repl.fileformat = " content:wsVersion lineEnd lineEnd
                { worksheetVersion = content; }

segmentWithBlankLine = seg:segment lineEnd? {return seg;}

segment = freeSegment / codeSegment

freeSegment = freeSegmentOpenTag content:stringNoDelim? freeSegmentCloseTag
                {return pinkgorilla.db.create_free_segment(pinkgorilla.db.unmake_clojure_comment(content));}

freeSegmentOpenTag = ";; **" lineEnd
freeSegmentCloseTag = lineEnd ";; **" lineEnd

codeSegment = codeSegmentOpenTag content:stringNoDelim? codeSegmentCloseTag cs:consoleSection? out:outputSection?
                {return pinkgorilla.db.create_code_segment(contepinkgorilla.db.unmake_clojure_comment(cs), pinkgorilla.db.unmake_clojure_comment(out), worksheetVersion);}
codeSegmentOpenTag = ";; @@" lineEnd
codeSegmentCloseTag = lineEnd ";; @@" lineEnd

outputSection = outputOpenTag output:stringNoDelim outputCloseTag {return output;}
outputOpenTag = ";; =>" lineEnd
outputCloseTag = lineEnd ";; <=" lineEnd

consoleSection = consoleOpenTag cs:stringNoDelim consoleCloseTag {return cs;}
consoleOpenTag = ";; ->" lineEnd
consoleCloseTag = lineEnd ";; <-" lineEnd

stringNoDelim = cs:noDelimChar+ {return cs.join("");}

delimiter = freeSegmentOpenTag / freeSegmentCloseTag /codeSegmentOpenTag / codeSegmentCloseTag / outputOpenTag /
                outputCloseTag / consoleOpenTag / consoleCloseTag

noDelimChar = !delimiter c:. {return c;}

wsVersion = [12]

