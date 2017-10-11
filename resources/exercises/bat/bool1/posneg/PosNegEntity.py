def posNeg(a, b, negative):
  if (negative):
    return a<0 and b<0;
  return (a<0 and b>0) or (a>0 and b<0)
