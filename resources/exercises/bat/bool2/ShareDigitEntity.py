def shareDigit(a, b):
   return (a/10 == b/10 or a/10 == b%10 or a%10 == b/10 or a%10 == b%10)
