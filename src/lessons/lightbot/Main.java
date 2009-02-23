package lessons.lightbot;

import jlm.lesson.Lesson;

public class Main extends Lesson {
	@Override
	protected void loadExercises() {
		addExercise(new Board1TwoSteps(this));
		addExercise(new Board2Turn(this));
		exercisesLoaded = true;
	}
}
/* L6: not enough space? try creating functions 
 * L7: reusing functions is great for repetitive tasks
 * L8: putting "fun" back in functions!
 */