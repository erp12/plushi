(ns pushi.instruction.numeric-test
  (:require [clojure.test :refer :all]
            [pushi.instruction :refer [instruction-set]]
            ;[pushi.interpreter :refer [evaluate-atom]]
            [pushi.state :as s]))

; (deftest a-test
;   (testing "FIXME, I fail."
;     (is (= 0 1))))

(def ut-state
  (s/new-state [:integer :float]))


(def eval-atom #'pushi.interpreter/evaluate-atom)


(deftest insufficient_args
  (testing "Insufficient args"
    (is (= (eval-atom ut-state
                      (:integer_add @instruction-set))
           {:integer '() :float '() :stdout "" :inputs []}))))


(deftest integer_add_stndrd
  (testing "integer add standard"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :integer 1)
                          (s/push-item :integer 3))
                      (:integer_add @instruction-set))
           {:integer '(4) :float '() :stdout "" :inputs []}))))


(deftest float_add_stndrd
  (testing "float sub standard"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :float 1)
                          (s/push-item :float 3))
                      (:float_sub @instruction-set))
           {:integer '() :float '(2.0) :stdout "" :inputs []}))))


(deftest integer_mult_stndrd
  (testing "integer mult standard"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :integer -2)
                          (s/push-item :integer 3))
                      (:integer_mult @instruction-set))
           {:integer '(-6) :float '() :stdout "" :inputs []}))))


(deftest float_div_stndrd
  (testing "float div standard"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :float 2.0)
                          (s/push-item :float 10.0))
                      (:float_div @instruction-set))
           {:integer '() :float '(5.0) :stdout "" :inputs []}))))


(deftest float_div_zero
  (testing "float div zero"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :float 0.0)
                          (s/push-item :float 3.0))
                      (:float_div @instruction-set))
           {:integer '() :float '(3.0 0.0) :stdout "" :inputs []}))))


(deftest int_div_stndrd
  (testing "int div standard"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :integer 2)
                          (s/push-item :integer 13))
                      (:integer_div @instruction-set))
           {:integer '(6) :float '() :stdout "" :inputs []}))))


(deftest float_mod_stndrd
  (testing "float mod standard"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :float 2)
                          (s/push-item :float 13))
                      (:float_mod @instruction-set))
           {:integer '() :float '(1.0) :stdout "" :inputs []}))))


(deftest int_mod_zero
  (testing "int mod zero"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :integer 0)
                          (s/push-item :integer 13))
                      (:integer_mod @instruction-set))
           {:integer '(13 0) :float '() :stdout "" :inputs []}))))


(deftest float_min_stndrd
  (testing "float min standard"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :float 5.0)
                          (s/push-item :float 4.0))
                      (:float_min @instruction-set))
           {:integer '() :float '(4.0) :stdout "" :inputs []}))))


(deftest integer_max_stndrd
  (testing "integer max standard"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :integer 4)
                          (s/push-item :integer 5))
                      (:integer_max @instruction-set))
           {:integer '(5) :float '() :stdout "" :inputs []}))))


(deftest float_inc_stndrd
  (testing "float inc standard"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :float 4.5))
                      (:float_inc @instruction-set))
           {:integer '() :float '(5.5) :stdout "" :inputs []}))))



(deftest integer_dec_stndrd
  (testing "integer dec standard"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :integer -3))
                      (:integer_dec @instruction-set))
           {:integer '(-4) :float '() :stdout "" :inputs []}))))
