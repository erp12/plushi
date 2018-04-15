(ns plushi.instruction
  "This namespace contains function which manage the set of supported
  instructinos, and the related information such as supported data types."
  (:require [clojure.spec.alpha :as spec]
            [plushi.utils :as u]))


(def instruction-set
  "An clojure atom containing a map of all registered instructions. Keys are
  keywords made from instruction names. Values are maps that conforms to the
  :plushi.instruction/instruction spec."
  (atom {}))


(spec/def ::name string?)
(spec/def ::function fn?)
(spec/def ::input-types (spec/or :type-list sequential? :state keyword?))
(spec/def ::output-types (spec/or :type-list sequential? :state keyword?))
(spec/def ::code-blocks int?)
(spec/def ::docstring string?)
(spec/def ::instruction
  (spec/keys :req-un [::name ::function ::input-types ::output-types]
             :opt-un [::code-blocks ::docstring]))


(defn make-instruction
  "Creates an instruction map that :plushi.instruction/instruction spec.
  - Name should be a unique string with respect to the other instructions in the instruction set.
  - Function can be any clojure function.
  - input-types is either the keyword :STATE or a vector of stack types.
  - output-types is either the keyword :STATE or a vector of stack types.
  - code-blocks is the number of code blocks that follow the instruction.
  - docstring (optional) is a string explaining what the instruction does."
  ([name function input-types output-types code-blocks]
    (make-instruction name function input-types output-types code-blocks "No docstring."))
  ([name function input-types output-types code-blocks docstring]
    {:name name
     :function function
     :input-types input-types
     :output-types output-types
     :code-blocks code-blocks
     :docstring docstring}))


(defn register
  "Given an instruction, or the necessary components to make an instruction,
  add a key-value pair to the instruction-set atom."
  ([instruction]
    (swap! instruction-set assoc (keyword (:name instruction)) instruction))
  ([name function input-types output-types code-blocks]
    (register (make-instruction name function input-types output-types code-blocks)))
  ([name function input-types output-types code-blocks docstring]
    (register (make-instruction name function input-types output-types code-blocks docstring))))


(defn unregister
  "Given an instruction name, remove the instruction from the instruction set."
  [instruction-name]
  (swap! instruction-set dissoc (keyword instruction-name)))


(defn get-supported-instructions
  "Returns all (or a subset) of the registered instructions in the
  instruction-set atom.

  By supplying a collection of types, the resulting set of instructions will be
  limited to instructions that deal with one or more of the specified types.

  By supplyhing a regex name-pattern, the resulting set of instructions will be
  filtered down to instructions whose name fits the pattern."
  ([]
    (get-supported-instructions :all))
  ([types]
    (get-supported-instructions types #".*"))
  ([types name-pattern]
    (filter #(and (or (= types :all)
                      (some (u/ensure-set (:input-types %)) types)
                      (some (u/ensure-set (:output-types %)) types))
                  (re-matches name-pattern (:name %)))
            (vals @instruction-set))))


(defn get-supported-types
  "Returns all of the stack types (input and output) mentioned in the
  instructions stored in the instruction-set atom."
  []
  (filter #(not= % :STATE)
          (set (flatten (map #(flatten (list (:input-types (second %))
                                              (:output-types (second %))))
                             @instruction-set)))))


(defn get-instruction
  "Given an instruction name (either string or keyword) return the instruction
  map of the corresponding push instruction."
  ([name]
   (get-instruction name false))
  ([name include-docstring]
   (let [i (get @instruction-set (keyword name))]
     (if include-docstring
       i
       (dissoc i :docstring)))))


(use
  '(plushi.instruction io code numeric text common))
