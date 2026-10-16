from RemoteBuggle import *
def run():
    # BEGIN SOLUTION 
    cpt = 0
    while not isOverBaggle():
      cpt += 1 
      step()
    pickupBaggle()
    while cpt>0:
      stepBack()
      cpt -= 1
    dropBaggle()
    # END SOLUTION
