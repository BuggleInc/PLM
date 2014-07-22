//RemoteBaseball

/* BEGIN TEMPLATE */
void run() {
	/* BEGIN SOLUTION */
	while (!isSorted()) {
		while (getHoleBase()>0) {
			int maxPos = 0;
			int maxColor = getPlayerColor(getHoleBase()-1, 0);
			int pos;
			for (pos=1;pos<getPositionsAmount();pos++)
				if (getPlayerColor(getHoleBase()-1, pos) > maxColor) {
					maxColor = getPlayerColor(getHoleBase()-1, pos);
					maxPos = pos;
				}
			move(getHoleBase()-1,maxPos);
		}
		while (getHoleBase()<getBasesAmount()-1) {
			int minPos = 0;
			int minColor = getPlayerColor(getHoleBase()+1, 0);
			int pos;
			for (pos=1;pos<getPositionsAmount();pos++)
				if (getPlayerColor(getHoleBase()+1, pos) < minColor) {
					minColor = getPlayerColor(getHoleBase()+1, pos);
					minPos = pos;
				}
			move(getHoleBase()+1,minPos);
		}
	}
	assertSorted("bubble sort");
	/* END SOLUTION */
}
/* END TEMPLATE */

