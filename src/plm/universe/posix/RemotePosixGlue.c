/* The constants of the protocol, which are the Linux ones whatever the system of the student */
enum { PLM_AF_INET = 2, PLM_SOCK_STREAM = 1, PLM_SOCK_DGRAM = 2, PLM_O_CREAT = 0100, PLM_O_TRUNC = 01000, PLM_O_APPEND = 02000 };

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
                {"EPROTONOSUPPORT", EPROTONOSUPPORT},
                {"EOPNOTSUPP", EOPNOTSUPP},
                {"EDESTADDRREQ", EDESTADDRREQ},
                {"EMSGSIZE", EMSGSIZE}};
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

/* Fills addr with the address ip:port (only its family if ip is NULL), cut to *len bytes, and sets *len to its full size */
static void set_address(struct sockaddr* addr, socklen_t* len, const char* ip, int port)
{
  if (addr == NULL || len == NULL)
    return;
  struct sockaddr_in in;
  memset(&in, 0, sizeof(in));
  in.sin_family = AF_INET;
  if (ip != NULL) {
    inet_pton(AF_INET, ip, &in.sin_addr);
    in.sin_port = htons((uint16_t)port);
  }
  memcpy(addr, &in, *len < (socklen_t)sizeof(in) ? (size_t)*len : sizeof(in));
  *len = sizeof(in);
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
  // IPPROTO_TCP and IPPROTO_UDP are the default protocols of SOCK_STREAM and SOCK_DGRAM
  int default_protocol = type == SOCK_STREAM ? IPPROTO_TCP : IPPROTO_UDP;
  int protocol_type    = type == SOCK_STREAM ? PLM_SOCK_STREAM : type == SOCK_DGRAM ? PLM_SOCK_DGRAM : -1;
  return checked(plm_remote_socket(domain == AF_INET ? PLM_AF_INET : -1, protocol_type, protocol == default_protocol ? 0 : protocol));
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
  if (res >= 0)
    set_address(addr, len, NULL, 0); // the world does not tell the address of the peer: only its family is filled
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

ssize_t plm_sendto(int fd, const void* buf, size_t len, int flags, const struct sockaddr* addr, socklen_t addrlen)
{
  if (addr == NULL) // to the destination given by connect()
    return plm_send(fd, buf, len, flags);
  char ip[INET_ADDRSTRLEN];
  int port;
  if (address_of(addr, addrlen, ip, &port) < 0)
    return -1;
  Bytes data = {(unsigned char*)buf, len};
  return checked(plm_remote_sendto(fd, data, flags, ip, port));
}

ssize_t plm_recvfrom(int fd, void* buf, size_t len, int flags, struct sockaddr* addr, socklen_t* addrlen)
{
  ssize_t res = received(plm_remote_recvfrom(fd, (int)len, flags), buf, len);
  if (res >= 0 && addr != NULL) {
    char* sender = plm_remote_lastSender(); // as ip:port
    char* colon  = strrchr(sender, ':');
    *colon       = '\0';
    set_address(addr, addrlen, sender, atoi(colon + 1));
    free(sender);
  }
  return res;
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

int plm_unlink(const char* path)
{
  return checked(plm_remote_unlink((char*)path));
}

/* The glue calls the real functions of stdio.h by their names, and the ones of the world by their plm_ names */
#undef vfprintf
#ifdef PLM_OWN_STREAMS
#undef fgetc
#undef ungetc
#undef fgets
#undef fread
#undef fwrite
#undef fflush
#undef fclose
#undef feof
#undef ferror
#undef clearerr
#undef fileno
#undef setvbuf

/* A stream of the glue on a descriptor of the world, whose address is given as a FILE* */
typedef struct own_stream {
  int fd;
  int readable, writable;
  int buffering; // _IOFBF, _IOLBF or _IONBF
  int eof, error;
  size_t start, end; // the unread part of in
  size_t pending;    // the part of out not written yet
  char in[BUFSIZ];
  char out[BUFSIZ];
  struct own_stream* next;
} own_stream;

static own_stream* own_streams; // the open ones

/* The stream of the glue at that address, or NULL for a stream of the C library */
static own_stream* own(FILE* stream)
{
  for (own_stream* s = own_streams; s != NULL; s = s->next)
    if ((FILE*)s == stream)
      return s;
  return NULL;
}

/* Writes the pending output, returning 0, or EOF on error (the output that could not be written is kept) */
static int drain(own_stream* s)
{
  size_t done = 0;
  while (done < s->pending) {
    ssize_t res = plm_write(s->fd, s->out + done, s->pending - done);
    if (res <= 0) {
      memmove(s->out, s->out + done, s->pending - done);
      s->pending -= done;
      s->error = 1;
      return EOF;
    }
    done += (size_t)res;
  }
  s->pending = 0;
  return 0;
}

/* Reads more input once the previous one is consumed, returning whether some is available */
static int refill(own_stream* s)
{
  if (s->start < s->end)
    return 1;
  if (!s->readable) {
    errno    = EBADF;
    s->error = 1;
    return 0;
  }
  ssize_t res = plm_read(s->fd, s->in, sizeof s->in);
  if (res <= 0) {
    if (res == 0)
      s->eof = 1;
    else
      s->error = 1;
    return 0;
  }
  s->start = 0;
  s->end   = (size_t)res;
  return 1;
}

/* Buffers the data, written according to the buffering of the stream, and returns how much was accepted */
static size_t own_write(own_stream* s, const char* data, size_t len)
{
  if (!s->writable) {
    errno    = EBADF;
    s->error = 1;
    return 0;
  }
  size_t done = 0;
  while (done < len) {
    if (s->pending == sizeof s->out && drain(s) == EOF)
      return done;
    size_t chunk = len - done < sizeof s->out - s->pending ? len - done : sizeof s->out - s->pending;
    memcpy(s->out + s->pending, data + done, chunk);
    s->pending += chunk;
    done += chunk;
  }
  if (s->buffering == _IONBF || (s->buffering == _IOLBF && memchr(data, '\n', len) != NULL))
    drain(s);
  return done;
}

/* exit() writes the pending output, as the C library does for its streams */
static void drain_at_exit(void)
{
  plm_fflush(NULL);
}

FILE* plm_fdopen(int fd, const char* mode)
{
  static int registered = 0;
  if (mode[0] == '\0' || strchr("rwa", mode[0]) == NULL) {
    errno = EINVAL;
    return NULL;
  }
  own_stream* s = calloc(1, sizeof *s);
  if (s == NULL)
    return NULL;
  s->fd        = fd;
  s->readable  = mode[0] == 'r' || strchr(mode, '+') != NULL;
  s->writable  = mode[0] != 'r' || strchr(mode, '+') != NULL;
  s->buffering = _IOFBF;
  s->next      = own_streams;
  own_streams  = s;
  if (!registered)
    registered = atexit(drain_at_exit) == 0;
  return (FILE*)s;
}

int plm_fgetc(FILE* stream)
{
  own_stream* s = own(stream);
  if (s == NULL)
    return fgetc(stream);
  return refill(s) ? (unsigned char)s->in[s->start++] : EOF;
}

int plm_getc(FILE* stream)
{
  return plm_fgetc(stream);
}

int plm_ungetc(int c, FILE* stream)
{
  own_stream* s = own(stream);
  if (s == NULL)
    return ungetc(c, stream);
  if (c == EOF || (s->start == 0 && s->end == sizeof s->in))
    return EOF;
  if (s->start == 0) {
    memmove(s->in + 1, s->in, s->end);
    s->start = 1;
    s->end++;
  }
  s->in[--s->start] = (char)c;
  s->eof            = 0;
  return (unsigned char)c;
}

char* plm_fgets(char* buf, int size, FILE* stream)
{
  own_stream* s = own(stream);
  if (s == NULL)
    return fgets(buf, size, stream);
  if (size <= 0)
    return NULL;
  int len = 0;
  while (len < size - 1 && refill(s)) {
    buf[len] = s->in[s->start++];
    if (buf[len++] == '\n')
      break;
  }
  if (len == 0 && size > 1)
    return NULL; // at the end of the stream, or on error
  buf[len] = '\0';
  return buf;
}

size_t plm_fread(void* buf, size_t size, size_t count, FILE* stream)
{
  own_stream* s = own(stream);
  if (s == NULL)
    return fread(buf, size, count, stream);
  size_t total = size * count, done = 0;
  while (done < total && refill(s)) {
    size_t chunk = total - done < s->end - s->start ? total - done : s->end - s->start;
    memcpy((char*)buf + done, s->in + s->start, chunk);
    s->start += chunk;
    done += chunk;
  }
  return size == 0 ? 0 : done / size;
}

size_t plm_fwrite(const void* buf, size_t size, size_t count, FILE* stream)
{
  own_stream* s = own(stream);
  if (s == NULL)
    return fwrite(buf, size, count, stream);
  return size == 0 ? 0 : own_write(s, buf, size * count) / size;
}

int plm_fputc(int c, FILE* stream)
{
  unsigned char byte = (unsigned char)c;
  return plm_fwrite(&byte, 1, 1, stream) == 1 ? byte : EOF;
}

int plm_putc(int c, FILE* stream)
{
  return plm_fputc(c, stream);
}

int plm_fscanf(FILE* stream, const char* format, ...)
{
  int res = EOF;
  if (own(stream) == NULL) {
    va_list args;
    va_start(args, format);
    res = vfscanf(stream, format, args);
    va_end(args);
  } else {
    static int told = 0; // the explanation goes once to the PLM console (the real standard error, not the world's one)
    if (!told) {
      const char* why = "PLM: fscanf() is not available on the streams of fopen() and fdopen() on this system (such as Windows). "
                        "Use fgets() and sscanf() instead, or run the PLM on Linux or macOS.\n";
      fwrite(why, 1, strlen(why), stderr);
      told = 1;
    }
    errno = ENOSYS;
  }
  return res;
}

int plm_fflush(FILE* stream)
{
  if (stream == NULL) { // all the streams
    int res = fflush(NULL);
    for (own_stream* s = own_streams; s != NULL; s = s->next)
      if (drain(s) == EOF)
        res = EOF;
    return res;
  }
  own_stream* s = own(stream);
  return s == NULL ? fflush(stream) : drain(s);
}

int plm_fclose(FILE* stream)
{
  own_stream* s = own(stream);
  if (s == NULL)
    return fclose(stream);
  int res = drain(s);
  if (plm_close(s->fd) < 0)
    res = EOF;
  for (own_stream** p = &own_streams; *p != NULL; p = &(*p)->next)
    if (*p == s) {
      *p = s->next;
      break;
    }
  free(s);
  return res;
}

int plm_feof(FILE* stream)
{
  own_stream* s = own(stream);
  return s == NULL ? feof(stream) : s->eof;
}

int plm_ferror(FILE* stream)
{
  own_stream* s = own(stream);
  return s == NULL ? ferror(stream) : s->error;
}

void plm_clearerr(FILE* stream)
{
  own_stream* s = own(stream);
  if (s == NULL)
    clearerr(stream);
  else
    s->eof = s->error = 0;
}

int plm_fileno(FILE* stream)
{
  own_stream* s = own(stream);
  return s == NULL ? fileno(stream) : s->fd;
}

int plm_setvbuf(FILE* stream, char* buf, int mode, size_t size)
{
  own_stream* s = own(stream);
  if (s == NULL)
    return setvbuf(stream, buf, mode, size);
  if (mode != _IOFBF && mode != _IOLBF && mode != _IONBF)
    return -1;
  s->buffering = mode; // the stream keeps its own buffer
  return 0;
}

void plm_setbuf(FILE* stream, char* buf)
{
  plm_setvbuf(stream, buf, buf == NULL ? _IONBF : _IOFBF, BUFSIZ);
}

#else
/* The functions of the streams on the descriptors of the world, the descriptor being their cookie */
#if defined(__GLIBC__)
static ssize_t cookie_read(void* cookie, char* buf, size_t size)
{
  return plm_read((int)(intptr_t)cookie, buf, size);
}

static ssize_t cookie_write(void* cookie, const char* buf, size_t size)
{
  return plm_write((int)(intptr_t)cookie, buf, size);
}
#else
static int cookie_read(void* cookie, char* buf, int size)
{
  return (int)plm_read((int)(intptr_t)cookie, buf, (size_t)size);
}

static int cookie_write(void* cookie, const char* buf, int size)
{
  return (int)plm_write((int)(intptr_t)cookie, buf, (size_t)size);
}
#endif

static int cookie_close(void* cookie)
{
  return plm_close((int)(intptr_t)cookie);
}

FILE* plm_fdopen(int fd, const char* mode)
{
#if defined(__GLIBC__)
  cookie_io_functions_t functions = {cookie_read, cookie_write, NULL, cookie_close};
  return fopencookie((void*)(intptr_t)fd, mode, functions);
#else
  (void)mode;
  return funopen((void*)(intptr_t)fd, cookie_read, cookie_write, NULL, cookie_close);
#endif
}
#endif

FILE* plm_fopen(const char* path, const char* mode)
{
  int flags;
  switch (mode[0]) {
    case 'r':
      flags = O_RDONLY;
      break;
    case 'w':
      flags = O_WRONLY | O_CREAT | O_TRUNC;
      break;
    case 'a':
      flags = O_WRONLY | O_CREAT | O_APPEND;
      break;
    default:
      errno = EINVAL;
      return NULL;
  }
  if (strchr(mode, '+') != NULL)
    flags = (flags & ~O_WRONLY) | O_RDWR;
  int fd = plm_open(path, flags);
  if (fd < 0)
    return NULL;
  FILE* res = plm_fdopen(fd, mode);
  if (res == NULL) {
    int error = errno;
    plm_close(fd);
    errno = error;
  }
  return res;
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

/* The formatted text, to be freed, whose length is stored in len, or NULL (with a negative len) if the format is wrong */
static char* formatted(const char* format, va_list args, int* len)
{
  va_list copy;
  va_copy(copy, args);
  *len = vsnprintf(NULL, 0, format, copy);
  va_end(copy);
  if (*len < 0)
    return NULL;
  char* text = malloc(*len + 1);
  vsnprintf(text, *len + 1, format, args);
  return text;
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
  int res = plm_vfprintf(stdout, format, args);
  va_end(args);
  return res;
}

int plm_fprintf(FILE* stream, const char* format, ...)
{
  va_list args;
  va_start(args, format);
  int res = plm_vfprintf(stream, format, args);
  va_end(args);
  return res;
}

/* Writes on the standard output and error of the world at once, as they are not buffered */
int plm_vfprintf(FILE* stream, const char* format, va_list args)
{
  int len;
#ifdef PLM_OWN_STREAMS
  own_stream* s = own(stream);
  if (s != NULL) {
    char* text = formatted(format, args, &len);
    if (text != NULL && own_write(s, text, (size_t)len) < (size_t)len)
      len = -1;
    free(text);
    return len;
  }
#endif
  int fd = descriptor_of(stream);
  if (fd < 0)
    return vfprintf(stream, format, args);
  char* text = formatted(format, args, &len);
  if (text != NULL)
    len = (int)plm_write(fd, text, len);
  free(text);
  return len;
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
