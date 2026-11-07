#include "../../../../target/classes/resources/langages/c/RemotePosix.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* BEGIN TEMPLATE */
void base64(const unsigned char* data, int length, char* out)
{
  const char* digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
  for (int i = 0; i < length; i += 3) {
    int n  = data[i] << 16 | (i + 1 < length ? data[i + 1] << 8 : 0) | (i + 2 < length ? data[i + 2] : 0);
    *out++ = digits[n >> 18 & 63];
    *out++ = digits[n >> 12 & 63];
    *out++ = i + 1 < length ? digits[n >> 6 & 63] : '=';
    *out++ = i + 2 < length ? digits[n & 63] : '=';
  }
  *out = '\0';
}

/* BEGIN SOLUTION */
char buffer[1024];

void fetch_stanza(FILE* in, int is_starting_stanza)
{
  buffer[0] = '\0';
  char* p   = buffer;
  int level = 0; // the header of the stream stops at level 1 (it stays open), a stanza at level 0 (it closes)

  do {
    // read the text up to the start of a tag
    while ((*(p++) = fgetc(in)) != '<')
      if (*p == EOF)
        return;

    *(p++) = fgetc(in); // The char after the '<'
    if (*(p - 1) == '/')
      level--; // Closing tag
    else
      level++; // Not closing tag: assume it's an opening tag

    // read the text up to the stop of a tag
    while ((*(p++) = fgetc(in)) != '>')
      if (*p == EOF)
        return;

    if (*(p - 2) == '/') // That's a self-closed tag => don't change the level, so compensate because we increased it above
      level--;
  } while (level != is_starting_stanza);
  *p = '\0'; // end the string, for printf("%s")
}
/* END SOLUTION */

void client(char* server, int port, char* room, char* password)
{
  /* BEGIN SOLUTION */
  int sock = socket(AF_INET, SOCK_STREAM, 0);
  if (sock < 0) {
    perror("socket");
    return;
  }

  struct sockaddr_in addr;
  memset(&addr, 0, sizeof(addr));
  addr.sin_family = AF_INET;
  addr.sin_port   = htons(port);
  inet_pton(AF_INET, server, &addr.sin_addr);
  if (connect(sock, (struct sockaddr*)&addr, sizeof(addr)) < 0) {
    perror("connect");
    return;
  }

  FILE* in = fdopen(sock, "r");

  // Send the header
  char* header = "<stream:stream to='buggleinc.lan' xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams' version='1.0'>";
  write(sock, header, strlen(header));

  char c;
  while ((c = fgetc(in)) != '>') // Pass the XML header
    if (c == EOF)
      return;

  fetch_stanza(in, 1); // Skip the document start
  fetch_stanza(in, 0);

  printf("stanza: %s\n", buffer);
  /* END SOLUTION */
}
/* END TEMPLATE */

void run()
{
  client(getParamString(0), getParamInt(1), getParamString(2), getParamString(3));
}
