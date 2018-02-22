(ns pushi.dataset
  "In pushi, a dataset is the a data structure that contains a vector of `cases`
  and a vector of `labels`. Each `case` is a vector of values of any type to
  be used as inputs to a push program."
  (:use [clojure.data]))


(defn initialize-dataset
  [features]
  (assoc {:data []} :features features))


(def wrong-feature-count-exception
  (Exception. "Incorrect number of features found in case."))


(defn add-case
  "WARNING: Should be changed to no longer rely on order of fields."
  [dataset case-to-add]
  (if (= (count (:features dataset))
         (count case-to-add))
    (assoc dataset :data (conj (:data dataset) (vec (vals case-to-add))))
    (throw wrong-feature-count-exception)))


(defn add-cases
  [dataset cases-to-add]
  (if (= (repeat (count cases-to-add) (count (:features dataset)))
         (map count cases-to-add))
    (assoc dataset
           :data
           (concat (:data dataset)
                   (vec (map #(vec (vals %))
                             cases-to-add))))
    (throw wrong-feature-count-exception)))


(defn add-feature
  [dataset feature]
  (-> dataset
      (assoc :features (conj (:features dataset) feature))
      (assoc :data (vec (map #(conj % nil) (:data dataset))))))


(defn missing-features
  [dataset attributes]
  (let [missing-feats (first (diff (set attributes)
                                   (set (:features dataset))))]
    (if (nil? missing-feats)
      (list)
      (list missing-feats))))
