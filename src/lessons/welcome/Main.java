package lessons.welcome;

import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.Lesson;

public class Main extends Lesson {

	@Override
	protected void loadExercises() {
		Exercise environment =     new Environment(this);
		addExercise(environment);
		
		Exercise basics =          new Basics(this);
		addExercise(basics,        new Exercise[] {environment});
		
		Exercise basicsDrawG =     new BasicsDrawG(this);
		addExercise(basicsDrawG,   new Exercise[] {basics});
		
		Exercise conditions =      new Conditions(this);
		addExercise(conditions,    new Exercise[] {basics});
		
		Exercise loopWhile =       new LoopWhile(this);
		addExercise(loopWhile,     new Exercise[] {basics,conditions});

		Exercise bagglerSeeker=    new BaggleSeeker(this);
		addExercise(bagglerSeeker, new Exercise[] {loopWhile});

		Exercise variables =       new Variables(this);
		addExercise(variables,     new Exercise[] {basics,conditions,loopWhile});
		
		Exercise loopFor =         new LoopFor(this);
		addExercise(loopFor,       new Exercise[] {basics, conditions, loopWhile, variables} );
		
		addExercise(new LoopDoWhile(this));
		addExercise(new Methods(this));
		addExercise(new MethodsDogHouse(this));
		addExercise(new MethodsReturning(this));
		addExercise(new MethodsArgs(this));
		addExercise(new MethodsPicture(this));
		addExercise(new MethodsPicture2(this));
		addExercise(new MethodsPicture3(this));
		addExercise(new MethodsPicture4(this));
		addExercise(new BDR(this));
		addExercise(new BDR2(this));
		addExercise(new SlugHunting(this));
		addExercise(new Snake(this));
		addExercise(new Array(this));
		addExercise(new Array2(this));
		addExercise(new TraversalByColumn(this));
		addExercise(new TraversalByLine(this));
		addExercise(new TraversalZigZag(this));
		addExercise(new TraversalDiagonal(this));
		exercisesLoaded = true;
	}

}
