(ns plushi.utils
  "Utility functions for use within plushi.")


(defn vector-to-list
  "Converts a vector into a list preserving the order."
  [v]
  (apply list v))


(defn ensure-list
  "Ensures the x is a list. Converts other collections to a list. Otherwise
  creates a list with x in it. Realizes x if x is a lazy sequence."
  [x]
  (cond
    (list? x) x
    (vector? x) (vector-to-list x)
    (instance? clojure.lang.LazySeq x) (apply list x)
    (coll? x) (into '() x)
    :else (list x)))


(defn ensure-set
  "Ensures the x is a set. Converts other collections to a set. Otherwise
  creates a set with x in it."
  [x]
  (cond
    (set? x) x
    (sequential? x) (set x)
    :else #{x}))


(defn ensure-vector
  "Ensures the x is a vector. Converts other collections to a vector. Otherwise
  creates a vector with x in it."
  [x]
  (cond
    (vector? x) x
    (sequential? x) (vec x)
    (coll? x) (into [] x)
    :else (vector x)))


(defn coerce-to-char
  "Attempts to coerce x to a character. If not possible, throws error."
  [x]
  (cond
    (or (char? x)
        (int? x))
    (char x)


    (and (string? x)
         (= (count x) 1))
    (first (char-array x))

    :else
    (throw (Exception. (str "Cannot coerce " (str x) " to char")))))


(defn keyword-to-str
  "Converts a keyword to a string without the colon."
  [keyword]
  (let [s (str keyword)]
    (subs s 1 (count s))))


(defn round-float
  "Rounds the float value to given number of decimal places"
  [value decimal-places]
  (let [scale (Math/pow 10 decimal-places)]
    (/ (Math/round (* value scale)) scale)))
