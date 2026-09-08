from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(island(param[0])))

# BEGIN TEMPLATE
def island(num):
# BEGIN SOLUTION
  nbisland=0
  for i in range(len(num)-1):
    if num[i]<num[i+1]:
      nbisland=nbisland+1
  return nbisland
# END SOLUTION
# END TEMPLATE
