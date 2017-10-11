def blueTicket(a, b, c):
  ab = a + b
  ac = a + c
  bc = b + c
  if (ab == 10 or ac == 10 or bc == 10):
    return 10
  elif (ab == (bc + 10) or ab == (ac + 10)):
    return 5
  else:
    return 0
