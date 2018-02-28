(ns plushi.interpreter-test
  (:require [clojure.test :refer :all]
            [plushi.state :as state]
            [plushi.interpreter :refer :all]))

; (deftest a-test
;   (testing "FIXME, I fail."
;     (is (= 0 1))))


(def ut-state
  (-> (state/new-state [:a :b])
      (state/push-item :b 5)))


(deftest load-program-stndrd
  (testing "Standard load program"
    (is (= (load-program ut-state (list 1 2 3))
           {:exec '(1 2 3)
            :a '()
            :b '(5)
            :stdout ""
            :inputs []}))))


(deftest load-inputs-stndrd
  (testing "Standard load inputs"
    (is (= (load-inputs ut-state (list 1 2 3))
           {:a '()
            :b '(5)
            :stdout ""
            :inputs [1 2 3]}))))


(deftest inspect-outputs-stndrd
  (testing "Standard inspect outputs"
    (is (= (inspect-outputs ut-state [:b :a :b])
           [5 :NO-STACK-ITEM :NO-STACK-ITEM]))))


(deftest pop-arguments-stndrd
  (testing "Standard pop arguments"
    (is (= (pop-arguments ut-state [:b])
            [{:a '() :b '() :stdout "" :inputs []} [5]]))))


(deftest pop-arguments-too-few
  (testing "Pop arguments when too few available"
    (is (= (pop-arguments ut-state [:b :a :b])
            [ut-state :REVERT]))))


(deftest push-returns-single
  (testing "Push single return value"
    (is (= (push-returns ut-state [3] [:a])
           {:a '(3) :b '(5) :stdout "" :inputs []}))))


(deftest push-returns-multiple
  (testing "Push multiple return values"
    (is (= (push-returns ut-state [3 2 1] [:a :b :a])
           {:a '(1 3) :b '(2 5) :stdout "" :inputs []}))))


; (deftest evaluate-atom-int-literal
;   (testing "Evaluate literal atom")
;     (is (= (evaluate-atom ut-state ))))
