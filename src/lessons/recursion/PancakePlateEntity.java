package lessons.recursion;

import jlm.universe.pancake.PancakeEntity;
import jlm.universe.pancake.PancakeWorld;
import jlm.universe.pancake.InvalidPancakeNumber;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 * @version 1.2
 */
public class PancakePlateEntity extends PancakeEntity {

	public void run() throws InvalidPancakeNumber {
		try
		{
		this.solve();
		}
		catch( InvalidPancakeNumber pne)
		{
			System.out.println(this.world.getName() +" :"+pne.getMessage());
		}
	}
	
	/* BEGIN TEMPLATE */
	public void solve() throws InvalidPancakeNumber {
		/* BEGIN SOLUTION */
		PancakeWorld myWorld = (PancakeWorld) this.world;
		int stackSize = this.getStackSize();
		boolean currentPancakeAlreadySorted;
		for ( int pancakesToSort = stackSize-1 ; pancakesToSort != -1 && !myWorld.isSorted() ; pancakesToSort-- )
		{
			currentPancakeAlreadySorted= (this.getPancakeSize(pancakesToSort)==pancakesToSort+1 
					&& !this.isPancakeUpsideDown(pancakesToSort) ) ;
			if ( !currentPancakeAlreadySorted)
			{
				int indexBigPancake =-1;
				boolean found = false;
				for ( int currentPancake = 0 ; currentPancake < pancakesToSort+1 && !found; currentPancake++)
				{
					if ( this.getPancakeSize(currentPancake) == pancakesToSort+1)
					{
						indexBigPancake = currentPancake;	// gotcha !
						found = true;
					}
				}
				if ( indexBigPancake != 0 )
				{
					this.flip(indexBigPancake+1);	// putting the pancake at the top
				}	
				if ( ! ( pancakesToSort == 0) )
				{
					if ( !this.isPancakeUpsideDown(0))
					{
						this.flip(1);	// show your dark side to the world
					}
					this.flip(this.getPancakeSize(0));	// hit the bottom !
				}
				else
				{
					if ( this.isPancakeUpsideDown(0))
					{
						this.flip(1);
					}
				}

			}	
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	
}
