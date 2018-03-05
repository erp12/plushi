(ns plushi.core
  (:gen-class)
  (:require [clojure.tools.cli :refer [parse-opts]]
            [plushi.encode :as e]
            [plushi.translate :as t]
            [plushi.instruction :as instr]
            [plushi.instruction.io :as instr-io]
            [plushi.interpreter :as i]))


(def cli-options
  [["-I" "--instruction-set" "Get serialized list of supported instructions."]
   ["-T" "--supported-types" "Get serialized list of supported types."]
   ["-R" "--run PROGRAM" "Run a push program."]
   ["-D" "--dataset DATASET" "Pass serialized dataset of input values to program."
    :default []]
   ["-f" "--format FORMAT" "Data format"
    :default "json"
    :validate [#(or (= % "json") (= % "edn")) "Invalid data format option"]]
   ["-a" "--arity ARITY" "Set arity of the push programs."
    :default 0
    :parse-fn #(Integer/parseInt %)
    :validate [int?]]
   ["-d" "--docs PATH" "Generates an html file documenting the instruction set."]
   ["-v" "--verbose"]
   ["-h" "--help"]])


(defn validate-cli-opts
  [cli-opts]
  (cond
    (:instruction-set cli-opts)
    (if (or (nil? (:format cli-opts))
            (nil? (:arity cli-opts)))
      (throw (Exception. "When getting instructions, --format and --arity must be specified.")))

    (:supported-types cli-opts)
    (if (nil? (:format cli-opts))
      (throw (Exception. "When getting supported types, --format must be specified.")))

    (:run cli-opts)
    (if (or (nil? (:format cli-opts))
            (nil? (:dataset cli-opts)))
      (throw (Exception. "When running programs --format, and --inputs must be specified.")))))


;; Used for faster development.
; (defn -main
;   "I don't do a whole lot ... yet."
;   [& args]
;   (let [program (e/parse-program (slurp "resources/simple_program.edn") "edn")]
;     (println program)
;     (-> (i/run-push (:code program) [] (:output-types program) true)
;         (println))))


(defn run-on-dataset
  [plush-program dataset is-verbose]
  (let [push-program (t/plush-to-push (:code plush-program))
        f #(i/run-push push-program % (:output-types plush-program) is-verbose)]
    (vec (map f (:data dataset)))))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [cli-map (parse-opts args cli-options)
        ;; _ (println cli-map)
        cli-opts (:options cli-map)
        is-verbose (not (nil? (:verbose cli-opts)))]
    (validate-cli-opts cli-opts)

    (cond
      ;; If there were cli arg parsing errors
      (not (nil? (:errors cli-map)))
      (throw (Exception. (first (:errors cli-map))))

      ;; the help flag is included.
      (not (nil? (:help cli-opts)))
      (println (:summary cli-map))

      ;; If the user is trying to get the set of supported instructions.
      (not (nil? (:instruction-set cli-opts)))
      (do
        (instr-io/register-input-instructions (:arity cli-opts))
        (println (e/encode-instruction-set (instr/get-supported-instructions)
                                           (:format cli-opts))))

      ;; If the user is trying to get the set of supported types.
      (not (nil? (:supported-types cli-opts)))
      (println (e/encode-supported-types (:format cli-opts)))

      ;; If the user is running  push program.
      (not (nil? (:run cli-opts)))
      (let [dataset (e/parse-input-dataset (:dataset cli-opts)
                                           (:format cli-opts))
            plush-program (e/parse-program (:run cli-opts)
                                     (:format cli-opts))
            outputs (run-on-dataset plush-program dataset is-verbose)]
        (println (e/encode-outputs outputs (:format cli-opts))))

      ;; If the user is generating instruction set documentation
      (not (nil? (:docs cli-opts)))
      (e/encode-instruction-set-docs (:docs cli-opts))

      :else
      (throw (Exception. "Must supply a valid option. See --help.")))))
