#include "../include/Remote.h"

#include <pthread.h>
#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

/*
 * This code rewires the classical files descriptors as follows:
 *
 * stdin/0: (unchanged) the PLM write answers to out commands on that FD
 * stderr/2 (unchanged) it goes to the terminal; don't use it
 *
 * protocol_out_fd (likely 3): cloned from the original stdout/1 using dup(). We
 * write protocol requests to the PLM on this FD.
 *
 * student_pipe[0] (likely 4): the read-end of the pipe. A background thread
 * reads this and writes lines prefixed with "STDOUT:" to protocol_out_fd.
 * student_pipe[1] (likely 5): the write-end of the pipe.
 *
 * stdout/1: redirected (via dup2) to student_pipe[1]. Standard printf() calls
 * land here, feeding the interceptor thread.
 */

static FILE *debug_fd =
    NULL; // Where to send the debug info if this fd is not NULL

int protocol_out_fd;
int student_pipe[2];

void *interceptor_thread(void *arg) {
  char buffer[1024];
  ssize_t count;

  // Read from the pipe where student stdout is redirected
  while ((count = read(student_pipe[0], buffer, sizeof(buffer) - 1)) > 0) {
    buffer[count] = '\0';
    // Write wrapped output to the original stdout (protocol channel)
    dprintf(protocol_out_fd, "STDOUT:%s", buffer);
  }
  return NULL;
}

static char answer_buffer[1024];

static void get_answer_line() {
  if (fgets(answer_buffer, sizeof(answer_buffer), stdin) == NULL) {
    exit(1);
  }
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

  vdprintf(protocol_out_fd, format, args);
  dprintf(protocol_out_fd, "\n");

  va_end(args);
}

int main(int argc, char *argv[]) {
  // Save original stdout (FD 1) for the PLM protocol
  protocol_out_fd = dup(STDOUT_FILENO);

  // Create a pipe to intercept student stdout
  pthread_t interceptor_tid;
  if (pipe(student_pipe) == 0) {
    dup2(student_pipe[1], STDOUT_FILENO);

    // Disable buffering on the new stdout so messages arrive immediately
    setvbuf(stdout, NULL, _IONBF, 0);

    pthread_create(&interceptor_tid, NULL, interceptor_thread, NULL);
  }

  debug_fd = fopen("/tmp/debug-PLM-C", "rw");
  if (debug_fd)
    fprintf(debug_fd, "Starting the entity\n");
  run();

  // The interceptor stops when STDOUT_FILENO is closed, so don't join it before
  // that
  fflush(stdout);
  close(STDOUT_FILENO);
  close(student_pipe[1]);
  close(student_pipe[0]);
  pthread_join(interceptor_tid, NULL);

  return 0;
}
