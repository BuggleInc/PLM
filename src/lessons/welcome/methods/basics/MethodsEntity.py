from RemoteBuggle import *
def run():
    # BEGIN TEMPLATE

    # Add your code here

    # BEGIN SOLUTION
    def goAndGet():
      cpt = 0
      
      while isOverBaggle() == False:
        cpt = cpt+1
        step()
      pickupBaggle()
      for i in range(cpt):
        stepBack()
      dropBaggle()
    # END SOLUTION

    for i in range(7):
      goAndGet()
      right()
      step()
      left()
    # END TEMPLATE