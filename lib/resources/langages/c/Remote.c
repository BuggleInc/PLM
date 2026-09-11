#include "Remote.h"

#include <pthread.h>
#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#ifdef _WIN32
#include <afunix.h>
#include <fcntl.h>
#include <io.h>
#include <winsock2.h>
#else
#include <sys/socket.h>
#include <sys/un.h>
#endif

/*
 * The protocol (commands to the PLM, answers from the PLM) travels over a UNIX domain socket whose path is passed as
 * args[0] -- see connect() below, called from the generated main().
 */

static FILE* protocol_in  = NULL;
static FILE* protocol_out = NULL;
static char answer_buffer[1024];
char* get_answer_line()
{
  if (fgets(answer_buffer, sizeof(answer_buffer), protocol_in) == NULL) {
    exit(1);
  }
  answer_buffer[strcspn(answer_buffer, "\r\n")] = 0;
  return answer_buffer;
}
int get_answer_int()
{
  get_answer_line();
  return (int)strtol(answer_buffer + 1, NULL, 10); // +1: skip the leading 'i' or 'b' tag
}
double get_answer_double()
{
  get_answer_line();
  return strtod(answer_buffer + 1, NULL); // +1: skip the leading 'f' tag
}
char* get_answer_string()
{
  get_answer_line();
  // answer_buffer holds e.g. "He said \"hi\"", i.e. a quoted, backslash-escaped string -- the exact inverse of
  // ValueSerializer.serialize()'s String case.
  char* src = answer_buffer;
  if (*src == '"')
    src++; // skip the opening quote

  char* result = malloc(strlen(src) + 1);
  char* dst    = result;
  while (*src != '\0' && *src != '"') {
    if (*src == '\\' && *(src + 1) != '\0')
      src++; // skip the backslash, emit the escaped character literally
    *dst++ = *src++;
  }
  *dst = '\0';
  return result;
}
char get_answer_char()
{
  get_answer_line();
  return answer_buffer[0];
}
char* escape_string(const char* s)
{
  // Worst case every char needs escaping, plus 2 surrounding quotes and the null terminator.
  char* result = malloc(strlen(s) * 2 + 3);
  char* dst    = result;
  *dst++       = '"';
  for (const char* src = s; *src != '\0'; src++) {
    if (*src == '"' || *src == '\\')
      *dst++ = '\\';
    *dst++ = *src;
  }
  *dst++ = '"';
  *dst   = '\0';
  return result;
}
void send_command(char* format, ...)
{
  va_list args;
  va_start(args, format);

  vfprintf(protocol_out, format, args);
  fprintf(protocol_out, "\n");
  fflush(protocol_out);

  va_end(args);
}

/* BEGIN UTILS FUNCTIONS */

char* int2str(int nb)
{
  static char str[16];
  snprintf(str, 15, "%d", nb);
  return str;
}

/* END UTILS FUNCTIONS */

int main(int argc, char* argv[])
{
  // Disable buffering on the new stdout so student's debug messages arrive immediately
  setvbuf(stdout, NULL, _IONBF, 0);

#ifdef _WIN32
  WSADATA wsaData;
  WSAStartup(MAKEWORD(2, 2), &wsaData);
#endif

  int sock = socket(AF_UNIX, SOCK_STREAM, 0);
  struct sockaddr_un addr;
  memset(&addr, 0, sizeof(addr));
  addr.sun_family = AF_UNIX;
  strncpy(addr.sun_path, argv[1], sizeof(addr.sun_path) - 1);
  connect(sock, (struct sockaddr*)&addr, sizeof(addr));

#ifdef _WIN32
  int fd       = _open_osfhandle(sock, _O_RDWR);
  protocol_in  = _fdopen(fd, "r");
  protocol_out = _fdopen(fd, "w");
#else
  protocol_in  = fdopen(sock, "r");
  protocol_out = fdopen(sock, "w");
#endif

  run();
  return 0;
}
