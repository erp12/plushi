(ns plushi.instruction.io-test
  (:require [clojure.test :refer :all]
            [plushi.instruction :refer [instruction-set]]
            [plushi.instruction.io :refer [register-input-instructions]]
            [plushi.state :as s]))

(def eval-atom #'plushi.interpreter/evaluate-atom)
(def load-inputs #'plushi.interpreter/load-inputs)


(def ut-state
  (-> (s/new-state [:integer :string])
      (load-inputs [5 3])))


(deftest input_stndrd
  (testing "input instruction (standard)"
    (is (= (do
             (register-input-instructions 2)
             (eval-atom ut-state
                        (:input_0 @instruction-set)))
           {:integer '() :string '() :stdout "" :inputs [5 3] :exec '(5)}))))


(deftest integer_print_stndrd
  (testing "integer print (standard)"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :integer 1)
                          (s/push-item :integer 3))
                      (:integer_print @instruction-set))
           {:integer '(1) :string '() :stdout "3" :inputs [5 3]}))))
