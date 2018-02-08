(ns pushi.instruction.numeric
  (:require [pushi.instruction :as i]))


(defn- p-div
  [a b]
  (if (zero? b)
    :REVERT
    (/ a b)))


(defn- p-mod
  [a b]
  (if (zero? b)
    :REVERT
    (mod a b)))


(defn- register-basic-math
  [type-kw]
  (let [type-str (name type-kw)]
    (i/register (str type-str "_add") + [type-kw type-kw] [type-kw]
                (str "Adds the top two " type-str "s and pushes the result."))
    (i/register (str type-str "_sub") - [type-kw type-kw] [type-kw]
                (str "Subtracts the top two " type-str "s and pushes the result."))
    (i/register (str type-str "_mult") * [type-kw type-kw] [type-kw]
                (str "Multiplies the top two " type-str "s and pushes the result."))
    (i/register (str type-str "_div") p-div [type-kw type-kw] [type-kw]
                (str "Divides the top two " type-str "s and pushes the result."))
    (i/register (str type-str "_mod") p-mod [type-kw type-kw] [type-kw]
                (str "Computes the modulous of the top two " type-str "s and pushes the result."))
    (i/register (str type-str "_min") min [type-kw type-kw] [type-kw]
                (str "Pushes the minimum of two " type-str))
    (i/register (str type-str "_max") max [type-kw type-kw] [type-kw]
                (str "Pushes the maximum of two " type-str))
    (i/register (str type-str "_inc") inc [type-kw type-kw] [type-kw]
                (str "Increments the top " type-str " by 1."))
    (i/register (str type-str "_dec") dec [type-kw type-kw] [type-kw]
                (str "Decrements the top " type-str " by 1."))
    (i/register (str type-str "_lt") < [type-kw type-kw] [:boolean]
                (str "Pushes true if the top " type-str " is less than the second. Pushes false otherwise."))
    (i/register (str type-str "_lte") <= [type-kw type-kw] [:boolean]
                (str "Pushes true if the top " type-str " is less than, or equal to, the second. Pushes false otherwise."))
    (i/register (str type-str "_gt") > [type-kw type-kw] [:boolean]
                (str "Pushes true if the top " type-str " is greater than the second. Pushes false otherwise."))
    (i/register (str type-str "_gte") >= [type-kw type-kw] [:boolean]
                (str "Pushes true if the top " type-str " is greater than, or equal to, the second. Pushes false otherwise."))))


(doall (map register-basic-math [:integer :float]))


(i/register "integer_from_boolean"
            #(if % 1 0)
            [:boolean] [:integer]
            "Pushes 1 in the top boolean is true. Pushes 0 if the top boolean is false.")
(i/register "float_from_boolean"
            #(if % 1.0 0.0)
            [:boolean] [:float]
            "Pushes 1.0 in the top boolean is true. Pushes 0.0 if the top boolean is false.")
(i/register "integer_from_float" int [:float] [:integer]
            "Casts the top float to an integer and pushes the result.")
(i/register "float_from_integer" float [:integer] [:float]
            "Casts the top integer to a float and pushes the result.")


(i/register "float_sin" #(Math/sin %) [:float] [:float]
            "Pushes the sin of the top float.")
(i/register "float_cos" #(Math/cos %) [:float] [:float]
            "Pushes the cos of the top float.")
(i/register "float_tan" #(Math/tan %) [:float] [:float]
            "Pushes the tan of the top float.")
