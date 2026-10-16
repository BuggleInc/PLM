from RemoteBuggle import *
def run():
    # BEGIN SOLUTION
    cpt = 0
    while cpt != 4:
    	step()
    	if isOverBaggle():
    		cpt += 1
    # END SOLUTION 
