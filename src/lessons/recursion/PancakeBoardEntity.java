package lessons.recursion;

import jlm.universe.pancake.PancakeEntity;
import jlm.universe.pancake.PancakeNumberException;
import jlm.universe.pancake.PancakeWorld;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 * @version 1.2
 */
public class PancakeBoardEntity extends PancakeEntity {

	public void run() throws PancakeNumberException {
		this.solve();
	}
	
	/* BEGIN TEMPLATE */
	public void solve() throws PancakeNumberException{
		/* BEGIN SOLUTION */
		PancakeWorld pancakeWorld = (PancakeWorld) world;
		int stackSize = this.getStackSize();
		boolean currentPancakeAlreadySorted;
		for ( int pancakesToSort = stackSize ; pancakesToSort != -1 &&! pancakeWorld.isSorted(); pancakesToSort-- )
		{
			currentPancakeAlreadySorted= (this.getPancakeSize(pancakesToSort-1)==pancakesToSort 
					&& !this.isPancakeUpsideDown(pancakesToSort-1) ) ;
			if ( !currentPancakeAlreadySorted)
			{
				int indexBigPancake =-1;
				boolean found = false;
				for ( int currentPancake = 0 ; currentPancake < pancakesToSort && !found; currentPancake++)
				{
					if ( this.getPancakeSize(currentPancake) == pancakesToSort)
					{
						indexBigPancake = currentPancake;	// gotcha !
						found = true;
					}
				}
				this.flip(indexBigPancake+1);	// putting the pancake at the top
				if ( !this.isPancakeUpsideDown(0))
				{
					this.flip(1);	// show your dark side to the world
				}
				this.flip(this.getPancakeSize(0));	// hit the bottom !
			}	
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	
}
