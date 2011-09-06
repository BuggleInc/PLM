package lessons.welcome;

import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.Lesson;
import lessons.welcome.bool1.Close10;
import lessons.welcome.bool1.CountTeen;
import lessons.welcome.bool1.Diff21;
import lessons.welcome.bool1.HasTeen;
import lessons.welcome.bool1.IcyHot;
import lessons.welcome.bool1.In1020;
import lessons.welcome.bool1.In3050;
import lessons.welcome.bool1.LastDigit;
import lessons.welcome.bool1.LoneTeen;
import lessons.welcome.bool1.Makes10;
import lessons.welcome.bool1.Max1020;
import lessons.welcome.bool1.MonkeyTrouble;
import lessons.welcome.bool1.NearHundred;
import lessons.welcome.bool1.ParotTrouble;
import lessons.welcome.bool1.PosNeg;
import lessons.welcome.bool1.SleepIn;
import lessons.welcome.bool1.SumDouble;

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
		
//		Exercise firstBat = new SleepIn(this);
		addExercise(new SleepIn(this),conditions);
		addExercise(new MonkeyTrouble(this));
		addExercise(new NearHundred(this));
		addExercise(new SumDouble(this));
		addExercise(new Diff21(this));
		addExercise(new ParotTrouble(this));
		addExercise(new Makes10(this));
		addExercise(new PosNeg(this));
		addExercise(new IcyHot(this));
		addExercise(new In1020(this));
		addExercise(new HasTeen(this));
		addExercise(new LoneTeen(this));
		addExercise(new CountTeen(this));
		addExercise(new Close10(this));
		addExercise(new In3050(this));
		addExercise(new Max1020(this));
		addExercise(new LastDigit(this));

		exercisesLoaded = true;
	}

}
