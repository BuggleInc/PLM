# BEGIN TEMPLATE
def stringSplosion(str):
  # BEGIN SOLUTION
  res = ''
  for i in range(len(str)):
    res += str[0:i+1]
  return res

# END SOLUTION
# END TEMPLATE
