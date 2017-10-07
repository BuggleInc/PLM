//RemoteBaseball

int findPlayerPos(int base, int color);
int findPlayerBase(int start, int color);
void bringPlayersHome(int base);

/* BEGIN TEMPLATE */
void run() {
	/* BEGIN SOLUTION */
	int base;
	for (base = 0 ; base < getBasesAmount() -1 ; base++)
		bringPlayersHome(base);

	assertSorted("selection sort");
	/* END SOLUTION */
}
/* END TEMPLATE */
/* BEGIN HIDDEN */

void bringPlayersHome(int base) {
	int positionToFill;
	for ( positionToFill= 0; positionToFill<getPositionsAmount(); positionToFill++) {

		if (getPlayerColor(base, positionToFill) == base)
			continue; // already home

		// search for the player on the ground
		int playerBase = findPlayerBase(base,base);
		int playerPos = findPlayerPos(playerBase, base);



		// bring the hole to the other position of that base
		while (getHoleBase() != playerBase) {
			if (getHoleBase()> playerBase) {
				move(getHoleBase()-1,(playerPos+1)%2);
			} else {
				move(getHoleBase()+1,(playerPos+1)%2);
			}
		}


		if (playerBase == base) {
			// Already in the base. Bring it to its position
			move(base,(positionToFill+1)%2);

		} else while (playerBase != base) { // bring the player to the base next to its home
			move (playerBase-1, positionToFill);
			move (playerBase, findPlayerPos(playerBase, base));
			if (playerBase-1 != base) {
				move (playerBase-1, (positionToFill+1) %2);
			}
			playerBase--;

		}
	}
}

int findPlayerBase(int start, int color) {
	int playerBase, pos;
	for (playerBase=start+1;playerBase<getBasesAmount();playerBase++){
		for (pos=0; pos<getPositionsAmount();pos++){
			if (getPlayerColor(playerBase, pos) == color){
				return playerBase;
			}
		}
	}
	return -1;
}

int findPlayerPos(int base, int color) {
	int pos;
	for (pos=0; pos<getPositionsAmount();pos++){
		if (getPlayerColor(base, pos) == color){
			return pos;
		}
	}
	return -1;
}
/* END HIDDEN */

