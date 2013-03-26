package lessons.welcome.cells;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;

public class TurmiteCreator extends ExerciseTemplated {
	
	public TurmiteCreator(Lesson lesson) {
		super(lesson);
		tabName = "Turmite";

		setup(new TurmiteWorld("blah",1000,null,100,100,50,50));
	}
}
