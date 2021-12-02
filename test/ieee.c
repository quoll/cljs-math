#include <stdio.h>
#include <math.h>

#include "ieee.h"

#define __HI(x) *(1+(int*)&x)
#define __LO(x) *(int*)&x

static const double one = 1.0, Zero[] = {0.0, -0.0,};
static const double zero = 0.0;

double ieee_fmod(double x, double y) {
  int n,hx,hy,hz,ix,iy,sx,i;
  unsigned lx,ly,lz;
  printf("ieee_fmod\n");

  hx = __HI(x);           /* high word of x */
  lx = __LO(x);           /* low  word of x */
  hy = __HI(y);           /* high word of y */
  ly = __LO(y);           /* low  word of y */
  sx = hx&0x80000000;             /* sign of x */
  hx ^=sx;                /* |x| */
  hy &= 0x7fffffff;       /* |y| */

  /* purge off exception values */
  if((hy|ly)==0||(hx>=0x7ff00000)||       /* y=0,or x not finite */
     ((hy|((ly|-ly)>>31))>0x7ff00000))     /* or y is NaN */
    return (x*y)/(x*y);
  if(hx<=hy) {
    if((hx<hy)||(lx<ly)) return x;      /* |x|<|y| return x */
    if(lx==ly)
      return Zero[(unsigned)sx>>31];  /* |x|=|y| return x*0*/
  }

  /* determine ix = ilogb(x) */
  if(hx<0x00100000) {     /* subnormal x */
    if(hx==0) {
      for (ix = -1043, i=lx; i>0; i<<=1) ix -=1;
    } else {
      for (ix = -1022,i=(hx<<11); i>0; i<<=1) ix -=1;
    }
  } else ix = (hx>>20)-1023;
  printf("ix=%x\n", ix);

  /* determine iy = ilogb(y) */
  if(hy<0x00100000) {     /* subnormal y */
    if(hy==0) {
      for (iy = -1043, i=ly; i>0; i<<=1) iy -=1;
    } else {
      for (iy = -1022,i=(hy<<11); i>0; i<<=1) iy -=1;
    }
  } else iy = (hy>>20)-1023;
  printf("iy=%x\n", iy);

  /* set up {hx,lx}, {hy,ly} and align y to x */
  if(ix >= -1022)
    hx = 0x00100000|(0x000fffff&hx);
  else {          /* subnormal x, shift x to normal */
    n = -1022-ix;
    if(n<=31) {
      hx = (hx<<n)|(lx>>(32-n));
      lx <<= n;
    } else {
      hx = lx<<(n-32);
      lx = 0;
    }
  }
  if(iy >= -1022)
    hy = 0x00100000|(0x000fffff&hy);
  else {          /* subnormal y, shift y to normal */
    n = -1022-iy;
    if(n<=31) {
      hy = (hy<<n)|(ly>>(32-n));
      ly <<= n;
    } else {
      hy = ly<<(n-32);
      ly = 0;
    }
  }
  printf("hx=%x  lx=%x\n", hx, lx);
  printf("hy=%x  ly=%x\n", hy, ly);

  /* fix point fmod */
  n = ix - iy;
  while(n--) {
    hz=hx-hy;lz=lx-ly; if(lx<ly) hz -= 1;
    if(hz<0){hx = hx+hx+(lx>>31); lx = lx+lx;}
    else {
      if((hz|lz)==0)          /* return sign(x)*0 */
        return Zero[(unsigned)sx>>31];
      hx = hz+hz+(lz>>31); lx = lz+lz;
    }
  }
  printf("1. hx=%x  lx=%x\n", hx, lx);
  printf("a. hz=%x  lz=%x\n", hz, lz);
  hz=hx-hy;lz=lx-ly; if(lx<ly) hz -= 1;
  if(hz>=0) {hx=hz;lx=lz;}
  printf("b. hz=%x  lz=%x\n", hz, lz);

  printf("normalized\n");
  printf("2. hx=%x  lx=%x\n", hx, lx);

  /* convert back to floating value and restore the sign */
  if((hx|lx)==0)                  /* return sign(x)*0 */
    return Zero[(unsigned)sx>>31];
  while(hx<0x00100000) {          /* normalize x */
    hx = hx+hx+(lx>>31); lx = lx+lx;
    iy -= 1;
  }
  if(iy>= -1022) {        /* normalize output */
    hx = ((hx-0x00100000)|((iy+1023)<<20));
    __HI(x) = hx|sx;
    __LO(x) = lx;
  } else {                /* subnormal output */
    n = -1022 - iy;
    if(n<=20) {
                lx = (lx>>n)|((unsigned)hx<<(32-n));
                hx >>= n;
    } else if (n<=31) {
                lx = (hx<<(32-n))|(lx>>n); hx = sx;
    } else {
                lx = hx>>(n-32); hx = sx;
    }
    __HI(x) = hx|sx;
    __LO(x) = lx;
    x *= one;           /* create necessary signal */
  }
  return x;               /* exact output */
}


double remainder(double x, double p) {
  int hx,hp;
  unsigned sx,lx,lp;
  double p_half;

  hx = __HI(x);           /* high word of x */
  lx = __LO(x);           /* low  word of x */
  hp = __HI(p);           /* high word of p */
  lp = __LO(p);           /* low  word of p */
  sx = hx&0x80000000;
  hp &= 0x7fffffff;
  hx &= 0x7fffffff;

  /* purge off exception values */
  if((hp|lp)==0) return (x*p)/(x*p);      /* p = 0 */
  if((hx>=0x7ff00000)||                   /* x not finite */
     ((hp>=0x7ff00000)&&                   /* p is NaN */
      (((hp-0x7ff00000)|lp)!=0)))
    return (x*p)/(x*p);


  if (hp<=0x7fdfffff) x = ieee_fmod(x,p+p);  /* now x < 2p */
  if (((hx-hp)|(lx-lp))==0) return zero*x;
  x = fabs(x);
  p = fabs(p);
  if (hp<0x00200000) {
    if(x+x>p) {
      x-=p;
      if(x+x>=p) x -= p;
    }
  } else {
     p_half = 0.5*p;
     if(x>p_half) {
       x-=p;
       if(x>=p_half) x -= p;
     }
  }
  __HI(x) ^= sx;
  return x;
}


