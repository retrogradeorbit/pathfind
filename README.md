# pathfind

A pure clojure implementation of the A* algorithm.

## Overview

Supports an arbitrary 2D grid. Obstucted cells are seen by calling a
passed-in function `passable?` with a tuple containg the x, y
coordinate being tested. Along with this is passed a start and end
coordinate tuple. The function returns a sequence of positions.

Optionally a keyword can be added to the call `:cut-corners` to enable
the path finder to cut directly across diagonal obstacles. Default is
to not cut corners.

## Example

```clojure
(require '[pathfind.core :as pf]
	     '[cljs.test :refer-macros [is]])

(let [passable? (fn [pos]
                    (-> pos
					    #{[3 3] [3 4] [4 4] [4 3]}
						boolean not))]
    (is (= (pf/A* passable? [0 0] [10 5])
           '([0 0] [1 1] [2 2]
             [3 2] [4 2] [5 2]
             [6 3] [7 4] [8 5]
             [9 5] [10 5]))))
```

## Tests

```
lein cljsbuild test
```

## Setup

To get an interactive development environment run:

    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:

    lein clean

To create a production build run:

    lein do clean, cljsbuild once min

And open your browser in `resources/public/index.html`. You will not
get live reloading, nor a REPL.

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
