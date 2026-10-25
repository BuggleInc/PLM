#include "../../../../target/classes/resources/langages/c/RemotePosix.h"
#include <stdlib.h>
#include <string.h>

/* BEGIN TEMPLATE */
void client(char* server, int port)
{
  /* BEGIN SOLUTION */
  struct sockaddr_in addr;
  memset(&addr, 0, sizeof(addr));
  addr.sin_family = AF_INET;
  addr.sin_port   = htons(port);
  inet_pton(AF_INET, server, &addr.sin_addr);

  int sock;
  for (;;) {
    sock = socket(AF_INET, SOCK_STREAM, 0);
    if (sock < 0) {
      perror("socket");
      return;
    }
    if (connect(sock, (struct sockaddr*)&addr, sizeof(addr)) == 0)
      break;
    if (errno != ECONNREFUSED) {
      perror("connect");
      close(sock);
      return;
    }
    close(sock); /* the server is not ready yet: retry one second later, with a new socket */
    sleep(1);
  }

  char buffer[512];
  ssize_t received;
  while ((received = recv(sock, buffer, sizeof(buffer), 0)) > 0)
    write(STDOUT_FILENO, buffer, received);
  if (received < 0)
    perror("recv");

  close(sock);
  /* END SOLUTION */
}
/* END TEMPLATE */

void run()
{
  char* server = getParamString(0);
  client(server, getParamInt(1));
  free(server);
}
