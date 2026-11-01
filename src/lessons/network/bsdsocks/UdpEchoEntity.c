#include "../../../../target/classes/resources/langages/c/RemotePosix.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* BEGIN TEMPLATE */
void server(int port)
{
  /* BEGIN SOLUTION */
  int sock = socket(AF_INET, SOCK_DGRAM, 0);

  struct sockaddr_in addr;
  memset(&addr, 0, sizeof(addr));
  addr.sin_family      = AF_INET;
  addr.sin_port        = htons(port);
  addr.sin_addr.s_addr = htonl(INADDR_ANY);
  if (bind(sock, (struct sockaddr*)&addr, sizeof(addr)) < 0) {
    perror("bind");
    close(sock);
    return;
  }

  char buffer[65507 + 1]; /* the largest possible datagram, and a zero byte to end its text */
  while (1) {
    struct sockaddr_in sender;
    socklen_t senderlen = sizeof(sender);
    int received        = recvfrom(sock, buffer, sizeof(buffer) - 1, 0, (struct sockaddr*)&sender, &senderlen);
    sendto(sock, buffer, received, 0, (struct sockaddr*)&sender, senderlen); /* all its bytes, even a zero one */

    buffer[received] = '\0';
    char* found      = strstr(buffer, "PORT <");
    int bob;
    if (found != NULL && sscanf(found, "PORT <%d>", &bob) == 1) {
      printf("%d\n", bob);
      close(sock);
      return;
    }
  }
  /* END SOLUTION */
}
/* END TEMPLATE */

void run()
{
  server(getParamInt(0));
}
