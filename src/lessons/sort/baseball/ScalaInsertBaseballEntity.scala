package lessons.sort.baseball;

import plm.universe.baseball.BaseballWorld
import plm.universe.baseball.BaseballEntity

class ScalaInsertBaseballEntity extends BaseballEntity {
	
	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
		/* Bring the hole in 0,1 */
		if (getHole() == 0) // It is already on base 0, but on another position
			move(1);
		while (getHole() > 1)
			move(getHole()-1);
		
		for (player <- 2 to getBasesAmount()*getPositionsAmount()-1) {
			//out("Sort player "+player);
			
			//out("Compare "+(getHole()+1)+":"+getPlayerColor(getHole()+1)+" < "+(getHole()-1)+":"+getPlayerColor(getHole()-1));
			while (getHole()>0 && getPlayerColor(getHole()+1) < getPlayerColor(getHole()-1)) {  
				val center = getHole();// ...2x1... with ascending positions from left to right
				move(center+1);        // ...21x...
				move(center-1);        // ...x12...
			}
			while (getHole() != player) 
				move(getHole()+1);
		}
		world.asInstanceOf[BaseballWorld].assertSorted("insertion sort");
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	
	def getPlayerColor(pos:Int):Int = getPlayerColor(pos / getPositionsAmount(), pos % getPositionsAmount());
	def move(pos:Int):Unit = move(pos / getPositionsAmount(), pos % getPositionsAmount());
	def getHole():Int = getPositionsAmount()*getHoleBase()+getHolePosition();
	
	def out(msg:String) {
		if (isSelected())
			getGame().getLogger().log(msg);
	}
	/* END HIDDEN */
}