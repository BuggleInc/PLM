#include "../../../../target/classes/resources/langages/c/RemotePosix.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/types.h>

#define MIN(a, b) ((a) < (b) ? (a) : (b))
/* BEGIN TEMPLATE */
/* Single-bit flags */
#define DNS_QR 0x8000 /* 1 = answer, 0 = query */
#define DNS_AA 0x0400 /* Authoritative Answer */
#define DNS_TC 0x0200 /* TrunCated: the answer did not fit in 512 bytes */
#define DNS_RD 0x0100 /* Recursion Desired (set by the client, copied back) */
#define DNS_RA 0x0080 /* Recursion Available */
#define DNS_Z 0x0040  /* reserved, must be 0 */

/* Response codes (flags & DNS_RCODE_MASK) */
#define DNS_RCODE_MASK 0x000F /* bits 0-3 */
#define DNS_RCODE_NOERROR 0
#define DNS_RCODE_FORMERR 1
#define DNS_RCODE_SERVFAIL 2
#define DNS_RCODE_NXDOMAIN 3
#define DNS_RCODE_NOTIMP 4
#define DNS_RCODE_REFUSED 5

/* BEGIN SOLUTION */

unsigned char query[512] = {0};
int pos                  = 0;

void reset()
{
  memset(query, 0, 512);
  pos = 0;
}
void pack_short(unsigned short val)
{
  *(unsigned short*)(query + pos) = htons(val);
  pos += 2;
  // fprintf(stderr, "Pack short %u\n", val);
}
void pack_uchar(unsigned char val)
{
  *(query + pos) = val;
  pos += 1;
  // fprintf(stderr, "Pack char %u\n", val);
}
void pack_string(char* str)
{
  unsigned char len = strlen(str);
  pack_uchar(len);
  // fprintf(stderr, "pack %s\n", str);
  for (unsigned i = 0; i < len; i++)
    pack_uchar(str[i]);
}
void pack_name(char* name)
{
  char* namedup = strdup(name);
  char* tok     = strtok(namedup, ".");
  pack_string(tok);
  while ((tok = strtok(NULL, ".")) != NULL)
    pack_string(tok);
  pack_uchar('\0');
  free(namedup);
}

