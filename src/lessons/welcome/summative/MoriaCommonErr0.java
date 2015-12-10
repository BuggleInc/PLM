package lessons.welcome.summative;

import plm.universe.bugglequest.SimpleBuggle;

public class MoriaCommonErr0 extends SimpleBuggle {
	@Override
	public void run() {
		back();
		while(!isFacingWall()) {
			forward();
		}
	}
}
