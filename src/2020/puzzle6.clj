(ns puzzle6
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as set]))

(def input
  (line-seq
   (io/reader
    (io/resource "input6.txt"))))

(defn find-unique [coll]
  (count (apply set/union (map set coll))))

(defn find-everyone [coll]
  (count (apply set/intersection (map set coll))))

(apply + (map find-unique
              (filter
               #(not (empty? (first %)))
               (partition-by empty? input))))
;; => 6630

(apply + (map find-everyone
              (filter
               #(not (empty? (first %)))
               (partition-by empty? input))))
;; => 3437