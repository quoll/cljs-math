(ns ^{:doc "Tests cljs-math to compare between JVM provided functions and the
      cljs-math implementations running on a JVM. JS constants and functions
      are mocked out in the 'js' namespace."
      :author "Paula Gearon"}
    cljs.math.jvm-test
    (:require [cljs.math :as m]
              [clojure.test :refer [deftest is testing run-tests]]
              [clojure.test.check.clojure-test :refer [defspec]]
              [clojure.test.check.generators :as gen]
              [clojure.test.check.properties :as prop])
    (:import [clojure.lang ExceptionInfo]))

(defn hex2
  [n]
  (let [s (.toString n 16)]
    (if (< (count s) 2) (str "0" s) s)))

(defn buffer-str
  [x]
  (Long/toHexString (Double/doubleToRawLongBits x)))

;; start with some simple function calls

(deftest test-fmod
  (testing "The fmod function"
    (let [a (m/IEEE-fmod 5.2 2.3)
          b (m/IEEE-fmod 339794.000868 1.69897000343)
          c (m/IEEE-fmod -339794.000868 1.69897000343)]
      (is (= 0.60000000000000053291 a))
      (is (= "3fe3333333333338" (buffer-str a)))
      (is (= 0.00018199998751811108 b))
      (is (= "3f27dae7fd100000" (buffer-str b)))
      (is (= -0.00018199998751811108 c))
      (is (= "bf27dae7fd100000" (buffer-str c))))))

(deftest test-remainder
  (testing "The remainder function"
    (let [a (m/IEEE-fmod 5.2 2.3)
          b (m/IEEE-remainder 339794.000868 1.69897000343)
          c (m/IEEE-remainder -339794.000868 1.69897000343)]
      (is (= 0.60000000000000053291 a))
      (is (= "3fe3333333333338" (buffer-str a)))
      (is (= 0.00018199998751811108 b))
      (is (= "3f27dae7fd100000" (buffer-str b)))
      (is (= -0.00018199998751811108 c))
      (is (= "bf27dae7fd100000" (buffer-str c))))))

;; Generative tests on all functions

(defn d= [a b] (or (= a b) (and (Double/isNaN a) (Double/isNaN b))))

(def safe-integer (gen/choose js/Number.MIN_SAFE_INTEGER js/Number.MAX_SAFE_INTEGER))

;; The following test functions that are wrappers for functionality provided in Math
(defspec sin-test 100
  (prop/for-all [v gen/double]
    (d= (m/sin v) (Math/sin v))))

(defspec cos-test 100
  (prop/for-all [v gen/double]
    (d= (m/cos v) (Math/cos v))))

(defspec tan-test 100
  (prop/for-all [v gen/double]
    (d= (m/tan v) (Math/tan v))))

(defspec asin-test 100
  (prop/for-all [v gen/double]
    (d= (m/asin v) (Math/asin v))))

(defspec acos-test 100
  (prop/for-all [v gen/double]
    (d= (m/acos v) (Math/acos v))))

(defspec atan-test 100
  (prop/for-all [v gen/double]
    (d= (m/atan v) (Math/atan v))))

(defspec exp-test 100
  (prop/for-all [v gen/double]
    (d= (m/exp v) (Math/exp v))))

(defspec log-test 100
  (prop/for-all [v gen/double]
    (d= (m/log v) (Math/log v))))

(defspec log10-test 100
  (prop/for-all [v gen/double]
    (d= (m/log10 v) (Math/log10 v))))

(defspec sqrt-test 100
  (prop/for-all [v gen/double]
    (d= (m/sqrt v) (Math/sqrt v))))

(defspec cbrt-test 100
  (prop/for-all [v gen/double]
    (d= (m/cbrt v) (Math/cbrt v))))

(defspec atan2-test 100
  (prop/for-all [y gen/double x gen/double]
    (d= (m/atan2 y x) (Math/atan2 y x))))

(defspec pow-test 100
  (prop/for-all [a gen/double b gen/double]
    (d= (m/pow a b) (Math/pow a b))))

(defspec sinh-test 100
  (prop/for-all [v gen/double]
    (d= (m/sinh v) (Math/sinh v))))

(defspec cosh-test 100
  (prop/for-all [v gen/double]
    (d= (m/cosh v) (Math/cosh v))))

(defspec tanh-test 100
  (prop/for-all [v gen/double]
    (d= (m/tanh v) (Math/tanh v))))

(defspec hypot-test 100
  (prop/for-all [x gen/double y gen/double]
    (d= (m/hypot x y) (Math/hypot x y))))

(defspec expm1-test 100
  (prop/for-all [v gen/double]
    (d= (m/expm1 v) (Math/expm1 v))))

(defspec log1p-test 100
  (prop/for-all [v gen/double]
    (d= (m/log1p v) (Math/log1p v))))

