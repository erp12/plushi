(ns plushi.core-test
  (:require [clojure.test :refer :all]
            [plushi.core :refer :all]
            [clojure.data.json :as json]
            [clojure.walk :as walk]
            [plushi.utils :as u]))


(deftest line-prog-run-on-dataset-test
  (testing "Running y=mx+b program"
    (is (= (let [requeset-body (-> (json/read-str (slurp "resources/programs/line.json"))
                                   (walk/keywordize-keys)
                                   (merge {:dataset (json/read-str (slurp "resources/data/univariate_float_data.json"))}))]
             (map #(vector (u/round-float (first %) 3))
                  (run-program-response-body requeset-body)))
           [[-1.1] [-0.98] [-0.86] [-0.74] [-0.62] [-0.5] [-0.38] [-0.26] [-0.14] [-0.02] [0.1]]))))


(deftest relu-prog-run-on-dataset-test
  (testing "Running relu program"
    (is (= (let [requeset-body (-> (json/read-str (slurp "resources/programs/relu.json"))
                                   (walk/keywordize-keys)
                                   (merge {:dataset (json/read-str (slurp "resources/data/univariate_float_data.json"))}))]
             (run-program-response-body requeset-body))
           [[0.0] [0.0] [0.0] [0.0] [0.0] [0.0] [0.1] [0.2] [0.3] [0.4] [0.5]]))))
