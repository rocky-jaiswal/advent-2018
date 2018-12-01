(ns advent-2018.utils)

(defn- maintain-hash [hash elem]
  (if (get hash elem)
    (assoc hash elem (+ 1 (get hash elem)))
    (assoc hash elem 1)))

(defn- to-hash [arr]
  (reduce (fn [hash elem] (maintain-hash hash elem)) {} arr))

(defn find-duplicate [arr]
  (let [hash (to-hash arr)]
    (filter (fn [key] (> (get hash key) 1)) (keys hash))))
