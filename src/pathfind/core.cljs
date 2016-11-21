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

(defn state-add-open [state neighbour]
  (update state :open-set conj neighbour))

(defn A*-step [{:keys [closed-set open-set came-from g-score f-score] :as state}
               current goal neigh]
  (let [tentative-score (+ (get g-score current 9999)
                           (distance-between current neigh))
        g-score-neigh (get g-score neigh 9999)

        ;; discover new node
        open-set (if (not (open-set neigh))
                      (conj open-set neigh)
                      open-set)]

    ;; is it a better path?
    (if (< tentative-score g-score-neigh)
      (->state
       open-set
       closed-set
       (assoc came-from neigh current)
       (assoc g-score neigh tentative-score)
       (assoc f-score neigh (+ tentative-score
                               (distance-between neigh goal))))
      (assoc state
             :open-set open-set
             :closed-set closed-set
             ))))

(defn reduce-state-over-neighbors
  [state current goal [neighbour & neighbours]]
  (if (or neighbour neighbours)
    (if neighbour
      (-> state
          (A*-step current goal neighbour)
          (reduce-state-over-neighbors current goal neighbours))
      (reduce-state-over-neighbors current goal neighbours))
    state))

(defn neighbors-for [[x y]]
  (for [dx [-1 0 1] dy [-1 0 1]
        :when (not (and (zero? dx) (zero? dy)))]
    [(+ x dx) (+ y dy)]))

(defn state-step [{:keys [open-set closed-set] :as state} current goal]
  (-> state
      (assoc :open-set (disj open-set current))
      (assoc :closed-set (conj closed-set current))
      (reduce-state-over-neighbors current goal (neighbors-for current)))
  )

(println  (let [{:keys [f-score closed-set]}
                (-> (->state #{} #{[0 0]} {} {[0 0] 0} {[0 0] 30})
                    (state-step [0 0] [3 5])
                    (state-step [0 1] [3 5])
                    ;(state-step [0 2] [3 5])
                    )]
;            [f-score closed-set]
            (->> f-score
                 (filter (fn [[k v]] (not (closed-set k))))
                 (sort-by second)
                 first
                 first)
            ))




#_(defn A* [start goal]
  (loop [closed-set #{}
         open-set #{start}
         came-from {}
         g-score {start 0}
         f-score {start 30}
         ]
    (println "f-score" f-score)
    (let [current (first (first (sort-by second f-score)))]
      (println "current:" current)
      (if (= current goal)
        nil

        (let [new-open-set (disj open-set current)
              new-closed-set (conj closed-set current)
              neighbours (->> [neigbour (neighbours current)]

                              ;; remove neighbours already evaluated
                              (filter #(not (new-closed-set %))))]
          (loop [[neigh & t] neighbours
                 open-set new-open-set
                 close-set new-closed-set
                 came-from came-from
                 g-score g-score
                 f-score f-score]
            (A*-step neight open-set close-set game-from g-score f-score)

            (recur t))

          ))
      )

    )

  )

#_(A*-step [0 0] [20 20])
