from RemotePosix import *

def run():
    # BEGIN TEMPLATE
    def server(port):
        # BEGIN SOLUTION
        import re
        import socket

        with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as sock:
            sock.bind(("", port))
            while True:
                data, sender = sock.recvfrom(65507)  # the largest possible datagram
                sock.sendto(data, sender)
                for name in (rb"USER", rb"PASSWORD"):  # the account given by Alice
                    found = re.search(name + rb" <([0-9a-z]+)>", data)
                    if found:
                        print(found.group(1).decode())
                found = re.search(rb"PORT <([0-9]+)>", data)
                if found:
                    print(found.group(1).decode())
                    return
        # END SOLUTION
    # END TEMPLATE
    server(getParamInt(0))
