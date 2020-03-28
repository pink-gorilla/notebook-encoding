(ns pinkgorilla.notebook.newnb
  "Create a new notebook:
   Take a sample notebook, and give it a hipster namespace name"
  (:require
   [pinkgorilla.notebook.core :as notebook]
   [pinkgorilla.notebook.hipster :refer [make-hip-nsname]]))

(defn create-new-worksheet
  "A pure function that creates a new worksheet in the browser.
  All db functions used are pure functions!"
  []
  (let [worksheet (notebook/empty-notebook)
        markdown-howto
        (notebook/create-free-segment
         (str "# Pink Gorilla \n\n"
              "Shift + enter evaluates code. "
              "Hit CTRL+g twice in quick succession or click the menu icon (upper-right corner) for more commands.\n\n"
              "It's a good habit to run each worksheet in its own namespace. We created a random namespace for you; you can keep using it."))
        code-dependencies
        (notebook/create-code-segment
         (str  "; Automatically Download Dependencies (if they are not installed already) \n "
               "(use '[pinkgorilla.helper]) \n "
               "(pinkgorilla.helper/add-dependencies '[org.pinkgorilla/gorilla-plot \"0.9.2\"])"))

        code-namespace
        (notebook/create-code-segment
         (str
          "; Define Namespace for your notebook and require namespaces \n"
          "(ns " (make-hip-nsname) "  \n"
          "  (:require \n"
          "     [pinkgorilla.ui.gorilla-plot.core :refer [list-plot bar-chart compose histogram plot]] \n"
          "     ))\n"))

        code-html
        (notebook/create-code-segment
         (str
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
          "             :src \"https://images-na.ssl-images-amazon.com/images/I/61LeuO%2Bj0xL._SL1500_.jpg\"}]]]"))

        code-vega
        (notebook/create-code-segment
         (str
          " ^:R [:vega \"https://raw.githubusercontent.com/vega/vega/master/docs/examples/bar-chart.vg.json\" ]"))
        code-plot
        (notebook/create-code-segment
         (str
          "(list-plot (concat (range 10) (reverse (range 10))))"))]
    (-> worksheet
        (notebook/insert-segment-at 0 markdown-howto)
        (notebook/insert-segment-at 1 code-dependencies)
        (notebook/insert-segment-at 2 code-namespace)
        (notebook/insert-segment-at 3 code-html)
        (notebook/insert-segment-at 5 code-vega)
        (notebook/insert-segment-at 6 code-plot))))
