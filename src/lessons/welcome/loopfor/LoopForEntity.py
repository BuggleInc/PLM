from RemoteBuggle import *
def run():
    # BEGIN SOLUTION
    cpt = 0

    while not isOverBaggle():
      cpt = cpt+1
      step()
    pickupBaggle()
    for i in range(cpt):
      stepBack()
    dropBaggle()
    # END SOLUTION
