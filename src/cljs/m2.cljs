(ns ^{:doc "Reimplementing cljs.math using raw JS for operations that are native in the JDK"
      :author "Paula Gearon"}
    cljs.m2)

(defn get-little-endian
  "Tests the platform for endianness. Returns true when little-endian, false otherwise."
  []
  (let [a (js/ArrayBuffer. 2)
        l (js/Uint16Array. a)
        b (js/Uint8Array. a)]
    (aset l 0 0xff00)
    (= 0 (aget b 0))))

(defonce ^:private little-endian? (get-little-endian))

;; the HI and LO labels are terse to reflect the C macros they represent
(def ^{:private true :doc "offset of hi integers in 64-bit values"} HI (if little-endian? 1 0))

(def ^{:private true :doc "offset of hi integers in 64-bit values"} LO (- 1 HI))


(defonce ^{:private true} Zero
  ;; a buffer that can hold a pair of 64 bit doubles
  (let [a (js/ArrayBuffer. 16)
        ;; represent the buffer as a 2 double array
        d (js/Float64Array. a)
        ;; represent the buffer as an array of bytes
        b (js/Uint8Array. a)]
    ;; initialize both doubles to 0.0
    (aset d 0 0.0)
    (aset d 1 0.0)
    ;; update the sign bit on the second double
    (aset b (if little-endian? 15 8) 0x80)
    ;; save the array of 2 doubles [0.0, -0.0]
    d))


