package lessons.sort.baseball;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import lessons.sort.baseball.universe.BaseballWorld;

public class InsertBaseball extends ExerciseTemplated {

	public InsertBaseball(Lesson lesson) {
		super(lesson);

		setup(new BaseballWorld[] {
				new BaseballWorld("4 bases",4,2),
				new BaseballWorld("5 bases",5,2),
				new BaseballWorld("6 bases",6,2),
				new BaseballWorld("7 bases",7,2),
				new BaseballWorld("8 bases",8,2),
				new BaseballWorld("9 bases",9,2),
				new BaseballWorld("10 bases",10,2),
				new BaseballWorld("15 bases",15,2),
		});
	}

}
