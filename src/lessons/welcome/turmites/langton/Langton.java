package lessons.welcome.turmites.langton;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import lessons.welcome.turmites.universe.TurmiteWorld;

public class Langton extends ExerciseTemplated {

	public Langton(Lesson lesson) {
		super(lesson);
		tabName = "LangtonsAnt";

		setup(new TurmiteWorld("12000 steps",12000,null,100, 70, 66,23));

	}
}
