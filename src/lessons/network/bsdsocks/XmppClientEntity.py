from RemotePosix import *

# BEGIN TEMPLATE
import base64
import socket
import xml.etree.ElementTree as ElementTree
# BEGIN SOLUTION
CLIENT = "{jabber:client}"  # the namespace of the stanzas, before their names in ElementTree


def stanzas(sock):
    """Gives the top-level elements of the stream of the server (its features and its stanzas), each once it arrived whole"""
    parser = ElementTree.XMLPullParser(events=("start", "end"))
    depth = 0
    while True:
        for event, element in parser.read_events():
            if event == "start":
                depth += 1
            else:
                depth -= 1
                if depth == 1:  # back in the stream, at the end of one of its children
                    yield element
        data = sock.recv(4096)
        if not data:  # the end of the stream
            return
        parser.feed(data)
        parser.flush()  # parses this data at once: expat may otherwise keep a short end of tag until more data comes


def body(stanza):
    """The text of the body of a message, or an empty text"""
    element = stanza.find(CLIENT + "body")
    if element is None or element.text is None:
        return ""
    return element.text
# END SOLUTION


def client(server, port, room, password):
    # BEGIN SOLUTION
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.connect((server, port))
    reader = stanzas(sock)
    sock.sendall(b"<?xml version='1.0'?><stream:stream to='buggleinc.lan' xmlns='jabber:client' "
                 b"xmlns:stream='http://etherx.jabber.org/streams' version='1.0'>")
    next(reader)  # the features of the stream

    credentials = base64.b64encode(b"\0newcomer\0" + password.encode("ascii"))
    sock.sendall(b"<auth xmlns='urn:ietf:params:xml:ns:xmpp-sasl' mechanism='PLAIN'>" + credentials + b"</auth>")
    next(reader)  # <success/>

    join = "<presence to='" + room + "/newcomer'><x xmlns='http://jabber.org/protocol/muc'/></presence>"
    sock.sendall(join.encode("ascii"))
    stanza = next(reader)
    while stanza.find(CLIENT + "subject") is None:  # the subject of the room ends the joining
        stanza = next(reader)

    while not body(stanza).startswith("newcomer:"):  # the friend of Bob calls the newcomer
        stanza = next(reader)
    friend = stanza.get("from").split("/")[1]
    message = "<message to='" + room + "' type='groupchat'><body>" + friend + ": Bob sent me for the treasure.</body></message>"
    sock.sendall(message.encode("ascii"))

    while stanza.get("from") != room + "/" + friend or "<http" not in body(stanza):
        stanza = next(reader)
    text = body(stanza)
    start = text.find("<http") + 1
    print(text[start:text.find(">", start)])

    sock.sendall(b"</stream:stream>")
    for stanza in reader:  # up to the end of the stream of the server
        pass
    sock.close()
    # END SOLUTION
# END TEMPLATE


def run():
    client(getParamString(0), getParamInt(1), getParamString(2), getParamString(3))
