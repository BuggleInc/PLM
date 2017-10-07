# BEGIN SOLUTION
while not isSorted():
	while getHoleBase()>0:
		maxPos = 0
		maxColor = getPlayerColor(getHoleBase()-1, 0)
		for pos in range(getPositionsAmount()):
			if getPlayerColor(getHoleBase()-1, pos) > maxColor:
				maxColor = getPlayerColor(getHoleBase()-1, pos)
				maxPos = pos
		move(getHoleBase()-1,maxPos);
	while getHoleBase()<getBasesAmount()-1:
		minPos = 0
		minColor = getPlayerColor(getHoleBase()+1, 0)
		for pos in range(getPositionsAmount()):
			if getPlayerColor(getHoleBase()+1, pos) < minColor:
				minColor = getPlayerColor(getHoleBase()+1, pos)
				minPos = pos
		move(getHoleBase()+1,minPos)
# END SOLUTION 
