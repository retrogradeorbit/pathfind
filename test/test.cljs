(ns pathfind.test
  (:require [cljs.test :refer-macros [run-all-tests]]
            [pathfind.core-test]
))

(enable-console-print!)

(defn ^:export run
  []
  (run-all-tests #"pathfind.*-test"))
