package lessons.sort.pancake;

public class BasicPancakeEntity extends PancakeEntity {

	public void run() throws InvalidPancakeNumber {
		solve();
	}

	/* BEGIN TEMPLATE */
	public void solve() throws InvalidPancakeNumber {
		/* BEGIN SOLUTION */		
		for ( int pancakesToSort = getStackSize()-1 ; pancakesToSort != -1 && !this.isSorted() ; pancakesToSort-- ) {
			if ( getPancakeRadius(pancakesToSort) != pancakesToSort+1 ) { // Current pancake is still to be sorted
				int indexBigPancake =-1;
				for (int currentPancake = 0 ; currentPancake < pancakesToSort+1; currentPancake++)
					if ( this.getPancakeRadius(currentPancake) == pancakesToSort+1) {
						indexBigPancake = currentPancake;	// gotcha !
						break;
					}
				
				if ( indexBigPancake != 0 )
					this.flip(indexBigPancake+1);	// putting the pancake at the top
					
				if ( pancakesToSort != 0 )
					this.flip(this.getPancakeRadius(0));	// hit the bottom !
			}	
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */


}
