def stringX(str):
  res = ''
  for i in range(len(str)):
    if str[i] != 'x' or i == 0 or i == len(str)-1:
      res += str[i:i+1]
  return res
