from RemotePosix import *

# BEGIN TEMPLATE
import socket
import struct
# BEGIN SOLUTION
SERVER = ("10.0.0.53", 53)
A, TXT, SRV = 1, 16, 33

def encode_name(name):
    """The name in the wire format: each label preceded by its length, then a zero byte"""
    result = b""
    for label in name.split("."):
        result += bytes([len(label)]) + label.encode("ascii")
    return result + b"\0"

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
    header = struct.pack("!HHHHHH", 1, 0x0100, 1, 0, 0, 0)       # ID 1, RD, one question
    question = encode_name(name) + struct.pack("!HH", qtype, 1)  # class IN
    sock.sendto(header + question, SERVER)

    data, sender = sock.recvfrom(512)
    answers = struct.unpack("!H", data[6:8])[0]
    _, offset = read_name(data, 12)
    offset += 4  # after the copy of the question, with its type and class
    records = []
    for i in range(answers):
        _, offset = read_name(data, offset)
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
        # The hints followed while exploring, which this program does not need to read: the notes of the zone lead to
        # conference.buggleinc.lan, whose notes lead to the SRV record of the XMPP service
        query(sock, "buggleinc.lan", TXT)
        query(sock, "conference.buggleinc.lan", TXT)

        # The XMPP server of buggleinc.lan with the lowest priority
        best_priority = None
        best_target = None
        for record in query(sock, "_xmpp-client._tcp.buggleinc.lan", SRV):
            priority = struct.unpack("!H", record[0:2])[0]
            if best_priority is None or priority < best_priority:
                best_priority = priority
                best_target, _ = read_name(record, 6)

        address = query(sock, best_target, A)[0]
        print(f"{address[0]}.{address[1]}.{address[2]}.{address[3]}")
    # END SOLUTION
# END TEMPLATE
