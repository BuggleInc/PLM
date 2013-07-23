package lessons.sort.pancake;

import jlm.core.model.lesson.Lesson;

// see http://www.cs.ubc.ca/~harrison/Java/sorting-demo.html

public class Main extends Lesson {
	@Override
	protected void loadExercises() {
		
		addExercise(new BasicPancake(this));
		addExercise(new BurnedPancake(this));
		addExercise(new GatesPancake(this));
	}
}
