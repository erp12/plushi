(ns pushi.core
  (:gen-class)
  (:require [pushi.encode :as e]
            [pushi.interpreter :as i]))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [program (e/parse-program (slurp "resources/simple_program.json") :json)]
    (-> (i/run-push (:code program) 5 (:output-types program) true)
        (println))))


; (defn -main
;   "I don't do a whole lot ... yet."
;   [& args]
  ; (println (a/recognize-atom-type (instr/get-instruction :integer_add))))
  ; (println (spec/explain :pushi.instruction/instruction (instr/get-instruction :integer_add))))
