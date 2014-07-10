#ifndef REMOTE_H
#define REMOTE_H

#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
#include <string.h>

#define printf myPrintf

void myPrintf(char* format, ...);
void run();



#endif