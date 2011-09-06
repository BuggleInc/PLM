package lessons.welcome;

import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.Lesson;

public class Main extends Lesson {

	@Override
	protected void loadExercises() {
		Exercise environment =     new Environment(this);
		addExercise(environment);
		
		Exercise basics =          new Basics(this);
		addExercise(basics,        environment);
		
		addExercise(new BasicsDrawG(this),   basics);
		
		Exercise conditions =      new Conditions(this);
		addExercise(conditions,    basics);
		
		Exercise loopWhile =       new LoopWhile(this);
		addExercise(loopWhile,     conditions);

		addExercise(new BaggleSeeker(this), loopWhile);

		Exercise variables =       new Variables(this);
		addExercise(variables,     loopWhile);
		
		Exercise loopFor =         new LoopFor(this);
		addExercise(loopFor,       variables );
		
		addExercise(new LoopDoWhile(this),    loopFor);
		
		Exercise methodsVoid =         new Methods(this); 
		addExercise(methodsVoid,       loopFor);
		
		addExercise(new MethodsDogHouse(this),methodsVoid);
		addExercise(new MethodsReturning(this),methodsVoid);
		
		Exercise methods = new MethodsArgs(this);
		addExercise(methods);
		addExercise(new MethodsPicture(this));
		addExercise(new MethodsPicture2(this));
		addExercise(new MethodsPicture3(this));
		addExercise(new MethodsPicture4(this));
		
		addExercise(new BDR(this),conditions);
		addExercise(new BDR2(this));
		
		addExercise(new SlugHunting(this),methods);
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
