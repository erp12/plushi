(ns plushi.instruction.common
  (:require [plushi.instruction :as i]
            [plushi.state :as state]
            [plushi.utils :as u]))


(defn register-common
  [type-kw]
  (let [type-str (name type-kw)]

    (i/register (str type-str "_pop")
                (fn [x] nil)
                [type-kw] [] 0)

    (i/register (str type-str "_dup")
                (fn [x] [x x])
                [type-kw] [type-kw type-kw] 0)

    ;; This instruction seems to do bad things for runtime.
    ; (i/register (str type-str "_dup_times")
    ;             (fn [x n] [(u/ensure-list (repeat n x))])
    ;             [type-kw :integer] [:exec] 0)
    ))

(doall (map register-common [:integer :float :string :boolean :code]))
