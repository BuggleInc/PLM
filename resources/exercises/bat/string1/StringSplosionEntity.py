def stringSplosion(str):
  res = ''
  for i in range(len(str)):
    res += str[0:i+1]
  return res
