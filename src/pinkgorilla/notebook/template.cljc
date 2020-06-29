(ns pinkgorilla.notebook.template
  "Create a new notebook:
   Take a sample notebook, and give it a hipster namespace name"
  (:require
   [pinkgorilla.notebook.core :refer [empty-notebook add-md add-code]]
   [pinkgorilla.notebook.hipster :refer [make-hip-nsname]]))

(defn create-new-worksheet
  "A pure function that creates a new worksheet in the browser.
  All db functions used are pure functions!"
  ([]
   (create-new-worksheet (make-hip-nsname)))
  ([hip-nsname]
   (-> empty-notebook
       (add-md "# Pink Gorilla \n\n"
               "Shift + enter evaluates code. "
               "Hit CTRL+g twice in quick succession or click the menu icon (upper-right corner) for more commands.\n\n"
               "It's a good habit to run each worksheet in its own namespace. We created a random namespace for you; you can keep using it.")
       (add-code
        :clj
        "; Automatically Download Dependencies (if they are not installed already) \n "
        "(use '[pinkgorilla.notebook.repl]) \n "
        "(pinkgorilla.notebook.repl/add-dependencies '[org.pinkgorilla/gorilla-plot \"0.9.3\"])")

       (add-code
        :clj
        "; Define Namespace for your notebook and require namespaces \n"
        "(ns " hip-nsname "  \n"
        "  (:require \n"
        "     [pinkgorilla.ui.gorilla-plot.core :refer [list-plot bar-chart compose histogram plot]] \n"
        "     ))\n")

       (add-code
        :clj
        "^:R \n"
        "  [:<> \n"
        "    [:h4 \"Hello, World!\"] \n"
        "    [:div {:style {:display :flex :flex-direction :row\n"
        "                   :color :green :font-weight :bold :background-color :pink \n"
        "                   :margin-left 20 :padding 10 :border :solid :width 500}} \n"
        "      [:ol \n"
        "        [:li \"The Pinkie\"] \n"
        "        [:li \"The Pinkie and the Brain\"]  \n"
        "        [:li \"What will we be doing today?\"]]  \n"
        "      [:img {:height 100 :width 100 \n"
        "             :src \"https://images-na.ssl-images-amazon.com/images/I/61LeuO%2Bj0xL._SL1500_.jpg\"}]]]")

       (add-code
        :clj
        " ^:R [:p/vega \"https://raw.githubusercontent.com/vega/vega/master/docs/examples/bar-chart.vg.json\" ]")

       (add-code
        :clj
        "(list-plot (concat (range 10) (reverse (range 10))))"))))

(comment
  (create-new-worksheet)
;  
  )