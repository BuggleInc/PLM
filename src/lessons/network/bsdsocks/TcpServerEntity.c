#include "../../../../target/classes/resources/langages/c/RemotePosix.h"
#include <stdlib.h>
#include <string.h>

/* BEGIN TEMPLATE */
void server(int port, char* message)
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

  if (send(client, message, strlen(message), 0) < 0)
    perror("send");

  close(client);
  close(master);
  /* END SOLUTION */
}
/* END TEMPLATE */

void run()
{
  char* message = getParamString(1);
  server(getParamInt(0), message);
  free(message);
}