void pack_header()
{
  pack_short(42);     // ID
  pack_short(DNS_RD); // flags, only RD
  pack_short(1);      // question count
  pack_short(0);      // answer count
  pack_short(0);      // autoritative answer count
  pack_short(0);      // additional
}
unsigned short unpack_short(unsigned char* in, int* pos)
{
  unsigned short res = *(unsigned short*)(in + *pos);
  *pos += 2;
  return ntohs(res);
}
unsigned char unpack_char_named(unsigned char* in, int* pos)
{
  unsigned char res = *(in + *pos);
  *pos += 1;
  return res;
}
unsigned char unpack_char()
{
  return unpack_char_named(query, &pos);
}
unsigned unpack_int()
{
  unsigned res = *(unsigned*)(query + pos);
  pos += 4;
  return ntohl(res);
}
void unpack_header(unsigned short* ID, unsigned short* qcount, unsigned short* ancount, unsigned short* addcount)
{
  pos = 0;

  *ID                  = unpack_short(query, &pos);
  unsigned short flags = unpack_short(query, &pos); // flags
  if ((flags & DNS_RCODE_MASK) != DNS_RCODE_NOERROR) {
    fprintf(stderr, "The server gave an error code: %u\n", flags & DNS_RCODE_MASK);
  }
  *qcount  = unpack_short(query, &pos);
  *ancount = unpack_short(query, &pos);
  unpack_short(query, &pos); // nscount is ignored
  *addcount = unpack_short(query, &pos);
}
void unpack_name(unsigned char* in, int* pos, char* out)
{
  int len = unpack_char_named(in, pos);
  if (len == 0) {
    out[0] = '\0';
    return;
  }
  for (unsigned i = 0; i < len; i++) {
    out[i] = unpack_char_named(in, pos);
  }
  out[len]     = '.';
  out[len + 1] = '\0';
  // fprintf(stderr, "Seen name >%s<\n", out);
  unpack_name(in, pos, out + len + 1);
}
/* END SOLUTION */
void run()
{
  /* BEGIN SOLUTION */
  int sock = socket(AF_INET, SOCK_DGRAM, 0);
  struct sockaddr_in serveraddr;
  socklen_t socklen = sizeof(serveraddr);
  memset(&serveraddr, 0, sizeof serveraddr);
  serveraddr.sin_family = AF_INET;
  serveraddr.sin_port   = htons(53);
  inet_pton(AF_INET, "10.0.0.53", &serveraddr.sin_addr);

  reset();
  pack_header();
  pack_name("buggleinc.lan");
  pack_short(16); // TXT command
  pack_short(1);  // IN class
  sendto(sock, query, pos, 0, (struct sockaddr*)&serveraddr, sizeof serveraddr);

  reset();
  recvfrom(sock, query, sizeof query, 0, (struct sockaddr*)&serveraddr, &socklen);

  reset();
  pack_header();
  pack_name("conference.buggleinc.lan");
  pack_short(16); // TXT command
  pack_short(1);  // IN class
  sendto(sock, query, pos, 0, (struct sockaddr*)&serveraddr, sizeof serveraddr);

  reset();
  recvfrom(sock, query, sizeof query, 0, (struct sockaddr*)&serveraddr, &socklen);

  /***** SRV query *****/
  reset();
  pack_header();
  pack_name("_xmpp-client._tcp.buggleinc.lan");
  pack_short(33); // SRV command
  pack_short(1);  // IN class
  sendto(sock, query, pos, 0, (struct sockaddr*)&serveraddr, sizeof serveraddr);

  reset();
  recvfrom(sock, query, sizeof query, 0, (struct sockaddr*)&serveraddr, &socklen);
  unsigned short ID, qcount, ancount, addcount;
  unpack_header(&ID, &qcount, &ancount, &addcount);
  if (ID != 42)
    fprintf(stderr, "Faulty ID %d is not 42\n", ID);
  if (qcount != 1)
    fprintf(stderr, "Faulty qcount %d is not 1\n", qcount);
  while (query[pos] != '\0')
    pos++;  // pass the name of the repeated question
  pos += 5; // pass 0x00 we just found, the type and the class

  char selected_target[512] = {0};
  int selected_priority     = 9999999;
  for (unsigned i = 0; i < ancount; i++) {
    char refname[512];
    unpack_name(query, &pos, refname);
    unpack_short(query, &pos); // type, ignored
    unpack_short(query, &pos); // class, ignored
    unpack_int();              // TTL, ignored
    int datalen = unpack_short(query, &pos);
    // fprintf(stderr, "Answer for name '%s' (type: %d, datalen: %d)\n", refname, type, datalen);

    unsigned char buffer[512];
    memcpy(buffer, query + pos, datalen);
    buffer[datalen] = '\0';
    pos += datalen;
    // Parse the content of the data
    int bufpos        = 0;
    unsigned priority = unpack_short(buffer, &bufpos);
    unpack_short(buffer, &bufpos); // weight, ignored
    unpack_short(buffer, &bufpos); // port, ignored as it's a decoy
    if (priority < selected_priority) {
      unpack_name(buffer, &bufpos, selected_target);
      selected_priority = priority;
      //            fprintf(stderr, "refname '%s' selects a new target %s of priority: %d\n", refname, selected_target, selected_priority);
    } else {
      char target[512];
      unpack_name(buffer, &bufpos, target);
      //            fprintf(stderr, "Target %s ignored because its priority %d is above the already selected target %s (prio %d)\n",
      //                    target, priority, selected_target, selected_priority);
    }
  }

  /***** A query to retrieve the IP of selected_target *****/
  reset();
  pack_header();
  pack_name(selected_target);
  pack_short(1); // A command
  pack_short(1); // IN class
  sendto(sock, query, pos, 0, (struct sockaddr*)&serveraddr, sizeof serveraddr);

  reset();
  recvfrom(sock, query, sizeof query, 0, (struct sockaddr*)&serveraddr, &socklen);
  unpack_header(&ID, &qcount, &ancount, &addcount);
  if (ID != 42)
    fprintf(stderr, "Faulty ID %d is not 42\n", ID);
  if (qcount != 1)
    fprintf(stderr, "Faulty qcount %d is not 1\n", qcount);
  if (ancount != 1)
    fprintf(stderr, "Faulty ancount %d is not 1\n", qcount);
  while (query[pos] != '\0')
    pos++;  // pass the name of the repeated question
  pos += 5; // pass 0x00 we just found, the type and the class
  {         // Parse the answer to the A query
    char refname[512];
    unpack_name(query, &pos, refname);
    unpack_short(query, &pos); // type, ignored
    unpack_short(query, &pos); // class, ignored
    unpack_int();              // TTL, ignored
    int datalen = unpack_short(query, &pos);
    if (datalen != 4)
      fprintf(stderr, "Faulty %d is not 4 when parsing the A answer\n", datalen);

    unsigned char buffer[512];
    memcpy(buffer, query + pos, datalen);
    buffer[datalen] = '\0';
    pos += datalen;
    // At least, we found the information. Parse and display it!
    printf("%d.%d.%d.%d\n", buffer[0], buffer[1], buffer[2], buffer[3]);
  }
  /* END SOLUTION */
}
/* END TEMPLATE */
