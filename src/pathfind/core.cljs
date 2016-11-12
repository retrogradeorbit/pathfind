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

(defn A* [start goal]
  (let [closed-set #{}
        open-set #{start}
        came-from {}
        g-score {start 0}
        f-score {start (manhattan start goal)}
        ]
    )

)
