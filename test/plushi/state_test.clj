(ns plushi.state-test
  (:require [clojure.test :refer :all]
            [plushi.state :refer :all]))


(def ut-state
  (-> (new-state [:a :b])
      (push-item :b 5)))


(deftest new-state-stndrd
  (testing "Standard creation of new push state"
    (is (= (new-state [:a :b])
           {:a '() :b '() :stdout "" :inputs []}))))


(deftest new-state-duplicate
  (testing "Creation of new push state with duplicate types"
    (is (= (new-state [:a :b :b])
           {:a '() :b '() :stdout "" :inputs []}))))


(deftest push-item-stndrd
  (testing "Standard push item to stack"
    (is (= (push-item ut-state :a "foo")
           {:a '("foo") :b '(5) :stdout "" :inputs []}))))


(deftest pop-item-stndrd
  (testing "Standard pop item from stack"
    (is (= (pop-item ut-state :b)
           {:a '() :b '() :stdout "" :inputs []}))))


(deftest pop-item-empty
  (testing "Pop item from empty stack"
    (is (= (pop-item ut-state :a)
           {:a '() :b '(5) :stdout "" :inputs []}))))


(deftest top-item-stndrd
  (testing "Standard call to top item"
    (is (= (top-item ut-state :b)
           5))))


(deftest top-item-empty
  (testing "Call top item on empty stack"
    (is (= (top-item ut-state :a)
           :NO-STACK-ITEM))))


(deftest nth-item-stndrd
  (testing "Nth item on stack"
    (is (= (nth-item ut-state :b 0)
           5))))


(deftest nth-item-oob
  (testing "Nth item on stack, out of bounds"
    (is (= (nth-item ut-state :b 1)
           :NO-STACK-ITEM))))


(deftest nth-item-empty
  (testing "Nth item on empty stack"
    (is (= (nth-item ut-state :a 0)
           :NO-STACK-ITEM))))


(deftest insert-item-stndrd
  (testing "Insert item on stack"
    (is (= (insert-item ut-state :b 1 6)
           {:a '() :b '(5 6) :stdout "" :inputs []}))))


(deftest insert-item-oob
  (testing "Insert item on stack, out of bounds"
    (is (= (insert-item ut-state :b 3 6)
           {:a '() :b '(5 6) :stdout "" :inputs []}))))


(deftest insert-item-empty
  (testing "Insert item on stack"
    (is (= (insert-item ut-state :a 0 6)
           {:a '(6) :b '(5) :stdout "" :inputs []}))))


(deftest assoc-item-stndrd
  (testing "Assoc position in stack with item"
    (is (= (assoc-item ut-state :b 0 :replaced)
           {:a '() :b '(:replaced) :stdout "" :inputs []}))))


(deftest assoc-item-empty
  (testing "Assoc position in empty stack with item"
    (is (= (assoc-item ut-state :a 0 3)
           {:a '(3) :b '(5) :stdout "" :inputs []}))))


(deftest assoc-item-oob
  (testing "Assoc OOB position in stack with item"
    (is (= (assoc-item ut-state :b 10 3)
           {:a '() :b '(5 3) :stdout "" :inputs []}))))


(deftest flush-stack-stndrd
  (testing "Flush a stack"
    (is (= (flush-stack ut-state :b)
           {:a '() :b '() :stdout "" :inputs []}))))


(deftest flush-stack-empty
  (testing "Flush an empty stack"
    (is (= (flush-stack ut-state :a)
           {:a '() :b '(5) :stdout "" :inputs []}))))
