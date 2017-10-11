def stringBits(str):
  res = ''
  for i in range(0,len(str),2):
    res += str[i:i+1]
  return res
