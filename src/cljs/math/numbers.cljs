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
  (int-value [this] "Converts this BigInteger to an int."))
