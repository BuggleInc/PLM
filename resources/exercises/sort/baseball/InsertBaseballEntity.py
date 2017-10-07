# BEGIN SOLUTION
def getColor(pos):
	return getPlayerColor(pos / getPositionsAmount(), pos % getPositionsAmount())
def doMove(pos):
	move(pos / getPositionsAmount(), pos % getPositionsAmount())
def getHole():
		return getPositionsAmount()*getHoleBase()+getHolePosition()


# Bring the hole in 0,1
while getHole() > 1:
	doMove(getHole()-1)
if getHole() == 0: # It was probably already on base 0, but on another position
	doMove(1)

for player in range(2,getBasesAmount()*getPositionsAmount()):
	while getHole()>0 and getColor(getHole()+1) < getColor(getHole()-1):
		center = getHole() # ...2x1... with ascending positions from left to right
		doMove(center+1)     # ...21x...
		doMove(center-1)     # ...x12...
	while getHole() != player:
		doMove(getHole()+1)
# END SOLUTION