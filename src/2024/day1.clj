(ns day1
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def input
  (line-seq
   (io/reader
    (io/resource "2024/input1.txt"))))

(def vlist
  (map #(map parse-long (str/split % #"\s+")) input))

(def lists (apply map vector vlist))

(def ll (first lists))
(def rl (second lists))

(reduce + (map (comp abs -) (sort ll) (sort rl)))
;; => 1765812

(let [fm (frequencies rl)]
  (reduce + (map (fn [x]
                   (* x (get fm x 0)))
                 ll)))
;; => 20520794
