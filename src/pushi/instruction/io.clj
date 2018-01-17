(ns pushi.instruction.io
  "This namespce contains functions which register input and output instructions
  to the instruction-set atom. The number of instructions required from this
  namespace will vary based on the program being run, thus these functions are
  generally called some time after the rest of insructions have been registered."
  (:require [pushi.instruction :as i]
            [pushi.state :as s]))


(defn register-input-instructions
  "Given some positive integer denoteing the number of input, a set of input
  instructions is registered to the instruction set."
  [num-inputs]
  (doall
    (map #(i/register (str "input_" %)
                      (fn [state]
                        (s/push-item state :exec (nth (:inputs state) %)))
                :STATE :STATE)
         (range num-inputs))))


;; TODO: Print instructions
