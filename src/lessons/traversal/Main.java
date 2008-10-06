package lessons.traversal;

import lessons.Lesson;

public class Main extends Lesson {

	public Main() {
		super();
		name = "Parcours";
		setSequential(true);
		addExercise(new ColumnByColumn(this));
		addExercise(new LineByLine(this));
		addExercise(new ZigZag(this));
		addExercise(new Diagonal(this));
	}
}
