package lessons.recursion;

import lessons.recursion.dragoncurve.DragonCurve1;
import lessons.recursion.dragoncurve.DragonCurve2;
import lessons.recursion.koch.Crab;
import lessons.recursion.koch.Koch;
import lessons.recursion.koch.PentaKoch;
import lessons.recursion.koch.SquareKoch;
import lessons.recursion.polygonfractal.PolygonFractal;
import lessons.recursion.sierpinski.Sierpinski;
import lessons.recursion.spiral.Spiral;
import lessons.recursion.spiral.SpiralUse;
import lessons.recursion.square.FourSquare;
import lessons.recursion.tree.Tree;
import plm.core.model.lesson.Lesson;

public class Main extends Lesson {
	@Override
	protected void loadExercises() {
		addExercise(new FourSquare(this));
		addExercise(new Spiral(this));
		addExercise(new SpiralUse(this));
		addExercise(new Tree(this));
		addExercise(new Koch(this));
		addExercise(new SquareKoch(this));
		addExercise(new PentaKoch(this));
		addExercise(new Crab(this));
		addExercise(new Sierpinski(this));
		addExercise(new PolygonFractal(this));
		addExercise(new DragonCurve1(this));
		addExercise(new DragonCurve2(this));
	}
}
