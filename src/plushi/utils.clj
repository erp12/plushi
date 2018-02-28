(ns plushi.utils
  "Utility functions for use within plushi.")


(defn vector-to-list
  "Converts a vector into a list preserving the order."
  [v]
  (apply list v))


(defn ensure-list
  "Ensures the x is a list. Converts other collections to a list. Otherwise
  creates a list with x in it."
  [x]
  (cond
    (list? x) x
    (vector? x) (vector-to-list x)
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


(defn keyword-to-str
  "Converts a keyword to a string without the colon."
  [keyword]
  (let [s (str keyword)]
    (subs s 1 (count s))))
