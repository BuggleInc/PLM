# BEGIN TEMPLATE
def stringBits(str):
  # BEGIN SOLUTION
  res = ''
  for i in range(0,len(str),2):
    res += str[i:i+1]
  return res

# END SOLUTION
# END TEMPLATE
