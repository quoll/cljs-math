(ns ^{:doc "ClojureScript wrapper functions for math operations"
      :author "Paula Gearon" }
  cljs.math)

(def
  ^{:doc "Constant for Euler's number e, the base for natural logarithms.
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/E"
    :added "1.10.892"
    :const true} E Math/E)

(def
  ^{:doc "Constant for pi, the ratio of the circumference of a circle to its diameter.
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/PI"
    :added "1.10.892"
    :const true} PI Math/PI)

(def
  ^{:doc "Constant used to convert an angular value in degrees to the equivalent in radians"
    :private true
    :added "1.10.892"
    :const true} DEGREES_TO_RADIANS 0.017453292519943295)

(def
  ^{:doc "Constant used to convert an angular value in radians to the equivalent in degrees"
    :private true
    :added "1.10.892"
    :const true} RADIANS_TO_DEGREES 57.29577951308232)


(defn sin
  {:doc "Returns the sine of an angle.
  If a is ##NaN, ##-Inf, ##Inf => ##NaN
  If a is zero => zero with the same sign as a
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/sin"
   :added "1.10.892"}
  [a] (Math/sin a))

(defn cos
  {:doc "Returns the cosine of an angle.
  If a is ##NaN, ##-Inf, ##Inf => ##NaN
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/cos"
   :added "1.10.892"}
  [a] (Math/cos a))

(defn tan
  {:doc "Returns the tangent of an angle.
  If a is ##NaN, ##-Inf, ##Inf => ##NaN
  If a is zero => zero with the same sign as a
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/tan"
   :added "1.10.892"}
  [a] (Math/tan a))

(defn asin
  {:doc "Returns the arc sine of an angle, in the range -pi/2 to pi/2.
  If a is ##NaN or |a|>1 => ##NaN
  If a is zero => zero with the same sign as a
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/asin"
   :added "1.10.892"}
  [a] (Math/asin a))

(defn acos
  {:doc "Returns the arc cosine of a, in the range 0.0 to pi.
  If a is ##NaN or |a|>1 => ##NaN
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/acos"
   :added "1.10.892"}
  [a] (Math/acos a))

(defn atan
  {:doc "Returns the arc tangent of a, in the range of -pi/2 to pi/2.
  If a is ##NaN => ##NaN
  If a is zero => zero with the same sign as a
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/atan"
   :added "1.10.892"}
  [a] (Math/atan a))

(defn to-radians
  {:doc "Converts an angle in degrees to an approximate equivalent angle in radians.
  See: https://docs.oracle.com/javase/8/docs/api/java/lang/Math.html#toRadians-double-"
   :added "1.10.892"}
  [deg]
  (* deg DEGREES_TO_RADIANS))

(defn to-degrees
  {:doc "Converts an angle in radians to an approximate equivalent angle in degrees.
  See: https://docs.oracle.com/javase/8/docs/api/java/lang/Math.html#toDegrees-double-"
   :added "1.10.892"}
  [r]
  (* r RADIANS_TO_DEGREES))

(defn exp
  {:doc "Returns Euler's number e raised to the power of a.
  If a is ##NaN => ##NaN
  If a is ##Inf => ##Inf
  If a is ##-Inf => +0.0
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/exp"
   :added "1.10.892"}
  [a] (Math/exp a))

