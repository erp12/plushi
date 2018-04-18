(ns plushi.instruction.text
  (:require [plushi.instruction :as i]
            [clojure.string :as s]))


;; Shared between string and char

(defn- register-basic-text
  [type-kw]
  (let [type-str (name type-kw)]

    (i/register (str type-str "_concat")
                #(str %1 %2)
                [type-kw type-kw] [:string] 0
                (str "Concatenates the top two " type-str " and pushes the resulting string."))

    ;; Getting characters

    (i/register (str type-str "_from_first_char")
                #(if (empty? %) :REVERT (first %))
                [:string] [type-kw] 0
                (str "Pushes a " type-str " of the first character of the top string."))

    (i/register (str type-str "_from_last_char")
                #(if (empty? %) :REVERT (last %))
                [:string] [type-kw] 0
                (str "Pushes a " type-str " of the last character of the top string."))

    (i/register (str type-str "_from_nth_char")
                (fn [s i]
                  (if (empty? s)
                    :REVERT
                    (nth s (mod i (count s)))))
                [:string :integer] [type-kw] 0
                (str "Pushes a " type-str " of the nth character of the top string. The top integer denotes nth position."))

    ;; Checking string contents

    (i/register (str "string_contains_" type-str)
                #(s/includes? %1 (str %2))
                [:string type-kw] [:boolean] 0
                (str "Pushes true if the next " type-str " is in the top string. Pushes false otherwise."))

    (i/register (str "string_index_of_" type-str)
                #(let [ndx (s/index-of %1 (str %2))]
                   (if (nil? ndx) -1 ndx))
                [:string type-kw] [:integer] 0
                (str "Pushes the index of the next " type-kw " in the top string. If not found, pushes -1."))

    (i/register (str "string_occurrences_of_" type-str)
                #(count (re-seq (re-pattern (str %2)) %1))
                [:string type-kw] [:integer] 0
                (str "Pushes the number of times the next " type-str " occurs in the top string."))

    ;; Replacing

    (i/register (str "string_replace_" type-str)
                #(s/replace %1 (str %2) (str %3))
                [:string type-kw type-kw] [:string] 0
                (str "Replaces all occurances of a " type-str " in the top string with another " type-str "."))

    (i/register (str "string_replace_first_" type-str)
                #(s/replace-first %1 (str %2) (str %3))
                [:string type-kw type-kw] [:string] 0
                (str "Replaces the first occurance of a " type-str " in the top string with another " type-str "."))
    ))

(doall (map register-basic-text [:string :char]))


;; Substring instructions


(defn- p-subs
  ([s start]
   (p-subs s start (count s)))
  ([s start end]
   (let [p-start (-> (max start 0)
                     (min (count s)))
         p-end (-> (max end 0)
                   (min (count s)))]
     (if (< p-start p-end)
       (subs s p-start p-end)
       (subs s p-end p-start)))))


(i/register "string_substring"
            #(p-subs %1 %2 %3)
            [:string :integer :integer] [:string] 0
            "Pushes a substring of the top string based on the top two integers.")


(i/register "string_head"
            #(p-subs %1 0 %2)
            [:string :integer] [:string] 0
            "Pushes a string of the first n characters from the top string. N is the top integer.")


(i/register "string_tail"
            #(p-subs %1 (- (count %1) %2))
            [:string :integer] [:string] 0
            "Pushes a string of the last n characters from the top string. N is the top integer.")


(i/register "string_drop_head"
            #(p-subs %1 %2)
            [:string :integer] [:string] 0
            "Pushes the top string without the first n characters. N is the top integer.")


(i/register "string_drop_tail"
            #(p-subs %1 0 (- (count %1) %2))
            [:string :integer] [:string] 0
            "Pushes the top string without the last n characters. N is the top integer.")


(i/register "string_drop_first"
            #(p-subs %1 1 (count %1))
            [:string] [:string] 0
            "Pushes the top string without the first character. N is the top integer.")


(i/register "string_drop_last"
            #(p-subs %1 0 (dec (count %1)))
            [:string] [:string] 0
            "Pushes the top string without the last character. N is the top integer.")


;; Character edits

; string_removechar
; string_setchar

;; Converting types

;; Misc string

; string_empty
; string_split
; string_reverse
; string_trim_whitespace
; string_exec_iterate

;; Misc char

; char_is_letter
; char_is_digit
; char_is_whitespace
