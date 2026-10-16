from RemoteBuggle import *
def run():
    # BEGIN SOLUTION
    def endingPosition():
    	if not isFacingWall():
    		return False
    	else:
    		res = False
    		left()
    		if isFacingWall():
    			res = True
    		right()
    		return res

    def snakeStep():
    	if isFacingWall():
    		if getDirection() == Direction.EAST:
    			left()
    			step()
    			left()
    		else:
    			right()
    			step()
    			right()
    	else:
    		step()

    brushDown()
    while not endingPosition():
    	snakeStep()
    # END SOLUTION
