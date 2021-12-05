(ns ^{:doc "Duplicate some of the functionality of ClojureScript for
            testing purposes"
      :author "Paula Gearon"}
    js
  (:refer-clojure :exclude [aset aget +])
  (:import [java.nio ByteBuffer IntBuffer DoubleBuffer]))

(def Number.MAX_SAFE_INTEGER  9007199254740991)
(def Number.MIN_SAFE_INTEGER -9007199254740991)
(def Number.POSITIVE_INFINITY ##Inf)

(defn Number.isNaN
  [^double d]
  (Double/isNaN d))

(defn Number.isFinite
  [^double d]
  (not (Double/isInfinite d)))

(def Number.isInteger int?)

(def INT_MASK 0xFFFFFFFF)

(defn as-int
  [x]
  (let [x (bit-and INT_MASK x)]
    (if (zero? (bit-and 0x80000000 x))
      x
      (bit-or -0x100000000 x))))

(defprotocol ArrayAccess
  (aget [a n] "Retrieves data at position n")
  (aset [a n v] "Sets a value n at position n"))

(gen-class
 :name js.ArrayBuffer
 :state buffer
 :init init
 :constructors {[int] []}
 :prefix "array-buffer-")

(gen-class
 :name js.Uint8Array
 :state buffer
 :init init
 :constructors {[js.ArrayBuffer] []}
 :prefix "uint8-")

(gen-class
 :name js.Uint32Array
 :state buffer
 :init init
 :constructors {[js.ArrayBuffer] []}
 :prefix "uint32-")

(gen-class
 :name js.Float64Array
 :state buffer
 :init init
 :constructors {[js.ArrayBuffer] []}
 :prefix "float64-")

(defn array-buffer-init
  [s]
  [[] (ByteBuffer/allocate s)])

(defn uint8-init
  [a]
  [[] (.buffer a)])

(defn uint32-init
  [a]
  [[] (.asIntBuffer (.buffer a))])

(defn float64-init
  [a]
  [[] (.asDoubleBuffer (.buffer a))])

(extend-protocol ArrayAccess
  js.Uint8Array
  (aget [a n] (.get ^ByteBuffer (.buffer a) ^int n))
  (aset [a n v]
    (let [v (bit-and 0xff v)
          v (byte (if (zero? (bit-and 0x80 v)) v (bit-or -0x100 v)))]
      (.put ^ByteBuffer (.buffer a) ^int n v)))

  js.Uint32Array
  (aget [a n] (.get ^IntBuffer (.buffer a) n))
  (aset [a n v]
    (let [v (bit-and 0xffffffff v)
          v (int (if (zero? (bit-and 0x80000000 v)) v (bit-or -0x100000000 v)))]
      (.put ^IntBuffer (.buffer a) ^int n v)))

  js.Float64Array
  (aget [a n]
    (.get ^DoubleBuffer (.buffer a) (int (as-int n))))
  (aset [a n v] (.put ^DoubleBuffer (.buffer a) ^int n ^double v)))

(defn <<
  [n s]
  (let [n (bit-shift-left n s)]
    (if (zero? (bit-and 0x80000000 n))
      (bit-and 0xffffffff n)
      (bit-or -0x100000000 n))))

(defn >>
  [n s]
  (let [n (bit-and 0xffffffff n)
        n (if (zero? (bit-and 0x80000000 n)) n (bit-or -0x100000000 n))]
    (bit-shift-right n s)))

(defn >>>
  [n s]
  (unsigned-bit-shift-right (bit-and 0xffffffff n) s))

(defn add-int
  [a b]
  (unchecked-add-int
   (as-int a)
   (as-int b)))

(defn +
  ([] 0)
  ([x] x)
  ([x y] (add-int x y))
  ([x y & xs] 
   (reduce add-int (add-int x y) xs)))
