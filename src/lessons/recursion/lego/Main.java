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
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;

public class Main extends Lesson {
	public Main(Game game) {
		super(game);
	}

	@Override
	protected void loadExercises() {
		addExercise(new FourSquare(getGame(), this));
		addExercise(new Spiral(getGame(), this));
		addExercise(new SpiralUse(getGame(), this));
		addExercise(new Tree(getGame(), this));
		addExercise(new Koch(getGame(), this));
		addExercise(new SquareKoch(getGame(), this));
		addExercise(new PentaKoch(getGame(), this));
		addExercise(new HexaKoch(getGame(), this));
		addExercise(new Crab(getGame(), this));
		addExercise(new Sierpinski(getGame(), this));
		addExercise(new PolygonFractal(getGame(), this));
		addExercise(new DragonCurve1(getGame(), this));
		addExercise(new DragonCurve2(getGame(), this));
	}
}
