(ns user
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(defn parse-input [input]
  (map {"a" 1 "b" 2}
       (doto (str/split input #"\|") prn)))
;; => #'user/parse-input

(parse-input "a|b|a|a|b")
;; prints  => ["a" "b" "a" "a" "b"]
;; returns => (1 2 1 1 2)