(defn IEEE-fmod
  {:doc "Return x mod y in exact arithmetic. Method: shift and subtract.
  Reimplements __ieee754_fmod from the JDK.
  Ported from: https://github.com/openjdk/jdk/blob/master/src/java.base/share/native/libfdlibm/e_fmod.c
  << and >> convert numbers to signed 32-bit
  Fortunately the values that are shifted are expected to be 32 bit signed."
   :private true}
  [x y]
  ((js*
    "function(x, y, HI, LO, Zero) {
       if (y == 0.0 || Number.isNaN(y) || !Number.isFinite(x)) return NaN;

       var a = new ArrayBuffer(16);
       var d = new Float64Array(a);
       var ui = new Uint32Array(a);
       d[0] = x;
       d[1] = y;
       var hx = ui[HI];            /* high word of x */
       var lx = ui[LO];            /* low  word of x */
       var hy = ui[HI+2];          /* high word of y */
       var ly = ui[LO+2];          /* low  word of y */
       var sx = hx & 0x80000000;   /* sign of x */
       hx ^= sx;                   /* |x| */
       hy &= 0x7fffffff;           /* |y| */

       var n = 0;

       /* purge off exception values */
       if (hx <= hy) {
         if ((hx < hy) || (lx < ly)) return x;      /* |x|<|y| return x */
         if (lx==ly)
           return Zero[sx >>> 31];  /* |x|=|y| return x*0*/
       }

       /* determine ix = ilogb(x) */
       var ix = 0;
       if (hx < 0x00100000) {     /* subnormal x */
         if (hx == 0) {
           for (ix = -1043, i = lx; i > 0; i <<= 1) ix -= 1;
         } else {
           for (ix = -1022, i = (hx << 11); i > 0; i <<= 1) ix -= 1;
         }
       } else ix = (hx >> 20) - 1023;

       /* determine iy = ilogb(y) */
       var iy = 0;
       if (hy < 0x00100000) {     /* subnormal y */
         if (hy == 0) {
           for (iy = -1043, i = ly; i > 0; i <<= 1) iy -=1;
         } else {
           for (iy = -1022, i = (hy << 11); i > 0; i <<= 1) iy -=1;
         }
       } else iy = (hy >> 20) - 1023;

       /* set up {hx,lx}, {hy,ly} and align y to x */
       if (ix >= -1022)
         hx = 0x00100000 | (0x000fffff & hx);
       else {          /* subnormal x, shift x to normal */
         n = -1022 - ix;
         if (n <= 31) {
           hx = (hx << n) | (lx >>> (32-n));
           lx <<= n;
         } else {
           hx = lx << (n - 32);
           lx = 0;
         }
       }
       if (iy >= -1022)
         hy = 0x00100000 | (0x000fffff & hy);
       else {          /* subnormal y, shift y to normal */
         n = -1022-iy;
         if (n <= 31) {
           hy = (hy << n) | (ly >>> (32 - n));
           ly <<= n;
         } else {
           hy = ly << (n - 32);
           ly = 0;
         }
       }

       /* fix point fmod */
       n = ix - iy;
       while (n--) {
         hz = hx - hy;
         lz = lx - ly;
         if (lx < ly) hz -= 1;
         if (hz < 0) {
           hx = hx + hx + (lx >>> 31);
           lx = lx + lx;
         } else {
           if ((hz | lz) == 0)          /* return sign(x)*0 */
             return Zero[sx >>> 31];
           hx = hz + hz + (lz >>> 31);
           lx = lz + lz;
         }
       }
       hz = hx - hy;
       lz = lx - ly;
       if (lx < ly) hz -= 1;
       if (hz >= 0) {
         hx = hz;
         lx= lz;
       }

       /* convert back to floating value and restore the sign */
       if ((hx | lx) == 0)                  /* return sign(x)*0 */
         return Zero[sx >>> 31];
       while (hx < 0x00100000) {          /* normalize x */
         hx = hx + hx + (lx >>> 31);
         lx = lx + lx;
         iy -= 1;
       }
       if (iy >= -1022) {        /* normalize output */
         hx = ((hx - 0x00100000) | ((iy + 1023) << 20));
         ui[HI] = hx | sx;
         ui[LO] = lx;
         x = d[0];
       } else {                /* subnormal output */
         n = -1022 - iy;
         if (n <= 20) {
           lx = (lx >>> n) | (hx << (32 - n));
           hx >>= n;
         } else if (n <= 31) {
           lx = (hx << (32 - n)) | (lx >>> n);
           hx = sx;
         } else {
           lx = hx >>> (n - 32);
           hx = sx;
         }
         ui[HI] = hx | sx;
         ui[LO] = lx;
         x = d[0] *= 1.0;      /* create necessary signal */
       }
       return x;               /* exact output */

     }") x y HI LO Zero))

(defn IEEE-remainder
  [dividend divisor]
  ((js*
    "function (x, p, ieee754_fmod, HI, LO) {
       var a = new ArrayBuffer(16);
       var d = new Float64Array(a);
       var ui = new Uint32Array(a);
       d[0] = x;
       d[1] = p;
       var hx = ui[HI];        /* high word of x */
       var lx = ui[LO];        /* low  word of x */
       var hp = ui[HI+2];      /* high word of p */
       var lp = ui[LO+2];      /* low  word of p */
       sx = hx & 0x80000000;
       hp &= 0x7fffffff;
       hx &= 0x7fffffff;

       /* purge off exception values */
       if ((hp | lp) == 0) return (x * p) / (x * p);      /* p = 0 */
       if ((hx >= 0x7ff00000) ||                   /* x not finite */
           ((hp >= 0x7ff00000) &&                   /* p is NaN */
            (((hp - 0x7ff00000) | lp) != 0)))
         return (x * p) / (x * p);


       if (hp <= 0x7fdfffff) x = ieee754_fmod(x, p + p);  /* now x < 2p */
       if (((hx - hp) | (lx - lp)) == 0) return 0.0 * x;
       x  = Math.abs(x);
       p  = Math.abs(p);
       if (hp < 0x00200000) {
         if (x + x > p) {
           x -= p;
           if (x + x >=p) x -= p;
         }
       } else {
         var p_half = 0.5 * p;
         if (x > p_half) {
           x -= p;
           if (x >= p_half) x -= p;
         }
       }
       d[0] = x;
       ui[HI] ^= sx;
       return d[0];
     }") dividend divisor IEEE-fmod HI LO))
