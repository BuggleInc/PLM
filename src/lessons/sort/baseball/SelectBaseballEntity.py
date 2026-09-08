from RemoteSort import *
def run():
    # BEGIN SOLUTION
    for base in range(getBasesAmount() -1): 
    	bringPlayersHome(base)

    assertSorted("selection sort")
    # END SOLUTION	