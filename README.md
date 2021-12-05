# cljs-math
A ClojureScript port of [clojure.java.math](https://clojure.github.io/clojure/branch-master/clojure.java.math-api.html)

This can be included in `deps.edn` by adding the following entry to the `:deps` map:
```
com.github.quoll/cljs-math {:git/tag "v0.0.1" :git/sha "f92217e"}
```

## Testing
Testing directly in ClojureScript is minimal. Checking for correctness has been done by stubbing out some js operations in Clojure, and running tests on the JVM. For this reason, the file is `.cljc` rather than `.cljs`.

Testing performs generative tests, directly comparing the results of this implementation and the implementations in `java.lang.Math`. Fortunately, the floating point representation on both platforms conforms to IEEE-754, which validates this comparison.

## Future
My hope is that this will be accepted by ClojureScript. If it is, then the primary file should be renamed to `.cljs` and the namespace updated to `clojure.math`.

## m2 Namespace
During development I also implemented one of the more complex functions in embedded JS code. I have left this in place temporarily, as a point of comparison.

# License
Copyright © 2021 Paula Gearon

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
