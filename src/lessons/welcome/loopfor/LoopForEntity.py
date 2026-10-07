from RemoteBuggle import *
def run():
    # BEGIN SOLUTION
    cpt = 0

    while not isOverBaggle():
      cpt = cpt+1
      forward()
    pickupBaggle()
    for i in range(cpt):
      backward()
    dropBaggle()
    # END SOLUTION
