#ifndef REMOTE_H
#define REMOTE_H

#include <stdarg.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void send_command(char* format, ...);
char* get_answer_line();
int get_answer_int();
double get_answer_double();
char* get_answer_string();
char get_answer_char();

/* Wraps a string in quotes with backslash-escaping, producing exactly the format ValueSerializer expects for a String
 * argument sent from C to the PLM (the mirror of get_answer_string()'s unescaping). Caller owns the returned buffer. */
char* escape_string(const char* s);

// Returns the address of a static buffer -- don't free it
char* int2str(int nb);

void run();

#endif
