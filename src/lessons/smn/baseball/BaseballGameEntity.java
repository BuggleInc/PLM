package lessons.smn.baseball;

import jlm.universe.smn.baseball.BaseballEntity;
import jlm.universe.smn.baseball.InvalidPositionException;
//import jlm.universe.smn.baseball.BaseballWorld;
import jlm.universe.smn.baseball.InvalidMoveException;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 */
public class BaseballGameEntity extends BaseballEntity {


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
		if ( this.getPlayerColor(baseIndex, 0) != baseIndex) // sort the first player
			{
				if ( this.getPlayerColor(baseIndex,1) == baseIndex)
				{
					this.bringHole(baseIndex, 0, 1);
					this.move(baseIndex, 1);
				}
				else
				{
					int[] wantedPlayerLocation = this.findNearestPlayer(baseIndex, baseIndex+1);
					this.bringHole(wantedPlayerLocation[0]-1, 0,wantedPlayerLocation[1]);
					this.bringPlayerHome(wantedPlayerLocation[0], wantedPlayerLocation[1], baseIndex,0);
				}
			}
		if ( this.getPlayerColor(baseIndex, 1) != baseIndex) // sort the second player
		{
			int[] wantedPlayerLocation = this.findNearestPlayer(baseIndex, baseIndex+1);
			this.bringHole(wantedPlayerLocation[0]-1, 1,wantedPlayerLocation[1]);
			this.bringPlayerHome(wantedPlayerLocation[0], wantedPlayerLocation[1], baseIndex,1);
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	
	private void bringPlayerHome(int baseSrc,int playerSrc,int baseDst,int playerDst) throws InvalidMoveException{
		move( baseSrc,playerSrc);
		for ( int i = baseSrc-1; i > baseDst; i--)
		{
			move(i,1-playerDst);
			move(i-1,playerDst);
			move(i,playerDst);
		}
	}
	
	private int[] findNearestPlayer(int colorWanted, int firstBaseToSearch ) throws InvalidPositionException {
		int[] location = new int[2];
		int nbBases = this.getAmountOfBases();
		boolean found = false;
		for ( int i = firstBaseToSearch; i < nbBases && !found ; i++)
		{
			for ( int j = 0 ; j < 2  ; j++)
			{
				if ( this.getPlayerColor(i, j)== colorWanted && !found )
				{
					location[0]= i;
					location[1]= j;
					found = true;
				}
			}
		}
		return location;
	}
	
	private void bringHole(int baseDst, int playerDst , int playerToProtect) throws InvalidMoveException {
		int[] holeLocation = { this.getHoleBase(), this.getHolePositionInBase() };
		if ( baseDst > holeLocation[0]) // the hole is before the base where we want it
		{
			for ( int i = holeLocation[0]+1; i<baseDst+1;i++)
			{
				move(i, playerDst);
			}
		}
		else if ( baseDst < holeLocation[0] ) // the hole is after the base where we want it
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
	}

	
}
