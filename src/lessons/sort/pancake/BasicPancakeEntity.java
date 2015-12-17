package lessons.sort.pancake;

import plm.universe.pancake.PancakeEntity;

public class BasicPancakeEntity extends PancakeEntity {
	
	public void run() {
		solve();
	}

	/* BEGIN TEMPLATE */
	public void solve() {
		/* BEGIN SOLUTION */		
		for ( int rank = getStackSize()-1 ; rank != -1 && !this.isSorted() ; rank-- ) {
			if ( getPancakeRadius(rank) != rank+1 ) { // Current pancake is still to be sorted
				int indexBigPancake =-1;
				for (int currentPancake = 0 ; currentPancake < rank+1; currentPancake++)
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


}
