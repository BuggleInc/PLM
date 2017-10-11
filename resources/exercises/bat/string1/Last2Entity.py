def last2(str):
  l = len(str)
  if l < 2:
    return 0
  end = str[l-2:l]
  count = 0
  for i in range(len(str)-2):
    if str[i:i+2] == end:
      count += 1
  return count
