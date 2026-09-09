"""Python port of lessons.recursion.cons.universe.RecList.java (see that file for the authoritative version)."""


class RecList:
  def __init__(self, head, tail):
    self.head = head
    self.tail = tail

  def __repr__(self):
    parts = []
    ptr = self
    while ptr is not None:
      parts.append(str(ptr.head))
      ptr = ptr.tail
    return " [" + ", ".join(parts) + "] "

  def plmInsiderLength(self):
    res     = 0
    ptr = self
    while ptr is not None:
      ptr = ptr.tail
      res += 1
    return res

def cons(head, tail):
  return RecList(head, tail)


def recListFromArray(a, rank=0):
  """Builds a RecList from a plain Python list of ints -- the deserialized wire form of a test parameter."""
  if rank >= len(a):
    return None
  return RecList(a[rank], recListFromArray(a, rank + 1))


def toRecListIfArray(a):
  """applied to EVERY test parameter uniformly (see the generated run() in each Cons exercise).
  Safe no-op for parameters that are not lists (e.g. a plain int second argument)."""
  if isinstance(a, list):
    return recListFromArray(a)
  return a

def RecListToArray(self):
  result = []
  ptr = self
  while ptr is not None:
    result.append(ptr.head)
    ptr = ptr.tail
  return result
