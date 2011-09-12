package lessons.welcome.cells;

import java.awt.Color;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.bugglequest.Buggle;
import jlm.universe.bugglequest.BuggleWorld;

public class TurmiteCreator extends ExerciseTemplated {
	
	public TurmiteCreator(Lesson lesson) {
		super(lesson);
		tabName = "Turmite";

		BuggleWorld bw = new BuggleWorld("blah",100,100);
		new Buggle(bw,"ant",50,50,Direction.NORTH,Color.red,Color.red);
		bw.setVisibleGrid(false);
		bw.setDelay(1);

		setup(bw);
	}
}
