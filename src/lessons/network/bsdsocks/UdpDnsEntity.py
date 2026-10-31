from RemotePosix import *

# BEGIN TEMPLATE
import socket
import struct
import sys
# BEGIN SOLUTION
SERVER = ("10.0.0.53", 53)
A, SRV = 1, 33

def encode_name(name):
    """The name in the wire format: each label preceded by its length, then a zero byte"""
    return b"".join(bytes([len(label)]) + label.encode("ascii") for label in name.split(".")) + b"\0"

def read_name(data, offset):
    """The name written at that offset (never compressed by this server), and the offset after it"""
    labels = []
    while data[offset] != 0:
        length = data[offset]
        labels.append(data[offset + 1:offset + 1 + length].decode("ascii"))
        offset += 1 + length
    return ".".join(labels), offset + 1

def query(sock, name, qtype):
    """Asks the records of that type of the name, and returns the data of each record of the answer section"""
    sock.sendto(struct.pack("!HHHHHH", 1, 0x0100, 1, 0, 0, 0) + encode_name(name) + struct.pack("!HH", qtype, 1), SERVER)
    data, sender = sock.recvfrom(512)
    answers = struct.unpack("!H", data[6:8])[0]
    offset = read_name(data, 12)[1] + 4  # after the copy of the question, with its type and class
    records = []
    for i in range(answers):
        offset = read_name(data, offset)[1]
        rtype, rclass, ttl, length = struct.unpack("!HHIH", data[offset:offset + 10])
        offset += 10
        if rtype == qtype:
            records.append(data[offset:offset + length])
        offset += length
    return records
# END SOLUTION
def run():
    # BEGIN SOLUTION
    with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as sock:
        # The XMPP servers of buggleinc.lan, the lowest priority first
        targets = sorted((struct.unpack("!H", record[:2])[0], read_name(record, 6)[0])
                         for record in query(sock, "_xmpp-client._tcp.buggleinc.lan", SRV))
        address = query(sock, targets[0][1], A)[0]
        print(".".join(str(byte) for byte in address))
    # END SOLUTION
# END TEMPLATE
