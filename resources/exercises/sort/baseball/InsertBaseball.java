package sort.baseball;


import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.baseball.BaseballWorld;

public class InsertBaseball extends ExerciseTemplated {

	public InsertBaseball() {
		super("InsertBaseball", "InsertBaseball");

		setup(new BaseballWorld[] {
				new BaseballWorld("4 bases",4,2),
				new BaseballWorld("5 bases",5,2),
				new BaseballWorld("6 bases",6,2),
				new BaseballWorld("7 bases",7,2),
				new BaseballWorld("8 bases",8,2),
				new BaseballWorld("9 bases",9,2),
				new BaseballWorld("10 bases",10,2,new int[]{-1,7 , 2,7 , 1,4 , 3,4 , 5,6 , 8,9 , 5,1 , 3,6 , 0,2 , 0,8}),
				new BaseballWorld("15 bases",15,2),
		});
	}

}
