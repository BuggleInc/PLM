package lessons.sort.baseball;

import plm.universe.baseball.BaseballWorld
import plm.universe.baseball.BaseballEntity


class ScalaSelectBaseballEntity extends BaseballEntity {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
		for (base <- 0 to getBasesAmount() -2) 
			bringPlayersHome(base);
		
		world.asInstanceOf[BaseballWorld].assertSorted("selection sort");
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	def out(msg:String) {
		//if (false)
		//	getGame().getLogger().log(msg);
	}
	
	def bringPlayersHome(base:Int) {
		for (positionToFill <- 0 to getPositionsAmount()-1) {
			out("Sort base "+base+", position "+positionToFill);
			if (getPlayerColor(base, positionToFill) != base) { // not home yet
		
				// search for the player on the ground
				var playerBase = findPlayerBase(base,base);
				val playerPos = findPlayerPos(playerBase, base);
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
					playerBase-=1;
					out("One step further. playerBase: "+playerBase+"; world:"+world.toString());
				}
			}
		}
	}
	
	def findPlayerBase(start:Int, color:Int):Int = {
		for (playerBase <- start+1 to getBasesAmount() -1)
			for (pos <- 0 to getPositionsAmount()-1)
				if (getPlayerColor(playerBase, pos) == color)
					return playerBase;
		throw new IllegalArgumentException("cannot find any player of color "+color+" starting at base "+start);
	}
	
	def findPlayerPos(base:Int, color:Int):Int = {
		for (pos <- 0 to getPositionsAmount()-1)
			if (getPlayerColor(base, pos) == color)
				return pos;
		throw new IllegalArgumentException("cannot find any player of color "+color+" within base "+base);
	}		
	/* END HIDDEN */
}
