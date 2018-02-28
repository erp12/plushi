(ns plushi.constraints-test
  (:require [clojure.test :refer :all]
            [plushi.constraints :refer :all]))


(deftest constrain-integer-stndrd
  (testing "Standard call to constrain integer"
    (is (= (constrain-integer 10)
           10))))


(deftest constrain-integer-pos
  (testing "Constrain big positive integer"
    (is (= (constrain-integer 1000000000005)
           1000000000000))))


(deftest constrain-integer-neg
  (testing "Constrain big negative integer"
    (is (= (constrain-integer -1000000000005)
           -1000000000000))))


(deftest constrain-float-stndrd
  (testing "Standard call to constrain float"
    (is (= (constrain-float 12.3)
           12.3))))


(deftest constrain-float-pos
  (testing "Constrain big positive float"
    (is (= (constrain-float 1000000000000.1)
           1.0E12))))


(deftest constrain-float-neg
  (testing "Constrain big negative float"
    (is (= (constrain-float -1000000000000.1)
           -1.0E12))))


(deftest constrain-float-precision
  (testing "Constrain float precision"
    (is (= (constrain-float 5.0E-11)
           0.0))))


(deftest constrain-string-test
  (testing "Constrain string"
    (let [too-big-str (apply str (repeat 6000 "!"))]
      (is (= (apply str (take 5000 too-big-str))
             (constrain-string too-big-str))))))
