package lessons.bat;

import universe.bat.BatEntity;
import universe.bat.BatWorld;

public class SleepInEntity extends BatEntity {
	@Override
	public void run() {
		BatWorld w = (BatWorld) this.getWorld();
		w.result = this.sleepIn((Boolean)getParam(0),(Boolean)getParam(1));
	}

	/* BEGIN TEMPLATE */
public boolean sleepIn(boolean weekday, boolean vacation) {
	/* BEGIN SOLUTION */
	 if (!weekday || vacation) {
		 return true;
	 } else {
		 return false;
	 }
	 // This can be shortened to: return(!weekday || vacation);
	/* END SOLUTION */
}
	/* END TEMPLATE */
}