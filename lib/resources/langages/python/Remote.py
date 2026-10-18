"""
Python port of Remote.java (see Remote.java for the authoritative comments). The protocol (commands to the PLM, answers from the
PLM) travels over a TCP socket on 127.0.0.1, whose port is passed as sys.argv[1] -- see connect() below, called from the
generated Main.py. The PLM only listens on that address.
"""

import socket
import sys

from ValueSerializer import serialize, serialize_args, deserialize

_sockFile = None


def connect(port):
  global _sockFile
  try:
    sock = socket.create_connection(("127.0.0.1", int(port)))
    sock.setsockopt(socket.IPPROTO_TCP, socket.TCP_NODELAY, 1)  # each command is a short request awaiting its answer
    _sockFile = sock.makefile("rw", encoding="utf-8", newline="\n")
  except OSError as e:
    print("Cannot connect to the PLM protocol on port %s: %s" % (port, e), file=sys.stderr)
    sys.exit(1)


_answerBuffer = None


def _getAnswerLine():
  global _answerBuffer
  try:
    line = _sockFile.readline()
  except OSError as e:
    print("IO exception while reading the protocol (reason: %s). Bailing out." % (e,), file=sys.stderr)
    sys.exit(1)
  if line == "":
    print("The PLM closed the protocol connection. Bailing out.", file=sys.stderr)
    sys.exit(1)
  _answerBuffer = line.rstrip("\n")


def getAnswerInt():
  _getAnswerLine()
  return deserialize(_answerBuffer)


def getAnswerBoolean():
  _getAnswerLine()
  return deserialize(_answerBuffer)


def getAnswerDouble():
  _getAnswerLine()
  return deserialize(_answerBuffer)


def getAnswerColor():
  _getAnswerLine()
  return deserialize(_answerBuffer)


def getAnswerString():
  _getAnswerLine()
  return deserialize(_answerBuffer)


def getAnswerChar():
  _getAnswerLine()
  return deserialize(_answerBuffer)


def getAnswerObject():
  """Generic escape hatch for return types Remote.py can't name specifically -- see Remote.java's getAnswerObject()."""
  _getAnswerLine()
  return deserialize(_answerBuffer)


def sendCommand(name, *args):
  command = str(name) + " " + serialize_args(list(args))
  _sockFile.write(command + "\n")
  _sockFile.flush()
