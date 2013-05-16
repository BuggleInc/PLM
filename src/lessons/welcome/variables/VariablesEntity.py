# BEGIN SOLUTION 
cpt = 0
while isOverBaggle() == False:
  cpt = cpt +1
  forward()
pickUpBaggle()
while cpt>0:
  backward()
  cpt = cpt-1
dropBaggle()
# END TEMPLATE 
