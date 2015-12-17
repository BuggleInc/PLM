package lessons.sort.baseball;

import plm.universe.baseball.BaseballEntity;
import plm.universe.baseball.BaseballWorld;

public class SelectBaseballEntity extends BaseballEntity {
	
	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
		for (int base = 0 ; base < getBasesAmount() -1 ; base++) 
			bringPlayersHome(base);
		
		((BaseballWorld) world).assertSorted("selection sort");
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* BEGIN HIDDEN */
	void out(String msg) {
		//if (false)
		//	getGame().getLogger().log(msg);
	}
	
	public void bringPlayersHome(int base) {
		for (int positionToFill = 0; positionToFill<getPositionsAmount(); positionToFill++) {
			out("Sort base "+base+", position "+positionToFill);
			if (getPlayerColor(base, positionToFill) == base)
				continue; // already home
		
			// search for the player on the ground
			int playerBase = findPlayerBase(base,base);
			int playerPos = findPlayerPos(playerBase, base);
			out("player is in "+playerBase+","+playerPos);
			
			
			// bring the hole to the other position of that base
			while (getHoleBase() != playerBase) {
				if (getHoleBase()> playerBase) {
					move(getHoleBase()-1,(playerPos+1)%2);
				} else {
					move(getHoleBase()+1,(playerPos+1)%2);
				}
			}
			out("The hole is now with the player in "+playerBase+": "+world.toString());
			
			if (playerBase == base) {
				// Already in the base. Bring it to its position
				move(base,(positionToFill+1)%2);
				
			} else while (playerBase != base) { // bring the player to the base next to its home
				move (playerBase-1, positionToFill);
				move (playerBase, findPlayerPos(playerBase, base));
				if (playerBase-1 != base) {
					move (playerBase-1, (positionToFill+1) %2);
				}
				playerBase--;
				out("One step further. playerBase: "+playerBase+"; world:"+world.toString());
			}
		}
	}
	
	int findPlayerBase(int start, int color) {
		for (int playerBase=start+1;playerBase<getBasesAmount();playerBase++)
			for (int pos=0; pos<getPositionsAmount();pos++)
				if (getPlayerColor(playerBase, pos) == color)
					return playerBase;
		throw new IllegalArgumentException("cannot find any player of color "+color+" starting at base "+start);
	}
	
	int findPlayerPos(int base, int color) {
		for (int pos=0; pos<getPositionsAmount();pos++)
			if (getPlayerColor(base, pos) == color)
				return pos;
		throw new IllegalArgumentException("cannot find any player of color "+color+" within base "+base);
	}		
	/* END HIDDEN */
}
