(ns pushi.instruction-test
  (:require [clojure.test :refer :all]
            [pushi.instruction :refer :all]))


(deftest register-noop-instruction
  (testing "Register a noop instruction"
    (do
      (register "noop-TEST" (fn [s] s) :STATE :STATE)
      (is (contains? @instruction-set :noop-TEST))
      (is (= ((:function (:noop-TEST @instruction-set)) {:a 1})
             {:a 1})))))


(deftest get-supported-instructions-all
  (testing "Get vector of all instructions"
    (is (= (count (get-supported-instructions))
           (count @instruction-set)))))


(deftest get-supported-instructions-type
  (testing "Get vector of all integer instructions"
    (is (= (count (get-supported-instructions [:integer]))
           16))))


(deftest get-supported-instructions-name-pattern
  (testing "Get vector of all instructions with `add` in name"
    (is (= (count (get-supported-instructions :all #".*_add.*"))
            2))))


(deftest get-supported-push-types
  (testing "Get vector of all push type names"
    (is (= (set (get-supported-types))
           #{:integer :boolean :float}))))


(deftest get-specified-instruction-stndrd
  (testing "Get a particular instruction map"
    (let [instr (get-instruction "integer_add")]
      (is (= (:name instr) "integer_add"))
      (is (= (:input-types instr) [:integer :integer]))
      (is (= (:output-types instr) [:integer])))))
