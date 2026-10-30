#include "../../../../target/classes/resources/langages/c/RemotePosix.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/types.h>

#define MIN(a, b) ((a) < (b) ? (a) : (b))
/* BEGIN TEMPLATE */
void client(char* server, int port, char* request)
{
  /* BEGIN SOLUTION */
  int sock = socket(AF_INET, SOCK_DGRAM, 0);

  struct sockaddr_in addr;
  memset(&addr, 0, sizeof(addr));
  addr.sin_family = AF_INET;
  addr.sin_port   = htons(port);
  inet_pton(AF_INET, server, &addr.sin_addr);

  ssize_t sent = sendto(sock, request, strlen(request), 0, (const struct sockaddr*)&addr, sizeof addr);
  if (sent < 0) {
    perror("sendto");
    close(sock);
    return;
  }

  char buffer[65508];
  struct sockaddr addr_recv;
  socklen_t addr_recv_len = sizeof(addr_recv);
  ssize_t received        = recvfrom(sock, buffer, sizeof(buffer), 0, &addr_recv, &addr_recv_len);
  if (received < 0) {
    perror("recvfrom");
    close(sock);
    return;
  }

  buffer[MIN(received, 65507)] = '\0';
  printf("%s", buffer);

  close(sock);
  /* END SOLUTION */
}
/* END TEMPLATE */

void run()
{
  char* server  = getParamString(0);
  char* request = getParamString(2);
  client(server, getParamInt(1), request);
  free(server);
  free(request);
}
