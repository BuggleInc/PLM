#include "../../../../target/classes/resources/langages/c/RemotePosix.h"
#include <string.h>

/* BEGIN TEMPLATE */
void server(int port)
{
  /* BEGIN SOLUTION */
  int master = socket(AF_INET, SOCK_STREAM, 0);
  if (master < 0) {
    perror("socket");
    return;
  }

  struct sockaddr_in addr;
  memset(&addr, 0, sizeof(addr));
  addr.sin_family      = AF_INET;
  addr.sin_port        = htons(port);
  addr.sin_addr.s_addr = htonl(INADDR_ANY);
  if (bind(master, (struct sockaddr*)&addr, sizeof(addr)) < 0) {
    perror("bind");
    close(master);
    return;
  }
  if (listen(master, 5) < 0) {
    perror("listen");
    close(master);
    return;
  }

  int client = accept(master, NULL, NULL);
  if (client < 0) {
    perror("accept");
    close(master);
    return;
  }

  /* The line may arrive in several packets: receive until its '\n' (or until the client closes the connection) */
  char line[512];
  size_t length    = 0;
  ssize_t received = 1;
  while (received > 0 && length < sizeof(line) && memchr(line, '\n', length) == NULL) {
    received = recv(client, line + length, sizeof(line) - length, 0);
    if (received > 0)
      length += received;
  }
  if (received < 0)
    perror("recv");
  else
    for (int i = 0; i < 2; i++) /* the doubling echo */
      if (send(client, line, length, 0) < 0) {
        perror("send");
        break;
      }

  close(client);
  close(master);
  /* END SOLUTION */
}
/* END TEMPLATE */

void run()
{
  server(getParamInt(0));
}
