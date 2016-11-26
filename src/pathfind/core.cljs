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

(defn calculate-open-fscore [{:keys [f-score g-score open-set] :as state}
                             parent destination]
  (let [to-calc (reduce disj open-set (keys f-score))
        new-g-score (into g-score
                          (for [cell to-calc]
                            [cell (+ (distance-between manhattan cell parent)
                                     (get g-score parent 0))]))
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

(defn A* [passable? start end]
  (-> (->state #{} #{start} {} {start 0} {start (distance-between manhattan start end)})
      (calculate-open-fscore start end)
      lowest-f-score-open-cell

      ))

(println (A* (constantly true) [0 0] [10 10]))