(defn log
  {:doc "Returns the natural logarithm (base e) of a.
  If a is ##NaN or negative => ##NaN
  If a is ##Inf => ##Inf
  If a is zero => ##-Inf
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/log"
   :inline-arities #{1}
   :inline (fn [a] `(Math/log ~a))
   :added "1.10.892"}
  ^double [^double a]
  (Math/log a))

(defn log10
  {:doc "Returns the logarithm (base 10) of a.
  If a is ##NaN or negative => ##NaN
  If a is ##Inf => ##Inf
  If a is zero => ##-Inf
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/log10"
   :added "1.10.892"}
  [a] (Math/log10 a))

(defn sqrt
  {:doc "Returns the positive square root of a.
  If a is ##NaN or negative => ##NaN
  If a is ##Inf => ##Inf
  If a is zero => a
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/sqrt"
   :added "1.10.892"}
  [a] (Math/sqrt a))

(defn cbrt
  {:doc "Returns the cube root of a.
  If a is ##NaN => ##NaN
  If a is ##Inf or ##-Inf => a
  If a is zero => zero with sign matching a
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/cbrt"
   :added "1.10.892"}
  [a] (Math/cbrt a))

(defn ceil
  {:doc "Returns the smallest double greater than or equal to a, and equal to a
  mathematical integer.
  If a is ##NaN or ##Inf or ##-Inf or already equal to an integer => a
  Note that if a is `nil` then an exception will be thrown. This matches Clojure, rather than js/Math.ceil
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/ceil"
   :added "1.10.892"}
  [a]
  (if a
    (Math/ceil a)
    (throw (ex-info "Unexpected Null passed to ceil" {:fn "ceil"}))))

(defn floor
  {:doc "Returns the largest double less than or equal to a, and equal to a
  mathematical integer.
  If a is ##NaN or ##Inf or ##-Inf or already equal to an integer => a
  If a is less than zero but greater than -1.0 => -0.0
  Note that if a is `nil` then an exception will be thrown. This matches Clojure, rather than js/Math.floor
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/floor"
   :added "1.10.892"}
  [a]
  (if a
    (Math/floor a)
    (throw (ex-info "Unexpected Null passed to floor" {:fn "floor"}))))

(defn atan2
  {:doc "Returns the angle theta from the conversion of rectangular coordinates (x, y) to polar coordinates (r, theta).
  Computes the phase theta by computing an arc tangent of y/x in the range of -pi to pi.
  For more details on special cases, see:
  https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/atan"
   :added "1.10.892"}
  [y x] (Math/atan2 y x))

(defn pow
  {:doc "Returns the value of a raised to the power of b.
  For more details on special cases, see:
  https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/pow"
   :added "1.10.892"}
  [a b] (Math/pow a b))

(defn round
  {:doc "Returns the closest long to a. If equally close to two values, return the one
  closer to ##Inf.
  If a is ##NaN => 0
  If a is ##-Inf => ##-Inf
  If a is ##Inf => ##Inf
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/round"
   :added "1.10.892"}
  [a]
  (if (js/Number.isNaN a)
    0
    (Math/round a)))

(defn random
  {:doc "Returns a positive double between 0.0 and 1.0, chosen pseudorandomly with
  approximately random distribution. Not cryptographically secure. The seed is chosen internally
  and cannot be selected.
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/random"
   :added "1.10.892"}
  [] (Math/random))

(defn add-exact
  {:doc "Returns the sum of x and y, throws an exception on overflow. "
   :added "1.10.892"}
  [x y]
  (let [r (+ x y)]
    (if (or (> r js/Number.MAX_SAFE_INTEGER) (< r js/Number.MIN_SAFE_INTEGER))
      (throw (ex-info "Integer overflow" {:fn "add-exact"}))
      r)))

(defn subtract-exact
  {:doc "Returns the difference of x and y, throws ArithmeticException on overflow. "
   :added "1.10.892"}
  [x y]
  (let [r (- x y)]
    (if (or (> r js/Number.MAX_SAFE_INTEGER) (< r js/Number.MIN_SAFE_INTEGER))
      (throw (ex-info "Integer overflow" {:fn "subtract-exact"}))
      r)))

(defn multiply-exact
  {:doc "Returns the product of x and y, throws ArithmeticException on overflow. "
   :added "1.10.892"}
  [x y]
  (let [r (* x y)]
    (if (or (> r js/Number.MAX_SAFE_INTEGER) (< r js/Number.MIN_SAFE_INTEGER))
      (throw (ex-info "Integer overflow" {:fn "multiply-exact"}))
      r)))

(defn increment-exact
  {:doc "Returns a incremented by 1, throws ArithmeticException on overflow."
   :added "1.10.892"}
  [a]
  (if (= a js/Number.MAX_SAFE_INTEGER)
    (throw (ex-info "Integer overflow" {:fn "increment-exact"}))
    (+ a 1)))

(defn decrement-exact
  {:doc "Returns a decremented by 1, throws ArithmeticException on overflow. "
   :added "1.10.892"}
  [a]
  (if (= a js/Number.MIN_SAFE_INTEGER)
    (throw (ex-info "Integer overflow" {:fn "decrement-exact"}))
    (- a 1)))

(defn negative-exact
  {:doc "Returns the negation of a, throws ArithmeticException on overflow. "
   :added "1.10.892"}
  [a]
  (if (or (> a js/Number.MAX_SAFE_INTEGER) (< a js/Number.MIN_SAFE_INTEGER))
    (throw (ex-info "Integer overflow" {:fn "negative-exact"}))
    (- a)))

(defn floor-div
  {:doc "Integer division that rounds to negative infinity (as opposed to zero).
  See: https://docs.oracle.com/javase/8/docs/api/java/lang/Math.html#floorDiv-long-long-"
   :added "1.10.892"}
  [x y]
  (if-not (and (js/Number.isInteger x) (js/Number.isInteger y))
    (throw (ex-info "Integer operation called with non-integer arguments"
                    {:x-int? (js/Number.isInteger x :y-int? (js/Number.isInteger y))}))
    (let [r (Math/floor (/ x y))]
      (if (and (< (bit-xor x y) 0) (not= (* r y) x))
        (dec r)
        r))))

(defn floor-mod
  {:doc "Integer modulus x - (floorDiv(x, y) * y). Sign matches y and is in the
  range -|y| < r < |y|.
  See: https://docs.oracle.com/javase/8/docs/api/java/lang/Math.html#floorMod-long-long-"
   :added "1.10.892"}
  [x y]
  (- x (* y (floor-div x y))))

(defn abs
  {:doc "Returns the absolute value of a (long or double).
  If not negative, a is returned, else negation of a is returned.
  If a < Number/MIN_SAFE_INTEGER => undefined (approximately -a)
  If a is a double and zero => +0.0
  If a is a double and ##Inf or ##-Inf => ##Inf
  If a is a double and ##NaN => ##NaN
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/abs"
   :added "1.10.892"}
  [a] (Math/abs a))

(defn max
  {:doc "Returns the greater of a or b.
  If a or b is ##NaN => ##NaN
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/max"
   :added "1.10.892"}
  [a b]
  (Math/max a b))

(defn min
  {:doc "Returns the lesser of a or b.
  If a or b is ##NaN => ##NaN
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/min"
   :added "1.10.892"}
  [p0 p1]
  (Math/min p0 p1))


;; TODO below here

(defn ulp
  {:doc "Returns the size of an ulp (unit in last place) for d.
  If d is ##NaN => ##NaN
  If d is ##Inf or ##-Inf => ##Inf
  If d is zero => Double/MIN_VALUE
  If d is +/- Double/MAX_VALUE => 2^971
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/"
   :inline-arities #{1}
   :inline (fn [d] `(Math/ulp ~d))
   :added "1.10.892"}
  ^double [^double d]
  (Math/ulp d))

