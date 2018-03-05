(ns plushi.translate
  (:require [clojure.spec.alpha :as spec]
            [plushi.atoms :as a]
            [plushi.utils :as u]))


(defn is-close-atom
  [atom]
  (and (spec/valid? :plushi.instruction/instruction atom)
       (= (:name atom) "close")))


(defn get-matching-close-index
  [sequence]
  (loop [remaining sequence
         open-count 0
         ndx 0]
    (cond
      (empty? remaining)
      :NO-CLOSE

      (= (first remaining) :plush-open)
      (recur (rest remaining)
             (inc open-count)
             (inc ndx))

      (= (first remaining) :plush-close)
      (if (zero? open-count)
        ndx
        (recur (rest remaining)
               (dec open-count)
               (inc ndx)))

      :else
      (recur (rest remaining)
             open-count
             (inc ndx)))))


(defn open-close-vec-to-push
  [open-close-vec]
  (cond
    (not (vector? open-close-vec))
    open-close-vec

    (empty? open-close-vec)
    (list)

    :else
    (loop [result []
           remaining open-close-vec]
      (if (empty? remaining)
        (u/vector-to-list result)
        (if (= (first remaining) :plush-open)
          (let [remaining (vec (rest remaining))
                ndx-of-close (get-matching-close-index remaining)
                sub-plush-expression (vec (take ndx-of-close remaining))
                expression-as-push (open-close-vec-to-push sub-plush-expression)]
            (recur (conj result expression-as-push)
                   (vec (drop (inc ndx-of-close) remaining))))
          (recur (conj result (first remaining))
                 (vec (rest remaining))))))))


(defn plush-to-push
  [plush-program]
  (loop [plush-open-close-vector []
         remaining-plush-code plush-program
         code-block-depth 0
         needed-close-paren-stack (list)]
    (cond
      ; Check if at end of program but still need to add parens.
      (and (empty? remaining-plush-code)
           (not (empty? needed-close-paren-stack)))
      (recur (if (= (first needed-close-paren-stack) :plush-close-open)
               (vec (concat plush-open-close-vector (list :plush-close :plush-open)))
               (conj plush-open-close-vector :plush-close))
             remaining-plush-code
             (dec code-block-depth)
             (rest needed-close-paren-stack))

      ; Check if done
      (and (empty? remaining-plush-code)
           (empty? needed-close-paren-stack))
      (open-close-vec-to-push plush-open-close-vector)

      :else
      (let [atom (first remaining-plush-code)
            n-code-blocks-opened (if (= (a/recognize-atom-type atom) :instruction)
                                   (:code-blocks atom)
                                   0)]
        (if (is-close-atom atom)
          (recur (if (empty? needed-close-paren-stack)
                   plush-open-close-vector
                   (if (= (first needed-close-paren-stack) :plush-close-open)
                     (vec (concat plush-open-close-vector (list :plush-close :plush-open)))
                     (conj plush-open-close-vector :plush-close)))
                 (rest remaining-plush-code)
                 (dec code-block-depth)
                 (rest needed-close-paren-stack))
          (recur (if (pos? n-code-blocks-opened)
                   (vec (concat plush-open-close-vector (list atom :plush-open)))
                   (conj plush-open-close-vector atom))
                 (rest remaining-plush-code)
                 (+ code-block-depth n-code-blocks-opened)
                 (if (pos? n-code-blocks-opened)
                   (concat (repeat (dec n-code-blocks-opened)
                                   :plush-close-open)
                           (list :plush-close)
                           needed-close-paren-stack)
                   needed-close-paren-stack)))))))
