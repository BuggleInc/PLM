def golomb(num):
  if num==1:
    return 1
  else:
    return 1+golomb(num-golomb(golomb(num-1)))
