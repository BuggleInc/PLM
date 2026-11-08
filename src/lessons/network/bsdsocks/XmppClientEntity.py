from RemotePosix import *

# BEGIN TEMPLATE
import base64
import socket
# BEGIN SOLUTION


def stanzas(stream):
    """Gives each stanza of the stream of the server as a text, once it arrived whole"""
    depth = 0  # 0 before the header of the stream, 1 inside it, 2 or more inside a stanza
    stanza = b""
    kind = b""  # the byte after the < of the tag being read, or b"" out of a tag
    previous = b""
    while True:
        if depth == 1 and stanza == b"":
            while stream.peek(1)[:1].isspace():  # the spaces between the stanzas
                stream.read(1)
        byte = stream.read(1)
        if byte == b"":  # the end of the stream
            return
        stanza += byte
        if previous == b"<":
            kind = byte
        elif byte == b">" and kind != b"":  # the end of a tag
            before = depth
            if kind == b"/":  # a closing tag
                depth -= 1
            elif kind != b"?" and previous != b"/":  # an opening tag, unless the XML declaration or a self-closing tag
                depth += 1
            kind = b""
            if before == 0:  # the XML declaration or the header of the stream, which are no stanzas
                stanza = b""
            elif depth == 0:  # </stream:stream>
                return
            elif depth == 1:
                yield stanza.decode()
                stanza = b""
        previous = byte


def body(stanza):
    """The text of the body of a stanza, or an empty text"""
    if "<body>" not in stanza:
        return ""
    return stanza[stanza.index("<body>") + len("<body>"):stanza.index("</body>")]


def sent_by(stanza, sender):
    """Whether the stanza itself (not a message that it quotes) was sent by that address"""
    return ' from="' + sender + '"' in stanza[:stanza.index(">")]
# END SOLUTION


def client(server, port, room, password, friend):
    # BEGIN SOLUTION
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.connect((server, port))
    stream = sock.makefile("rb")
    reader = stanzas(stream)
    sock.sendall(b"<?xml version='1.0'?><stream:stream to='buggleinc.lan' xmlns='jabber:client' "
                 b"xmlns:stream='http://etherx.jabber.org/streams' version='1.0'>")
    next(reader)  # the features of the stream

    credentials = base64.b64encode(b"\0newcomer\0" + password.encode("ascii"))
    sock.sendall(b"<auth xmlns='urn:ietf:params:xml:ns:xmpp-sasl' mechanism='PLAIN'>" + credentials + b"</auth>")
    next(reader)  # <success/>

    join = "<presence to='" + room + "/newcomer'><x xmlns='http://jabber.org/protocol/muc'/></presence>"
    sock.sendall(join.encode("ascii"))
    stanza = next(reader)
    while "<subject>" not in stanza:  # the subject of the room ends the joining
        stanza = next(reader)

    stanza = next(reader)
    while not sent_by(stanza, room + "/" + friend):  # the friend of Bob calls the newcomer once he is back
        stanza = next(reader)
    message = "<message to='" + room + "' type='groupchat'><body>" + friend + ": Bob sent me for the treasure.</body></message>"
    sock.sendall(message.encode("ascii"))

    while not sent_by(stanza, room + "/" + friend) or "[http" not in body(stanza):
        stanza = next(reader)
    text = body(stanza)
    start = text.index("[http") + 1
    print(text[start:text.index("]", start)])

    sock.sendall(b"</stream:stream>")
    for stanza in reader:  # up to the end of the stream of the server
        pass
    stream.close()
    sock.close()
    # END SOLUTION
# END TEMPLATE


def run():
    client(getParamString(0), getParamInt(1), getParamString(2), getParamString(3), getParamString(4))
