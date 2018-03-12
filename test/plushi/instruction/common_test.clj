(ns plushi.instruction.common-test
  (:require [clojure.test :refer :all]
            [plushi.instruction :refer [instruction-set]]
            [plushi.state :as s]))


(def ut-state
  (s/new-state [:integer :string]))


(def eval-atom #'plushi.interpreter/evaluate-atom)


(deftest insufficient_args
  (testing "Insufficient args"
    (is (= (eval-atom ut-state
                      (:integer_add @instruction-set))
           {:integer '() :string '() :stdout "" :inputs []}))))


(deftest integer_pop_stndrd
  (testing "integer pop standard"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :integer 1)
                          (s/push-item :integer 3))
                      (:integer_pop @instruction-set))
           {:integer '(1) :string '() :stdout "" :inputs []}))))


(deftest string_dup_stndrd
  (testing "string dup standard"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :string "abc"))
                      (:string_dup @instruction-set))
           {:integer '() :string '("abc" "abc") :stdout "" :inputs []}))))


(deftest integer_dup_times_test
  (testing "integer dup_times test"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :integer 3)
                          (s/push-item :integer 7))
                      (:integer_dup_times @instruction-set))
           {:integer '() :string '() :stdout "" :inputs [] :exec (list '(7 7 7))}))))


(deftest string_dup_times_test
  (testing "string dup_times test"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :string "a")
                          (s/push-item :integer 2))
                      (:string_dup_times @instruction-set))
           {:integer '() :string '() :stdout "" :inputs [] :exec (list '("a" "a"))}))))


(deftest dup_times_zero_test
  (testing "string dup_times with n = 0"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :string "a")
                          (s/push-item :integer 0))
                      (:string_dup_times @instruction-set))
           {:integer '() :string '() :stdout "" :inputs [] :exec (list '())}))))
