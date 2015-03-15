package lessons.welcome;

import java.io.IOException;

import lessons.welcome.array.array123.Array123;
import lessons.welcome.array.array667.Array667;
import lessons.welcome.array.arraycount9.ArrayCount9;
import lessons.welcome.array.arrayfront9.ArrayFront9;
import lessons.welcome.array.basics.Array1;
import lessons.welcome.array.basics.Array2;
import lessons.welcome.array.golomb.Golomb;
import lessons.welcome.array.has271.Has271;
import lessons.welcome.array.search.IndexOfValue;
import lessons.welcome.array.island.Island;
import lessons.welcome.array.notriples.NoTriples;
import lessons.welcome.array.search.AverageValue;
import lessons.welcome.array.search.Extrema;
import lessons.welcome.array.search.IndexOfMaxValue;
import lessons.welcome.array.search.MaxValue;
import lessons.welcome.array.search.OccurrenceOfValue;
import lessons.welcome.bat.bool1.Close10;
import lessons.welcome.bat.bool1.CountTeen;
import lessons.welcome.bat.bool1.Diff21;
import lessons.welcome.bat.bool1.HasTeen;
import lessons.welcome.bat.bool1.IcyHot;
import lessons.welcome.bat.bool1.In1020;
import lessons.welcome.bat.bool1.In3050;
import lessons.welcome.bat.bool1.LastDigit;
import lessons.welcome.bat.bool1.LoneTeen;
import lessons.welcome.bat.bool1.Makes10;
import lessons.welcome.bat.bool1.Max1020;
import lessons.welcome.bat.bool1.MonkeyTrouble;
import lessons.welcome.bat.bool1.NearHundred;
import lessons.welcome.bat.bool1.ParotTrouble;
import lessons.welcome.bat.bool1.PosNeg;
import lessons.welcome.bat.bool1.SleepIn;
import lessons.welcome.bat.bool1.SumDouble;
import lessons.welcome.bat.bool2.AlarmClock;
import lessons.welcome.bat.bool2.AnswerCell;
import lessons.welcome.bat.bool2.BlueTicket;
import lessons.welcome.bat.bool2.CaughtSpeeding;
import lessons.welcome.bat.bool2.CigarParty;
import lessons.welcome.bat.bool2.DateFashion;
import lessons.welcome.bat.bool2.FizzBuzz;
import lessons.welcome.bat.bool2.GreenTicket;
import lessons.welcome.bat.bool2.In1To10;
import lessons.welcome.bat.bool2.InOrder;
import lessons.welcome.bat.bool2.InOrderEqual;
import lessons.welcome.bat.bool2.LastDigit2;
import lessons.welcome.bat.bool2.LessBy10;
import lessons.welcome.bat.bool2.MaxMod5;
import lessons.welcome.bat.bool2.NearTen;
import lessons.welcome.bat.bool2.RedTicket;
import lessons.welcome.bat.bool2.ShareDigit;
import lessons.welcome.bat.bool2.SortaSum;
import lessons.welcome.bat.bool2.SquirrelPlay;
import lessons.welcome.bat.bool2.TeaParty;
import lessons.welcome.bat.bool2.TeenSum;
import lessons.welcome.bat.bool2.TwoAsOne;
import lessons.welcome.bat.bool2.WithoutDoubles;
import lessons.welcome.bdr.BDR;
import lessons.welcome.bdr.BDR2;
import lessons.welcome.conditions.Conditions;
import lessons.welcome.environment.Environment;
import lessons.welcome.instructions.Instructions;
import lessons.welcome.instructions.InstructionsDrawG;
import lessons.welcome.loopdowhile.LoopDoWhile;
import lessons.welcome.loopdowhile.Poucet1;
import lessons.welcome.loopdowhile.Poucet2;
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
import lessons.welcome.methods.flowerpot.FlowerCase;
import lessons.welcome.methods.flowerpot.FlowerPot;
import lessons.welcome.methods.picture.MethodsPicture;
import lessons.welcome.methods.picture.MethodsPictureLarge;
import lessons.welcome.methods.picture.PatternPicture;
import lessons.welcome.methods.picture.PictureMono1;
import lessons.welcome.methods.picture.PictureMono2;
import lessons.welcome.methods.picture.PictureMono3;
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
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;

