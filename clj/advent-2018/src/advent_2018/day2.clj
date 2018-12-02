(ns advent-2018.day2
  (:require [advent-2018.utils :as utils]))

(def file-to-charr
  (map 
    (fn [str] (clojure.string/split str #"")) 
    (utils/file-to-arr "resources/day2.txt")))

(defn hash-with-val [h v]
  (let [filtered (filter (fn [k] (= v (get h k))) (keys h))]
    (not (empty? filtered))))

(defn find-lines-with-count [count]
  (let [hashed-lines (map utils/to-hash file-to-charr)]
    (filter (fn [h] (hash-with-val h count)) hashed-lines)))

(defn solve-1 []
  (let [x1 (find-lines-with-count 2) x2 (find-lines-with-count 3)]
    (* (count x1) (count x2))))

;; part 2

(defn same-but-one [[charr1 charr2]]
  (loop [diff 0 count 0]
    (if (== count 25)
      (== 1 diff)
      (if (== 0 (compare (nth charr1 count) (nth charr2 count)))
        (recur diff (inc count))
        (recur (inc diff) (inc count))))))

(def combos (for [x1 file-to-charr x2 file-to-charr] [x1 x2]))

(defn solve-2 []
  (map 
    (fn [arr] (clojure.string/join "" arr)) 
    (first (filter (fn [elem] (same-but-one elem)) combos))))
