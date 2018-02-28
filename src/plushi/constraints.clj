(ns plushi.constraints
  "This namespace controls the constraints applied to the push interpreters.
  These include, valid ranges for data types, program rumtime timeouts, and
  others.")

(def lang-constraints
  "Clojure atom containing a map of constraints."
  (atom
    {;; DATA TYPE CONSTRAINTS
     :max-number-magnitude 1000000000000
     :min-number-magnitude 1.0E-10
     :max-string-length 5000
     :max-vector-length 5000

     ;; PROGRAM EXECUTION CONSTRAINTS

     :growth-cap 100
     ; If any stack grows by more than this many elements after an atom is
     ; evaluated, the instruction will not be processed.

     :atom-limit 500
     ; Program execution is terminated after this many atoms are evaluated.

     :runtime-limit 2.0E10
     ; Program execution is terminated after this many nanoseconds.
     }))


(defn constrain-integer
  "Returns n constrained to the range of supported integers."
  [n]
  (cond
    (> n (:max-number-magnitude @lang-constraints))
    (:max-number-magnitude @lang-constraints)

    (< n (- (:max-number-magnitude @lang-constraints)))
    (- (:max-number-magnitude @lang-constraints))

    :else n))

(defn constrain-float
  "Returns n constrained to the range of supported floats. If the magnitude of
  n is sufficiently small, 0 is returned."
  [n]
  (cond
    (> n (:max-number-magnitude @lang-constraints))
    (* 1.0 (:max-number-magnitude @lang-constraints))

    (< n (- (:max-number-magnitude @lang-constraints)))
    (* 1.0 (- (:max-number-magnitude @lang-constraints)))

    (and (< n (:min-number-magnitude @lang-constraints))
         (> n (- (:min-number-magnitude @lang-constraints))))
    0.0

    :else n))

(defn constrain-string
  "Returns s where the length is capped based on the push language constraints."
  [s]
  (apply str (take (:max-string-length @lang-constraints) s)))


(defn constrain-push-vector
  "Returns the given vector where the length is capped based on the push
  language constraints."
  [push-vector]
  {:push-type (:push-type push-vector)
   :vector-data (vec (take (:max-vector-length @lang-constraints)
                           (:vector-data push-vector)))})
