package lessons.recursion;

import jlm.core.model.lesson.Lesson;
import lessons.recursion.circle.Circle;
import lessons.recursion.dragoncurve1.DragonCurve1;
import lessons.recursion.dragoncurve2.DragonCurve2;
import lessons.recursion.hanoiboard.HanoiBoard;
import lessons.recursion.koch.Koch;
import lessons.recursion.polygonfractal.PolygonFractal;
import lessons.recursion.sizepinski.Sierpinski;
import lessons.recursion.spiral.Spiral;
import lessons.recursion.spiraluse.SpiralUse;
import lessons.recursion.square.Square;
import lessons.recursion.star.Star;
import lessons.recursion.tree.Tree;

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
