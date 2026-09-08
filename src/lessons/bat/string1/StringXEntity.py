from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(stringX(param[0])))

# BEGIN TEMPLATE
def stringX(str):
# BEGIN SOLUTION
  res = ''
  for i in range(len(str)):
    if str[i] != 'x' or i == 0 or i == len(str)-1:
      res += str[i:i+1]
  return res
# END SOLUTION
# END TEMPLATE
