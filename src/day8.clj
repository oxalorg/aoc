(ns day8
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(def input
  (str/split-lines
   (slurp
    (io/resource "input8.txt"))))

(def commands
  (mapv 
   (comp 
    #(hash-map :op (first %) :val (Integer/parseInt (second %)))
    #(str/split % #" ")) input))

commands

(defn find-loop [commands]
  (loop [commands commands
         n 0
         register 0]
    (let [command (nth commands n)]
      (cond
        (nil? command) register
        (true? (:visited command)) register
        :else
        (recur
         (assoc-in commands [n :visited] true)
         (case (:op command)
           "jmp" (+ n (:val command))
           (inc n))
         (case (:op command)
           "acc" (+ register (:val command))
           register))))))

(defn find-loop-2 [commands]
  (loop [commands commands
         n 0
         register 0]
    (let [command (get commands n)]
      (cond
        (nil? command) register
        (true? (:visited command)) nil
        :else
        (recur
         (assoc-in commands [n :visited] true)
         (case (:op command)
           "jmp" (+ n (:val command))
           (inc n))
         (case (:op command)
           "acc" (+ register (:val command))
           register))))))

(find-loop commands)

(defn any-val [coll]
  (some #(when (not (nil? %)) %) coll))

(defn find-recursive [commands]
(loop [commands commands
       n 0
       found-val nil]
  (let [command (get commands n)]
    (cond
      (nil? command) nil
      (not (nil? found-val)) found-val
      :else
      (recur
       commands
       (inc n)
       (any-val 
        [(find-loop-2 commands)
         (case (:op command)
           "jmp" (find-loop-2 (assoc-in commands [n :op] "nop"))
           "nop" (find-loop-2 (assoc-in commands [n :op] "jmp"))
           nil)]))
        ))))

(find-recursive commands)