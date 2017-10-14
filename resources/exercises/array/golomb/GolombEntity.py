# BEGIN TEMPLATE
def golomb(num):
  # BEGIN SOLUTION
  if num==1:
    return 1
  else:
    return 1+golomb(num-golomb(golomb(num-1)))

# END SOLUTION
# END TEMPLATE
