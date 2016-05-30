package lessons.recursion.logo;

import lessons.recursion.logo.dragoncurve.DragonCurve1;
import lessons.recursion.logo.dragoncurve.DragonCurve2;
import lessons.recursion.logo.koch.Crab;
import lessons.recursion.logo.koch.HexaKoch;
import lessons.recursion.logo.koch.Koch;
import lessons.recursion.logo.koch.PentaKoch;
import lessons.recursion.logo.koch.SquareKoch;
import lessons.recursion.logo.polygonfractal.PolygonFractal;
import lessons.recursion.logo.sierpinski.Sierpinski;
import lessons.recursion.logo.spiral.Spiral;
import lessons.recursion.logo.spiral.SpiralUse;
import lessons.recursion.logo.square.FourSquare;
import lessons.recursion.logo.tree.Tree;
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
