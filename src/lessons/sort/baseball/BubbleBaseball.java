package lessons.sort.baseball;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.baseball.BaseballWorld;

public class BubbleBaseball extends ExerciseTemplated {

	public BubbleBaseball(Game game, Lesson lesson) {
		super(game, lesson);

		setup(new BaseballWorld[] {
				new BaseballWorld(game, "5 bases, 2 positions",5,2),
				new BaseballWorld(game, "5 bases, 3 positions",5,3),
				new BaseballWorld(game, "6 bases, 2 positions",6,2),
				new BaseballWorld(game, "6 bases, 3 positions",6,3),
				new BaseballWorld(game, "7 bases, 2 positions",7,2),
				new BaseballWorld(game, "7 bases, 3 positions",7,3),
				new BaseballWorld(game, "7 bases, 4 positions",7,4),
				new BaseballWorld(game, "8 bases, 2 positions",8,2),
				new BaseballWorld(game, "8 bases, 3 positions",8,3),
		});
	}

}