;; The following test functions that have been implemented by cljs-math
(defspec to-radians-test 1000
  (prop/for-all [v gen/double]
    (d= (m/to-radians v) (Math/toRadians v))))

(defspec to-degrees-test 1000
  (prop/for-all [v gen/double]
    (d= (m/to-degrees v) (Math/toDegrees v))))

(defspec ieee-remainder-test 1000
  (prop/for-all [dividend gen/double divisor gen/double]
    (d= (m/IEEE-remainder dividend divisor) (Math/IEEEremainder dividend divisor))))

(defspec ceil-test 1000
  (prop/for-all [v gen/double]
    (d= (m/ceil v) (Math/ceil v))))

(deftest ceil-null-test
  (is (thrown? Exception (Math/ceil nil)))
  (is (thrown? ExceptionInfo (m/ceil nil))))

(defspec floor-test 1000
  (prop/for-all [v gen/double]
    (d= (m/floor v) (Math/floor v))))

(deftest floor-null-test
  (is (thrown? Exception (Math/floor nil)))
  (is (thrown? ExceptionInfo (m/floor nil))))

(defspec copy-sign-test 1000
  (prop/for-all [magnitude gen/double sign gen/double]
    (d= (m/copy-sign magnitude sign) (Math/copySign magnitude sign))))

(defspec rint-test 1000
  (prop/for-all [a gen/double]
    (d= (m/rint a) (Math/rint a))))

(defn max=
  [a b]
  (or (= a b)
      (and (= a js/Number.MAX_SAFE_INTEGER) (= b Long/MAX_VALUE))
      (and (= a js/Number.MIN_SAFE_INTEGER) (= b Long/MIN_VALUE))))

(defspec round-test 1000
  (prop/for-all [a gen/double]
    (max= (m/round a) (Math/round a))))

;; utililties for the -exact tests
(defn no-overflow?
  [f ^long x ^long y]
  (try
    (js/Number.isSafeInteger (f x y))
    (catch ArithmeticException _ false)))

(defmacro throws?
  [expr]
  `(try ~expr false (catch Exception _# true)))

(defspec add-exact 1000
  (prop/for-all [[x y] (gen/tuple safe-integer safe-integer)]
    (if (no-overflow? + x y)
      (= (m/add-exact x y) (Math/addExact ^long x ^long y))
      (throws? (m/add-exact x y)))))

(defspec subtract-exact 1000
  (prop/for-all [[x y] (gen/tuple safe-integer safe-integer)]
    (if (no-overflow? - x y)
      (= (m/subtract-exact x y) (Math/subtractExact ^long x ^long y))
      (throws? (m/subtract-exact x y)))))

(defspec multiply-exact 1000
  (prop/for-all [[x y] (gen/tuple safe-integer safe-integer)]
    (if (no-overflow? * x y)
      (= (m/multiply-exact x y) (Math/multiplyExact ^long x ^long y))
      (throws? (m/multiply-exact x y)))))

(defspec increment-exact 1000
  (prop/for-all [x safe-integer]
    (if (no-overflow? + x 1)
      (= (m/increment-exact x) (Math/incrementExact ^long x))
      (throws? (m/increment-exact x)))))

(defspec decrement-exact 1000
  (prop/for-all [x safe-integer]
    (if (no-overflow? - x 1)
      (= (m/decrement-exact x) (Math/decrementExact ^long x))
      (throws? (m/decrement-exact x)))))

(defspec floor-div-test 1000
  (prop/for-all [x safe-integer y (gen/such-that #(not= % 0) safe-integer)]
    (= (m/floor-div x y) (Math/floorDiv ^long x ^long y))))

(defspec floor-mod-test 1000
  (prop/for-all [x safe-integer y (gen/such-that #(not= % 0) safe-integer)]
    (= (m/floor-mod x y) (Math/floorMod ^long x ^long y))))  ;; type hints to avoid CLJ-2674

(defspec get-exponent-test 1000
  (prop/for-all [d gen/double]
    (= (m/get-exponent d) (Math/getExponent d))))

(defspec ulp-test 1000
  (prop/for-all [d gen/double]
    (d= (m/ulp d) (Math/ulp d))))

(defspec signum-test 1000
  (prop/for-all [d gen/double]
    (d= (m/signum d) (Math/signum d))))

(defspec next-after-test 1000
  (prop/for-all [start gen/double direction gen/double]
    (d= (m/next-after start direction) (Math/nextAfter start direction))))

(defspec next-up-test 1000
  (prop/for-all [d gen/double]
    (d= (m/next-up d) (Math/nextUp d))))

(defspec next-down-test 1000
  (prop/for-all [d gen/double]
    (d= (m/next-down d) (Math/nextDown d))))

(defspec scalb-test 1000
  (prop/for-all [d gen/double scale-factor (gen/choose Integer/MIN_VALUE Integer/MAX_VALUE)]
    (d= (m/scalb d scale-factor) (Math/scalb d scale-factor))))
