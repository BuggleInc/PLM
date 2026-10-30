from RemotePosix import *

def run():
    # BEGIN TEMPLATE
    def client(server, port, request):
        # BEGIN SOLUTION
        import socket
        import sys

        with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as sock:
            sock.sendto(request.encode(), (server, port))
            data, sender = sock.recvfrom(65507)  # the largest possible datagram
            sys.stdout.buffer.write(data)
        # END SOLUTION
    # END TEMPLATE
    client(getParamString(0), getParamInt(1), getParamString(2))