(defn signum
  {:doc "Returns the signum function of d - zero for zero, 1.0 if >0, -1.0 if <0.
  If d is ##NaN => ##NaN
  If d is ##Inf or ##-Inf => d
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/"
   :inline-arities #{1}
   :inline (fn [d] `(Math/signum ~d))
   :added "1.10.892"}
  ^double [^double d]
  (Math/signum d))

(defn sinh
  {:doc "Returns the hyperbolic sine of x, (e^x - e^-x)/2.
  If x is ##NaN => ##NaN
  If x is ##Inf or ##-Inf or zero => x
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/"
   :inline-arities #{1}
   :inline (fn [x] `(Math/sinh ~x))
   :added "1.10.892"}
  ^double [^double x]
  (Math/sinh x))

(defn cosh
  {:doc "Returns the hyperbolic cosine of x, (e^x + e^-x)/2.
  If x is ##NaN => ##NaN
  If x is ##Inf or ##-Inf => ##Inf
  If x is zero => 1.0
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/"
   :inline-arities #{1}
   :inline (fn [x] `(Math/cosh ~x))
   :added "1.10.892"}
  ^double [^double x]
  (Math/cosh x))

(defn tanh
  {:doc "Returns the hyperbolic tangent of x, sinh(x)/cosh(x).
  If x is ##NaN => ##NaN
  If x is zero => zero, with same sign
  If x is ##Inf => +1.0
  If x is ##-Inf => -1.0
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/"
   :inline-arities #{1}
   :inline (fn [x] `(Math/tanh ~x))
   :added "1.10.892"}
  ^double [^double x]
  (Math/tanh x))

