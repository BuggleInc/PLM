package lessons.sort.pancake;

import plm.universe.pancake.PancakeEntity;

public class BubblePancakeEntity extends PancakeEntity {
	
	public void run() {
		this.solve();
	}
	
	/* BEGIN TEMPLATE */
	public void solve() {
		/* BEGIN SOLUTION */
		int stackSize = getStackSize();
		boolean swapped;
		
		do {
			swapped = false; 
			
			for(int rank = 0; rank < stackSize - 1; rank++) { // Check all pancakes
				
				if(getPancakeRadius(rank) > getPancakeRadius(rank + 1)) {
					// When the pancake above is bigger than the next one...
					
					swapped = true; // We have to check all the pancakes again next time
					
					flip(rank + 2); // Flip all the pancakes to get the two get on top
					flip(2); // Flip the two pancakes to sort
					flip(rank + 2); // Flip all the pancakes back in place
				}
			}
		} while (swapped);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
