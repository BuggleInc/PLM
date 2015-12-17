package lessons.sort.baseball;

import plm.universe.baseball.BaseballEntity;
import plm.universe.baseball.BaseballWorld;

public class NaiveBaseballEntity extends BaseballEntity {
	
	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
		while (!isSorted()) {
			int baseNext = (getHoleBase()+1) % getBasesAmount();
			int posNext = -1;
			int maxDistance = -1;
			for (int pos=0;pos<getPositionsAmount();pos++) {
				int player = getPlayerColor(baseNext, pos);
				int distance = (baseNext - player + getBasesAmount()) % getBasesAmount();
				if (distance > maxDistance) {
					maxDistance = distance;
					posNext = pos;
				}
//				getGame().getLogger().log(world.toString()+"  baseNext:"+baseNext+" player:"+player+"  distance:"+distance+" (#bases:"+getBasesAmount()+")");
			}
//			getGame().getLogger().log("move "+baseNext+","+posNext);
			move(baseNext,posNext);
		}
		((BaseballWorld) world).assertSorted("naive sort");
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