(defn hypot
  {:doc "Returns sqrt(x^2 + y^2) without intermediate underflow or overflow.
  If x or y is ##Inf or ##-Inf => ##Inf
  If x or y is ##NaN and neither is ##Inf or ##-Inf => ##NaN
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/"
   :inline-arities #{2}
   :inline (fn [x y] `(Math/hypot ~x ~y))
   :added "1.10.892"}
  ^double [^double x ^double y]
  (Math/hypot x y))

(defn expm1
  {:doc "Returns e^x - 1. Near 0, expm1(x)+1 is more accurate to e^x than exp(x).
  If x is ##NaN => ##NaN
  If x is ##Inf => #Inf
  If x is ##-Inf => -1.0
  If x is zero => x
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/"
   :inline-arities #{1}
   :inline (fn [x] `(Math/expm1 ~x))
   :added "1.10.892"}
  ^double [^double x]
  (Math/expm1 x))

(defn log1p
  {:doc "Returns ln(1+x). For small values of x, log1p(x) is more accurate than
  log(1.0+x).
  If x is ##NaN or < -1 => ##NaN
  If x is ##Inf or ##-Inf or x => x
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/"
   :inline-arities #{1}
   :inline (fn [x] `(Math/log1p ~x))
   :added "1.10.892"}
  ^double [^double x]
  (Math/log1p x))

(defn get-exponent
  {:doc "Returns the exponent of d.
  If d is ##NaN, ##Inf, ##-Inf => Double/MAX_EXPONENT + 1
  If d is zero or subnormal => Double/MIN_EXPONENT - 1
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/"
   :inline-arities #{1}
   :inline (fn [d] `(Math/getExponent ~d))
   :added "1.10.892"}
  [^double d]
  (Math/getExponent d))

(defn next-after
  {:doc "Returns the adjacent floating point number to start in the direction of
  the second argument. If the arguments are equal, the second is returned.
  If either arg is #NaN => #NaN
  If both arguments are signed zeros => direction
  If start is +-Double/MIN_VALUE and direction would cause a smaller magnitude
    => zero with sign matching start
  If start is ##Inf or ##-Inf and direction would cause a smaller magnitude
    => Double/MAX_VALUE with same sign as start
  If start is equal to +=Double/MAX_VALUE and direction would cause a larger magnitude
    => ##Inf or ##-Inf with sign matching start
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/"
   :inline-arities #{2}
   :inline (fn [start direction] `(Math/nextAfter ~start ~direction))
   :added "1.10.892"}
  ^double [^double start ^double direction]
  (Math/nextAfter start direction))

(defn next-up
  {:doc "Returns the adjacent double of d in the direction of ##Inf.
  If d is ##NaN => ##NaN
  If d is ##Inf => ##Inf
  If d is zero => Double/MIN_VALUE
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/"
   :inline-arities #{1}
   :inline (fn [d] `(Math/nextUp ~d))
   :added "1.10.892"}
  ^double [^double d]
  (Math/nextUp d))

