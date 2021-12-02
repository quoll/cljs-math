#include <stdio.h>

#include "ieee.h"

int main(void) {
  /* double a = ieee_fmod(5.2, 2.3);
  printf("expecting 0.6. Got: %le, 0x%lX\n", a, *((unsigned long *)(&a))); */
  double b = ieee_fmod(339794.000868, 1.69897000343);
  printf("expecting 0.0001. Got: %le, 0x%lX\n", b, *((unsigned long *)(&b)));
  return 0;
}
