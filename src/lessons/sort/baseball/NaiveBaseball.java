package lessons.sort.baseball;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import lessons.sort.baseball.universe.BaseballWorld;

public class NaiveBaseball extends ExerciseTemplated {

	public NaiveBaseball(Lesson lesson) {
		super(lesson);

		setup(new BaseballWorld[] {
				new BaseballWorld("Field 1",4,2,true),
				new BaseballWorld("Field 2",4,2,true)
		});
	}

}
