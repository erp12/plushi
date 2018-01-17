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
    (i/register (str type-str "_add") + [type-kw type-kw] [type-kw])
    (i/register (str type-str "_sub") - [type-kw type-kw] [type-kw])
    (i/register (str type-str "_mult") * [type-kw type-kw] [type-kw])
    (i/register (str type-str "_div") p-div [type-kw type-kw] [type-kw])
    (i/register (str type-str "_mod") p-mod [type-kw type-kw] [type-kw])
    (i/register (str type-str "_min") min [type-kw type-kw] [type-kw])
    (i/register (str type-str "_max") max [type-kw type-kw] [type-kw])
    (i/register (str type-str "_inc") inc [type-kw type-kw] [type-kw])
    (i/register (str type-str "_dec") dec [type-kw type-kw] [type-kw])
    (i/register (str type-str "_lt") < [type-kw type-kw] [:boolean])
    (i/register (str type-str "_lte") <= [type-kw type-kw] [:boolean])
    (i/register (str type-str "_gt") > [type-kw type-kw] [:boolean])
    (i/register (str type-str "_gte") >= [type-kw type-kw] [:boolean])))


(doall (map register-basic-math [:integer :float]))


(i/register "integer_from_boolean"
            #(if % 1 0)
            [:boolean] [:integer])
(i/register "integer_from_boolean"
            #(if % 1.0 0.0)
            [:boolean] [:float])
(i/register "integer_from_float" int [:float] [:integer])
(i/register "float_from_integer" float [:integer] [:float])


(i/register "float_sin" #(Math/sin %) [:float] [:float])
(i/register "float_cos" #(Math/cos %) [:float] [:float])
(i/register "float_tan" #(Math/tan %) [:float] [:float])
