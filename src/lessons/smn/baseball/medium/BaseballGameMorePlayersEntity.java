package lessons.smn.baseball.medium;

import jlm.universe.smn.baseball.BaseballEntity;
import jlm.universe.smn.baseball.InvalidMoveException;
import jlm.universe.smn.baseball.InvalidPositionException;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 */
public class BaseballGameMorePlayersEntity extends BaseballEntity {


	public void run() {
		try
		{
			this.homerun();
		}
		catch( InvalidMoveException ime)
		{
			System.out.println(this.world.getName() +" :"+ime.getMessage());
		}
		catch( InvalidPositionException ipe)
		{
			System.out.println(this.world.getName() +" :"+ipe.getMessage());
		}
	}
	
	/* BEGIN TEMPLATE */
	private void homerun() throws InvalidMoveException, InvalidPositionException {
		int amountOfBases = this.getAmountOfBases()-1;
		for ( int baseIndex = 0 ; baseIndex < amountOfBases ; baseIndex++)
		{
			if ( !this.isBaseSorted(baseIndex) )
			{
				this.solve(baseIndex);
			}
		}
	}
	
	private void solve(int baseIndex) throws InvalidMoveException, InvalidPositionException{
		/* BEGIN SOLUTION */
		int nbPlayers = this.getLocationsAmount();
		 
		for ( int i = 0 ; i < nbPlayers ; i++ )	
		{
			// Look if the player number i has the good color
			if ( this.getPlayerColor(baseIndex, i) != baseIndex) 
			{
				// Search for a player with matching color within the base and do the swapping
				boolean found = false ;
				for ( int j = i+1 ; j < nbPlayers && !found ; j++) 	
				{
					if ( this.getPlayerColor(baseIndex,j) == baseIndex) 
					{
						this.bringHole(baseIndex, i, j); 
						this.move(baseIndex, j);	
						found = true ;
					}
				}
				// Search for a player with matching color in the reszt of the field and bring him home
				if ( !found )
				{
					int[] wantedPlayerLocation = this.findNearestPlayer(baseIndex, baseIndex+1);	
					this.bringHole(wantedPlayerLocation[0]-1, i,wantedPlayerLocation[1]);	
					this.bringPlayerHome(wantedPlayerLocation[0], wantedPlayerLocation[1], baseIndex,i);
				}
			}
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	
	// Bring the player at (baseDst,playerDst) to (baseSrc,playerDst)
	private void bringPlayerHome(int baseSrc,int playerSrc,int baseDst,int playerDst) throws InvalidMoveException{
		move( baseSrc,playerSrc);
		int nbPlayers = this.getLocationsAmount();
		for ( int i = baseSrc-1; i > baseDst; i--)
		{
			move(i,(1+playerDst)%nbPlayers);
			move(i-1,playerDst);
			move(i,playerDst);
		}
	}
	
	// Search for the player closer from his base
	private int[] findNearestPlayer(int colorWanted, int firstBaseToSearch ) throws InvalidPositionException {
		int[] location = new int[2];
		int nbBases = this.getAmountOfBases();
		int nbPlayers = this.getLocationsAmount();
		boolean found = false;
		for ( int i = firstBaseToSearch; i < nbBases && !found ; i++)
		{
			for ( int j = 0 ; j < nbPlayers && !found  ; j++)
			{
				if ( this.getPlayerColor(i, j)== colorWanted )
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
	private void bringHole(int baseDst, int playerDst , int playerToProtect) throws InvalidMoveException {
		int[] holeLocation = { this.getHoleBase(), this.getHolePositionInBase() };
		int nbPlayers = this.getLocationsAmount();
		if ( baseDst < holeLocation[0] ) // the hole is after the base where we want it
		{
			for ( int i = holeLocation[0]-1; i > baseDst;i--)
			{
				move(i,(1+playerToProtect)%nbPlayers);
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
