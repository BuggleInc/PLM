package lessons.sort.pancake;

import plm.core.model.Game
import plm.universe.pancake.PancakeEntity

class ScalaBubblePancakeEntity extends PancakeEntity {

	override def run() {
		solve();
	}

	/* BEGIN TEMPLATE */
	def solve() {
		/* BEGIN SOLUTION */		
    val stackSize = getStackSize();
    var swapped=false;
    
    do {
      swapped = false; 
      
      for(rank <- 0 to getStackSize()-2) { // Check all pancakes
        
        if(getPancakeRadius(rank) > getPancakeRadius(rank + 1)) {
          // When the pancake above is bigger than the next one...
          
          swapped = true; // We have to check all the pancakes again next time
          
          flip(rank + 2); // Flip all the pancakes to get the two get on top
          flip(2); // Flip the two pancakes to sort
          flip(rank + 2); // Flip all the pancakes back in place
        }
      }
    } while (swapped)
		/* END SOLUTION */
	}
	/* END TEMPLATE */


}
