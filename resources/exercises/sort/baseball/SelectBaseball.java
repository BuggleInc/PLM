package sort.baseball;


import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.baseball.BaseballWorld;

public class SelectBaseball extends ExerciseTemplated {

	public SelectBaseball() {
		super("SelectBaseball", "SelectBaseball");

		setup(new BaseballWorld[] {
				new BaseballWorld("Almost",4,2, BaseballWorld.MIX_ALMOST_SORTED),
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
