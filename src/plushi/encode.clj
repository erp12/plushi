(ns plushi.encode
  "This namespace contains functions that parse serialized push programs,
  and serialized input vectors. This namespace also provides functions to
  encode program outputs and supported instructions."
  (:require [clojure.edn :as edn]
            [clojure.data.json :as json]
            [clojure.string :as str]
            [hiccup.core :as h]
            [hiccup.page :as hp]
            [plushi.instruction :as instr]
            [plushi.instruction.io :as instr-io]
            [plushi.utils :as u]
            [plushi.dataset.json :as json-dataset]))


(defn- parse-code-vector
  "Parses a vector which represents a push program. Keywords or strings that
  start with 'plushi:' are considered instruction names. "
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
               (str/starts-with? serialized-atom "plushi:"))
          (recur (rest remaining-code-vec)
                 (conj code
                       (instr/get-instruction (keyword (second (str/split serialized-atom
                                                                          #"plushi:"))))))

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
        output-types (map keyword (get program-map "output-types"))]
    (instr-io/register-input-instructions arity)
    {:code (parse-code-vector (get program-map "code"))
     :arity arity
     :output-types output-types}))


(defn- parse-program-edn
 [program-edn]
 (let [program-map (edn/read-string program-edn)
       arity (:arity program-map)]
   (instr-io/register-input-instructions arity)
   {:code (parse-code-vector (:code program-map))
    :arity arity
    :output-types (:output-types program-map)}))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; PUBLIC FUNCTIONS

; TODO: Check all required instructions exist.
(defn parse-program
  "Parses a serialized program into a runnable program list. Supported
  serialized program formats are :json and :edn."
  [serialized-program format]
  (cond
    (= format "json")
    (parse-program-json serialized-program)

    (= format "edn")
    (parse-program-edn serialized-program)))


(defn parse-input-dataset
  [serialized-inputs format]
  (cond
    (= format "json")
    (json-dataset/json-to-dataset serialized-inputs)

    (= format "edn")
    (edn/read-string serialized-inputs)))


(defn encode-instruction-set
  [instruction-set format]
    (cond
      (= format "json")
      (json/write-str (vec (map #(-> (assoc % :name (str "plushi:" (:name %)))
                                     (dissoc :function))
                                instruction-set)))

      (= format "edn")
      (pr-str (vec (map #(dissoc % :function)
                        instruction-set)))))


(defn encode-supported-types
  [format]
  (cond
    (= format "json")
    (json/write-str (instr/get-supported-types))

    (= format "edn")
    (pr-str (instr/get-supported-types))))


(defn encode-outputs
  [output-vals format]
  (cond
    (= format "json")
    (json/write-str output-vals)

    (= format "edn")
    (pr-str output-vals)))


(defn encode-instruction-set-docs
  [path]
  (spit path
        (hp/html5 [:head (hp/include-css "css/default.css")]
                  [:body
                   (list
                     [:div#header
                      [:h1
                       [:a {:href "index.html"}
                        [:span.project-title
                         [:span.project-name "plushi Documentation"]]]]]
                     [:div#content.namespace-docs {:style "left:0px;"}
                      (list [:h2#top.anchor "Instruction Set"]
                            [:pre.doc "Documentation on the supported instructions of the plushi interpreter."]
                            (for [i (sort #(compare (:name %1) (:name %2))
                                          (vals @instr/instruction-set))]
                              [:div#var-image.public.anchor
                                (list [:h3 (:name i)]
                                      [:div.usage
                                       [:code (str "Input Types: " (pr-str (:input-types i)))]
                                       [:code (str "Output Types: " (pr-str (:output-types i)))]]
                                      [:div.doc
                                       [:pre.plaintext (:docstring i)]])]))])])))
