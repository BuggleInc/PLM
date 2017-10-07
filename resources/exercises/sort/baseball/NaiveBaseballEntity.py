# BEGIN SOLUTION
while not isSorted():
	baseNext = (getHoleBase()+1) % getBasesAmount()
	posNext = -1
	maxDistance = -1
	for pos in range(getPositionsAmount()):
		player = getPlayerColor(baseNext, pos)
		distance = (baseNext - player + getBasesAmount()) % getBasesAmount()
		if distance > maxDistance:
			maxDistance = distance
			posNext = pos
	move(baseNext,posNext);
# END SOLUTION