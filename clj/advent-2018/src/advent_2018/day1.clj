(ns advent-2018.day1
  (:require [advent-2018.utils :as utils]))

(def file-to-nums
  (map (fn [elem] (Integer/parseInt elem)) (utils/file-to-arr "resources/day1.txt")))

(defn solve-1 []
  (let [input file-to-nums]
    (reduce (fn [acc elem] (+ acc elem)) 0 input)))

;; part - 2

(def total (atom 0))
(def visited (atom #{}))
(def duplicate-exists (atom false))

(defn duplicate-found [pointer]
  (swap! total (fn [t] (+ t pointer)))
  (if (contains? @visited @total)
    (reset! duplicate-exists true)
    (swap! visited (fn [v] (conj v @total))))
  @duplicate-exists)

(defn solve-2 []
  (loop [xs (cycle file-to-nums)]
    (if (duplicate-found (first xs))
      @total
      (recur (rest xs)))))
