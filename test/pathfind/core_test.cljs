(ns pathfind.core-test
  (:require [cljs.test :refer-macros [deftest is]]
            [pathfind.core :as core]))


(deftest A*-step
  (let [start [0 0]
        result (core/A*-step
                (core/->state #{} #{} {} {} {})
                [0 0] [10 10] [0 1])
        {:keys [closed-set open-set came-from g-score f-score]} result]
    (println result)

    ))

(deftest reduce-state
  (let [start [0 0]
        result (core/reduce-state-over-neighbors
                (core/->state #{} #{} {} {} {})
                [0 0] [10 10] (core/neighbors-for [0 0]))
        {:keys [closed-set open-set came-from g-score f-score]} result]
    ; (println result)
    (is (= came-from
           {[-1 -1] [0 0]
            [-1 0]  [0 0]
            [-1 1]  [0 0]
            [0 -1]  [0 0]
            [0 1]   [0 0]
            [1 -1]  [0 0]
            [1 0]   [0 0]
            [1 1]   [0 0]}))
    (is (= open-set
           #{[-1 -1]
             [-1 0]
             [-1 1]
             [0 -1]
             [0 1]
             [1 -1]
             [1 0]
             [1 1]}))
    (is (= closed-set
           #{[0 0]}))
    ))
