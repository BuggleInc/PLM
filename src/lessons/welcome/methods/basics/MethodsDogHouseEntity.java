package lessons.welcome.methods.basics;

import jlm.core.model.Game;
import jlm.core.model.lesson.Exercise;
import jlm.universe.bugglequest.SimpleBuggle;

public class MethodsDogHouseEntity extends SimpleBuggle {
	@Override
	public void right() {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use right."));
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
					int offset = ((Exercise)Game.getInstance().getCurrentLesson().getCurrentExercise()).getSourceFile(Game.JAVA, 0).getOffset();
				    String msg = Game.i18n.tr("I''m sorry Dave, I''m affraid I cant let you use left() both in lines {0} and {1}.",
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
