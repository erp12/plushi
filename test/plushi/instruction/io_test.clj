(ns plushi.instruction.io-test
  (:require [clojure.test :refer :all]
            [plushi.instruction :refer [instruction-set]]
            [plushi.state :as s]))


(def ut-state
  (s/new-state [:integer :string]))


(def eval-atom #'plushi.interpreter/evaluate-atom)


(deftest integer_print_stndrd
  (testing "integer print standard"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :integer 1)
                          (s/push-item :integer 3))
                      (:integer_print @instruction-set))
           {:integer '(1) :string '() :stdout "3" :inputs []}))))
