#include "../../../../target/classes/resources/langages/c/RemotePosix.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* BEGIN TEMPLATE */
void base64(const char* data, int length, char* out)
{
  const char* digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
  for (int i = 0; i < length; i += 3) {
    int n  = (unsigned char)data[i] << 16 | (i + 1 < length ? (unsigned char)data[i + 1] << 8 : 0) | (i + 2 < length ? (unsigned char)data[i + 2] : 0);
    *out++ = digits[n >> 18 & 63];
    *out++ = digits[n >> 12 & 63];
    *out++ = i + 1 < length ? digits[n >> 6 & 63] : '=';
    *out++ = i + 2 < length ? digits[n & 63] : '=';
  }
  *out = '\0';
}
/* BEGIN SOLUTION */
int depth = 0; /* 0 before the header of the stream, 1 inside it, 2 or more inside a stanza */

/* Writes the next stanza of the stream into stanza (an array of that size), ended by a zero byte, and returns 1, or 0 at the
 * end of the stream */
int next_stanza(FILE* in, char* stanza, int size)
{
  int length = 0, kind = 0, previous = 0; /* kind: the byte after the < of the tag being read, or 0 out of a tag */
  while (1) {
    int c;
    if (depth == 1 && length == 0) { /* the spaces between the stanzas */
      while ((c = fgetc(in)) == ' ' || c == '\t' || c == '\r' || c == '\n')
        ;
      ungetc(c, in);
    }
    c = fgetc(in);
    if (c == EOF)
      return 0;
    if (length < size - 1)
      stanza[length++] = c;
    if (previous == '<')
      kind = c;
    else if (c == '>' && kind != 0) { /* the end of a tag */
      int before = depth;
      if (kind == '/') /* a closing tag */
        depth--;
      else if (kind != '?' && previous != '/') /* an opening tag, unless the XML declaration or a self-closing tag */
        depth++;
      kind = 0;
      if (before == 0) /* the XML declaration or the header of the stream, which are no stanzas */
        length = 0;
      else if (depth == 0) /* </stream:stream> */
        return 0;
      else if (depth == 1) {
        stanza[length] = '\0';
        return 1;
      }
    }
    previous = c;
  }
}

/* Writes the text of the body of the stanza into text (an array of that size), and returns 1, or 0 if it has no body */
int body(const char* stanza, char* text, int size)
{
  const char* start = strstr(stanza, "<body>");
  if (start == NULL)
    return 0;
  start += strlen("<body>");
  int length = strstr(start, "</body>") - start;
  if (length >= size)
    length = size - 1;
  memcpy(text, start, length);
  text[length] = '\0';
  return 1;
}

/* Returns 1 if the stanza itself was sent by that address, and 0 otherwise */
int sent_by(const char* stanza, const char* sender)
{
  char attribute[256];
  snprintf(attribute, sizeof(attribute), " from=\"%s\"", sender);
  const char* found = strstr(stanza, attribute);
  return found != NULL && found < strchr(stanza, '>'); /* in the opening tag of the stanza, not in a message that it quotes */
}

/* Sends that text on the socket */
void send_text(int sock, const char* text)
{
  send(sock, text, strlen(text), 0);
}
/* END SOLUTION */

void client(char* server, int port, char* room, char* password, char* friend)
{
  /* BEGIN SOLUTION */
  struct sockaddr_in addr;
  memset(&addr, 0, sizeof(addr));
  addr.sin_family = AF_INET;
  addr.sin_port   = htons(port);
  inet_pton(AF_INET, server, &addr.sin_addr);

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
  static char stanza[65536];
  char text[1024], out[1024];

  send_text(sock, "<?xml version='1.0'?><stream:stream to='buggleinc.lan' xmlns='jabber:client' "
                  "xmlns:stream='http://etherx.jabber.org/streams' version='1.0'>");
  next_stanza(in, stanza, sizeof(stanza)); /* the features of the stream */

  char credentials[128], encoded[256];
  int length = snprintf(credentials, sizeof(credentials), "%cnewcomer%c%s", 0, 0, password);
  base64(credentials, length, encoded);
  snprintf(out, sizeof(out), "<auth xmlns='urn:ietf:params:xml:ns:xmpp-sasl' mechanism='PLAIN'>%s</auth>", encoded);
  send_text(sock, out);
  next_stanza(in, stanza, sizeof(stanza)); /* <success/> */

  snprintf(out, sizeof(out), "<presence to='%s/newcomer'><x xmlns='http://jabber.org/protocol/muc'/></presence>", room);
  send_text(sock, out);
  while (next_stanza(in, stanza, sizeof(stanza)) && strstr(stanza, "<subject>") == NULL) /* the subject of the room ends the joining */
    ;

  char sender[256];
  snprintf(sender, sizeof(sender), "%s/%s", room, friend);
  while (next_stanza(in, stanza, sizeof(stanza)) && !sent_by(stanza, sender)) /* the friend of Bob calls the newcomer once he is back */
    ;
  snprintf(out, sizeof(out), "<message to='%s' type='groupchat'><body>%s: Bob sent me for the treasure.</body></message>", room, friend);
  send_text(sock, out);

  while (next_stanza(in, stanza, sizeof(stanza)))
    if (sent_by(stanza, sender) && body(stanza, text, sizeof(text)) && strstr(text, "[http") != NULL) {
      char* address         = strstr(text, "[http") + 1;
      *strchr(address, ']') = '\0';
      printf("%s\n", address);
      break;
    }

  send_text(sock, "</stream:stream>");
  while (next_stanza(in, stanza, sizeof(stanza))) /* up to the end of the stream of the server */
    ;
  fclose(in); /* closes the socket too */
  /* END SOLUTION */
}
/* END TEMPLATE */

void run()
{
  char* server   = getParamString(0);
  char* room     = getParamString(2);
  char* password = getParamString(3);
  char* friend   = getParamString(4);
  client(server, getParamInt(1), room, password, friend);
  free(server);
  free(room);
  free(password);
  free(friend);
}
