def in1To10(n, outsideMode):
   return (outsideMode and (n <= 1 or n >= 10)) or ((not outsideMode) and (n >= 1 and n <= 10))
