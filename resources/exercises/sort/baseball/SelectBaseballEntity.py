# BEGIN SOLUTION
def bringPlayersHome(base):
	for positionToFill in range(getPositionsAmount()):
		if getPlayerColor(base, positionToFill) == base:
			continue # already home
	
		# search for the player on the ground
		playerBase = findPlayerBase(base,base);
		playerPos = findPlayerPos(playerBase, base);
		
		# bring the hole to the other position of that base
		while getHoleBase() != playerBase:
			if getHoleBase()> playerBase:
				move(getHoleBase()-1,(playerPos+1)%2);
			else:
				move(getHoleBase()+1,(playerPos+1)%2);
		
		if playerBase == base:
			# Already in the base. Bring it to its position
			move(base,(positionToFill+1)%2)
			
		else:
			while playerBase != base: # bring the player to the base next to its home
				move (playerBase-1, positionToFill);
				move (playerBase, findPlayerPos(playerBase, base));
				if playerBase-1 != base:
					move (playerBase-1, (positionToFill+1) %2);
				playerBase -= 1

def findPlayerBase(start, color):
	for playerBase in range(start+1,getBasesAmount()):
		for pos in range(getPositionsAmount()):
			if getPlayerColor(playerBase, pos) == color:
				return playerBase;
	raise Exception("cannot find any player of color "+color+" starting at base "+start);

def findPlayerPos(base, color):
	for pos in range(getPositionsAmount()):
		if getPlayerColor(base, pos) == color:
			return pos;
	raise Exception("cannot find any player of color "+color+" within base "+base);

for base in range(getBasesAmount() -1): 
	bringPlayersHome(base);

# END SOLUTION	