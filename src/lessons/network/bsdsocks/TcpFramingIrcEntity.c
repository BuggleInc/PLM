#include "../../../../target/classes/resources/langages/c/RemotePosix.h"

/* BEGIN TEMPLATE */
#include <string.h>

/* BEGIN SOLUTION */
char buffer[513]; /* 512 bytes of IRC, plus the final \0 */

void sendall(int fd, char* msg)
{
  char* p  = msg;
  int todo = strlen(msg);
  while (todo) {
    int res = send(fd, p, todo, 0);
    todo -= res;
    p += res;
  }
}

void waitline(FILE* in, const char* msg)
{
  char* line;
  while ((line = fgets(buffer, (sizeof buffer) - 1, in)) != NULL) {
    if (strstr(line, msg) != NULL)
      return;
  }
  fprintf(stderr, "Could not find the marker %s in the stream\n", msg);
  exit(1);
}
/* END SOLUTION */

void run()
{
  /* BEGIN SOLUTION */
  struct sockaddr_in addr;
  memset(&addr, 0, sizeof(addr));
  addr.sin_family = AF_INET;
  addr.sin_port   = htons(194);
  inet_pton(AF_INET, "10.0.0.1", &addr.sin_addr);

  int sock = socket(AF_INET, SOCK_STREAM, 0);
  if (sock < 0) {
    perror("socket");
    return;
  }
  if (connect(sock, (struct sockaddr*)&addr, sizeof(addr)) < 0) {
    perror("connect");
    close(sock);
    return;
  }

  FILE* in = fdopen(sock, "r");

  /* Pass the server greetings */
  fgets(buffer, sizeof buffer - 1, in);

  /* Connect */
  sendall(sock, "NICK student\r\n");
  sendall(sock, "USER student 0 * :myself.\r\n");

  waitline(in, "001");

  /* Join */
  sendall(sock, "JOIN #welcome\r\n");
  waitline(in, "366");

  /* Talk to Bob */
  sendall(sock, "PRIVMSG #welcome :Bob: Please tell me where the documentation is.\r\n");
  waitline(in, "/docs/");

  /* Extract the file from what Bob said */
  char* begin = strchr(buffer, '/');
  char* end   = strchr(begin, ' ');
  *end        = '\0';
  printf("%s", begin);

  /* display the rest for debugging, but this hangs once the end is reached */
  // while ((line = fgets(buffer, (sizeof buffer)-1, in)) != NULL) { }

  close(sock);
  /* END SOLUTION */
}
/* END TEMPLATE */
