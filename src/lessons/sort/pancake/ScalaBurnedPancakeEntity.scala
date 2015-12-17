package lessons.sort.pancake;

import plm.core.model.Game
import plm.universe.pancake.PancakeEntity

class ScalaBurnedPancakeEntity extends PancakeEntity {


	override def run() {
		solve();
	}

	/* BEGIN TEMPLATE */
	def solve() {
		/* BEGIN SOLUTION */
		val stackSize = getStackSize();
		for ( rank <- stackSize-1 to 0 by -1) {
			
			if ( getPancakeRadius(rank)!=rank+1 || isPancakeUpsideDown(rank)) { // current pancake not sorted yet
				var indexBigPancake = -1;
				for (currentPancake <- 0 to rank)
					if ( getPancakeRadius(currentPancake) == rank+1) 
						indexBigPancake = currentPancake;	// gotcha !
				
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
