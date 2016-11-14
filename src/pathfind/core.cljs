(ns pathfind.core
  (:require ))

(enable-console-print!)

(println "This text is printed from src/pathfind/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hello world!"}))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

(defrecord node [x y walk?])

(defn manhattan [x y] (+ x y))

(defn chebyshev [x y] (max x y))

(defn euclid [x y] (.sqrt js/Math (+ (* x x) (* y y))))

(defn neigbours [x y]
  ;; return a sequence of walkable neighbours to a given co-ordinate
  )



(defn distance-between [[x1 y1] [x2 y2]]
  (manhattan
   (Math/abs (- x1 x2))
   (Math/abs (- y1 y2))))


(defrecord state [closed-set open-set came-from g-score f-score])

(defn A*-step [{:keys [closed-set open-set came-from g-score f-score] :as state} current goal neigh]
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
      (assoc state :open-set open-set))))

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

(println (-> (->state #{} #{[0 0]} {} {[0 0] 0} {[0 0] 30})
             (reduce-state-over-neighbors [0 0] [3 5]
                                          (neighbors-for [0 0]))
             :f-score
             (->> (sort-by second)
                  first
                  first)
             ))

(println (-> (->state #{} #{[0 0]} {} {[0 0] 0} {[0 0] 30})
             (reduce-state-over-neighbors [0 0] [3 5]
                                          (neighbors-for [0 0]))

             (reduce-state-over-neighbors [0 1] [3 5]
                                          (neighbors-for [0 1]))
             :f-score
             (->> (sort-by second)
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
