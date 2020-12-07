(ns puzzle7
  (:require [clojure.java.io :as io]
            [clojure.string]))

(def input
  (line-seq
   (io/reader
    (io/resource "input7.txt"))))

(defn parse [input-list]
  (map
   (comp
    #(map clojure.string/trim %)
    #(clojure.string/split % #"(contain|,)")
    #(clojure.string/replace % "bags" "bag")
    #(clojure.string/replace % "." ""))   
   input-list))

(defn make-value-map [bag]
  (if (= bag "no other bag")
    {:num 0 :bag nil}
    (let [[num bag-name] (clojure.string/split bag #" " 2)]
      {:num (Integer/parseInt num) :bag bag-name})))

(def graph-list
  (parse input))

(def graph
  (reduce #(assoc %1 (first %2) (map make-value-map (rest %2))) {} graph-list))

(defn find-bag [key]
  (loop [bags (get graph key)]
    (cond
      (empty? bags) false
      (some #{"shiny gold bag"} (map #(:bag %) bags)) true
      :else 
      (recur 
       (concat (rest bags) (get graph (:bag (first bags))))))))

(defn count-bags [key]
  (let [bags (get graph key)]
    (if (empty? bags)
      0
      (apply +
       (for [bag bags]
         (+ (:num bag) (* (:num bag) (count-bags (:bag bag)))))))))

(count-bags "shiny gold bag")

(count
 (filter true?
         (map #(find-bag (first %)) graph-list)))