package lessons.turmites.turmitecreator;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import lessons.turmites.universe.TurmiteWorld;

public class TurmiteCreator extends ExerciseTemplated {
	
	public TurmiteCreator(Game game, Lesson lesson) {
		super(game, lesson);
		tabName = "Turmite";

		setup(new TurmiteWorld(game, "blah",1000,null,100,100,50,50));
	}
}
