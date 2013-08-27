package lessons.sort.baseball;

import plm.core.model.lesson.Lesson;

public class Main extends Lesson {

	protected void loadExercises() {
		addExercise(new NaiveBaseball(this));
		addExercise(new SelectBaseball(this));  
		addExercise(new InsertBaseball(this));
		addExercise(new BubbleBaseball(this));
	}
}
