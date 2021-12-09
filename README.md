# cljs-math
A ClojureScript port of [`clojure.java.math`](https://clojure.github.io/clojure/branch-master/clojure.java.math-api.html)

This can be included in `deps.edn` by adding the following entry to the `:deps` map:
```
com.github.quoll/cljs-math {:git/tag "v0.1.1" :git/sha "eef5831"}
```

The prime focus on this library is correctness before performance. It matches `clojure.java.math` as closely as possible.

## Testing
Tests are run on both the JVM and through a connection to ClojureScript running on Node. For this reason, the file is `.cljc` rather than `.cljs`.

Testing performs generative tests, directly comparing the results of this implementation and the implementations in `java.lang.Math`. Fortunately, the floating point representation on both platforms conforms to IEEE-754, which validates this comparison.

Only the JavaScript Math.sin() function is compared to the equivalent Java function, to check that built-in functions have been wrapped the same way. Some built-in functions JavaScript functions have slightly less precision than the equivalent Java function, so the variance between differing answers will be displayed. 

To run the tests entirely in Java, run:
```bash
clj -X:test
```

## Future
My hope is that this will be accepted by ClojureScript. If it is, then the Java tests may be dropped. This would allow the primary file to be renamed to `.cljs` and the namespace updated to `clojure.math`.

# License
Copyright © 2021 Paula Gearon

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
