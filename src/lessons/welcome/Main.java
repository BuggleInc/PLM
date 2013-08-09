package lessons.welcome;

import java.io.IOException;

import jlm.core.model.lesson.Lecture;
import jlm.core.model.lesson.Lesson;
import jlm.universe.BrokenWorldFileException;
import lessons.welcome.array.array123.Array123;
import lessons.welcome.array.array667.Array667;
import lessons.welcome.array.arraycount9.ArrayCount9;
import lessons.welcome.array.arrayfront9.ArrayFront9;
import lessons.welcome.array.averagevalue.AverageValue;
import lessons.welcome.array.basics.Array;
import lessons.welcome.array.basics.Array2;
import lessons.welcome.array.has271.Has271;
import lessons.welcome.array.indexof.maxvalue.IndexOfMaxValue;
import lessons.welcome.array.indexof.value.IndexOfValue;
import lessons.welcome.array.maxvalue.MaxValue;
import lessons.welcome.array.notriples.NoTriples;
import lessons.welcome.array.occurenceofvalue.OccurrenceOfValue;
import lessons.welcome.bdr.BDR;
import lessons.welcome.bdr.BDR2;
import lessons.welcome.conditions.Conditions;
import lessons.welcome.conditions.bool1.Close10;
import lessons.welcome.conditions.bool1.CountTeen;
import lessons.welcome.conditions.bool1.Diff21;
import lessons.welcome.conditions.bool1.HasTeen;
import lessons.welcome.conditions.bool1.IcyHot;
import lessons.welcome.conditions.bool1.In1020;
import lessons.welcome.conditions.bool1.In3050;
import lessons.welcome.conditions.bool1.LastDigit;
import lessons.welcome.conditions.bool1.LoneTeen;
import lessons.welcome.conditions.bool1.Makes10;
import lessons.welcome.conditions.bool1.Max1020;
import lessons.welcome.conditions.bool1.MonkeyTrouble;
import lessons.welcome.conditions.bool1.NearHundred;
import lessons.welcome.conditions.bool1.ParotTrouble;
import lessons.welcome.conditions.bool1.PosNeg;
import lessons.welcome.conditions.bool1.SleepIn;
import lessons.welcome.conditions.bool1.SumDouble;
import lessons.welcome.conditions.bool2.AlarmClock;
import lessons.welcome.conditions.bool2.AnswerCell;
import lessons.welcome.conditions.bool2.BlueTicket;
import lessons.welcome.conditions.bool2.CaughtSpeeding;
import lessons.welcome.conditions.bool2.CigarParty;
import lessons.welcome.conditions.bool2.DateFashion;
import lessons.welcome.conditions.bool2.GreenTicket;
import lessons.welcome.conditions.bool2.In1To10;
import lessons.welcome.conditions.bool2.InOrder;
import lessons.welcome.conditions.bool2.InOrderEqual;
import lessons.welcome.conditions.bool2.LastDigit2;
import lessons.welcome.conditions.bool2.LessBy10;
import lessons.welcome.conditions.bool2.MaxMod5;
import lessons.welcome.conditions.bool2.NearTen;
import lessons.welcome.conditions.bool2.RedTicket;
import lessons.welcome.conditions.bool2.ShareDigit;
import lessons.welcome.conditions.bool2.SortaSum;
import lessons.welcome.conditions.bool2.SquirrelPlay;
import lessons.welcome.conditions.bool2.TeaParty;
import lessons.welcome.conditions.bool2.TeenSum;
import lessons.welcome.conditions.bool2.TwoAsOne;
import lessons.welcome.conditions.bool2.WithoutDoubles;
import lessons.welcome.environment.Environment;
import lessons.welcome.instructions.Instructions;
import lessons.welcome.instructions.InstructionsDrawG;
import lessons.welcome.loopdowhile.LoopDoWhile;
import lessons.welcome.loopdowhile.Poucet;
import lessons.welcome.loopfor.LoopCourse;
import lessons.welcome.loopfor.LoopCourseForest;
import lessons.welcome.loopfor.LoopFor;
import lessons.welcome.loopfor.LoopStairs;
import lessons.welcome.loopwhile.BaggleSeeker;
import lessons.welcome.loopwhile.LoopWhile;
import lessons.welcome.loopwhile.WhileMoria;
import lessons.welcome.methods.args.MethodsArgs;
import lessons.welcome.methods.basics.Methods;
import lessons.welcome.methods.basics.MethodsDogHouse;
import lessons.welcome.methods.picture.MethodsPicture;
import lessons.welcome.methods.picture.MethodsPicture2;
import lessons.welcome.methods.picture.MethodsPicture3;
import lessons.welcome.methods.picture.MethodsPicture4;
import lessons.welcome.methods.returning.MethodsReturning;
import lessons.welcome.methods.slug.SlugHunting;
import lessons.welcome.methods.slug.SlugSnail;
import lessons.welcome.methods.slug.SlugTracking;
import lessons.welcome.traversal.Snake;
import lessons.welcome.traversal.column.TraversalByColumn;
import lessons.welcome.traversal.diagonal.TraversalDiagonal;
import lessons.welcome.traversal.line.TraversalByLine;
import lessons.welcome.traversal.zigzag.TraversalZigZag;
import lessons.welcome.variables.RunFour;
import lessons.welcome.variables.RunHalf;
import lessons.welcome.variables.Variables;

