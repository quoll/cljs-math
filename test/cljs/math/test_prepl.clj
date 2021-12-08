(ns cljs.math.test-prepl
  (:require [cljs.core.server]
            [cljs.repl.node]
            [clojure.core.server :as server]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.test :as t :refer [deftest is]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [js]))

;; run the tests with clojure -M:test -n cljs.math.test-prepl

(def reader (atom nil))
(def writer (atom nil))

(defn cljs-eval [expr]
  (-> (binding [*out* @writer
                *in* @reader]
        (println expr)
        (read-line))
      edn/read-string
      :val))

(t/use-fixtures :once
  (fn [f]
    (println "Launching test pREPL.")
    (let [server (server/start-server {:accept 'cljs.core.server/io-prepl
                                       :address "127.0.0.1"
                                       :port 0
                                       :name "cljs.math-repl"
                                       :args [:repl-env (cljs.repl.node/repl-env)]})
          port (-> server (.getLocalPort))]
      (println "Server opened on port" port)
      (with-open [socket (java.net.Socket. "127.0.0.1" port)
                  rdr (io/reader socket)
                  wrtr (io/writer socket)]
        (reset! reader rdr)
        (reset! writer wrtr)
        (println "Executing tests")
        (cljs-eval "(require 'cljs.math)")
        (f)
        (println "Tearing down test pREPL.")))))

(deftest sanity-test
  (is (= "6" (cljs-eval "(+ 1 2 3)"))))

(deftest cljs-match-sanity-test
  (is (= "42" (cljs-eval "(cljs.math/abs -42)"))))

(defn n==
  [a b]
  (or (and (Double/isNaN a) (Double/isNaN b)) (== a b)))

(defn maxi==
  [a b]
  (or (and (Double/isNaN a) (Double/isNaN b))
      (and (== a js/Number.MAX_SAFE_INTEGER) (== b Long/MAX_VALUE))
      (and (== a js/Number.MIN_SAFE_INTEGER) (== b Long/MIN_VALUE))
      (== a b)))

(defmacro test-t->t
  [n jfn cfn gen & [equals]]
  (let [jmfn (symbol "Math" (str jfn))
        cmfn (name cfn)
        eq (or equals n==)]
    `(let [ds# (gen/sample ~gen ~n)]
       (is (every? identity
            (map ~eq
                 (read-string
                  (cljs-eval (str "(->> '" (pr-str ds#)
                                  " (map double)"
                                  " (map cljs.math/" ~cmfn "))")))
                 (map #(~jmfn %) ds#)))
           (str "data: " (pr-str ds#))))))

(defmacro test-double->double
  [n jfn cfn & [equals]]
  `(test-t->t ~n ~jfn ~cfn gen/double ~equals))

(defmacro test-t-t->double
  [n jfn cfn gen1 gen2 & [equals]]
  (let [jmfn (symbol "Math" (str jfn))
        cmfn (name cfn)
        eq (or equals n==)]
    `(let [ds# (gen/sample ~gen1 ~n)
           ds2# (gen/sample ~gen2 ~n)]
       (is (every? identity
            (map ~eq
                 (read-string
                  (cljs-eval (str "(->> (map #(vector %1 %2) '"
                                  (pr-str ds#) " '" (pr-str ds2#) ")"
                                  " (map #(apply cljs.math/" ~cmfn " %)))")))
                 (map #(~jmfn %1 %2) ds# ds2#)))
           (str "data: " (pr-str (map vector ds# ds2#)))))))

(defmacro test-double-double->double
  [n jfn cfn & [equals]]
  `(test-t-t->double ~n ~jfn ~cfn gen/double gen/double ~equals))

(def safe-integer (gen/sized (fn [_] (gen/choose js/Number.MIN_SAFE_INTEGER js/Number.MAX_SAFE_INTEGER))))

(defmacro test-zlong-long->long
  [n jfn cfn]
  (let [jmfn (symbol "Math" (str jfn))
        cmfn (name cfn)]
    `(let [lzs# (gen/sample safe-integer ~n)
           ls# (gen/sample (gen/such-that #(not= % 0) safe-integer) ~n)]
       (is (every? identity
            (map ==
                 (read-string
                  (cljs-eval (str "(->> (map #(vector (long %1) (long %2)) '"
                                  (pr-str lzs#) " '" (pr-str ls#) ")"
                                  " (map #(apply cljs.math/" ~cmfn " %)))")))
                 (map #(~jmfn (long %1) (long %2)) lzs# ls#)))
           (str "data: " (pr-str (map vector lzs# ls#)))))))

(def ^:const delta 1E-15)

(defn nd==
  [label a b]
  (or (and (Double/isNaN a) (Double/isNaN b))
      (== a b)
      (do
        (println label "variance:" a "\u2260" b)
        (< (Math/abs (- a b)) delta))))

(deftest sin-test
  (test-double->double 100 sin sin #(nd== "sin()" %1 %2)))

(deftest to-radians-test
  (test-double->double 100 toRadians to-radians))

(deftest to-degrees-test
  (test-double->double 100 toDegrees to-degrees))

;; -1.14721315848192E14 -1.237275875410596E-17
;; 5.503575055203991E37 -4.62739269375E8
;; 2.967419634324934E69 -3.4540078290247674E-27
;; -4.1456352331463285E32 -1.982925677012589E-72
;; 1.2749085103512E-68 1.5437296986151283E-88
;; -9.768412744064235E-26 1.8519927751293727E-141
;; -7.427763013341889E147 -9.035587260971381E116
;; -5.8180201806299136E16 -1.3399554637510478E-9
;; 1.0474131290782905E105 1.0101217083682073E-112
;; 1.4225760000554146 7.52260372476889E-294
;; 1.4225760000554146 7.52260372476889E-294
(deftest ieee-remainder-test
  (test-double-double->double 100 IEEEremainder IEEE-remainder))

(deftest ceil-test
  (test-double->double 100 ceil ceil))

(deftest ceil-null-test
  (is (= ":exception" (cljs-eval (str "(try (cljs.math/ceil nil) (catch :default _ :exception))")))))

(deftest floor-test
  (test-double->double 100 floor floor))

(deftest floor-null-test
  (is (= ":exception" (cljs-eval (str "(try (cljs.math/floor nil) (catch :default _ :exception))")))))

(deftest copy-sign-test
  (test-double-double->double 100 copySign copy-sign))

(deftest rint-test
  (test-double->double 100 rint rint))

(deftest round-test
  (test-t->t 100 round round (gen/double* {:min js/Number.MIN_SAFE_INTEGER :max js/Number.MAX_SAFE_INTEGER}) maxi==))

(deftest floor-div-test
  (test-zlong-long->long 100 floorDiv floor-div))

(deftest floor-mod-test
  (test-zlong-long->long 100 floorMod floor-mod))

(deftest get-exponent-test
  (test-double->double 100 getExponent get-exponent))

(deftest ulp-test
  (test-double->double 100 ulp ulp))

(deftest signum-test
  (test-double->double 100 signum signum))

(deftest next-after-test
  (test-double-double->double 100 nextAfter next-after))

(deftest next-up-test
  (test-double->double 100 nextUp next-up))

(deftest next-down-test
  (test-double->double 100 nextDown next-down))

(def ^:const MAX-INT 0x7fffffff)

(deftest scalb-test
  (test-t-t->double 100 scalb scalb
                    gen/double
                    (gen/such-that
                     #(<= % MAX-INT)
                     (gen/resize (inc MAX-INT) gen/small-integer))))
