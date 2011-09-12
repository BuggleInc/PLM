package lessons.welcome.cells;

import java.awt.Color;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.bugglequest.Buggle;
import jlm.universe.bugglequest.BuggleWorld;

public class Langton extends ExerciseTemplated {

	public Langton(Lesson lesson) {
		super(lesson);
		tabName = "LangtonsAnt";


		BuggleWorld bw =  new BuggleWorld("12000 steps",100, 70);
		bw.setDelay(1);
		new Buggle(bw, "ant", 2*bw.getWidth()/3,  bw.getHeight()/3,  Direction.NORTH, Color.red, Color.black);
		bw.setParameter(new Object[] {12000});
		bw.setVisibleGrid(false);

		setup(bw);

	}
}
