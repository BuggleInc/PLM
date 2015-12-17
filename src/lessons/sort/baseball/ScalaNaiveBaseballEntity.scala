package lessons.sort.baseball;

import plm.universe.baseball.BaseballWorld
import plm.universe.baseball.BaseballEntity

class ScalaNaiveBaseballEntity extends BaseballEntity {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
		while (!isSorted()) {
			val baseNext = (getHoleBase()+1) % getBasesAmount();
			var posNext = -1;
			var maxDistance = -1;
			for (pos <- 0 to getPositionsAmount() -1) {
				val player = getPlayerColor(baseNext, pos);
				var distance = (baseNext - player + getBasesAmount()) % getBasesAmount();
				if (distance > maxDistance) {
					maxDistance = distance;
					posNext = pos;
				}
//				getGame().getLogger().log(world.toString()+"  baseNext:"+baseNext+" player:"+player+"  distance:"+distance+" (#bases:"+getBasesAmount()+")");
			}
//			getGame().getLogger().log("move "+baseNext+","+posNext);
			move(baseNext,posNext);
		}
		world.asInstanceOf[BaseballWorld].assertSorted("naive sort");
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
