package lessons.sort.baseball;

import lessons.sort.baseball.universe.BaseballEntity;

public class BaseballGameEntity extends BaseballEntity {


	public void run() {
		this.homerun();
	}

	/* BEGIN TEMPLATE */
	private void homerun() {
		int amountOfBases = this.getBasesAmount()-1;
		for (int base = 0 ; base < amountOfBases ; base++)
			if ( !this.isBaseSorted(base) )
				this.solve(base);
	}

	private void solve(int base) {
		/* BEGIN SOLUTION */
		// Look if the first player is sorted
		if ( this.getPlayerColor(base, 0) != base) 
		{
			/*
			 * Look if maybe the second player has the good color
			 * so we will swap them
			 */
			if ( this.getPlayerColor(base,1) == base)
			{	
				this.bringHole(base, 0, 1);
				this.move(base, 1);
			}
			else
			{
				// Find the nearest player witch matching color		
				int[] wantedPlayerLocation = this.findNearestPlayer(base, base+1);
				// Bring the hole next to him				 
				this.bringHole(wantedPlayerLocation[0]-1, 0,wantedPlayerLocation[1]);
				// Bring the player home
				this.bringPlayerHome(wantedPlayerLocation[0], wantedPlayerLocation[1], base,0);
			}
		}
		// Look if the second player is sorted
		if ( this.getPlayerColor(base, 1) != base) 
		{
			// Find the nearest player witch matching color		
			int[] wantedPlayerLocation = this.findNearestPlayer(base, base+1);
			// Bring the hole next to him		
			this.bringHole(wantedPlayerLocation[0]-1, 1,wantedPlayerLocation[1]);
			// Bring the player home
			this.bringPlayerHome(wantedPlayerLocation[0], wantedPlayerLocation[1], base,1);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	// Bring the player at (baseDst,playerDst) to (baseSrc,playerDst)
	private void bringPlayerHome(int baseSrc,int playerSrc,int baseDst,int playerDst) {
		move( baseSrc,playerSrc);
		for ( int i = baseSrc-1; i > baseDst; i--)
		{
			move(i,1-playerDst); 
			move(i-1,playerDst);
			move(i,playerDst);
		}
	}

	// Search for the player closer from his base
	private int[] findNearestPlayer(int colorWanted, int firstBaseToSearch )  {
		int[] location = new int[2];
		int nbBases = this.getBasesAmount();
		boolean found = false;
		for ( int i = firstBaseToSearch; i < nbBases && !found ; i++)
		{
			for ( int j = 0 ; j < 2 && !found ; j++)
			{
				if ( this.getPlayerColor(i, j)== colorWanted  )
				{
					location[0]= i;
					location[1]= j;
					found = true;
				}
			}
		}
		return location;
	}

	// Bring the hole to (baseDst,playerDst) while protecting the position playerToProtect
	private void bringHole(int baseDst, int playerDst , int playerToProtect) {
		int[] holeLocation = { this.getHoleBase(), this.getHolePosition() };
		if ( baseDst < holeLocation[0] ) // the hole is after the base where we want it
		{
			for ( int i = holeLocation[0]-1; i > baseDst;i--)
			{
				move(i,1-playerToProtect);
			}
			move(baseDst,playerDst);	
		}
		else if ( baseDst == holeLocation[0] && playerDst != holeLocation[1]) // good base but wrong player =/
		{
			move(baseDst,playerDst);
		}
		else if ( baseDst > holeLocation[0]) // the hole is before the base where we want it
		{
			for ( int i = holeLocation[0]+1; i<baseDst+1;i++)
			{
				move(i, playerDst);
			}
		}
	}


}
