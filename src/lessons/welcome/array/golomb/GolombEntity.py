def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(golomb(param[0])))

# BEGIN TEMPLATE
def golomb(num):
# BEGIN SOLUTION
  if num==1:
    return 1
  else:
    return 1+golomb(num-golomb(golomb(num-1)))
# END SOLUTION
# END TEMPLATE
