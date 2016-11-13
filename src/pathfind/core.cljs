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

(defn A* [start goal]
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
              new-closed-set (conj closed-set current)]
          (println new-closed-set new-open-set)
          (->> [neigbour (neighbours current)]
               (filter #(not (new-closed-set %)))

               ;; distance from start to a neighbour



               )
          ))
      )

    )

)

(A* [0 0] [20 20])
