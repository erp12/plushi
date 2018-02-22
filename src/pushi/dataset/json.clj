(ns pushi.dataset.json
  (:require [clojure.data.json :as json]
            [pushi.dataset :as ds]))


(defn json-to-dataset
  [json-str]
  (let [json-vec (json/read-str json-str)]
    (loop [dataset (ds/initialize-dataset (vec (keys (first json-vec))))
           remaining-records json-vec]
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
                   (rest remaining-records))))))))
