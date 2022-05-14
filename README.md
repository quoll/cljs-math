# cljs-math
A ClojureScript port of [`clojure.math`](https://clojure.github.io/clojure/branch-master/clojure.math-api.html)

# Notice
This code is now merged (along with some improvements) into ClojureScript. It first appeared in the [ClojureScript 1.11.51 release](https://clojurescript.org/news/2022-05-13-release).

This code remains as a historical reference.

## Obsolete
This can be included in `deps.edn` by adding the following entry to the `:deps` map:
```
com.github.quoll/cljs-math {:git/tag "v0.1.3" :git/sha "39ad53c"}
```

The prime focus on this library is correctness before performance. It matches `clojure.math` as closely as possible.

## Testing
Tests are run on both the JVM and through a connection to ClojureScript running on Node. For this reason, the file is `.cljc` rather than `.cljs`.

Testing performs generative tests, directly comparing the results of this implementation and the implementations in `java.lang.Math`. Fortunately, the floating point representation on both platforms conforms to IEEE-754, which validates this comparison.

Only the JavaScript `Math.sin()` function is compared to the equivalent Java function, to check that built-in functions have been wrapped the same way. Some built-in functions JavaScript functions have slightly less precision than the equivalent Java function, so the variance between differing answers will be displayed. 

To run the tests entirely in Java, run:
```bash
clj -X:test
```

# License
Copyright © 2021-2022 Paula Gearon

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
