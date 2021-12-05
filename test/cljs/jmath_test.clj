(ns cljs.jmath-test
  (:require [cljs.math :as m]
            [clojure.test :refer [deftest is testing run-tests]]
            [clojure.test.check :as tc]
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

(defn d= [a b] (or (= a b) (and (Double/isNaN a) (Double/isNaN b))))

(defspec sin-test 100
  (prop/for-all [v gen/double]
    (d= (m/sin v) (Math/sin v))))


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

(defspec round-test 1000
  (prop/for-all [a gen/double]
    (= (m/round a) (Math/round a))))

(defspec floor-div-test 1000
  (prop/for-all [x gen/large-integer y (gen/such-that #(not= % 0) gen/large-integer)]
    (= (m/floor-div x y) (Math/floorDiv x y))))

(defspec floor-mod-test 1000
  (prop/for-all [x gen/large-integer y (gen/such-that #(not= % 0) gen/large-integer)]
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

;; Want to generate the full range of Integer. |Integer/MIN_VALUE| = 1+Integer/MAX_VALUE
(defspec scalb-test 1000
  (prop/for-all [d gen/double scale-factor (gen/such-that #(<= % Integer/MAX_VALUE)
                                                          (gen/resize (inc Integer/MAX_VALUE) gen/small-integer))]
    (d= (m/scalb d scale-factor) (Math/scalb d scale-factor))))
