# The POSIX API of the world, in place of the system one: the socket module, the descriptor functions of os, time.sleep() and the
# standard output and error act on the world of the exercise. They are substituted at import, so that the student's code imports
# and uses them as usual.
import errno
import io
import os
import sys
import time
import traceback
import types

__all__ = ["getParamString", "getParamInt"]  # for the code that calls the student's function, who imports the usual modules

AF_INET, SOCK_STREAM, IPPROTO_TCP = 2, 1, 6
SHUT_RD, SHUT_WR, SHUT_RDWR = 0, 1, 2
_PLM_O_CREAT, _PLM_O_TRUNC, _PLM_O_APPEND = 0o100, 0o1000, 0o2000

getParamString = _plm_remote_getParamString
getParamInt = _plm_remote_getParamInt


def _checked(result):
    """The result of a system call of the world, or raises the OSError of its failure (e.g. ConnectionRefusedError)"""
    if result is None or (isinstance(result, int) and result < 0):
        code = getattr(errno, _plm_remote_lastError(), errno.EINVAL)
        raise OSError(code, os.strerror(code))
    return result


def _ip_and_port(address):
    """The IP and the port of an address (host, port), where the empty host stands for any interface"""
    host, port = address
    return host or "0.0.0.0", port


def _open(path, flags, mode=0o777):
    # The access mode (O_RDONLY, O_WRONLY or O_RDWR) is 0, 1 or 2 on every system. The mode of the created files does not matter.
    protocol_flags = (flags & 3) | (_PLM_O_CREAT if flags & os.O_CREAT else 0) | (_PLM_O_TRUNC if flags & os.O_TRUNC else 0) | (
        _PLM_O_APPEND if flags & os.O_APPEND else 0)
    return _checked(_plm_remote_open(os.fspath(path), protocol_flags))


def _read(fd, n):
    return _checked(_plm_remote_read(fd, n))


def _write(fd, data):
    return _checked(_plm_remote_write(fd, bytes(memoryview(data))))


def _close(fd):
    _checked(_plm_remote_close(fd))


def _sleep(seconds):
    _plm_remote_sleep(int(seconds))  # the world counts whole seconds


class _Descriptor(io.RawIOBase):
    """A descriptor of the world, as the binary stream under a standard stream"""

    def __init__(self, fd):
        super().__init__()
        self._fd = fd

    def fileno(self):
        return self._fd

    def writable(self):
        return True

    def write(self, data):
        return _write(self._fd, data)


class socket:
    """A socket of the world, with the interface of socket.socket"""

    def __init__(self, family=AF_INET, type=SOCK_STREAM, proto=0, fileno=None):
        if fileno is None:
            fileno = _checked(_plm_remote_socket(family, type, 0 if proto == IPPROTO_TCP else proto))
        self._fd = fileno

    def __enter__(self):
        return self

    def __exit__(self, *exc_info):
        self.close()

    def fileno(self):
        return self._fd

    def bind(self, address):
        _checked(_plm_remote_bind(self._fd, *_ip_and_port(address)))

    def listen(self, backlog=128):
        _checked(_plm_remote_listen(self._fd, backlog))

    def accept(self):
        # The world does not tell the address of the peer
        return socket(fileno=_checked(_plm_remote_accept(self._fd))), ("0.0.0.0", 0)

    def connect(self, address):
        _checked(_plm_remote_connect(self._fd, *_ip_and_port(address)))

    def send(self, data, flags=0):
        return _checked(_plm_remote_send(self._fd, bytes(memoryview(data)), flags))

    def sendall(self, data, flags=0):
        data = bytes(memoryview(data))
        while data:
            data = data[self.send(data, flags):]

    def recv(self, bufsize, flags=0):
        return _checked(_plm_remote_recv(self._fd, bufsize, flags))

    def shutdown(self, how):
        _checked(_plm_remote_shutdown(self._fd, how))

    def close(self):
        # As in Python, closing a closed socket does nothing
        if self._fd >= 0:
            fd, self._fd = self._fd, -1
            _close(fd)


def create_connection(address):
    """A socket connected to the address, closed if the connection fails"""
    sock = socket()
    try:
        sock.connect(address)
    except OSError:
        sock.close()
        raise
    return sock


def create_server(address, *, backlog=128):
    """A socket listening on the address, closed if that fails"""
    sock = socket()
    try:
        sock.bind(address)
        sock.listen(backlog)
    except OSError:
        sock.close()
        raise
    return sock


_socket_module = types.ModuleType("socket", "The sockets of the world")
_socket_module.__dict__.update(socket=socket, create_connection=create_connection, create_server=create_server, error=OSError, AF_INET=AF_INET,
                               SOCK_STREAM=SOCK_STREAM, IPPROTO_TCP=IPPROTO_TCP, SHUT_RD=SHUT_RD, SHUT_WR=SHUT_WR, SHUT_RDWR=SHUT_RDWR)
sys.modules["socket"] = _socket_module
os.open, os.read, os.write, os.close, time.sleep = _open, _read, _write, _close, _sleep

# The standard streams of the world write each line at once, as on a terminal, and their binary buffer is not buffered. Their line
# breaks are not translated. The uncaught exceptions still reach the PLM console, through the standard error of the process.
sys.stdout, sys.stderr = (io.TextIOWrapper(_Descriptor(fd), encoding="utf-8", newline="\n", line_buffering=True) for fd in (1, 2))
sys.excepthook = lambda *exc_info: traceback.print_exception(*exc_info, file=sys.__stderr__)
