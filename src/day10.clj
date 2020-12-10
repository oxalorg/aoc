(ns day10
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(def input
  (sort
   (map #(Integer/parseInt %)
        (str/split-lines
         (slurp
          (io/resource "input10.txt"))))))

(defn part1 [coll]
  (let [diff-list (map #(apply - (reverse %)) (partition 2 1 input))]
    (* (count (filter #(= 1 %) diff-list))
       (count (filter #(= 3 %) diff-list)))))

(def coll (vec (sort (conj input 0 (+ (apply max input) 3)))))

(defn sol-exists? [i step]
  (let [len (count coll)
        j (+ i step)]
    (and
     (< j len)
     (<= (- (get coll j) (get coll i)) 3))))

(def sol-exists-memo? (memoize sol-exists?))

(def mem (atom {}))

(defn part2 [i]
  ;; (println coll i)
  (cond
    (>= i (count coll)) 0
    (= i (- (count coll) 1)) 1
    (find @mem i) (val (find @mem i))
    :else
    (let [len (count coll)
          ans [(when (sol-exists-memo? i 1) (part2 (+ i 1)))
               (when (sol-exists-memo? i 2) (part2 (+ i 2)))
               (when (sol-exists-memo? i 3) (part2 (+ i 3)))]
          sol (apply + (remove nil? ans))]
      (swap! mem assoc i sol)
      sol)))

(part1 input)
;; => 2652

(part2 0)
;; => 13816758796288
