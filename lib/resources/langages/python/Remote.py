"""
Python port of Remote.java (see Remote.java for the authoritative comments). The protocol (commands to the PLM, answers from the
PLM) travels over a UNIX domain socket whose path is passed as sys.argv[1] -- see connect() below, called from the generated
Main.py.
"""

import socket
import sys

from ValueSerializer import serialize, serialize_args, deserialize

_sockFile = None


def connect(socketPath):
  global _sockFile
  try:
    sock = socket.socket(socket.AF_UNIX, socket.SOCK_STREAM)
    sock.connect(socketPath)
    _sockFile = sock.makefile("rw", encoding="utf-8", newline="\n")
  except OSError as e:
    print("Cannot connect to the PLM protocol socket '%s': %s" % (socketPath, e), file=sys.stderr)
    sys.exit(1)


_answerBuffer = None


def _getAnswerLine():
  global _answerBuffer
  try:
    line = _sockFile.readline()
  except OSError as e:
    print("IO exception while reading the protocol (reason: %s). Bailing out." % (e,), file=sys.stderr)
    sys.exit(1)
    return
  if line == "":
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


def sendCommand(opCode, name, *args):
  command = str(opCode) + " " + serialize_args(list(args)) + " " + str(name)
  _sockFile.write(command + "\n")
  _sockFile.flush()


def errorMsg(msg):
  """Reports a student-facing error (e.g. misuse of a restricted function, or a wrong move detected by the
  correction itself). Always printed to stderr. Also reported via seenError() when that primitive is available for
  the current universe -- not every universe has it yet (only Buggle and Turmite, as of this writing), so this looks
  it up in the caller's own module globals (populated by e.g. "from RemoteBuggle import *") rather than assuming it
  exists; globals() here would only ever see Remote.py's own namespace, never Entity.py's, so a plain "if seenError
  in globals()" check would never find it even when it's genuinely available.
  """
  print(msg, file=sys.stderr)
  callerGlobals = sys._getframe(1).f_globals
  if "seenError" in callerGlobals:
    callerGlobals["seenError"](msg)
  else:
    sys.error.write(msg)
