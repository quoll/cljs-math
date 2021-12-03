(ns cljs.jmath-test
  (:require [cljs.math :as m]
            [clojure.test :refer [deftest is testing run-tests]]))

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
  (testing "The JS remainder function"
    (let [a (m/IEEE-fmod 5.2 2.3)
          b (m/IEEE-remainder 339794.000868 1.69897000343)
          c (m/IEEE-remainder -339794.000868 1.69897000343)]
      (is (= 0.60000000000000053291 a))
      (is (= "3fe3333333333338" (buffer-str a)))
      (is (= 0.00018199998751811108 b))
      (is (= "3f27dae7fd100000" (buffer-str b)))
      (is (= -0.00018199998751811108 c))
      (is (= "bf27dae7fd100000" (buffer-str c))))))

