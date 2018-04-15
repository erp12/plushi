(ns plushi.instruction.io
  "This namespce contains functions which register input and output instructions
  to the instruction-set atom. The number of instructions required from this
  namespace will vary based on the program being run, thus these functions are
  generally called some time after the rest of insructions have been registered."
  (:require [plushi.instruction :as i]
            [plushi.state :as s]))


(defn register-input-instructions
  "Given some positive integer denoteing the number of input, a set of input
  instructions is registered to the instruction set."
  [num-inputs]
  (doall
    (map #(i/register (str "input_" %)
                      (fn [state]
                        (nth (:inputs state) %))
                :STATE [:exec] 0)
         (range num-inputs))))


(defn remove-input-instructions
  "Removes all input instructions from the instruction set."
  []
  (doall
    (map #(i/unregister (:name %))
         (i/get-supported-instructions :all #"input_."))))


(defn register-print-instruction
  [type-kw]
  (let [type-str (name type-kw)]
    (i/register (str type-str "_print")
                #(str %)
                [type-kw] [:stdout] 0)))


(doall (map register-print-instruction [:integer :float :string :char :boolean]))
