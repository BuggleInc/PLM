# BEGIN TEMPLATE
def shareDigit(a, b):
   # BEGIN SOLUTION
   return (a/10 == b/10 or a/10 == b%10 or a%10 == b/10 or a%10 == b%10)

# END SOLUTION
# END TEMPLATE
