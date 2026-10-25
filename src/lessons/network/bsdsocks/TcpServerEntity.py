from RemotePosix import *

def run():
    # BEGIN TEMPLATE
    def server(port, message):
        # BEGIN SOLUTION
        import socket

        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as master:
            master.bind(("", port))
            master.listen(5)
            client, address = master.accept()
            with client:
                client.sendall(message.encode())
        # END SOLUTION
    # END TEMPLATE
    server(getParamInt(0), getParamString(1))
