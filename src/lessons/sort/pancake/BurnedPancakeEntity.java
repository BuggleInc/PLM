package lessons.sort.pancake;

import plm.universe.pancake.PancakeEntity;

public class BurnedPancakeEntity extends PancakeEntity {
	
	public void run() {
		this.solve();
	}

	/* BEGIN TEMPLATE */
	public void solve() {
		/* BEGIN SOLUTION */
		int stackSize = this.getStackSize();
		for ( int rank = stackSize-1 ; rank != -1 && !isSorted(); rank-- ) {
			
			if ( getPancakeRadius(rank)!=rank+1 || isPancakeUpsideDown(rank)) { // current pancake not sorted yet
				int indexBigPancake =-1;
				for ( int iter = 0 ; iter < rank+1; iter++)
					if ( getPancakeRadius(iter) == rank+1) {
						indexBigPancake = iter;	// gotcha !
						break;
					}
				
				if ( indexBigPancake != 0 )
					flip(indexBigPancake+1);	// move that pancake to the top
					
				if ( ! ( rank == 0) ) {
					if (!isPancakeUpsideDown(0)) 
						flip(1);	// show your dark side to the world
					flip(getPancakeRadius(0));	// hit the bottom !
				} else {
					if (isPancakeUpsideDown(0)) 
						flip(1);	// show your dark side to the world
					
				}
			}	
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
