package lessons.welcome;

import lessons.Lesson;

public class Main extends Lesson {

	public Main() {
		super();
		name = "Premiers pas";
		setSequential(true);
		addExercise(new Environment(this));
		addExercise(new Basics(this));
		addExercise(new Basics3Pas(this));
		addExercise(new BasicsDrawG(this));
		addExercise(new Conditions(this));
		addExercise(new LoopWhile(this));
		addExercise(new LoopDoWhile(this));
		addExercise(new Variables(this));
		addExercise(new LoopFor(this));
		addExercise(new Methods(this));
		addExercise(new MethodsDogHouse(this));
		addExercise(new MethodsReturning(this));
		addExercise(new MethodsArgs(this));
		addExercise(new MethodsPicture(this));
		addExercise(new MethodsPicture2(this));
		addExercise(new MethodsPicture3(this));
		addExercise(new BDR(this));
		addExercise(new BDR2(this));
		addExercise(new SlugHunting(this));
		addExercise(new Snake(this));
		addExercise(new MethodsPicture4(this));
		addExercise(new Array(this));
		addExercise(new Array2(this));
	}
}
