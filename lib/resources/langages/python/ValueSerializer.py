"""
Python port of plm.core.ValueSerializer (see that file for the authoritative grammar comments). Kept in sync by hand.
Java/Scala reuse the real ValueSerializer.java directly (JVM interop), but Python and C must have their own implementation.

One real difference from Java worth calling out: Python has no separate "char" type (a length-1 str IS just a str), so
serialize() never emits the "c" tag for anything -- a Python str always serializes as a quoted string. Receiving a "c"
tagged value (a primitive that *returns* a char) still works fine (see parse_char() below); only *sending* a char as a
bare Python value is not supported, since there is nothing in the language to disambiguate it from a one-character string.
No primitive targeted so far needs that direction.
"""


class Point:
  def __init__(self, x, y):
    self.x = x
    self.y = y

  def __repr__(self):
    return "Point(%r, %r)" % (self.x, self.y)


def _to_signed32(v):
  """Folds an arbitrary Python int down to Java's signed 32-bit int representation (Python ints don't wrap on their
  own), so that RGB triplets packed here compare/serialize exactly like the packed-int values coming over the wire."""
  v &= 0xFFFFFFFF
  return v - 0x100000000 if v >= 0x80000000 else v


class Color:
  """Mirrors java.awt.Color just enough for round-tripping: rgb is the same packed signed 32-bit ARGB int
  Color.getRGB() produces on the Java side."""
  def __init__(self, r, g=None, b=None):
    if g is None and b is None:
      self.rgb = r  # single packed ARGB int, e.g. as received over the wire, or Color(-1) below
    else:
      # Matches java.awt.Color(int r, int g, int b): fully opaque (alpha=255), like exercises written as
      # "new Color(255, 240, 240)" in Java/Scala expect.
      self.rgb = _to_signed32((0xFF << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF))

  def __repr__(self):
    return "Color(%r)" % (self.rgb,)

  def __eq__(self, other):
    return isinstance(other, Color) and self.rgb == other.rgb

  def __hash__(self):
    return hash(self.rgb)


# Named constants, matching java.awt.Color.X.getRGB() exactly (same values as LangC's generated Color enum), so that
# exercise code can compare getGroundColor()/getBrushColor() results against e.g. Color.orange like in any other language.
Color.white     = Color(-1)
Color.black     = Color(-16777216)
Color.blue      = Color(-16776961)
Color.cyan      = Color(-16711681)
Color.darkGray  = Color(-12566464)
Color.gray      = Color(-8355712)
Color.green     = Color(-16711936)
Color.lightGray = Color(-4144960)
Color.magenta   = Color(-65281)
Color.orange    = Color(-14336)
Color.pink      = Color(-20561)
Color.red       = Color(-65536)
Color.yellow    = Color(-256)
Color.WHITE     = Color.white     
Color.BLACK     = Color.black    
Color.BLUE      = Color.blue     
Color.CYAN      = Color.cyan     
Color.DARKGRAY  = Color.darkGray 
Color.GRAY      = Color.gray     
Color.GREEN     = Color.green    
Color.LIGHTGRAY = Color.lightGray
Color.MAGENTA   = Color.magenta  
Color.ORANGE    = Color.orange   
Color.PINK      = Color.pink     
Color.RED       = Color.red      
Color.YELLOW    = Color.yellow   

def _type_tag(v):
  if v is None:
    return "Z"
  if isinstance(v, bool):
    return "b"
  if isinstance(v, int):
    return "i"
  if isinstance(v, float):
    return "f"
  if isinstance(v, Color):
    return "C"
  if isinstance(v, Point):
    return "P"
  if isinstance(v, (str, list)):
    return ""
  raise ValueError("Unknown serializable type: %r" % (type(v),))


def serialize(o):
  if o is None:
    return "Z"

  if isinstance(o, list):
    tag = "" if len(o) == 0 else _type_tag(o[0])
    return tag + "[" + str(len(o)) + "".join(":" + serialize(v) for v in o) + "]"

  if isinstance(o, str):
    escaped = o.replace("\\", "\\\\").replace('"', '\\"')
    return '"' + escaped + '"'

  # bool must be checked before int: in Python, bool is a subclass of int.
  if isinstance(o, bool):
    return "b" + ("1" if o else "0")

  if isinstance(o, int):
    return "i" + str(o)

  if isinstance(o, float):
    return "f" + repr(o)

  if isinstance(o, Color):
    return "C" + str(o.rgb)

  if isinstance(o, Point):
    return "P" + repr(o.x) + ":" + repr(o.y)

  raise ValueError("Unknown serializable type: %r" % (type(o),))


