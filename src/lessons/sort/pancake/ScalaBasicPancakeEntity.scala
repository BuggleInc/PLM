package lessons.sort.pancake;

import plm.core.model.Game
import plm.universe.pancake.PancakeEntity

class ScalaBasicPancakeEntity extends PancakeEntity {

	override def run() {
		solve();
	}

	/* BEGIN TEMPLATE */
	def solve() {
		/* BEGIN SOLUTION */		
		for (rank <- getStackSize()-1 to 0 by -1) {
			if (isSorted()) 
				return;
			if ( getPancakeRadius(rank) != rank+1 ) { // Current pancake is still to be sorted
				var indexBigPancake = -1
				for (currentPancake <- 0 to rank)
					if ( getPancakeRadius(currentPancake) == rank+1) 
						indexBigPancake = currentPancake;	// gotcha !

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
