(ns pushi.atoms-test
  (:require [clojure.test :refer :all]
            [pushi.atoms :refer :all]))


(deftest recognize-type-instruction
  (testing "Recognize a push instruction"
    (is (= :instruction
           (recognize-atom-type {:name "no-op"
                                 :function #(%)
                                 :input-types :STATE
                                 :output-types :STATE
                                 :docstring "Does nothing"})))))


(deftest recognize-type-float
  (testing "Recognize a push float. Standard function use."
    (is (= :float
           (recognize-atom-type -5.6)))))


(deftest recognize-type-invalid
  (testing "Recognize object atom is not a valid push atom."
    (is (thrown? Exception
                 (recognize-atom-type {:name "invalid-instruction"})))))


(deftest coerce-atom-list
  (testing "Coerce a collection atom to a list."
    (is (= '(1 2)
           (coerce-atom-type [1 2] :list)))))


(deftest coerce-atom-string
  (testing "Coerce and constrain a string atom."
    (let [too-big-str (apply str (repeat 6000 "!"))]
      (is (= (apply str (take 5000 too-big-str))
             (coerce-atom-type too-big-str :string))))))
