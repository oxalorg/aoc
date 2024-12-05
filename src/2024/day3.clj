(ns day3
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def input
  (line-seq
   (io/reader
    (io/resource "2024/input3.txt"))))

(def memory
  (str/join "" input))

memory
(seq memory)

;; part 1
(re-seq #"mul\((\d+),(\d+)\)" "mul(1,3)okokmul(1,2)")
;; => (["mul(1,3)" "1" "3"] ["mul(1,2)" "1" "2"])
(reduce (fn [acc [_ a b]]
          (+ acc (* (parse-long a) (parse-long b))))
        0
        (re-seq #"mul\((\d+),(\d+)\)" memory))
;; => 183669043

;; part 2
(re-seq #"don't\(\)|do\(\)|mul\((\d+),(\d+)\)" "don't()oakosdkaomul(1,3)okokundo()mul(1,2)")
;; => (["don't()" nil nil] ["mul(1,3)" "1" "3"] ["do()" nil nil] ["mul(1,2)" "1" "2"])

(reduce (fn [{:keys [do? ans] :as acc} [op a b]]
          (println acc [op a b])
          (case op
            "don't()"
            (assoc acc :do? false)

            "do()"
            (assoc acc :do? true)

            (if do?
              (update acc :ans + (* (parse-long a) (parse-long b)))
              acc)))
        {:do? true :ans 0}
        (re-seq #"don't\(\)|do\(\)|mul\((\d+),(\d+)\)" memory))
;; => {:do? false, :ans 59097164}

;; WIP: Non regex approach
(defn advance [ctx]
  (update ctx :coll rest))

(defn scan-mul [coll]
  (let [mul (str/join "" (take 3 coll))]
    (if (= mul "mul")
      [:mul (drop 3 coll)]
      [nil (drop 1 coll)])))

(defn tokenize [{:keys [coll tokens] :as ctx}]
  (let [c (first coll)]
    (cond
      (nil? c)
      ctx

      (= c \m)
      (let [[token coll] (scan-mul coll)]
        (recur {:tokens (conj tokens token)
                :coll coll}))

      (= c \()
      (recur {:tokens (conj tokens :lbracket)
              :coll (rest coll)})

      (= c \))
      (recur {:tokens (conj tokens :lbracket)
              :coll (rest coll)})

      :else
      (-> ctx
          advance
          recur))))

(tokenize {:coll (->> memory seq vec (take 300))})
