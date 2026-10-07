from RemoteBuggle import *
def run():
    # BEGIN SOLUTION
    cpt = 0
    while cpt != 4:
    	forward()
    	if isOverBaggle():
    		cpt += 1
    # END SOLUTION 
