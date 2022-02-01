(ns ^{:doc "Large number implementations for ClojureScript"
      :author "Paula Gearon"}
    cljs.math.numbers
  (:refer-clojure :exclude [bit-and]))

(defprotocol BigInteger
  (abs [this] "Returns a BigInteger whose value is the absolute value of this BigInteger.")
  (add [this val] "Returns a BigInteger whose value is (this + val).")
  (bit-and [this val] "Returns a BigInteger whose value is (this & val).")
  (bit-and-not [this val] "Returns a BigInteger whose value is (this & ~val).")
  (bit-count [this] "Returns the number of bits in the two's complement representation of this BigInteger that differ from its sign bit.")
  (bit-length [this] "Returns the number of bits in the minimal two's-complement representation of this BigInteger, excluding a sign bit.")
  (byte-value-exact [this] "Converts this BigInteger to a byte, checking for lost information.")
  (clear-bit [this n] "Returns a BigInteger whose value is equivalent to this BigInteger with the designated bit cleared.")
  (compare-to [this val] "Compares this BigInteger with the specified BigInteger.")
  (divide [this val] "Returns a BigInteger whose value is (this / val).")
  (divide-and-remainder [this val] "Returns an array of two BigIntegers containing (this / val) followed by (this % val).")
  (double-value [this] "Converts this BigInteger to a double.")
  (equals [this x] "Compares this BigInteger with the specified Object for equality.")
  (flip-bit [this n] "Returns a BigInteger whose value is equivalent to this BigInteger with the designated bit flipped.")
  (float-value [this] "Converts this BigInteger to a float.")
  (gcd [this val] "Returns a BigInteger whose value is the greatest common divisor of abs(this) and abs(val).")
  (get-lowest-set-bit [this] "Returns the index of the rightmost (lowest-order) one bit in this BigInteger (the number of zero bits to the right of the rightmost one bit).")
  (int-value [this] "Converts this BigInteger to an int.")
  (int-value-exact [this] "Converts this BigInteger to an int, checking for lost information.")
  (is-probable-prime [this] "Returns true if this BigInteger is probably prime, false if it's definitely composite.")
  (max [this val] "Returns the maximum of this BigInteger and val.")
  (min [this val] "Returns the minimum of this BigInteger and val.")
  (mod [this m] "Returns a BigInteger whose value is (this mod m).")
  (mod-inverse [this m] "Returns a BigInteger whose value is (this^-1 mod m).")
  (mod-pow [this exponent m] "Returns a BigInteger whose value is (this^exponent mod m).")
  (multiply [this val] "Returns a BigInteger whose value is (this * val).")
  (negate [this] "Returns a BigInteger whose value is (-this).")
  (next-probable-prime [this] "Returns the first integer greater than this BigInteger that is probably prime.")
  (not [this] "Returns a BigInteger whose value is (~this).")
  (pow [this exponent] "Returns a BigInteger whose value is (this | val).")
  (remainder [this val] "Returns a BigInteger whose value is (this % val).")
  (set-bit [this n] "Returns a BigInteger whose value is equivalent to this BigInteger with the designated bit set.")
  (shift-left [this n] "Returns a BigInteger whose value is (this << n).")
  (shift-right [this n] "Returns a BigInteger whose value is (this >> n).")
  (signum [this] "Returns the signum value of this BigInteger.")
  (sqrt [this] "Returns the integer square root of this BigInteger.")
  (sqrt-and-remainder [this] "Returns an array of two BigIntegers containing the integer square root s of this and its remainder (this - s*s), respectively.")
  (subtract [this val] "Returns a BigInteger whose value is (this - val).")
  (test-bit [this n] "Returns true if and only if the designated bit is set.")
  (to-byte-array [this] "Returns a byte array containing the two's-complement representation of this BigInteger.")
  (to-string [this] "Returns the String representation of this BigInteger in the given radix. Separate from the Object toString function")
  (xor [this val] "Returns a BigInteger whose value is (this ^ val)."))


