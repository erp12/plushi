(ns plushi.translate-test
  (:require [clojure.test :refer :all]
            [plushi.translate :refer :all]
            [plushi.instruction :as instr]))


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


(deftest get-matching-close-index-flat
  (testing "Get matching close index (flat)"
    (is (= (get-matching-close-index [:a :b :plush-close :d])
           2))))


(deftest get-matching-close-index-deep
 (testing "Get matching close index (not flat)"
   (is (= (get-matching-close-index [:a :plush-open :b :plush-close :plush-close :d])
          4))))


(deftest open-close-vec-to-push-stndrd
 (testing "Open close vec to push (standard)"
   (is (= (open-close-vec-to-push [5 :plush-open 4 :plush-close 3])
          (list 5 (list 4) 3)))))


(deftest open-close-vec-to-push-flat
  (testing "Open close vec to push (flat)"
    (is (= (open-close-vec-to-push [5 4 3])
           (list 5 4 3)))))


;; These should not happen if open-close-vec comes from plush-to-push
;; TODO: Add these tests just in case.

; (deftest open-close-vec-to-push-extra-close
;   (testing "Open close vec to push (extra cose)"
;     (is (= (open-close-vec-to-push [5 :plush-open 4 :plush-close :plush-close 3])
;            (list 5 (list 4) 3)))))
;
;
; (deftest open-close-vec-to-push-missing-close
;   (testing "Open close vec to push (missing cose)"
;     (is (= (open-close-vec-to-push [5 :plush-open 4  3])
;            (list 5 (list 4 3))))))


(deftest plush-to-push-standard
  (testing "Plush to push (standard)"
    (is (= (plush-to-push ut-plush-program)
           (list true 2 3
                 (instr/get-instruction :exec_if)
                 (list (instr/get-instruction :integer_add)
                       (instr/get-instruction :integer_inc))
                 (list (instr/get-instruction :integer_mult)
                       (instr/get-instruction :integer_dec)))))))
