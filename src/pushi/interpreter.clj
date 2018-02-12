(ns pushi.interpreter
  "Contains function to asist in running a push program."
  (:require [pushi.instruction :as instr]
            [pushi.atoms :as a]
            [pushi.state :as state]
            [pushi.utils :as u]
            [pushi.constraints :as c]))


(def stack-types
  "A set of push types supported by the interpreter. This set is computed based
  on types manipulated by the set of registered instructions."
  (set (concat (instr/get-supported-types)
               [:exec :tag])))


(defn load-program
  "Sets the exec stack of the push state to be the program."
  [state program]
  (assoc state :exec (u/ensure-list program)))


(defn load-inputs
  "Sets the vector of inputs in the push state to be the given inputs."
  [state inputs]
  (assoc state :inputs (u/ensure-vector inputs)))


(defn inspect-outputs
  "Given a state and a list of stack types, returns a vector of values copied
  from the corresponding stacks of the state. If multiple instances of the same
  type are in output-types, then it will copy deeper from that stack for each
  occurance."
  [state output-types]
  (loop [remaining-output-types output-types
         outputs []
         type-counts {}]
    (if (empty? remaining-output-types)
      outputs
      (let [type-to-output (first remaining-output-types)
            ndx (get type-counts type-to-output 0)]
        (recur (rest remaining-output-types)
               (conj outputs (state/nth-item state type-to-output ndx))
               (if (contains? type-counts type-to-output)
                 (assoc type-counts type-to-output (inc (type-to-output type-counts)))
                 (assoc type-counts type-to-output 1)))))))


(defn pop-arguments
  "Given a push state and a vector of stack types, return the following:
    1. The push state the top items for each arg-type popped off.
    2. A vector of the values popped from each stack to be used as instruction arguments."
  [state arg-types]
  (loop [remaining-arg-types arg-types
         s state
         args []]
    (if (empty? remaining-arg-types)
      [s args]
      (let [arg (state/top-item s (first remaining-arg-types))]
        (if (= arg :NO-STACK-ITEM)
          [state :REVERT]
          (recur (rest remaining-arg-types)
                 (state/pop-item s (first remaining-arg-types))
                 (conj args arg)))))))


(defn push-returns
  "Given a push state, a collection of values returned by an instruction, and a
  vector of stack types corresponding to each returned-value, check that all
  returned values can be coerced into their expected stack type and push them
  onto the stack. Return the resulting stack."
  [state returned-values expected-types]
  (let [items-to-push (map #(if (contains? a/type-lookup %2) (a/coerce-atom-type %1 %2) %1)
                           returned-values
                           expected-types)]
    (loop [remaining-items items-to-push
           remaining-types expected-types
           s state]
      (if (empty? remaining-items)
        s
        (recur (rest remaining-items)
               (rest remaining-types)
               (state/push-item s (first remaining-types) (first remaining-items)))))))


(defn- evaluate-atom
  [state atom]
  (let [atom-type (a/recognize-atom-type atom)]
    (cond
      (= atom-type :list)
        (assoc state :exec (concat atom (get state :exec)))

      (= atom-type :instruction)
      (cond
        ;; If instruction takes entire state and returns entire state.
        (and (= :STATE (:input-types atom))
             (= :STATE (:output-types atom)))
        ((:function atom) state)

        ;; If instruction takes entire state and returns values to push.
        (= :STATE (:input-types atom))
        (let [returned-values (u/ensure-vector ((:function atom) state))]
          (if (= :REVERT returned-values)
            state
            (push-returns state returned-values (:output-types atom))))

        ;; If instruction takes arguments from stacks and returns whole
        ;; new state.
        (= :STATE (:output-types atom))
          (let [[state-no-args args] (pop-arguments state (:input-types atom))]
            (if (= :REVERT args)
              state
              (apply (:function atom) args)))

        ;; If instruction takes arguments from stacks and returns values
        ;; to push.
        :else
        (let [[state-no-args args] (pop-arguments state (:input-types atom))
              returned-values (if (= :REVERT args)
                                [:REVERT]
                                (u/ensure-vector (apply (:function atom) args)))]
          (if (= :REVERT (first returned-values))
            state
            (push-returns state-no-args
                          returned-values
                          (:output-types atom)))))

      ;; Evaluating a literal
      :else
      (assoc state atom-type (conj (get state atom-type) atom)))))


(defn run-push
  "Runs a push program. Requires a vector of input values availalble to the
  program and stack types to use when looking for program outputs.

  The general flow of this function is:
    1. Create a new push state
    2. Load the program and inputs.
    3. If the exec stack is empty, return the outputs.
    4. Else, pop the exec stack and process the atom.
    5. Return to step 3."
  ([program inputs output-types]
    (run-push program inputs output-types false))
  ([program inputs output-types verbose]
    (loop [state (-> (state/new-state stack-types)
                     (load-program program)
                     (load-inputs inputs))
           steps 0]
      (if (or (empty? (:exec state))
              (> steps (:atom-limit @c/lang-constraints)))
        (do
          (if verbose
            (do
              (println "\nFinal state:")
              (state/pretty-print state)))
          (inspect-outputs state output-types))
        (let [atom (state/top-item state :exec)
              state-no-atom (state/pop-item state :exec)]
          (if verbose
            (do
              (println "\nCurrent atom:" atom)
              (println "Current state:")
              (state/pretty-print state-no-atom)))
          (recur (evaluate-atom state-no-atom atom)
                 (inc steps)))))))
