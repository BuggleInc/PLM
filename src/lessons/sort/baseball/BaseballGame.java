package lessons.sort.baseball;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import lessons.sort.baseball.universe.BaseballWorld;

public class BaseballGame extends ExerciseTemplated {

	public BaseballGame(Lesson lesson) {
		super(lesson);

		setup(new BaseballWorld[] {
				new BaseballWorld("4 bases",4,2),
				new BaseballWorld("10 bases",10,2),
				new BaseballWorld("15 bases",15,2),
		});
	}

}
