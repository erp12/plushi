(ns pushi.utils-test
  (:require [clojure.test :refer :all]
            [pushi.utils :refer :all]))

; (deftest a-test
;   (testing "FIXME, I fail."
;     (is (= 0 1))))


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
