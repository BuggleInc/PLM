def altPairs(str):
  res = ''
  for i in range(0,len(str),4):
    res += str[i:i+2]
  return res
