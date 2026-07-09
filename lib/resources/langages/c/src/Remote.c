#include "../include/Remote.h"

#include <pthread.h>
#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

/*
 * stdin/0:  where we read the answers from the PLM
 * stdout/1: where the user does its debug output (shown on the PLM console)
 * stderr/2: where we send commands to the PLM
 */

static FILE* debug_fd = NULL; // Where to send the debug info if not NULL
static char answer_buffer[1024];
static void get_answer_line() {
  if (fgets(answer_buffer, sizeof(answer_buffer), stdin) == NULL) {
    exit(1);
  }
  if (debug_fd)
    fprintf(debug_fd, "Answer: %s\n", answer_buffer);
  answer_buffer[strcspn(answer_buffer, "\r\n")] = 0;
}
int get_answer_int() {
  get_answer_line();
  return (int)strtol(answer_buffer, NULL, 10);
}
double get_answer_double() {
  get_answer_line();
  return strtod(answer_buffer, NULL);
}
char *get_answer_string() {
  get_answer_line();
  return strdup(answer_buffer);
}
char get_answer_char() {
  get_answer_line();
  return answer_buffer[0];
}

void send_command(char *format, ...) {
  va_list args, args_copy;
  va_start(args, format);

  if (debug_fd) {
    va_copy(args_copy, args);
    fprintf(debug_fd, "Command from C world: '");
    vfprintf(debug_fd, format, args_copy);
    fprintf(debug_fd, "'\n");
    fflush(debug_fd);
    va_end(args_copy);
  }

  vfprintf(stderr, format, args);
  fprintf(stderr, "\n");

  va_end(args);
}

/* BEGIN UTILS FUNCTIONS */

char* int2str(int nb){
	char* str = malloc(sizeof(char)*16);
	sprintf(str, "%d", nb);
	return str;
}

/* END UTILS FUNCTIONS */

int main(int argc, char *argv[]) {
  // Disable buffering on the new stdout so student's debug messages arrive immediately
  setvbuf(stdout, NULL, _IONBF, 0);

  debug_fd = fopen("/tmp/debug-PLM-C", "a");
  if (debug_fd)
    fprintf(debug_fd, "Starting the entity %s\n", argv[0]);
  run();
  return 0;
}
