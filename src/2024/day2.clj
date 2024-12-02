(ns day2
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))


(def input
  (line-seq
   (io/reader
    (io/resource "2024/input2.txt"))))

(def vlist
  (map #(map parse-long (str/split % #"\s+")) input))

(comment
  (time
   (map #(map parse-long (str/split % #"\s+")) input))
  ;; "Elapsed time: 0.093916 msecs"

  (time
   (into []
         (comp
          (map #(str/split % #"\s+"))
          (map #(map parse-long %))) input)
   )
  ;; "Elapsed time: 1.04675 msecs"

  )

(defn safe? [coll]
  (let [ascending? (< (first coll) (second coll))]
    (every? (fn [[a b]]
              (if ascending?
                (<= 1 (- b a) 3)
                (<= 1 (- a b) 3)))
            (partition 2 1 coll))))

(->> vlist
     (map safe?)
     (filter true?)
     count)
;; => 591


;; WRONG approach
(defn safeable? [tolerance coll]
  (let [ascending? (< (first coll) (second coll))
        safe-pairs (map (fn [[a b c]]
                          (let [safe?
                                (if ascending?
                                  (and
                                   (< a b c)
                                   (<= 1 (- b a) 3)
                                   (<= 1 (- c b) 3))
                                  (and
                                   (< c b a)
                                   (<= 1 (- a b) 3)
                                   (<= 1 (- b c) 3)))]
                            (cond
                              safe? :safe

                              (and ascending? (<= 1 (- c a) 3))
                              :safeable

                              (and (not ascending?) (<= 1 (- a c) 3))
                              :safeable

                              :else
                              :unsafe)))
                        (partition 3 1 coll))
        safeable-levels (->> safe-pairs
                             (filter #{:safeable :safe})
                             count)]
    [(<= safeable-levels tolerance)
     safe-pairs
     ]))

(defn coll-cycle-without-ith [coll]
  (map-indexed (fn [i _]
                 (concat (subvec coll 0 i) (subvec coll (inc i))))
               coll))

(->> vlist
     (map (fn [coll]
            (some safe? (coll-cycle-without-ith (vec coll)))))
     (filter true?)
     count)
;; => 621
