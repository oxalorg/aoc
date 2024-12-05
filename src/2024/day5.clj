(ns day5
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as set]))

(def day 5)

(def input
  (line-seq
   (io/reader
    (io/resource (str "2024/input" day ".txt")))))

;; split-with but im lazy
(def rules (->> input
                (take-while (comp not str/blank?))
                (map #(str/split % #"\|"))))

(def updates (->> input
                  (drop-while (comp not str/blank?))
                  rest
                  (map #(str/split % #"\,"))
                  ))

(def graph
  (->> rules
       (reduce (fn [graph [v1 v2]]
                 (update graph v1 (fnil conj #{}) v2))
               {})))

(defn update-valid? [update]
  (loop [visited #{}
         up update]
    (cond
      (not (seq up))
      true

      (seq (set/intersection visited (get graph (first up))))
      false

      :else
      (recur (conj visited (first up)) (rest up)))
    ))

(defn get-mid [update]
  (parse-long (get update (quot (count update) 2))))

(->> updates
     (filter update-valid?)
     (map get-mid)
     (reduce +))
;; => 6384

;; part 2
(comment
  ;; YIKES this wasn't giving me the correct answer because there is
  ;; no ONE ORDER TO RULE THEM ALL :()
  (def one-order-to-rule-them-all (->> graph
                                       (into [])
                                       (sort-by #(count (second %1)))
                                       (map first)
                                       reverse))
  (defn fix-update [update]
    (let [in-update? (set update)]
      (->> one-order-to-rule-them-all
           (filter in-update?))))


  (->> updates
       (remove update-valid?)
       (map fix-update)
       (map vec)
       (map get-mid)
       (reduce +))
  ;; => 123
  ;; => 4427
  )

;; okay need to try an alternative solution now
(defn fix-update [update]
  (sort-by identity
           (fn [v1 v2]
             ;; can also check rules instead of graph
             (if ((get graph v1) v2)
               -1
               1))
           update))

(->> updates
     (remove update-valid?)
     (map fix-update)
     (map vec)
     (map get-mid)
     (reduce +))
;; => 5353