(defrecord BigIntegerCljs [m byte-array signum]
  (abs [this] "Returns a BigInteger whose value is the absolute value of this BigInteger.")
  (add [this val] "Returns a BigInteger whose value is (this + val).")
  (bit-and [this val] "Returns a BigInteger whose value is (this & val).")
  (bit-and-not [this val] "Returns a BigInteger whose value is (this & ~val).")
  (bit-count [this] "Returns the number of bits in the two's complement representation of this BigInteger that differ from its sign bit.")
  (bit-length [this] "Returns the number of bits in the minimal two's-complement representation of this BigInteger, excluding a sign bit.")
  (byte-value-exact [this] "Converts this BigInteger to a byte, checking for lost information.")
  (clear-bit [this n] "Returns a BigInteger whose value is equivalent to this BigInteger with the designated bit cleared.")
  (compare-to [this val] "Compares this BigInteger with the specified BigInteger.")
  (divide [this val] "Returns a BigInteger whose value is (this / val).")
  (divide-and-remainder [this val] "Returns an array of two BigIntegers containing (this / val) followed by (this % val).")
  (double-value [this] "Converts this BigInteger to a double.")
  (equals [this x] "Compares this BigInteger with the specified Object for equality.")
  (flip-bit [this n] "Returns a BigInteger whose value is equivalent to this BigInteger with the designated bit flipped.")
  (float-value [this] "Converts this BigInteger to a float.")
  (gcd [this val] "Returns a BigInteger whose value is the greatest common divisor of abs(this) and abs(val).")
  (get-lowest-set-bit [this] "Returns the index of the rightmost (lowest-order) one bit in this BigInteger (the number of zero bits to the right of the rightmost one bit).")
  (int-value [this] "Converts this BigInteger to an int.")
  (int-value-exact [this] "Converts this BigInteger to an int, checking for lost information.")
  (is-probable-prime [this] "Returns true if this BigInteger is probably prime, false if it's definitely composite.")
  (max [this val] "Returns the maximum of this BigInteger and val.")
  (min [this val] "Returns the minimum of this BigInteger and val.")
  (mod [this m] "Returns a BigInteger whose value is (this mod m).")
  (mod-inverse [this m] "Returns a BigInteger whose value is (this^-1 mod m).")
  (mod-pow [this exponent m] "Returns a BigInteger whose value is (this^exponent mod m).")
  (multiply [this val] "Returns a BigInteger whose value is (this * val).")
  (negate [this] "Returns a BigInteger whose value is (-this).")
  (next-probable-prime [this] "Returns the first integer greater than this BigInteger that is probably prime.")
  (not [this] "Returns a BigInteger whose value is (~this).")
  (pow [this exponent] "Returns a BigInteger whose value is (this | val).")
  (remainder [this val] "Returns a BigInteger whose value is (this % val).")
  (set-bit [this n] "Returns a BigInteger whose value is equivalent to this BigInteger with the designated bit set.")
  (shift-left [this n] "Returns a BigInteger whose value is (this << n).")
  (shift-right [this n] "Returns a BigInteger whose value is (this >> n).")
  (signum [this] "Returns the signum value of this BigInteger.")
  (sqrt [this] "Returns the integer square root of this BigInteger.")
  (sqrt-and-remainder [this] "Returns an array of two BigIntegers containing the integer square root s of this and its remainder (this - s*s), respectively.")
  (subtract [this val] "Returns a BigInteger whose value is (this - val).")
  (test-bit [this n] "Returns true if and only if the designated bit is set.")
  (to-byte-array [this] "Returns a byte array containing the two's-complement representation of this BigInteger.")
  (to-string [this] "Returns the String representation of this BigInteger in the given radix. Separate from the Object toString function")
  (xor [this val] "Returns a BigInteger whose value is (this ^ val)."))


(defn probable-prime
  "Returns a positive BigInteger that is probably prime, with the specified bitLength.
  TODO: consider if this can take a pseudo-random generator"
  [bit-length]
  )

(def ^:const BYTE-MASK (js/BigInt 0xFF))
(def ^:const THREE (js/BigInt 3))
(def ^:const ZERO (js/BigInt 0))

(defn bytes-for
  "Converts the bytes if the provided BigInt into an array"
  [m]
  (let [s (.toString m 16)
        l (int (/ (inc (count s)) 2))
        a (js/Uint8Array. l)]
    (amap a i ret (js/Number (bit-and BYTE-MASK (bit-shift-right m (js/BigInt (bit-shift-left i 3))))))))

(defn get-signum
  [m]
  (cond
    (> ZERO m) -1
    (=== ZERO m) 0
    1))

(defn value-of
  "Returns a BigInteger whose value is equal to that of the specified long."
  [val]
  (if (or (string? val) (integer? val))
    (let [m (js/BigInt val)
          signum (get-signum m)]
      (->BigIntegerCljs m signum (bytes-for (if (=== -1 signum) (- m) m))))
    (throw (ex-info "Unsupported datatype" {:status :todo}))))

(defn from-array
  "Creates a BigInteger from a byte array and a signum value"
  [signum byte-array]
  (if (#{-1 0 1} signum)
    (let [m (areduce byte-array i ret ZERO
                     (let [b (aget byte-array i)]
                       (if (and (integer? b) (< b 0x100))
                         (bit-or (js/BigInt b)
                                 (bit-shift-left ret (js/BigInt (bit-shift-left i 3))))
                         (throw (ex-info (str "Array does not contain bytes: " b)
                                         {:array byte-array :element b :index i})))))]
      (->BigIntegerCljs (if (=== -1 signum) (- m) m) signum byte-arry))
    (throw (ex-info "signum out of range" {:signum signum}))))
