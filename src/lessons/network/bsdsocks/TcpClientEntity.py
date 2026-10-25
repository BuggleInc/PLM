from RemotePosix import *

def run():
    # BEGIN TEMPLATE
    def client(server, port):
        # BEGIN SOLUTION
        import socket
        import sys
        import time

        while True:
            sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            try:
                sock.connect((server, port))
                break
            except ConnectionRefusedError:  # the server is not ready yet: retry one second later, with a new socket
                sock.close()
                time.sleep(1)

        with sock:
            while data := sock.recv(512):
                sys.stdout.buffer.write(data)
        # END SOLUTION
    # END TEMPLATE
    client(getParamString(0), getParamInt(1))
