(ns plushi.instruction.code
  (:require [plushi.instruction :as i]
            [plushi.state :as state]))


(i/register "close"
            (fn [s] s)
            :STATE :STATE
            "Denotes a close marker in the program used by control structures.")


(i/register "noop"
            (fn [s] s)
            :STATE :STATE
            "A noop instruction which does nothing.")


(i/register "exec_if"
            (fn [b then else]
              (if b then else))
            [:boolean :exec :exec] [:exec]
            "If the top boolean is true, implement execute the top element of the
            exec stack and skip the second. Otherwise, skip the top element of the
            exec stack and execute the second.")
