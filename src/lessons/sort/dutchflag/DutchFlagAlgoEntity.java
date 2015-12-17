package lessons.sort.dutchflag;

import plm.universe.dutchflag.DutchFlagEntity;
import plm.universe.dutchflag.DutchFlagWorld;

public class DutchFlagAlgoEntity extends DutchFlagEntity {
	
	public void run() {
		solve();
	}

	/* BEGIN TEMPLATE */
	void solve() {
		/* BEGIN SOLUTION */
		int afterBlue=0;
		int beforeWhite=getSize()-1;
		int beforeRed=getSize()-1;
		while (afterBlue <= beforeWhite) {
			
			switch (getColor(afterBlue)) {
			case BLUE:
				afterBlue++;
				break;
			case WHITE:
				swap(afterBlue, beforeWhite);
				beforeWhite --;
				break;
			case RED:
				swap(afterBlue, beforeWhite);
				swap(beforeRed, beforeWhite);
				beforeWhite--;
				beforeRed--;
			}
		}
		((DutchFlagWorld) world).assertSorted();
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
