package lessons.turmites.turmitecreator;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import lessons.turmites.universe.TurmiteWorld;

public class TurmiteCreator extends ExerciseTemplated {
	
	public TurmiteCreator(Lesson lesson) {
		super(lesson);
		tabName = "Turmite";

		setup(new TurmiteWorld("blah",1000,null,100,100,50,50));
	}
}
