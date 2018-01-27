(ns pushi.atoms
  "This namespace contains functions related to recognizing and enforcing push
  types and push atoms.

  NOTE: In this context, 'atom' refers to an element of a push program, not a
  clojure atom."
  (:require [clojure.spec.alpha :as spec]
            [pushi.instruction :as i]
            [pushi.utils :as u]
            [pushi.constraints :as c]))


(spec/def ::push-type keyword?)
(spec/def ::push-vector
  (spec/keys :req-un [::push-type ::vector-data]))


(def type-lookup
  "A map where keys are push types and values are maps that contain a
  corresponding predicate (:pred key) and coerce-ing function (:coerce key)."
  {:list {:pred list? :coerce u/ensure-list}
   :integer {:pred int? :coerce #(int (c/constrain-integer %))}
   :float {:pred float? :coerce #(float (c/constrain-float %))}
   :boolean {:pred boolean? :coerce boolean}
   :string {:pred string? :coerce #(str (c/constrain-string %))}})


(defn recognize-atom-type
  "Returns a keyword of the push type of the given atom. If no type is
  recognized will throw an error.

  NOTE: In this context, 'atom' refers to an element of a push program, not a
  clojure atom."
  [atom]
  (cond
    (spec/valid? :pushi.instruction/instruction atom) :instruction
    (spec/valid? ::push-vector atom) (keyword (str (u/keyword-to-str (:push-type atom)) "_vector"))
    (list? atom) :list
    (int? atom) :integer
    (float? atom) :float
    (boolean? atom) :boolean
    (string? atom) :string
    :else (throw (Exception. "Unknown Push atom type"))))


(defn coerce-atom-type
  "Given a push atom and the keyword of a desired push-type, attempt to coerce
  the push atom into the push-type.

  NOTE: In this context, 'atom' refers to an element of a push program, not a
  clojure atom."
  [atom push-type]
  ((:coerce (push-type type-lookup)) atom))
