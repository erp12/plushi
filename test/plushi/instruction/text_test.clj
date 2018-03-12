(ns plushi.instruction.text-test
  (:require [clojure.test :refer :all]
            [plushi.instruction :refer [instruction-set]]
            [plushi.state :as s]))


(def eval-atom #'plushi.interpreter/evaluate-atom)


(def ut-state
  (s/new-state [:char :string]))


(deftest string_concat_stndrd
  (testing "string concatenation (standard)"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :string "cd")
                          (s/push-item :string "ab"))
                      (:string_concat @instruction-set))
           {:string '("abcd") :char '() :stdout "" :inputs []}))))


(deftest char_concat_stndrd
  (testing "character concatenation (standard)"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :char \b)
                          (s/push-item :char \a))
                      (:char_concat @instruction-set))
           {:string '("ab") :char '() :stdout "" :inputs []}))))


(deftest str_from_first_char_stndrd
  (testing "First character as string (standard)"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :string "abcde"))
                      (:string_from_first_char @instruction-set))
           {:string '("a") :char '() :stdout "" :inputs []}))))


(deftest char_from_last_char_stndrd
  (testing "First character as character (standard)"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :string "abcde"))
                      (:char_from_last_char @instruction-set))
           {:string '() :char '(\e) :stdout "" :inputs []}))))


(deftest str_from_nth_char_stndrd
  (testing "Nth character as string (standard)"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :string "abcdefg")
                          (s/push-item :integer 3))
                      (:string_from_nth_char @instruction-set))
           {:integer '() :string '("d") :char '() :stdout "" :inputs []}))))


(deftest string_contains_string_stndrd
  (testing "String contains string (standard)"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :string "cd")
                          (s/push-item :string "abcdefg"))
                      (:string_contains_string @instruction-set))
           {:boolean '(true) :string '() :char '() :stdout "" :inputs []}))))


(deftest string_contains_char_not_present
 (testing "String contains char (not present)"
   (is (= (eval-atom (-> ut-state
                         (s/push-item :char \z)
                         (s/push-item :string "abcdefg"))
                     (:string_contains_char @instruction-set))
          {:boolean '(false) :string '() :char '() :stdout "" :inputs []}))))


(deftest string_index_of_string_not_present
  (testing "String index of string (not present)"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :string "zzz")
                          (s/push-item :string "abcdefg"))
                      (:string_index_of_string @instruction-set))
           {:integer '(-1) :string '() :char '() :stdout "" :inputs []}))))


(deftest string_index_of_char_stndrd
  (testing "String index of char (standard)"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :char "d")
                          (s/push-item :string "abcdefg"))
                      (:string_index_of_char @instruction-set))
           {:integer '(3) :string '() :char '() :stdout "" :inputs []}))))


(deftest string_occurrences_of_string_stndrd
  (testing "String occurrences of string (standard)"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :string "ab")
                          (s/push-item :string "abcdefg abcd"))
                      (:string_occurrences_of_string @instruction-set))
           {:integer '(2) :string '() :char '() :stdout "" :inputs []}))))


(deftest string_occurrences_of_char_not_present
  (testing "String occurrences of char (not present)"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :char \z)
                          (s/push-item :string "abcdefg"))
                      (:string_occurrences_of_char @instruction-set))
           {:integer '(0) :string '() :char '() :stdout "" :inputs []}))))


(deftest string_replace_string_not_present
  (testing "String replace string (not present)"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :string "ZZZ")
                          (s/push-item :string "Z")
                          (s/push-item :string "abcdefg"))
                      (:string_replace_string @instruction-set))
           {:string '("abcdefg") :char '() :stdout "" :inputs []}))))


(deftest string_replace_char_stndrd
  (testing "String replace char (standard)"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :char \Z)
                          (s/push-item :char \c)
                          (s/push-item :string "abcdefg cd"))
                      (:string_replace_char @instruction-set))
           {:string '("abZdefg Zd") :char '() :stdout "" :inputs []}))))


(deftest string_replace_first_string_stndrd
  (testing "String replace first string (not present)"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :string "ZZZ")
                          (s/push-item :string "cd")
                          (s/push-item :string "abcdefg cd"))
                      (:string_replace_first_string @instruction-set))
           {:string '("abZZZefg cd") :char '() :stdout "" :inputs []}))))


(deftest string_replace_first_char_not_present
  (testing "String replace char (not present)"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :char \Z)
                          (s/push-item :char \z)
                          (s/push-item :string "abcdefg"))
                      (:string_replace_first_char @instruction-set))
           {:string '("abcdefg") :char '() :stdout "" :inputs []}))))


(deftest string_substring_stndrd
  (testing "string substring (standard)"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :string "abcdefg")
                          (s/push-item :integer 2)
                          (s/push-item :integer 5))
                      (:string_substring @instruction-set))
           {:integer '() :string '("cde") :char '() :stdout "" :inputs []}))))


(deftest string_head_stndrd
  (testing "string head (standard)"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :string "abcdefg")
                          (s/push-item :integer 3))
                      (:string_head @instruction-set))
           {:integer '() :string '("abc") :char '() :stdout "" :inputs []}))))


(deftest string_tail_stndrd
  (testing "string tail (standard)"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :string "abcdefg")
                          (s/push-item :integer 3))
                      (:string_tail @instruction-set))
           {:integer '() :string '("efg") :char '() :stdout "" :inputs []}))))


(deftest string_drop_head_stndrd
  (testing "string drop head (standard)"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :string "abcdefg")
                          (s/push-item :integer 3))
                      (:string_drop_head @instruction-set))
           {:integer '() :string '("defg") :char '() :stdout "" :inputs []}))))


(deftest string_drop_tail_stndrd
  (testing "string drop tail (standard)"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :string "abcdefg")
                          (s/push-item :integer 3))
                      (:string_drop_tail @instruction-set))
           {:integer '() :string '("abcd") :char '() :stdout "" :inputs []}))))


(deftest string_drop_first_stndrd
  (testing "string drop first (standard)"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :string "abcdefg"))
                      (:string_drop_first @instruction-set))
           {:string '("bcdefg") :char '() :stdout "" :inputs []}))))


(deftest string_drop_last_stndrd
  (testing "string drop last (standard)"
    (is (= (eval-atom (-> ut-state
                          (s/push-item :string "abcdefg"))
                      (:string_drop_last @instruction-set))
           {:string '("abcdef") :char '() :stdout "" :inputs []}))))
