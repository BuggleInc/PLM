package lessons.sort.baseball;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import lessons.sort.baseball.universe.BaseballWorld;

public class BaseballGameMorePlayers extends ExerciseTemplated {

	public BaseballGameMorePlayers(Lesson lesson) {
		super(lesson);

		setup(new BaseballWorld[] {
				new BaseballWorld("12 bases with three players each",12,3),
				new BaseballWorld("10 bases with four players each",10,4),
				new BaseballWorld("7 bases with five players each",7,5),
				new BaseballWorld("5 bases with eight players each",5,8),
				new BaseballWorld("15 bases with two players each",15,2)
		});
	}

}
