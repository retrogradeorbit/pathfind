(ns pathfind.core
  (:require ))

(enable-console-print!)

(defn manhattan [dx dy] (+ (Math/abs dx) (Math/abs dy)))

(defn chebyshev [dx dy] (max (Math/abs dx) (Math/abs dy)))

(defn euclid [dx dy] (.sqrt js/Math (+ (* dx dx) (* dy dy))))

(defn distance-between [func [x1 y1] [x2 y2]]
  (func
   (Math/abs (- x1 x2))
   (Math/abs (- y1 y2))))

(defrecord state [closed-set open-set came-from g-score f-score])

(defn state-add-neighbour [{:keys [closed-set open-set came-from]
                            :as  state} current neighbour]
  (assoc state
         :open-set (conj open-set neighbour)
         :came-from (assoc came-from neighbour current)))

(defn state-add-open [state cell]
  (update state :open-set conj cell))

(defn state-open-to-closed [state cell]
  (-> state
      (update :open-set disj cell)
      (update :closed-set conj cell)))

(defn reduce-state-over-neighbours [state current neighbours]
  (reduce
   (fn [acc item] (state-add-neighbour acc current item))
   state
   neighbours))

(defn g-dist [[x1 y1] [x2 y2]]
  (let [dx (Math/abs (- x1 x2))
        dy (Math/abs (- y1 y2))]
    (cond
      (and (= 1 dx) (= 1 dy))
      14

      (or (= 1 dx) (= 1 dy))
      10)))

(defn calculate-open-fscore [{:keys [f-score g-score open-set] :as state}
                             parent destination]
  (let [to-calc (reduce disj open-set (keys f-score))
        new-g-score (into g-score
                          (for [cell to-calc]
                            [cell (+ (g-dist cell parent)
                                     (get g-score parent))]))
        new-f-score (into f-score
                          (for [cell to-calc]
                            [cell (+ (new-g-score cell)
                                     (distance-between manhattan cell destination))]))]
    (assoc state :f-score new-f-score :g-score new-g-score)))

(defn neighbors-for [[x y]]
  (for [dx [-1 0 1] dy [-1 0 1]
        :when (not (and (zero? dx) (zero? dy)))]
    [(+ x dx) (+ y dy)]))

(defn lowest-f-score-open-cell [{:keys [f-score open-set]}]
  (->> f-score
       (sort-by second)
       (filter (fn [[k v]] (open-set k)))
       first
       first))

(defn A*-step [state start end current]
  (let [
        neighbors (apply disj (into #{} (neighbors-for current)) (:closed-set state))
        state (-> state
                  (reduce-state-over-neighbours current neighbors)
                  (calculate-open-fscore current end)
                  (state-open-to-closed current))
        next-cell (lowest-f-score-open-cell state)]
    [state next-cell]))

(defn A* [passable? start end]
  (let [state (-> (->state #{} #{start} {} {start 0} {start (distance-between manhattan start end)}))
        [state next-cell] (A*-step state start end start)]
    (println state next-cell)
    (println "=================")
    (let [[state next-cell] (A*-step state start end next-cell)]
      (println ;state
               next-cell)
      (println "================")
      (let [[state next-cell] (A*-step state start end next-cell)]
        (println ;state
                 next-cell)
        (println "================")
        (let [[state next-cell] (A*-step state start end next-cell)]
          (println ;state
                   next-cell)
          (println "================")
          (let [[state next-cell] (A*-step state start end next-cell)]
            (println ;state
                     next-cell)
            (println "================")
            (let [[state next-cell] (A*-step state start end next-cell)]
              (println ;state
                       next-cell)
              (println "================")
              (let [[state next-cell] (A*-step state start end next-cell)]
                (println ;state
                         next-cell)
                (println "================")
                (let [[state next-cell] (A*-step state start end next-cell)]
                  (println ;state
                           next-cell)
                  (println "================")

                  ))
              )
            ))
        ))))

;(println (A* (constantly true) [0 0] [10 10]))
