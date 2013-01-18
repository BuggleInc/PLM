package lessons.recursion;

import jlm.universe.pancake.PancakeEntity;
import jlm.universe.pancake.PancakeNumberException;
import jlm.universe.pancake.PancakeWorld;

public class PancakeBoardEntity extends PancakeEntity {

	/* BEGIN TEMPLATE */
	public void run() throws PancakeNumberException {
		this.solve();
	}
	
	
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
				int index = this.findBiggestPancake(pancakesToSort);
				this.flip(index+1);	// putting the pancake at the top
				if ( !this.isPancakeUpsideDown(0))
				{
					this.flip(1);	// show your dark side to the world
				}
				this.flip(this.getPancakeSize(0));	// hit the bottom !
			}	
		}
		
	}
	
	
	/*
	 *  We are looking for the next big thing !
	 *  And we know that it is somewhere in the remaining stack !
	 */
	private int findBiggestPancake(int numberOfPancakesNotSorted) {
		int indexBigPancake =-1;
		boolean found = false;
		for ( int j = 0 ; j < numberOfPancakesNotSorted && !found; j++)
		{
			if ( this.getPancakeSize(j) == numberOfPancakesNotSorted)
			{
				indexBigPancake = j;	// gotcha !
				found = true;
			}
		}
		return indexBigPancake;
		/* END SOLUTION */
	}
	

	/* END TEMPLATE */
	
}
