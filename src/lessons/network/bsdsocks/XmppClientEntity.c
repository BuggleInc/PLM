#include "../../../../target/classes/resources/langages/c/RemotePosix.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* BEGIN TEMPLATE */
void base64(const unsigned char* data, int length, char* out)
{
  const char* digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
  for (int i = 0; i < length; i += 3) {
    int n  = data[i] << 16 | (i + 1 < length ? data[i + 1] << 8 : 0) | (i + 2 < length ? data[i + 2] : 0);
    *out++ = digits[n >> 18 & 63];
    *out++ = digits[n >> 12 & 63];
    *out++ = i + 1 < length ? digits[n >> 6 & 63] : '=';
    *out++ = i + 2 < length ? digits[n & 63] : '=';
  }
  *out = '\0';
}

void client(char* server, int port, char* room, char* password)
{
  /* BEGIN SOLUTION */
  /* END SOLUTION */
}
/* END TEMPLATE */

void run()
{
  client(getParamString(0), getParamInt(1), getParamString(2), getParamString(3));
}
