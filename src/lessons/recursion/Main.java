package lessons.recursion;

import jlm.core.model.lesson.Lesson;

public class Main extends Lesson {
	@Override
	protected void loadExercises() {
		addExercise(new Square(this));
		addExercise(new Circle(this));
		addExercise(new Star(this));
		addExercise(new Spiral(this));
		addExercise(new SpiralUse(this));
		addExercise(new Tree(this));
		addExercise(new Koch(this));
		addExercise(new Sierpinski(this));
		addExercise(new PolygonFractal(this));
		addExercise(new DragonCurve1(this));
		addExercise(new DragonCurve2(this));
		
		addExercise(new HanoiBoard(this));

	}
}