public class Main extends Lesson {

	@Override
	protected void loadExercises() throws IOException, BrokenWorldFileException {
		addExercise(new Environment(this));
		
		Lecture instructions = addExercise(new Instructions(this));
		addExercise(new InstructionsDrawG(this), instructions);
		
		Lecture conditions = addExercise(new Conditions(this));
		
		Lecture loopWhile = addExercise(new LoopWhile(this));
		addExercise(new BaggleSeeker(this), loopWhile);
		addExercise(new BDR(this),loopWhile);
		addExercise(new BDR2(this),loopWhile);
		addExercise(new WhileMoria(this),loopWhile);
		
		Lecture vars = addExercise(new Variables(this));
		addExercise(new RunFour(this), vars);
		addExercise(new RunHalf(this), vars);
		
		Lecture loopFor = addExercise(new LoopFor(this));
		addExercise(new LoopStairs(this), loopFor);
		addExercise(new LoopCourse(this),loopFor);
		addExercise(new LoopCourseForest(this),loopFor);
		
		Lecture loopDoWhile = addExercise(new LoopDoWhile(this));
		addExercise(new Poucet(this), loopDoWhile);
		
		Lecture methodsVoid = addExercise(new Methods(this));
		  addExercise(new MethodsDogHouse(this),methodsVoid);
		
  		  Lecture picture = addExercise(new MethodsPicture(this),methodsVoid);
		    addExercise(new MethodsPicture2(this),picture);
		    addExercise(new MethodsPicture3(this),picture);
 	 	    addExercise(new MethodsPicture4(this),picture);
 	 	  
 	 	Lecture methodReturns = addExercise(new MethodsReturning(this));
		  addExercise(new SlugTracking(this),methodReturns);
		  addExercise(new SlugHunting(this),methodReturns);
		
		Lecture methodArg = addExercise(new MethodsArgs(this));
		  addExercise(new SlugSnail(this), methodArg);
				
		// arrays exercises 
		Lecture arrays = addExercise(new Array(this));
		addExercise(new Array2(this),arrays);
		addExercise(new IndexOfValue(this),arrays);
		addExercise(new OccurrenceOfValue(this),arrays);
		addExercise(new AverageValue(this),arrays);
		addExercise(new MaxValue(this),arrays);
		addExercise(new IndexOfMaxValue(this),arrays);
		addExercise(new ArrayCount9(this),arrays);
		addExercise(new ArrayFront9(this),arrays);
		addExercise(new Array123(this),arrays);
		addExercise(new Array667(this),arrays);
		addExercise(new NoTriples(this),arrays);
		addExercise(new Has271(this),arrays);

		
		// 2D traversals
		Lecture snake = addExercise(new Snake(this));
		addExercise(new TraversalByColumn(this),snake);
		addExercise(new TraversalByLine(this),snake);
		addExercise(new TraversalZigZag(this),snake);
		addExercise(new TraversalDiagonal(this),snake);
		
		// First serie of boolean fun
		Lecture bat = addExercise(new SleepIn(this),conditions);
		addExercise(new MonkeyTrouble(this),bat);
		addExercise(new NearHundred(this),bat);
		addExercise(new SumDouble(this),bat);
		addExercise(new Diff21(this),bat);
		addExercise(new ParotTrouble(this),bat);
		addExercise(new Makes10(this),bat);
		addExercise(new PosNeg(this),bat);
		addExercise(new IcyHot(this),bat);
		addExercise(new In1020(this),bat);
		addExercise(new HasTeen(this),bat);
		addExercise(new LoneTeen(this),bat);
		addExercise(new CountTeen(this),bat);
		addExercise(new Close10(this),bat);
		addExercise(new In3050(this),bat);
		addExercise(new Max1020(this),bat);
		addExercise(new LastDigit(this),bat);

		// Second serie of boolean fun
		bat = addExercise(new AlarmClock(this),conditions);
		addExercise(new AnswerCell(this),bat);
		addExercise(new BlueTicket(this),bat);
		addExercise(new CaughtSpeeding(this),bat);
		addExercise(new CigarParty(this),bat);
		addExercise(new DateFashion(this),bat);
		addExercise(new GreenTicket(this),bat);
		addExercise(new In1To10(this),bat);
		addExercise(new InOrder(this),bat);
		addExercise(new InOrderEqual(this),bat);
		addExercise(new LastDigit2(this),bat);
		addExercise(new LessBy10(this),bat);
		addExercise(new MaxMod5(this),bat);
		addExercise(new NearTen(this),bat);
		addExercise(new RedTicket(this),bat);
		addExercise(new ShareDigit(this),bat);
		addExercise(new SortaSum(this),bat);
		addExercise(new SquirrelPlay(this),bat);
		addExercise(new TeaParty(this),bat);
		addExercise(new TeenSum(this),bat);
		addExercise(new TwoAsOne(this),bat);
		addExercise(new WithoutDoubles(this),bat);
	}
}
