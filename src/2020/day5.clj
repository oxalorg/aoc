(ns day5
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(def input (str/split-lines (slurp "resources/input5.txt")))
(def fblrbits {\F 0 \B 1 \L 0 \R 1})
(defn seatcode [v]
  (-> (apply str (map fblrbits v)) (Integer/parseInt 2)))
(def codes (map seatcode input))
(apply max codes)
;; part 2
(let [dc (partition 2 1 (sort codes))]
  (inc (ffirst (filter (fn [[a b]] (= 2 (- b a))) dc))))
