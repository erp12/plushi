(ns plushi.atoms
  "This namespace contains functions related to recognizing and enforcing push
  types and push atoms.

  NOTE: In this context, 'atom' refers to an element of a push program, not a
  clojure atom."
  (:require [clojure.spec.alpha :as spec]
            [plushi.instruction :as i]
            [plushi.utils :as u]
            [plushi.constraints :as c]))


(spec/def ::push-type keyword?)
(spec/def ::vector-data vector?)
(spec/def ::push-vector
  (spec/keys :req-un [::push-type ::vector-data]))


(def type-lookup
  "A map where keys are push types and values are maps that contain a
  corresponding predicate (:pred key) and coerce-ing function (:coerce key)."
  {:list {:pred list? :coerce u/ensure-list}
   :char {:pred char? :coerce u/coerce-to-char}
   :integer {:pred int? :coerce #(c/constrain-integer (unchecked-int %))}
   :float {:pred float? :coerce #(c/constrain-float (unchecked-float %))}
   :boolean {:pred boolean? :coerce boolean}
   :string {:pred string? :coerce #(c/constrain-string (str %))}})


(defn recognize-atom-type
  "Returns a keyword of the push type of the given atom. If no type is
  recognized will throw an error.

  NOTE: In this context, 'atom' refers to an element of a push program, not a
  clojure atom."
  [atom]
  (cond
    (nil? atom) (throw (Exception. "nil is not valid plush atom type."))
    (spec/valid? :plushi.instruction/instruction atom) :instruction
    (spec/valid? ::push-vector atom) (keyword (str (u/keyword-to-str (:push-type atom)) "_vector"))
    (list? atom) :list
    (char? atom) :char
    (int? atom) :integer
    (float? atom) :float
    (boolean? atom) :boolean
    (string? atom) :string
    :else (throw (Exception. (str "Unknown Push atom type: " (str (type atom))
                                  ". From atom: " atom)))))


(defn coerce-atom-type
  "Given a push atom and the keyword of a desired push-type, attempt to coerce
  the push atom into the push-type.

  NOTE: In this context, 'atom' refers to an element of a push program, not a
  clojure atom."
  [atom push-type]
  ((:coerce (push-type type-lookup)) atom))
