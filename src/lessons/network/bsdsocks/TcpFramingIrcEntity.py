from RemotePosix import *

# BEGIN TEMPLATE
import socket
import sys
import time
# BEGIN SOLUTION
def lines(sock):
    buffer = b""
    while True:
        end = buffer.find(b"\r\n")
        if end >= 0:
            line, buffer = buffer[:end], buffer[end + 2:]
            yield line.decode("ascii")
        else:
            data = sock.recv(4096)
            if not data:             # end of the stream
                return
            buffer += data
# END SOLUTION
def run():
    # BEGIN SOLUTION
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.connect(("10.0.0.1", 194))

    reader = lines(sock)
    data = next(reader)
    
    sock.sendall(b"NICK student\r\n")
    sock.sendall(b"USER student 0 * :myself.\r\n")
    while "001" not in data:
    	data = next(reader)

    sock.sendall(b"JOIN #welcome\r\n")
    while "366" not in data:
    	data = next(reader)

    data=""
    sock.sendall(b"PRIVMSG #welcome :Bob: Please show me the way to the treasure trove.\r\n")
    while "xmpp:" not in data:
    	data = next(reader)
    	
    begin = data.find("xmpp:")  # the URL, between < and >
    print(data[begin:data.find(">", begin)])

    sock.close()
# END SOLUTION
# END TEMPLATE
