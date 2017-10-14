# BEGIN TEMPLATE
def stringYak(str):
  # BEGIN SOLUTION
  res = ''
  i=0
  while i<len(str):
    if i+2<len(str)  and str[i] == 'y' and str[i+2]=='k':
      i += 2
    else:
      res += str[i]
    i+=1
  return res

# END SOLUTION
# END TEMPLATE
