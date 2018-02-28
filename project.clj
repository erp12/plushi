(defproject plushi "0.1.0-SNAPSHOT"
  :description "A language agnostic Plush language interpreter."
  :url "https://github.com/erp12/plushi"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/tools.cli "0.3.5"]
                 [hiccup "1.0.5"]]
  :plugins [[lein-codox "0.10.3"]]
  :main ^:skip-aot pushi.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :codox {:namespaces [pushi.atoms pushi.core pushi.encode pushi.instruction
                       pushi.interpreter pushi.state pushi.utils
                       pushi.instruction.io]
          :metadata {:doc "FIXME: write docs"}})
