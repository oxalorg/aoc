(ns day11
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))


(defn parse-row [row]
  (println row)
  (map #({"L" 1 "." 0 "#" 2} %) (str/split (str/trim row) #"")))

(defn parse-input [input]
   (map parse-row
        (str/split-lines
         (slurp
          (io/resource "input11.txt")))))

(defn in-range? [matrix i j]
  (let [row-length (count matrix)
        col-length (count (first matrix))]
    (if 
     (and (< i row-length) (>= i 0) (< j col-length) (>= j 0))
      true
      false)))

(defn find-adjacents [matrix i j]
  (let [directions [[0 1] [0 -1] [1 0] [1 -1] [1 1] [-1 1] [-1 0] [-1 -1]]]
    (for [[dx dy] directions]
      (let [inext (+ i dx) jnext (+ j dy)]
        (if (in-range? matrix inext jnext)
          (get-in matrix [inext jnext])
          0)))))

(defn single-pass [matrix]
  (let [row-length (count matrix)
        col-length (count (first matrix))]
    (for [i row-length
          j col-length]
      (let [adjacents (find-adjacents matrix i j)
            adj-freq (frequencies adjacents)
            seat (get-in matrix [i j])]
        (cond
          (and (= seat 1) (= (:2 adj-freq) 0)) ))
    )

(def mat1 [[0 1 0 0]
            [1 0 0 0]])

(find-adjacents mat1
                1 0)

(get-in mat1 [0 1])