#include "Remote.h"

#include <pthread.h>
#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#ifdef _WIN32
#include <fcntl.h>
#include <io.h>
#include <winsock2.h>
#else
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <sys/socket.h>
#endif

/*
 * The protocol (commands to the PLM, answers from the PLM) travels over a TCP socket on 127.0.0.1, whose port is passed as
 * argv[1] -- see main() below. The PLM only listens on that address.
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
  return answer_buffer[1]; // +1: skip the leading 'c' tag
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

int main(int argc, char* argv[])
{
  // Disable buffering on the new stdout so student's debug messages arrive immediately
  setvbuf(stdout, NULL, _IONBF, 0);

#ifdef _WIN32
  WSADATA wsaData;
  WSAStartup(MAKEWORD(2, 2), &wsaData);
  // Unlike socket(), no WSA_FLAG_OVERLAPPED: the C runtime can only use non-overlapped sockets through a file descriptor
  SOCKET sock = WSASocketW(AF_INET, SOCK_STREAM, IPPROTO_TCP, NULL, 0, 0);
#else
  int sock = socket(AF_INET, SOCK_STREAM, 0);
#endif
  int nodelay = 1; // each command is a short request awaiting its answer
  setsockopt(sock, IPPROTO_TCP, TCP_NODELAY, (const char*)&nodelay, sizeof(nodelay));

  struct sockaddr_in addr;
  memset(&addr, 0, sizeof(addr));
  addr.sin_family      = AF_INET;
  addr.sin_port        = htons((unsigned short)atoi(argv[1]));
  addr.sin_addr.s_addr = htonl(INADDR_LOOPBACK);
  if (connect(sock, (struct sockaddr*)&addr, sizeof(addr)) != 0) {
    fprintf(stderr, "Cannot connect to the PLM protocol on port %s\n", argv[1]);
    return 1;
  }

#ifdef _WIN32
  int fd       = _open_osfhandle((intptr_t)sock, _O_RDWR | _O_BINARY); // binary: no CR/LF translation of the protocol
  protocol_in  = _fdopen(fd, "rb");
  protocol_out = _fdopen(fd, "wb");
#else
  protocol_in  = fdopen(sock, "r");
  protocol_out = fdopen(sock, "w");
#endif

  run();
  return 0;
}
