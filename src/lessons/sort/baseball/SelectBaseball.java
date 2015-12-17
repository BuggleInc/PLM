package lessons.sort.baseball;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.baseball.BaseballWorld;

public class SelectBaseball extends ExerciseTemplated {

	public SelectBaseball(Game game, Lesson lesson) {
		super(game, lesson);

		setup(new BaseballWorld[] {
				new BaseballWorld(game, "Almost",4,2, BaseballWorld.MIX_ALMOST_SORTED),
				new BaseballWorld(game, "5 bases",5,2),
				new BaseballWorld(game, "6 bases",6,2),
				new BaseballWorld(game, "7 bases",7,2),
				new BaseballWorld(game, "8 bases",8,2),
				new BaseballWorld(game, "9 bases",9,2),
				new BaseballWorld(game, "10 bases",10,2),
				new BaseballWorld(game, "15 bases",15,2),
		});
	}

}
