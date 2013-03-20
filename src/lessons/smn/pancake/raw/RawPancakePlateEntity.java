package lessons.smn.pancake.raw;

import jlm.universe.smn.pancake.raw.InvalidPancakeNumber;
import jlm.universe.smn.pancake.raw.PancakeEntity;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 */
public class RawPancakePlateEntity extends PancakeEntity {

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
		int stackSize = this.getStackSize();
		boolean currentPancakeAlreadySorted;
		for ( int pancakesToSort = stackSize-1 ; pancakesToSort != -1 && !this.isSorted() ; pancakesToSort-- )
		{
			currentPancakeAlreadySorted= (this.getPancakeRadius(pancakesToSort)==pancakesToSort+1 ) ;
			if ( !currentPancakeAlreadySorted)
			{
				int indexBigPancake =-1;
				boolean found = false;
				for ( int currentPancake = 0 ; currentPancake < pancakesToSort+1 && !found; currentPancake++)
				{
					if ( this.getPancakeRadius(currentPancake) == pancakesToSort+1)
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
					this.flip(this.getPancakeRadius(0));	// hit the bottom !
				}
			}	
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	
}
