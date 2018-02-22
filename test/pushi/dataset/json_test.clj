(ns pushi.dataset.json-test
  (:require [clojure.test :refer :all]
            [pushi.dataset.json :refer :all]))


(deftest json-to-dataset-standard
  (testing "Simple JSON string to dataset"
    (is (= (json-to-dataset (slurp "resources/data.json"))
           {:features ["in1", "in2"]
            :data [[0 0] [0 1] [1 0] [1 1] [1 2] [2 1] [2 2]]}))))
