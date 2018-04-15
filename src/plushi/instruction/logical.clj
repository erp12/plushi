(ns plushi.instruction.logical
  (:require [plushi.instruction :as i]))


(i/register "boolean_and"
            #(and %1 %2)
            [:boolean :boolean] [:boolean] 0
            "Pushes the result of calling AND on the top two booleans.")


(i/register "boolean_or"
            #(or %1 %2)
            [:boolean :boolean] [:boolean] 0
            "Pushes the result of calling OR on the top two booleans.")


(i/register "boolean_not"
            not
            [:boolean] [:boolean] 0
            "Pushes the result of calling NOT on the top boolean.")


(i/register "boolean_xor"
            not=
            [:boolean :boolean] [:boolean] 0
            "Pushes the result of calling XOR on the top two booleans.")


(i/register "boolean_invert_first_then_and"
            #(and (not %1) %2)
            [:boolean :boolean] [:boolean] 0
            "Pushes the result of inverting the top boolean and then calling AND on the second boolean.")


(i/register "boolean_invert_second_then_and"
            #(and %1 (not %2))
            [:boolean :boolean] [:boolean] 0
            "Pushes the result of inverting the second boolean and then calling AND on the top boolean.")


(i/register "boolean_from_integer"
            #(not (zero? %))
            [:integer] [:boolean] 0
            "Pushes false if the top integer is zero. Pushes true for any other value of integer.")


(i/register "boolean_from_float"
            #(not (zero? %))
            [:float] [:boolean] 0
            "Pushes false if the top float is zero. Pushes true for any other value of float.")
