package lessons.smn;

import jlm.core.model.lesson.Lesson;
import lessons.smn.BurnedPancakePlate;
import lessons.smn.RawPancakePlate;

public class Main extends Lesson {
	@Override
	protected void loadExercises() {
		
		addExercise(new RawPancakePlate(this));
		addExercise(new BurnedPancakePlate(this));

	}
}
