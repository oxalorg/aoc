(ns day4
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def input
  (line-seq
   (io/reader
    (io/resource "2024/input4.txt"))))

(def matrix (mapv (comp vec seq) input))

(defn m [i j] (get-in matrix [i j]))

(defn scan-xmas [i j]
  (let [xmas [[0 0] [0 1] [0 2] [0 3]]
        xmas-down [[0 0] [1 0] [2 0] [3 0]]
        xmas-diag-rd [[0 0] [1 1] [2 2] [3 3]]
        xmas-diag-ru [[0 0] [-1 1] [-2 2] [-3 3]]
        combos [xmas xmas-down xmas-diag-ru xmas-diag-rd]]
    (for [word combos]
      (str/join "" (map (fn [[di dj]]
                          (m (+ i di) (+ j dj)))
                        word)))))

(->>
 (for [i (range (count matrix))
       j (range (count (first matrix)))]
   (scan-xmas i j))
 (apply concat)
 (filter #{"XMAS" "SAMX"})
 count)
;; => 2336

(defn scan-x-mas [i j]
  (let [mas-diag-rd [[0 0] [1 1] [2 2]]
        mas-diag-ld [[0 2] [1 1] [2 0]]
        combos [mas-diag-rd mas-diag-ld]]
    (for [word combos]
      (str/join "" (map (fn [[di dj]]
                          (m (+ i di) (+ j dj)))
                        word)))))


(->>
 (for [i (range (count matrix))
       j (range (count (first matrix)))]
   (scan-x-mas i j))
 (filter #(every? #{"MAS" "SAM"} %1))
 count)
;; => 1831
