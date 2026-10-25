/* The constants of the protocol, which are the Linux ones whatever the system of the student */
enum { PLM_AF_INET = 2, PLM_SOCK_STREAM = 1, PLM_O_CREAT = 0100, PLM_O_TRUNC = 01000, PLM_O_APPEND = 02000 };

/* Sets errno from the name of the last error in the world, and returns -1 */
static int failed(void)
{
  static const struct {
    const char* name;
    int value;
  } errors[] = {{"EBADF", EBADF},
                {"ENOENT", ENOENT},
                {"EINVAL", EINVAL},
                {"ENOTSOCK", ENOTSOCK},
                {"ENOTCONN", ENOTCONN},
                {"ECONNREFUSED", ECONNREFUSED},
                {"EADDRINUSE", EADDRINUSE},
                {"EISCONN", EISCONN},
                {"EAFNOSUPPORT", EAFNOSUPPORT},
                {"EPROTONOSUPPORT", EPROTONOSUPPORT}};
  char* name = plm_remote_lastError();
  errno      = EINVAL;
  for (size_t i = 0; i < sizeof(errors) / sizeof(errors[0]); i++)
    if (strcmp(name, errors[i].name) == 0)
      errno = errors[i].value;
  free(name);
  return -1;
}

static int checked(int result)
{
  return result < 0 ? failed() : result;
}

/* Reads the IPv4 address and the port (in host byte order) of a struct sockaddr_in, or returns -1 with errno set */
static int address_of(const struct sockaddr* addr, socklen_t len, char ip[INET_ADDRSTRLEN], int* port)
{
  if (addr == NULL || len < (socklen_t)sizeof(struct sockaddr_in)) {
    errno = EINVAL;
    return -1;
  }
  if (addr->sa_family != AF_INET) {
    errno = EAFNOSUPPORT;
    return -1;
  }
  const struct sockaddr_in* in = (const struct sockaddr_in*)addr;
  inet_ntop(AF_INET, &in->sin_addr, ip, INET_ADDRSTRLEN);
  *port = ntohs(in->sin_port); // a forgotten htons() gives the wrong port in the world, as on a real system
  return 0;
}

/* Copies the received bytes into buf (at most len of them), and returns their amount, or -1 with errno set */
static ssize_t received(Bytes data, void* buf, size_t len)
{
  if (data.data == NULL)
    return failed();
  size_t size = data.size < len ? data.size : len;
  memcpy(buf, data.data, size);
  free(data.data);
  return (ssize_t)size;
}

int plm_socket(int domain, int type, int protocol)
{
  // IPPROTO_TCP is the default protocol of SOCK_STREAM
  return checked(plm_remote_socket(domain == AF_INET ? PLM_AF_INET : -1, type == SOCK_STREAM ? PLM_SOCK_STREAM : -1, protocol == IPPROTO_TCP ? 0 : protocol));
}

int plm_bind(int fd, const struct sockaddr* addr, socklen_t len)
{
  char ip[INET_ADDRSTRLEN];
  int port;
  if (address_of(addr, len, ip, &port) < 0)
    return -1;
  return checked(plm_remote_bind(fd, ip, port));
}

int plm_listen(int fd, int backlog)
{
  return checked(plm_remote_listen(fd, backlog));
}

int plm_accept(int fd, struct sockaddr* addr, socklen_t* len)
{
  int res = plm_remote_accept(fd);
  if (res >= 0 && addr != NULL && len != NULL) {
    // The world does not tell the address of the peer: only its family is filled
    struct sockaddr_in in;
    memset(&in, 0, sizeof(in));
    in.sin_family = AF_INET;
    memcpy(addr, &in, *len < (socklen_t)sizeof(in) ? (size_t)*len : sizeof(in));
    *len = sizeof(in);
  }
  return checked(res);
}

int plm_connect(int fd, const struct sockaddr* addr, socklen_t len)
{
  char ip[INET_ADDRSTRLEN];
  int port;
  if (address_of(addr, len, ip, &port) < 0)
    return -1;
  return checked(plm_remote_connect(fd, ip, port));
}

ssize_t plm_send(int fd, const void* buf, size_t len, int flags)
{
  Bytes data = {(unsigned char*)buf, len};
  return checked(plm_remote_send(fd, data, flags));
}

ssize_t plm_recv(int fd, void* buf, size_t len, int flags)
{
  return received(plm_remote_recv(fd, (int)len, flags), buf, len);
}

int plm_shutdown(int fd, int how)
{
  return checked(plm_remote_shutdown(fd, how));
}

int plm_open(const char* path, int flags, ...)
{
  // The access mode (O_RDONLY, O_WRONLY or O_RDWR) is 0, 1 or 2 on every system. The mode of the created files does not matter.
  int protocol_flags = (flags & 3) | (flags & O_CREAT ? PLM_O_CREAT : 0) | (flags & O_TRUNC ? PLM_O_TRUNC : 0) | (flags & O_APPEND ? PLM_O_APPEND : 0);
  return checked(plm_remote_open((char*)path, protocol_flags));
}

ssize_t plm_read(int fd, void* buf, size_t count)
{
  return received(plm_remote_read(fd, (int)count), buf, count);
}

ssize_t plm_write(int fd, const void* buf, size_t count)
{
  Bytes data = {(unsigned char*)buf, count};
  return checked(plm_remote_write(fd, data));
}

int plm_close(int fd)
{
  return checked(plm_remote_close(fd));
}

char* getParamString(int i)
{
  return plm_remote_getParamString(i);
}

int getParamInt(int i)
{
  return plm_remote_getParamInt(i);
}

unsigned int plm_sleep(unsigned int seconds)
{
  plm_remote_sleep((int)seconds);
  return 0;
}

/* Writes the formatted text on a descriptor of the world, at once: the standard streams of the world are not buffered */
static int formatted(int fd, const char* format, va_list args)
{
  va_list copy;
  va_copy(copy, args);
  int len = vsnprintf(NULL, 0, format, copy);
  va_end(copy);
  if (len < 0)
    return len;
  char* text = malloc(len + 1);
  vsnprintf(text, len + 1, format, args);
  int res = (int)plm_write(fd, text, len);
  free(text);
  return res;
}

/* The descriptor of the world for the standard output and error, or -1 for the other streams (left to the system) */
static int descriptor_of(FILE* stream)
{
  return stream == stdout ? STDOUT_FILENO : stream == stderr ? STDERR_FILENO : -1;
}

int plm_printf(const char* format, ...)
{
  va_list args;
  va_start(args, format);
  int res = formatted(STDOUT_FILENO, format, args);
  va_end(args);
  return res;
}

int plm_fprintf(FILE* stream, const char* format, ...)
{
  va_list args;
  va_start(args, format);
  int fd  = descriptor_of(stream);
  int res = fd < 0 ? vfprintf(stream, format, args) : formatted(fd, format, args);
  va_end(args);
  return res;
}

int plm_puts(const char* s)
{
  return plm_printf("%s\n", s);
}

int plm_fputs(const char* s, FILE* stream)
{
  return plm_fprintf(stream, "%s", s);
}

int plm_putchar(int c)
{
  unsigned char byte = (unsigned char)c;
  return plm_write(STDOUT_FILENO, &byte, 1) == 1 ? c : EOF;
}

void plm_perror(const char* msg)
{
  int error = errno; // writing the message must not change the error
  if (msg != NULL && *msg != '\0')
    plm_fprintf(stderr, "%s: %s\n", msg, strerror(error));
  else
    plm_fprintf(stderr, "%s\n", strerror(error));
  errno = error;
}
