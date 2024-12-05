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

(defn scan-text [coll text]
  (let [n (count text)
        text* (str/join "" (take n coll))]
    (if (= text* text)
      n
      nil)))

(defn scan-mul [coll]
  (let [mul (str/join "" (take 3 coll))]
    (if (= mul "mul")
      [:mul (drop 3 coll)]
      [nil (drop 1 coll)])))

(def is-num-char? #{\1 \2 \3 \4 \5 \6 \7 \8 \9 \0})

(defn scan-number [coll]
  (assert (is-num-char? (first coll)))
  (let [n (loop [i 0]
            (cond
              (>= i (count coll))
              i

              (is-num-char? (nth coll i))
              (recur (inc i))

              :else
              i))]
    [{:token :num
      :value (parse-long (str/join "" (take n coll)))}
     (drop n coll)]))

(defn tokenize [{:keys [coll tokens] :as ctx
                 :or {tokens []}}]
  (let [c (first coll)]
    (cond
      (nil? c)
      tokens

      (scan-text coll "mul")
      (recur {:tokens (conj tokens {:token :mul})
              :coll (drop 3 coll)})

      (scan-text coll "do()")
      (recur {:tokens (conj tokens {:token :do})
              :coll (drop 4 coll)})

      (scan-text coll "don't()")
      (recur {:tokens (conj tokens {:token :dont})
              :coll (drop 7 coll)})

      ;; (= c \m)
      ;; (let [[token coll] (scan-mul coll)]
      ;;   (recur {:tokens (conj tokens token)
      ;;           :coll coll}))

      (#{\1 \2 \3 \4 \5 \6 \7 \8 \9 \0} c)
      (let [[token coll] (scan-number coll)]
        (recur {:tokens (conj tokens token)
                :coll coll}))

      (= c \()
      (recur {:tokens (conj tokens {:token :lbracket})
              :coll (rest coll)})

      (= c \))
      (recur {:tokens (conj tokens {:token :rbracket})
              :coll (rest coll)})

      (= c \,)
      (recur {:tokens (conj tokens {:token :comma})
              :coll (rest coll)})

      :else
      (recur {:tokens (conj tokens {:token :unwanted})
              :coll (rest coll)})
      ;; for non strict parsing, ignore spaces here or change to your liking
      #_(-> ctx
            advance
            recur))))

(def tokens (tokenize {:coll (->> memory seq (take 300) vec)}))

(defn p-mul [tokens]
  (let [tokens* (take 6 tokens)]
    (if (= [:mul :lbracket :num :comma :num :rbracket] (mapv :token tokens*))
      [(->> tokens*
            (filter #(= :num (:token %)))
            (map :value)
            (reduce *))
       (drop 6 tokens)]
      [0 (rest tokens)])))

(defn parser [acc tokens]
  (let [top (first tokens)]
    (cond
      (nil? top)
      acc

      (= (:token top) :mul)
      (let [[res tokens*] (p-mul tokens)]
        (recur (+ acc res) tokens*))

      :else
      (recur acc (rest tokens)))))

(parser 0 (tokenize {:coll (->> memory seq)}))
;; => 183669043

(defn p-dont [tokens]
  [0 (drop-while (fn [{:keys [token]}]
                   (not= token :do))
                 tokens)])

(defn parser-handle-do-dont [acc tokens]
  (let [top (first tokens)]
    (cond
      (nil? top)
      acc

      (= (:token top) :dont)
      (let [[_ tokens*] (p-dont tokens)]
        (recur acc tokens*))

      (= (:token top) :mul)
      (let [[res tokens*] (p-mul tokens)]
        (recur (+ acc res) tokens*))

      :else
      (recur acc (rest tokens)))))

(parser-handle-do-dont 0 (tokenize {:coll (->> memory seq)}))
;; => 59097164