def serialize_args(args):
  """Wraps a heterogeneous argument list the way Java's Object... varargs would: a blank-tagged bracket array, regardless
  of the actual runtime types inside. NOT the same as serialize(list(args)): serialize() derives its tag from the first
  element (mirroring Java's List<Object> behavior), which is wrong here whenever args are of mixed types -- e.g.
  send_command(..., 5, "hi") must not come out tagged "i" just because the first argument happens to be an int.
  CommandExecutor.command() parses this exact blank-tagged shape for every language uniformly."""
  return "[" + str(len(args)) + "".join(":" + serialize(a) for a in args) + "]"


def deserialize(text):
  parser = _Parser(text)
  value  = parser.parse_value()
  if not parser.is_finished():
    raise ValueError("Unexpected trailing characters.")
  return value


class _Parser:
  def __init__(self, text):
    self.text = text
    self.pos  = 0

  def is_finished(self):
    return self.pos == len(self.text)

  def _peek(self):
    return self.text[self.pos]

  def _expect(self, c):
    if self.is_finished() or self.text[self.pos] != c:
      raise ValueError("Expected '%s' at %d" % (c, self.pos))
    self.pos += 1

  @staticmethod
  def _is_number_char(c):
    return c.isdigit() or c == "-"

  @staticmethod
  def _is_double_char(c):
    return c.isdigit() or c in "-.eE+"

  def parse_value(self):
    c = self._peek()

    if c == "Z":
      self.pos += 1
      return None

    if c == '"':
      return self._parse_string()

    if c == "i":
      self.pos += 1
      if self._peek() == "[":
        return self._parse_array_contents()
      return self._parse_int()

    if c == "f":
      self.pos += 1
      if self._peek() == "[":
        return self._parse_array_contents()
      return self._parse_double()

    if c == "b":
      self.pos += 1
      if self._peek() == "[":
        return self._parse_array_contents()
      return self._parse_bool()

    if c == "c":
      self.pos += 1
      if self._peek() == "[":
        return self._parse_array_contents()
      return self._parse_char()

    if c == "C":
      self.pos += 1
      if self._peek() == "[":
        return self._parse_array_contents()
      return self._parse_color()

    if c == "P":
      self.pos += 1
      if self._peek() == "[":
        return self._parse_array_contents()
      return self._parse_point()

    if c == "[":
      return self._parse_array_contents()

    raise ValueError("Unexpected character '%s' at %d" % (c, self.pos))

  def _parse_array_contents(self):
    self._expect("[")
    size   = self._parse_unsigned_int()
    values = []
    for _ in range(size):
      self._expect(":")
      values.append(self.parse_value())
    self._expect("]")
    return values

  def _parse_int(self):
    start = self.pos
    while not self.is_finished() and self._is_number_char(self._peek()):
      self.pos += 1
    return int(self.text[start:self.pos])

  def _parse_unsigned_int(self):
    start = self.pos
    while not self.is_finished() and self._peek().isdigit():
      self.pos += 1
    return int(self.text[start:self.pos])

  def _parse_double(self):
    start = self.pos
    while not self.is_finished() and self._is_double_char(self._peek()):
      self.pos += 1
    return float(self.text[start:self.pos])

  def _parse_bool(self):
    c = self._peek()
    self.pos += 1
    if c == "0":
      return False
    if c == "1":
      return True
    raise ValueError("Invalid boolean")

  def _parse_char(self):
    c = self._peek()
    self.pos += 1
    return c

  def _parse_color(self):
    return Color(self._parse_int())

  def _parse_point(self):
    x = self._parse_double()
    self._expect(":")
    y = self._parse_double()
    return Point(x, y)

  def _parse_string(self):
    self._expect('"')
    chars = []
    while True:
      if self.is_finished():
        raise ValueError("Unterminated string")
      c = self.text[self.pos]
      self.pos += 1
      if c == "\\":
        if self.is_finished():
          raise ValueError("Invalid escape")
        chars.append(self.text[self.pos])
        self.pos += 1
      elif c == '"':
        break
      else:
        chars.append(c)
    return "".join(chars)
