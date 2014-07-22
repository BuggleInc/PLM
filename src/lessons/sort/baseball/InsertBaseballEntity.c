//RemoteBaseball

void out(char* msg);
int getHoleInsert();
void moveInsert(int pos);
int getPlayerColorInsert(int pos);

/* BEGIN TEMPLATE */
void run() {
	/* BEGIN SOLUTION */
	/* Bring the hole in 0,1 */
	if (getHoleInsert() == 0) // It is already on base 0, but on another position
		moveInsert(1);
	while (getHoleInsert() > 1)
		moveInsert(getHoleInsert()-1);
	int player;
	for (player = 2; player < getBasesAmount()*getPositionsAmount(); player ++) {
		//out("Sort player "+player);

		//out("Compare "+(getHole()+1)+":"+getPlayerColor(getHole()+1)+" < "+(getHole()-1)+":"+getPlayerColor(getHole()-1));
		while (getHoleInsert()>0 && getPlayerColorInsert(getHoleInsert()+1) < getPlayerColorInsert(getHoleInsert()-1)) {
			int center = getHoleInsert();// ...2x1... with ascending positions from left to right
			moveInsert(center+1);        // ...21x...
			moveInsert(center-1);        // ...x12...
		}
		while (getHoleInsert() != player)
			moveInsert(getHoleInsert()+1);
	}
	assertSorted("insertion sort");
	/* END SOLUTION */
}
/* END TEMPLATE */

/* BEGIN HIDDEN */

int getPlayerColorInsert(int pos) {
	return getPlayerColor(pos / getPositionsAmount(), pos % getPositionsAmount());
}

void moveInsert(int pos) {
	move(pos / getPositionsAmount(), pos % getPositionsAmount());
}

int getHoleInsert() {
	return getPositionsAmount()*getHoleBase()+getHolePosition();
}

void out(char* msg) {
	if (isSelected()) {
		printf("100 %s\n",msg);
		fflush(stdout);
	}

}
/* END HIDDEN */

