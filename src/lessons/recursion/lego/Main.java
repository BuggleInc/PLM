package lessons.recursion.lego;

import lessons.recursion.lego.dragoncurve.DragonCurve1;
import lessons.recursion.lego.dragoncurve.DragonCurve2;
import lessons.recursion.lego.koch.Crab;
import lessons.recursion.lego.koch.HexaKoch;
import lessons.recursion.lego.koch.Koch;
import lessons.recursion.lego.koch.PentaKoch;
import lessons.recursion.lego.koch.SquareKoch;
import lessons.recursion.lego.polygonfractal.PolygonFractal;
import lessons.recursion.lego.sierpinski.Sierpinski;
import lessons.recursion.lego.spiral.Spiral;
import lessons.recursion.lego.spiral.SpiralUse;
import lessons.recursion.lego.square.FourSquare;
import lessons.recursion.lego.tree.Tree;
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
		addExercise(new HexaKoch(this));
		addExercise(new Crab(this));
		addExercise(new Sierpinski(this));
		addExercise(new PolygonFractal(this));
		addExercise(new DragonCurve1(this));
		addExercise(new DragonCurve2(this));
	}
}
