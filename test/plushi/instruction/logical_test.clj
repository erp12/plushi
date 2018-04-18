(ns plushi.instruction.logical-test
  (:require [clojure.test :refer :all]
            [plushi.instruction :refer [instruction-set]]
            [plushi.state :as s]))


(def ut-state
  (s/new-state [:boolean :integer]))


(def eval-atom #'plushi.interpreter/evaluate-atom)


(deftest boolean_and_stndrd
  (testing "boolean and standard"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :boolean true)
                          (s/push-item :boolean false))
                      (:boolean_and @instruction-set))
           {:boolean '(false) :integer '() :stdout "" :inputs []}))))


(deftest boolean_or_stndrd
  (testing "boolean or standard"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :boolean true)
                          (s/push-item :boolean false))
                      (:boolean_or @instruction-set))
           {:boolean '(true) :integer '() :stdout "" :inputs []}))))


(deftest boolean_not_stndrd
  (testing "boolean not standard"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :boolean true))
                      (:boolean_not @instruction-set))
           {:boolean '(false) :integer '() :stdout "" :inputs []}))))


(deftest boolean_xor_stndrd
  (testing "boolean xor standard"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :boolean true)
                          (s/push-item :boolean true))
                      (:boolean_xor @instruction-set))
           {:boolean '(false) :integer '() :stdout "" :inputs []}))))


(deftest boolean_invert_first_then_and_stndrd
  (testing "boolean invert first then and standard"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :boolean true)
                          (s/push-item :boolean false))
                      (:boolean_invert_first_then_and @instruction-set))
           {:boolean '(true) :integer '() :stdout "" :inputs []}))))


(deftest boolean_invert_second_then_and_stndrd
  (testing "boolean invert second then and standard"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :boolean true)
                          (s/push-item :boolean false))
                      (:boolean_invert_second_then_and @instruction-set))
           {:boolean '(false) :integer '() :stdout "" :inputs []}))))


(deftest boolean_from_integer_stndrd
  (testing "boolean from integer standard"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :integer 0))
                      (:boolean_from_integer @instruction-set))
           {:boolean '(false) :integer '() :stdout "" :inputs []}))))


(deftest boolean_from_float_stndrd
  (testing "boolean from float standard"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :float 0.1))
                      (:boolean_from_float @instruction-set))
           {:boolean '(true) :integer '() :float '() :stdout "" :inputs []}))))
