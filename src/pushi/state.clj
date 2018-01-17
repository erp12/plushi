(ns pushi.state
  "Functions to manipulate push states. Push states are composed of one stack
  per supported data type, a string representing stdout, and a vector where
  input values can be staged."
  (:require [pushi.instruction :as i]))


(defn new-state
  "Returns a new empty state based on a collections of stack types."
  [stack-types]
  (merge (into {} (map #(vector % '()) stack-types))
         {:stdout ""
          :inputs []}))


(defn pretty-print
  "Prints a push state in a human-friendly way."
  [state]
  (doseq [t (keys state)]
    (printf "%s = " t)
    (prn (t state))
    (flush)))


(defn push-item
  "Returns a copy of the state with the value pushed on the named stack."
  [state stack-type value]
  (assoc state stack-type (conj (stack-type state) value)))


(defn pop-item
  "Returns a copy of the state with the specified stack popped."
  [state stack-type]
  (assoc state stack-type (rest (stack-type state))))


(defn top-item
  "Returns the top item of the type stack in state. Returns :NO-STACK-ITEM if
  called on an empty stack."
  [state stack-type]
  (nth (stack-type state) 0 :NO-STACK-ITEM))


(defn nth-item
  "Returns the item at a certain position (aka depth) of the type stack in
  state. Returns :no-stack-item if called on an empty stack, or out of bounds."
  [state stack-type position]
  (nth (stack-type state) position :NO-STACK-ITEM))


(defn insert-item
  "Returns a copy of the state with the valued inserted into the specified stack
  at the specified position."
  [state stack-type position value]
  (let [head (take position (stack-type state))
        tail (drop position (stack-type state))]
    (assoc state stack-type (concat head (list value) tail))))


(defn assoc-item
  "Retuns a copy of the state where the value at the specified position in the
  specified stack is replaced with the given value."
  [state stack-type position value]
  (assoc-in state [stack-type position] value))


(defn flush-stack
  "Returns a copy of the state with the specified stack emptied."
  [state stack-type]
  (assoc state stack-type '()))
