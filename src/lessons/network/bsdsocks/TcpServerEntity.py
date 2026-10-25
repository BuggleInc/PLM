from RemotePosix import *

def run():
    # BEGIN TEMPLATE
    def server(port):
        # BEGIN SOLUTION
        import socket

        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as master:
            master.bind(("", port))
            master.listen(5)
            client, address = master.accept()
            with client:
                # The line may arrive in several packets: receive until its '\n' (or until the client closes the connection)
                line = b""
                while b"\n" not in line:
                    data = client.recv(512)
                    if not data:
                        break
                    line += data
                client.sendall(line * 2)  # the doubling echo
        # END SOLUTION
    # END TEMPLATE
    server(getParamInt(0))
