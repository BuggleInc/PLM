//RemotePancake

/* BEGIN TEMPLATE */
void solve() {
	/* BEGIN SOLUTION */
	int rank,currentPancake;
	for (rank = getStackSize()-1 ; rank != -1 && !isSorted() ; rank-- ) {
		if ( getPancakeRadius(rank) != rank+1 ) { // Current pancake is still to be sorted
			int indexBigPancake =-1;
			for (currentPancake = 0 ; currentPancake < rank+1; currentPancake++)
				if ( getPancakeRadius(currentPancake) == rank+1) {
					indexBigPancake = currentPancake;	// gotcha !
					break;
				}

			if ( indexBigPancake != 0 )
				flip(indexBigPancake+1);	// putting the pancake at the top

			if ( rank != 0 )
				flip(getPancakeRadius(0));	// hit the bottom now !
		}
	}
	/* END SOLUTION */
}
/* END TEMPLATE */

void run(){
	solve();
}
