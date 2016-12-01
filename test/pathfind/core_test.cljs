(ns pathfind.core-test
  (:require [cljs.test :refer-macros [deftest is]]
            [pathfind.core :as core]))

(deftest manhattan-test
  (is (= (core/manhattan 0 0) 0))
  (is (= (core/manhattan 1 4) 5))
  (is (= (core/manhattan 1 -4) 5))
  (is (= (core/manhattan -1 4) 5))
  (is (= (core/manhattan -1 -4) 5)))

(deftest chebyshev-test
  (is (= (core/chebyshev 0 0) 0))
  (is (= (core/chebyshev 1 4) 4))
  (is (= (core/chebyshev 1 -4) 4))
  (is (= (core/chebyshev -1 4) 4))
  (is (= (core/chebyshev -1 -4) 4)))

(deftest euclid-test
  (is (= (core/euclid 0 0) 0))
  (is (= (core/euclid 3 4) 5))
  (is (= (core/euclid 3 -4) 5))
  (is (= (core/euclid -3 4) 5))
  (is (= (core/euclid -3 -4) 5)))

(deftest distance-between-test
  (is (= (core/distance-between core/manhattan [0 0] [-3 4]) 7))
  (is (= (core/distance-between core/chebyshev [0 0] [-3 4]) 4))
  (is (= (core/distance-between core/euclid    [0 0] [-3 4]) 5)))

(deftest state-add-neighbour-test
  (is (=
       (core/state-add-neighbour
        (core/->state #{} #{} {} {} {})
        [0 0]
        [1 1])
       (core/->state #{} #{[1 1]} {[1 1] [0 0]} {} {}))))

(deftest state-add-open-test
  (is (=
       (core/state-add-open
        (core/->state #{} #{} {} {} {})
        [0 0])
       (core/->state #{} #{[0 0]} {} {} {}))))

(deftest state-open-to-closed-test
  (is (=
       (core/state-open-to-closed
        (core/->state #{} #{[0 0]} {} {} {})
        [0 0])
       (core/->state #{[0 0]} #{} {} {} {}))))

(deftest reduce-state-over-neighbours
  (is (=
       (core/reduce-state-over-neighbours
        (core/->state #{} #{[0 0]} {} {} {})
        [0 0]
        [[0 1] [0 -1] [1 0] [-1 0] [1 1] [-1 1] [1 -1] [-1 -1]])
       (core/->state
        #{}
        #{[0 0] [0 1] [0 -1] [1 0] [-1 0] [1 1] [-1 1] [1 -1] [-1 -1]}
        {[0 1] [0 0]
         [0 -1] [0 0]
         [1 0] [0 0]
         [-1 0] [0 0]
         [1 1] [0 0]
         [-1 1] [0 0]
         [1 -1] [0 0]
         [-1 -1] [0 0]
         }
        {} {}))))

(deftest calculate-open-fscore-test
  (let [{:keys [g-score f-score]}
        (core/calculate-open-fscore
         (core/->state
          #{} #{[0 1] [0 0] [-1 1] [1 1] [1 -1] [1 0] [-1 0] [-1 -1] [0 -1]}
          {[0 1] [0 0], [0 -1] [0 0], [1 0] [0 0], [-1 0] [0 0], [1 1] [0 0], [-1 1] [0 0], [1 -1] [0 0], [-1 -1] [0 0]}
          {}, {})
         [0 1] [10 10])]
    (is (= g-score
           {[0 1] 0
            [0 0] 1
            [-1 1] 1
            [1 1] 1
            [1 -1] 3
            [1 0] 2
            [-1 0] 2
            [-1 -1] 3
            [0 -1] 2}))
    (is (= f-score
           {[0 1] 19
            [0 0] 21
            [-1 1] 21
            [1 1] 19
            [1 -1] 23
            [1 0] 21
            [-1 0] 23
            [-1 -1] 25
            [0 -1] 23}))))

(deftest lowest-f-score-open-cell-test
  (is
   (#{[0 1] [1 1]}
    (->
     (core/->state
      #{} #{[0 1] [0 0] [-1 1] [1 1] [1 -1] [1 0] [-1 0] [-1 -1] [0 -1]}
      {[0 1] [0 0], [0 -1] [0 0], [1 0] [0 0], [-1 0] [0 0], [1 1] [0 0], [-1 1] [0 0], [1 -1] [0 0], [-1 -1] [0 0]}
      {}, {})
     (core/calculate-open-fscore [0 1] [10 10])
     (core/lowest-f-score-open-cell)))))

(deftest A*-step-test
  (let [start [0 0]
        end [10 10]
        [{:keys [open-set came-from g-score] :as state} next-cell]
        (-> (core/->state #{} #{start} {} {start 0} {start 10})
            (core/A*-step start end start))]
    (is (= open-set #{[-1 -1] [0 -1] [1 -1] [-1 0] [1 0] [-1 1] [0 1] [1 1]}))
    (is (= came-from
           {
            [-1 -1] [0 0]
            [0 -1] [0 0]
            [1 -1] [0 0]
            [-1 0] [0 0]
            [1 0] [0 0]
            [-1 1] [0 0]
            [0 1] [0 0]
            [1 1] [0 0]
            }))
    (is (= g-score
           {[0 -1] 10
            [-1 0] 10
            [0 1] 10
            [1 0] 10
            [-1 -1] 14
            [-1 1] 14
            [1 -1] 14
            [1 1] 14
            [0 0] 0
            }))

    ))
