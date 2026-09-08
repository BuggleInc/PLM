from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(frontTimes(param[0], param[1])))

# BEGIN TEMPLATE
def frontTimes(str, n):
# BEGIN SOLUTION
  frontLen = 3
  if frontLen > len(str):
    frontLen = len(str)
  front = ''
  if len(str)>0:
    front = str[0:frontLen]
  res = ''
  for i in range(n):
    res += front
  return res
# END SOLUTION
# END TEMPLATE
