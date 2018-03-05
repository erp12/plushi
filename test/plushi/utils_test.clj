(ns plushi.utils-test
  (:require [clojure.test :refer :all]
            [plushi.utils :refer :all]))


(deftest vector-to-list-stndrd
  (testing "Convert a vector to a list"
    (is (= (vector-to-list [1 2 3])
           '(1 2 3)))))


(deftest vector-to-list-empty
  (testing "Convert an empty vector to an empty list"
    (is (= (vector-to-list [])
           '()))))


(deftest ensure-list-single
  (testing "Ensure list, single element"
    (is (= (ensure-list 5)
           '(5)))))


(deftest ensure-list-coll
  (testing "Ensure list, some collection"
    (is (= (ensure-list #{1 2 3})
           '(2 3 1)))))


(deftest ensure-vector-single
  (testing "Ensure vector, single element"
    (is (= (ensure-vector 5)
           [5]))))


(deftest ensure-vector-from-list
  (testing "Ensure vector, list"
    (is (= (ensure-vector '(1 2 3))
           [1 2 3]))))


(deftest ensure-vector-from-set
  (testing "Ensure vector, set"
    (is (= (ensure-vector #{1 2 3})
           [1 3 2]))))


(deftest keyword-to-str-stndrd
  (testing "Standard call to keyword-to-str"
    (is (= (keyword-to-str :foo)
           "foo"))))


(deftest round-float-stndrd
  (testing "Standard call to round-float"
    (is (= (round-float -0.02000001 3)
           -0.02))))