public class Main extends Lesson {

	@Override
	protected void loadExercises() throws IOException, BrokenWorldFileException {
		addExercise(new Environment(this));
		
		Lecture instructions = addExercise(new Instructions(this));
		addExercise(new InstructionsDrawG(this), instructions);
		
		//Lecture conditions = 
		addExercise(new Conditions(this));

		Lecture loopWhile = addExercise(new LoopWhile(this));
		addExercise(new BaggleSeeker(this), loopWhile);
		
		Lecture vars = addExercise(new Variables(this));
		addExercise(new RunFour(this), vars);
		addExercise(new RunHalf(this), vars);
		addExercise(new BDR(this),vars);
		addExercise(new BDR2(this),vars);
		
		Lecture loopFor = addExercise(new LoopFor(this));
		addExercise(new LoopStairs(this), loopFor);
		addExercise(new LoopCourse(this),loopFor);
		addExercise(new LoopCourseForest(this),loopFor);
		
		Lecture loopDoWhile = addExercise(new LoopDoWhile(this));
		addExercise(new Poucet1(this), loopDoWhile);
		addExercise(new Poucet2(this), loopDoWhile);
		
		Lecture methodsVoid = addExercise(new Methods(this));
		  addExercise(new MethodsDogHouse(this),methodsVoid);
  		  addExercise(new PictureMono1(this),methodsVoid);
		  addExercise(new PictureMono2(this),methodsVoid);
		  addExercise(new PictureMono3(this),methodsVoid);
		 	 	  
 	 	Lecture methodReturns = addExercise(new MethodsReturning(this));
		  addExercise(new SlugTracking(this),methodReturns);
		  addExercise(new SlugHunting(this),methodReturns);
		
		Lecture methodArg = addExercise(new MethodsArgs(this));
		  addExercise(new SlugSnail(this), methodArg);
		  addExercise(new MethodsPicture(this), methodArg);
		  addExercise(new MethodsPictureLarge(this), methodArg);
 	 	  addExercise(new PatternPicture(this), methodArg);
 	 	  addExercise(new FlowerPot(this), methodArg);
 	 	  addExercise(new FlowerCase(this), methodArg);
	
		  // First serie of boolean fun
		  Lecture bat = addExercise(new SleepIn(this));
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
		  bat = addExercise(new AlarmClock(this));
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
		  addExercise(new FizzBuzz(this), bat);
		  addExercise(new SquirrelPlay(this),bat);
		  addExercise(new TeaParty(this),bat);
		  addExercise(new TeenSum(this),bat);
		  addExercise(new TwoAsOne(this),bat);
		  addExercise(new WithoutDoubles(this),bat);

		  
		// arrays exercises 
		Lecture arrays = addExercise(new Array1(this));
		addExercise(new Array2(this),arrays);
		addExercise(new IndexOfValue(this),arrays);
		addExercise(new OccurrenceOfValue(this),arrays);
		addExercise(new AverageValue(this),arrays);
		addExercise(new MaxValue(this),arrays);
		addExercise(new IndexOfMaxValue(this),arrays);
		addExercise(new Extrema(this),arrays);
		addExercise(new ArrayCount9(this),arrays);
		addExercise(new ArrayFront9(this),arrays);
		addExercise(new Array123(this),arrays);
		addExercise(new Array667(this),arrays);
		addExercise(new NoTriples(this),arrays);
		addExercise(new Has271(this),arrays);
		addExercise(new Golomb(this),arrays);
		addExercise(new Island(this),arrays);

		
		// 2D traversals
		Lecture snake = addExercise(new Snake(this));
		addExercise(new TraversalByColumn(this),snake);
		addExercise(new TraversalByLine(this),snake);
		addExercise(new TraversalZigZag(this),snake);
		addExercise(new TraversalDiagonal(this),snake);
		
		// Other exercises
		addExercise(new WhileMoria(this));

		
	}
}
