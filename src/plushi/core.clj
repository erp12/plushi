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
                 (dissoc :code-blocks)
                 ((fn [instr-map]
                    (if include-docstrings
                      instr-map
                      (dissoc instr-map :docstring)))))
            instruction-set)))


;; Server Functions

(defn instruction-set-response-body
  [{:keys [arity docstrings] :or {arity 0 docstrings false}}]
  (do
    (instr-io/remove-input-instructions)
    (instr-io/register-input-instructions arity)
    (prepare-instruction-set (instr/get-supported-instructions) docstrings)))


(defn run-program-response-body
  [{:keys [code arity output-types dataset verbose] :or {verbose false}}]
  (instr-io/remove-input-instructions)
  (instr-io/register-input-instructions arity)
  (let [dataset (ds/json-data-to-dataset dataset)
        push-program (t/plush-to-push code)
        output-types (map keyword output-types)]
    (run-on-dataset push-program output-types dataset verbose)))


(defn handler [request]
  (let [request-body (json/read-str (get request :body) :key-fn keyword)]
    (response
      (cond
        (= "instructions" (:action request-body))
        (instruction-set-response-body request-body)

        (= "types" (:action request-body))
        (instr/get-supported-types)

        (= "run" (:action request-body))
        (run-program-response-body request-body)))))


(def plushi-server
  (-> handler
      wrap-json-body
      wrap-json-response))


;; CLI Interface

(def cli-options
  [["-s" "--start" "Start the Plushi server."]
  ["-p" "--port PORT" "Port number. Default is 8075."
   :default 8075
   :parse-fn #(Integer/parseInt %)
   :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ["-d" "--docs PATH" "Generates an html file documenting the instruction set."]
   ["-h" "--help"]])


(defn -main
  [& args]
  (let [cli-map (parse-opts args cli-options)
        ;; _ (println cli-map)
        cli-opts (:options cli-map)]
    (cond
      ;; If there were cli arg parsing errors
      (not (nil? (:errors cli-map)))
      (throw (Exception. (first (:errors cli-map))))

      ;; the help flag is included.
      (not (nil? (:help cli-opts)))
      (println (:summary cli-map))

      ;; If the user is trying to get the set of supported instructions.
      (not (nil? (:start cli-opts)))
      (run-jetty plushi-server {:port (:port cli-opts)})

      ;; If the user is generating instruction set documentation
      (not (nil? (:docs cli-opts)))
      (spit (:docs cli-opts) (t/instruction-set-docs-to-html @instr/instruction-set))

      :else
      (throw (Exception. "Must supply a valid option. See --help.")))))