(defn next-down
  {:doc "Returns the adjacent double of d in the direction of ##-Inf.
  If d is ##NaN => ##NaN
  If d is ##Inf => ##-Inf
  If d is zero => Double/MIN_VALUE
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/"
   :inline-arities #{1}
   :inline (fn [d] `(Math/nextDown ~d))
   :added "1.10.892"}
  ^double [^double d]
  (Math/nextDown d))

(defn scalb
  {:doc "Returns d * 2^scaleFactor, scaling by a factor of 2. If the exponent
  is between Double/MIN_EXPONENT and Double/MAX_EXPONENT, the answer is exact.
  If d is ##NaN => ##NaN
  If d is ##Inf or ##-Inf => ##Inf or ##-Inf respectively
  If d is zero => zero of same sign as d
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/"
   :inline-arities #{1}
   :inline (fn [d scaleFactor] `(Math/scalb ~d ~scaleFactor))
   :added "1.10.892"}
  ^double [^double d scaleFactor]
  (Math/scalb d scaleFactor))

(defn get-little-endian
  "Tests the platform for endianness. Returns true when little-endian, false otherwise."
  []
  (let [a (js/ArrayBuffer. 2)
        l (js/Uint16Array. a)
        b (js/Uint8Array. a)]
    (aset l 0 0xff00)
    (= 0 (aget b 0))))

(def ^{:private true :const true} little-endian? (get-little-endian))

(defn copy-sign
  {:doc "Returns a double with the magnitude of the first argument and the sign of
  the second.
  See: https://docs.oracle.com/javase/8/docs/api/java/lang/Math.html#copySign-double-double-"
   :added "1.10.892"}
  [magnitude sign]
  ;; create a buffer large enough for 2 doubles
  (let [a (js/ArrayBuffer. 16)
        ;; represent the buffer as a double array
        d (js/Float64Array. a)
        ;; represent the buffer as bytes
        b (js/Uint8Array. a)
        ;; find the offset of the byte that holds the sign bit
        sbyte (if little-endian? 7 0)]
    ;; the first double holds the magnitude, the second holds the sign value
    (aset d 0 magnitude)
    (aset d 1 sign)
    ;; read the sign bit from the sign value
    (let [sign-sbyte (bit-and 0x80 (aget b (+ 8 sbyte)))
          ;; read all the bits that aren't the sign bit in the same byte of the magnitude
          mag-sbyte (bit-and 0x7F (aget b sbyte))]
      ;; combine the sign bit from the sign value and the non-sign-bits from the magnitude value
      ;; write it back into the byte in the magnitude
      (aset b sbyte (bit-or sign-sbyte mag-sbyte))
      ;; retrieve the full magnitude value with the updated byte
      (aget d 0))))

(def ^{:private true :const true}
  two-to-the-52 0x10000000000000)

(defn rint
  {:doc "Returns the double closest to a and equal to a mathematical integer.
  If two values are equally close, return the even one.
  If a is ##NaN or ##Inf or ##-Inf or zero => a
  See: https://docs.oracle.com/javase/8/docs/api/java/lang/Math.html#rint-double-"
   :added "1.10.892"}
  [a]
  (let [sign (copy-sign 1.0, a)
        a (abs a)
        a (if (< a two-to-the-52)
            (- (+ two-to-the-52 a) two-to-the-52) a)]
    (* sign a)))

(comment
  "TODO: Add IEEE-remainder
  Based on Java code in java.base/share/native/libfdlibm/e_remainder.c
  This gets words from the doubles which rely on platform endianness,
  so detect this and create hi-word lo-word accordingly
  (def a (js/ArrayBuffer. 8))
  (def fa (js/Float64Array. a))
  (def u8 (js/Uint8Array. a))
  (def u32 (js/Uint32Array. a))
  (def hi-word (aget u32 1))
  (def lo-word (aget u32 0))")

(defn IEEE-remainder
  {:doc "Returns the remainder per IEEE 754 such that
    remainder = dividend - divisor * n
  where n is the integer closest to the exact value of dividend / divisor.
  If two integers are equally close, then n is the even one.
  If the remainder is zero, sign will match dividend.
  If dividend or divisor is ##NaN, or dividend is ##Inf or ##-Inf, or divisor is zero => ##NaN
  If dividend is finite and divisor is infinite => dividend
  See: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/"
   :inline-arities #{2}
   :inline (fn [dividend divisor] `(Math/IEEEremainder ~dividend ~divisor))
   :added "1.10.892"}
  ^double [^double dividend ^double divisor]
  (Math/IEEEremainder dividend divisor))
