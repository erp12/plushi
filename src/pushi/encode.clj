(ns pushi.encode
  "This namespace contains functions that parse serialized push programs,
  and serialized input vectors. This namespace also provides functions to
  encode program outputs and supported instructions."
  (:require [clojure.edn :as edn]
            [clojure.data.json :as json]
            [clojure.string :as str]
            [pushi.instruction :as instr]
            [pushi.instruction.io :as instr-io]
            [pushi.utils :as u]))


(defn- parse-code-vector
  "Parses a vector which represents a push program. Keywords or strings that
  start with 'pushi:' are considered instruction names. "
  [code-vec]
  (loop [remaining-code-vec code-vec
         code []]
    (if (empty? remaining-code-vec)
      (u/vector-to-list code)
      (let [serialized-atom (first remaining-code-vec)]
        (cond
          ; If atom is a vector, it is a nested push expression.
          (vector? serialized-atom)
          (recur (rest remaining-code-vec)
                 (conj code (parse-code-vector serialized-atom)))

          ; If atom is an instruction name in a string.
          (and (string? serialized-atom)
               (str/starts-with? serialized-atom "pushi:"))
          (recur (rest remaining-code-vec)
                 (conj code
                       (instr/get-instruction (keyword (second (str/split serialized-atom
                                                                          #"pushi:"))))))

          ; If atom is an instruction name in a keyword
          (keyword? serialized-atom)
          (recur (rest remaining-code-vec)
                 (conj code (instr/get-instruction serialized-atom)))

          :else
          (recur (rest remaining-code-vec)
                 (conj code serialized-atom)))))))



(defn- parse-program-json
  [program-json]
  (let [program-map (json/read-str program-json)
        arity (get program-map "arity")
        output-types (map keyword (get program-map "output_types"))]
    (instr-io/register-input-instructions arity)
    {:code (parse-code-vector (get program-map "code"))
     :arity arity
     :output-types output-types}))


(defn- parse-program-edn
 [program-edn]
 )


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; PUBLIC FUNCTIONS

; TODO: Check all required instructions exist.
(defn parse-program
  "Parses a serialized program into a runnable program list. Supported
  serialized program formats are :json and :edn."
  [serialized-program format]
  (cond
    (= format :json)
    (parse-program-json serialized-program)))


(defn parse-inputs
  [])


(defn encode-instruction-set
  [])


(defn encode-outputs
  [])
