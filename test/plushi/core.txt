(ns plushi.core-test
  (:require [clojure.test :refer :all]
            [plushi.core :refer :all]
            [plushi.encode :as e]
            [plushi.translate :as t]
            [plushi.utils :as u]))


(deftest line-prog-run-on-dataset-test
  (testing "Running y=mx+b program"
    (is (= (let [dataset (e/parse-input-dataset (slurp "resources/data/univariate-float-data.json")
                                                "json")
                 plush-program (e/parse-program (slurp "resources/programs/line.json")
                                                "json")]
             ;; Round the floats because of precision  errors
             (map #(vector (u/round-float (first %) 3))
                  (run-on-dataset plush-program dataset false)))
           [[-1.1] [-0.98] [-0.86] [-0.74] [-0.62] [-0.5] [-0.38] [-0.26] [-0.14] [-0.02] [0.1]]))))


(deftest relu-prog-run-on-dataset-test
  (testing "Running ReLU program"
    (is (= (let [dataset (e/parse-input-dataset (slurp "resources/data/univariate-float-data.json")
                                                "json")
                 plush-program (e/parse-program (slurp "resources/programs/relu.json")
                                                "json")]
             (run-on-dataset plush-program dataset false))
           [[0.0] [0.0] [0.0] [0.0] [0.0] [0.0] [0.1] [0.2] [0.3] [0.4] [0.5]]))))


;; TODO: n-fibbonacci (from discourse)
;; TODO: RSWN
