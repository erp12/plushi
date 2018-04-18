(ns plushi.dataset.json
  (:require [clojure.data.json :as json]
            [plushi.dataset :as ds]))


(defn- json-vectors-to-ds
  [json-data]
  (let [feature-names (map #(str "x" %) (range (count (first json-data))))]
    (loop [dataset (ds/initialize-dataset feature-names)
           remaining-records json-data]
      (if (empty? remaining-records)
        dataset
        (let [next-case (zipmap feature-names
                                (first remaining-records))]
          (recur (ds/add-case dataset next-case)
                 (rest remaining-records)))))))


(defn- json-maps-to-ds
  [json-data]
  (loop [dataset (ds/initialize-dataset (vec (keys (first json-data))))
        remaining-records json-data]
    (if (empty? remaining-records)
      dataset
      (let [next-case (first remaining-records)
            missing-features (ds/missing-features dataset (keys next-case))]
        (if (empty? missing-features)
          (recur (ds/add-case dataset next-case)
                 (rest remaining-records))
          (recur (-> (reduce #(ds/add-feature %1 %2)
                             dataset
                             missing-features)
                     (ds/add-case next-case))
                 (rest remaining-records)))))))


(defn json-data-to-dataset
  [json-data]
  (if (vector? (first json-data))
    (json-vectors-to-ds json-data)
    (json-maps-to-ds json-data)))
