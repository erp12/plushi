(ns plushi.translate-test
  (:require [clojure.test :refer :all]
            [plushi.translate :refer :all]
            [plushi.instruction :as instr]))


(def is-close-atom #'plushi.translate/is-close-atom)


(def ut-plush-encoding
  (list true 2 3 "plushi:exec_if" "plushi:integer_add" "plushi:integer_inc"
        "plushi:close" "plushi:integer_mult" "plushi:integer_dec" "plushi:close"))


(def ut-plush-program
  (list true 2 3
        (instr/get-instruction :exec_if)
        (instr/get-instruction :integer_add)
        (instr/get-instruction :integer_inc)
        (instr/get-instruction :close)
        (instr/get-instruction :integer_mult)
        (instr/get-instruction :integer_dec)
        (instr/get-instruction :close)))


(deftest test-is-close-atom-t
  (testing "Test close atom true"
    (is (is-close-atom (instr/get-instruction :close)))))


(deftest test-is-close-atom-f1
  (testing "Test close atom false 1"
    (is (not (is-close-atom (instr/get-instruction :noop))))))


(deftest test-is-close-atom-f2
  (testing "Test close atom false 2"
    (is (not (is-close-atom 2)))))


(deftest load-instructions-standard
  (testing "Loading instructions into plush program (standard)"
    (is (= (load-instructions ut-plush-encoding)
           ut-plush-program))))


(deftest plush-to-push-standard
  (testing "Plush to push (standard)"
    (is (= (plush-to-push ut-plush-encoding)
           (list true 2 3
                 (instr/get-instruction :exec_if)
                 (list (instr/get-instruction :integer_add)
                       (instr/get-instruction :integer_inc))
                 (list (instr/get-instruction :integer_mult)
                       (instr/get-instruction :integer_dec)))))))


(deftest plush-to-push-empty
  (testing "Plush to push (empty)"
    (is (= (plush-to-push '())
           '()))))


(deftest plush-to-push-flat
  (testing "Plush to push (flat)"
    (is (= (plush-to-push '(1 2 3))
           '(1 2 3)))))
