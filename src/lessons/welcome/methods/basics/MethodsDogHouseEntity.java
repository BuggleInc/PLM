package lessons.welcome.methods.basics;

import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.universe.bugglequest.SimpleBuggle;

public class MethodsDogHouseEntity extends SimpleBuggle {
	@Override
	public void right() {
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use right() in this exercise. Use left() instead."));
	}

	private int line = -1;
	private boolean studentCode = true;
	@Override
	public void left() {
		if (!studentCode) {
			super.left();
			return;
		}
		
		for (StackTraceElement s : Thread.currentThread().getStackTrace()) {
			if (s.getMethodName().equals("dogHouse")) {
				if (line != -1 && line != s.getLineNumber()) {
					int offset = ((Exercise)getGame().getCurrentLesson().getCurrentExercise()).getSourceFile(Game.JAVA, 0).getOffset();
				    String msg = getGame().i18n.tr("Sorry Dave, I cannot let you use left() both in lines {0} and {1} in this exercise. You can write left() only once in this exercise.",
					        (line-offset+1),(s.getLineNumber()-offset+1));

					throw new RuntimeException(msg);
				} else {
					line = s.getLineNumber();
					super.left();
					return;
				}
			}

		}
	}
	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	void dogHouse() {
		for (int i=0;i<4;i++) {
			forward();
			forward();
			left();
		}
	}
	/* END SOLUTION */
	/* END TEMPLATE */

	@Override
	public void run() {
		studentCode = true;
		brushDown();
		dogHouse();
		brushUp();

		forward(4);

		brushDown();
		dogHouse();		
		brushUp();

		forward(2);
		studentCode = false;left(); studentCode = true;
		forward(4);

		brushDown();
		dogHouse();		
		brushUp();

		forward(2);
		studentCode = false;left(); studentCode = true;
		forward(4);

		brushDown();
		dogHouse();		
	} 
}
