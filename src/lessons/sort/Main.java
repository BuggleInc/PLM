package lessons.sort;

import jlm.lesson.Lesson;

// see http://www.cs.ubc.ca/~harrison/Java/sorting-demo.html

public class Main extends Lesson {
	public Main() {
		super();	
		name = "Algorithmes de tri";
		addExercise(new ExExchangeSort(this));
	}
}
