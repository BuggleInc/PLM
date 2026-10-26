/* The POSIX API of the world: the same functions as the system ones, acting on the world of the exercise instead, plus the
 * output functions of stdio.h on the standard streams, unbuffered, and fopen() and fdopen() giving real streams of the C
 * library (buffered as usual) on the descriptors of the world. The system headers come first, so that the renaming below
 * only applies to the code that follows (the student's one). */
#ifdef _WIN32
#include <fcntl.h>
#include <io.h>
#include <winsock2.h>
#include <ws2tcpip.h>
#define SHUT_RD SD_RECEIVE
#define SHUT_WR SD_SEND
#define SHUT_RDWR SD_BOTH
#define STDIN_FILENO 0
#define STDOUT_FILENO 1
#define STDERR_FILENO 2
#else
#include <arpa/inet.h>
#include <fcntl.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <unistd.h>
#endif
#include <errno.h>
#include <stdint.h>
#include <stdio.h>
#include <sys/types.h>

int plm_socket(int domain, int type, int protocol);
int plm_bind(int fd, const struct sockaddr* addr, socklen_t len);
int plm_listen(int fd, int backlog);
int plm_accept(int fd, struct sockaddr* addr, socklen_t* len);
int plm_connect(int fd, const struct sockaddr* addr, socklen_t len);
ssize_t plm_send(int fd, const void* buf, size_t len, int flags);
ssize_t plm_recv(int fd, void* buf, size_t len, int flags);
int plm_shutdown(int fd, int how);
int plm_open(const char* path, int flags, ...);
ssize_t plm_read(int fd, void* buf, size_t count);
ssize_t plm_write(int fd, const void* buf, size_t count);
int plm_close(int fd);
int plm_unlink(const char* path);
FILE* plm_fopen(const char* path, const char* mode);
FILE* plm_fdopen(int fd, const char* mode);
unsigned int plm_sleep(unsigned int seconds);
int plm_printf(const char* format, ...);
int plm_fprintf(FILE* stream, const char* format, ...);
int plm_puts(const char* s);
int plm_fputs(const char* s, FILE* stream);
int plm_putchar(int c);
void plm_perror(const char* msg);
/* The parameters of the world, for the code that calls the student's function (the string is to be freed) */
char* getParamString(int i);
int getParamInt(int i);

#define socket plm_socket
#define bind plm_bind
#define listen plm_listen
#define accept plm_accept
#define connect plm_connect
#define send plm_send
#define recv plm_recv
#define shutdown plm_shutdown
#define open plm_open
#define read plm_read
#define write plm_write
#define close plm_close
#define unlink plm_unlink
#define fopen plm_fopen
#define fdopen plm_fdopen
#define sleep plm_sleep
#define printf plm_printf
#define fprintf plm_fprintf
#define puts plm_puts
#define fputs plm_fputs
#define putchar plm_putchar
#define perror plm_perror
