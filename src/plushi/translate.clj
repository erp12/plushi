(ns plushi.translate
  (:require [clojure.spec.alpha :as spec]
            [clojure.string :as s]
            [hiccup.core :as h]
            [hiccup.page :as hp]
            [plushi.atoms :as a]
            [plushi.utils :as u]
            [plushi.instruction :as i]))


(defn- is-close-atom
  [atom]
  (and (spec/valid? :plushi.instruction/instruction atom)
       (= (:name atom) "close")))


(defn- opener?
  [atom]
  (and (vector? atom)
       (= (first atom) :plush-open)))



(defn load-instructions
  [plush-code]
  (loop [remaining-plush-encoding plush-code
         plush []]
    (if (empty? remaining-plush-encoding)
      (u/vector-to-list plush)
      (let [atom (first remaining-plush-encoding)]
        (cond
          ; If atom is an instruction name in a string. Used by JSON.
          (and (string? atom)
               (s/starts-with? atom "plushi:"))
          (recur (rest remaining-plush-encoding)
                 (conj plush
                       (i/get-instruction (keyword (second (s/split atom
                                                                    #"plushi:"))))))

          ; If atom is an instruction name in a keyword. Used by EDN.
          (keyword? atom)
          (recur (rest remaining-plush-encoding)
                 (conj plush (i/get-instruction atom)))

          ; If atom is anything else, leave it as is.
          :else
          (recur (rest remaining-plush-encoding)
                 (conj plush atom)))))))


(defn plush-to-push
  [plush-code]
  (let [close-instr (i/get-instruction :close)]
    (loop [push ()
           plush (mapcat #(if-let [needs-open (or (nil? (:code-blocks %))
                                                  (zero? (:code-blocks %)))]
                            [%]
                            [% [:plush-open (:code-blocks %)]])
                         (load-instructions plush-code))]
      (cond
        ; If done with plush but unclosed opens, recur with one more close.
        (and (empty? plush)
             (some opener? push))
        (recur push (list close-instr))

        ; If done with plush and all opens closed, return push.
        (empty? plush)
        push

        :else
        (let [i (first plush)]
          (cond
            ; If next instruction is a close, and there is an open.
            (and (= i close-instr)
                 (some opener? push))
            (recur (let [post-open (reverse (take-while (comp not opener?)
                                                        (reverse push)))
                         open-index (- (count push) (count post-open) 1)
                         num-open (second (nth push open-index))
                         pre-open (take open-index push)]
                     (if (= 1 num-open)
                       (concat pre-open [post-open])
                       (concat pre-open [post-open [:plush-open (dec num-open)]])))
                   (rest plush))

            ; If next instruction is a close, and there is no open.
            (= i close-instr)
            (recur push (rest plush))

            :else
            (recur (concat push [i])
                   (rest plush))))))))



(defn instruction-set-docs-to-html
  [instruction-set]
  (hp/html5 [:head (hp/include-css "css/default.css")]
            [:body
             (list
                   [:div#header
                    [:h1
                     [:a {:href "index.html"}
                      [:span.project-title
                       [:span.project-name "Plushi Documentation"]]]]]
                   [:div#content.namespace-docs {:style "left:0px;"}
                    (list [:h2#top.anchor "Instruction Set"]
                          [:pre.doc "Documentation on the supported instructions of the plushi interpreter."]
                          (for [i (sort #(compare (:name %1) (:name %2))
                                        (vals instruction-set))]
                            [:div#var-image.public.anchor
                             (list [:h3 (:name i)]
                                   [:div.usage
                                    [:code (str "Input Types: " (pr-str (:input-types i)))]
                                    [:code (str "Output Types: " (pr-str (:output-types i)))]]
                                   [:div.doc
                                    [:pre.plaintext (:docstring i)]])]))])]))
