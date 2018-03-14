(ns plushi.core
  (:gen-class)
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.data.json :as json]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :refer [response]]
            [ring.adapter.jetty :refer [run-jetty]]
            [plushi.translate :as t]
            [plushi.instruction :as instr]
            [plushi.instruction.io :as instr-io]
            [plushi.interpreter :as i]
            [plushi.dataset.json :as ds]))


;; Running Plushi

(defn run-on-dataset
  [push-program output-types dataset verbose]
  (vec (map #(i/run-push push-program % output-types verbose)
            (:data dataset))))


(defn prepare-instruction-set
  [instruction-set include-docstrings]
  (vec (map #(-> (assoc % :name (str "plushi:" (:name %)))
                 (dissoc :function)
                 ((fn [i]
                    (if include-docstrings
                      i
                      (dissoc i :docstring)))))
            instruction-set)))

;; Server Functions

(defn instruction-set-response
  [{:keys [arity docstrings] :or {arity 0 docstrings false}}]
  (do
    (instr-io/register-input-instructions arity)
    (response (prepare-instruction-set (instr/get-supported-instructions)
                                       docstrings))))


(defn supported-types-response
  []
  (response (instr/get-supported-types)))


(defn run-program-response
  [{:keys [code arity output-types dataset verbose] :or {verbose false}}]
  (let [dataset (ds/json-data-to-dataset dataset)
        push-program (t/plush-to-push code)
        output-types (map keyword output-types)]
    (instr-io/register-input-instructions arity)
    (response (run-on-dataset push-program output-types dataset verbose))))


(defn handler [request]
  (let [request-body (json/read-str (get request :body) :key-fn keyword)]
    (println request-body)
    (cond
      (= "instructions" (:action request-body))
      (instruction-set-response request-body)

      (= "types" (:action request-body))
      (supported-types-response)

      (= "run" (:action request-body))
      (run-program-response request-body)

      (= "shutdown" (:action request-body))
      (System/exit 0)

      )))


(def plushi-server
  (-> handler
      wrap-json-body
      wrap-json-response))


;; TODO: Change CLI to do things like setting port.
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (run-jetty plushi-server {:port 8080}))






;
; (def cli-options
;   [["-I" "--instruction-set" "Get serialized list of supported instructions."]
;    ["-T" "--supported-types" "Get serialized list of supported types."]
;    ["-R" "--run PROGRAM" "Run a push program."]
;    ["-D" "--dataset DATASET" "Pass serialized dataset of input values to program."
;     :default []]
;    ["-f" "--format FORMAT" "Data format"
;     :default "json"
;     :validate [#(or (= % "json") (= % "edn")) "Invalid data format option"]]
;    ["-a" "--arity ARITY" "Set arity of the push programs."
;     :default 0
;     :parse-fn #(Integer/parseInt %)
;     :validate [int?]]
;    ["-d" "--docs PATH" "Generates an html file documenting the instruction set."]
;    ["-v" "--verbose"]
;    ["-h" "--help"]])
;
;
; (defn validate-cli-opts
;   [cli-opts]
;   (cond
;     (:instruction-set cli-opts)
;     (if (or (nil? (:format cli-opts))
;             (nil? (:arity cli-opts)))
;       (throw (Exception. "When getting instructions, --format and --arity must be specified.")))
;
;     (:supported-types cli-opts)
;     (if (nil? (:format cli-opts))
;       (throw (Exception. "When getting supported types, --format must be specified.")))
;
;     (:run cli-opts)
;     (if (or (nil? (:format cli-opts))
;             (nil? (:dataset cli-opts)))
;       (throw (Exception. "When running programs --format, and --inputs must be specified.")))))
;
;
; ;; Used for faster development.
; ; (defn -main
; ;   "I don't do a whole lot ... yet."
; ;   [& args]
; ;   (let [program (e/parse-program (slurp "resources/simple_program.edn") "edn")]
; ;     (println program)
; ;     (-> (i/run-push (:code program) [] (:output-types program) true)
; ;         (println))))
;
;
;
; (defn -main
;   "I don't do a whole lot ... yet."
;   [& args]
;   (let [cli-map (parse-opts args cli-options)
;         ;; _ (println cli-map)
;         cli-opts (:options cli-map)
;         is-verbose (not (nil? (:verbose cli-opts)))]
;     (validate-cli-opts cli-opts)
;
;     (cond
;       ;; If there were cli arg parsing errors
;       (not (nil? (:errors cli-map)))
;       (throw (Exception. (first (:errors cli-map))))
;
;       ;; the help flag is included.
;       (not (nil? (:help cli-opts)))
;       (println (:summary cli-map))
;
;       ;; If the user is trying to get the set of supported instructions.
;       (not (nil? (:instruction-set cli-opts)))
;       (do
;         (instr-io/register-input-instructions (:arity cli-opts))
;         (println (e/encode-instruction-set (instr/get-supported-instructions)
;                                            (:format cli-opts))))
;
;       ;; If the user is trying to get the set of supported types.
;       (not (nil? (:supported-types cli-opts)))
;       (println (e/encode-supported-types (:format cli-opts)))
;
;       ;; If the user is running  push program.
;       (not (nil? (:run cli-opts)))
;       (let [dataset (e/parse-input-dataset (:dataset cli-opts)
;                                            (:format cli-opts))
;             plush-program (e/parse-program (:run cli-opts)
;                                      (:format cli-opts))
;             outputs (run-on-dataset plush-program dataset is-verbose)]
;         (println (e/encode-outputs outputs (:format cli-opts))))
;
;       ;; If the user is generating instruction set documentation
;       (not (nil? (:docs cli-opts)))
;       (e/encode-instruction-set-docs (:docs cli-opts))
;
;       :else
;       (throw (Exception. "Must supply a valid option. See --help.")))))
