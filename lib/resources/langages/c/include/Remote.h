#ifndef REMOTE_H
#define REMOTE_H

#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
#include <string.h>

void send_command(char *format, ...);
int get_answer_int();
double get_answer_double();
char *get_answer_string();
char get_answer_char();

void run();



#endif