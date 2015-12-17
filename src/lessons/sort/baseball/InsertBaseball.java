package lessons.sort.baseball;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.baseball.BaseballWorld;

public class InsertBaseball extends ExerciseTemplated {

	public InsertBaseball(Game game, Lesson lesson) {
		super(game, lesson);

		setup(new BaseballWorld[] {
				new BaseballWorld(game, "4 bases",4,2),
				new BaseballWorld(game, "5 bases",5,2),
				new BaseballWorld(game, "6 bases",6,2),
				new BaseballWorld(game, "7 bases",7,2),
				new BaseballWorld(game, "8 bases",8,2),
				new BaseballWorld(game, "9 bases",9,2),
				new BaseballWorld(game, "10 bases",10,2,new int[]{-1,7 , 2,7 , 1,4 , 3,4 , 5,6 , 8,9 , 5,1 , 3,6 , 0,2 , 0,8}),
				new BaseballWorld(game, "15 bases",15,2),
		});
	}

}
