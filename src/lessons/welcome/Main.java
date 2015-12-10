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
import lessons.welcome.array.island.Island;
import lessons.welcome.array.notriples.NoTriples;
import lessons.welcome.array.search.AverageValue;
import lessons.welcome.array.search.Extrema;
import lessons.welcome.array.search.IndexOfMaxValue;
import lessons.welcome.array.search.IndexOfValue;
import lessons.welcome.array.search.MaxValue;
import lessons.welcome.array.search.OccurrenceOfValue;
import lessons.welcome.array.search.SecondMaxValue;
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
import lessons.welcome.summative.Moria;
import lessons.welcome.traversal.Snake;
import lessons.welcome.traversal.column.TraversalByColumn;
import lessons.welcome.traversal.diagonal.TraversalDiagonal;
import lessons.welcome.traversal.line.TraversalByLine;
import lessons.welcome.traversal.zigzag.TraversalZigZag;
import lessons.welcome.variables.RunFour;
import lessons.welcome.variables.RunHalf;
import lessons.welcome.variables.Variables;
import plm.core.model.Game;
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.Lesson;
import plm.universe.BrokenWorldFileException;

public class Main extends Lesson {

	public Main(Game game) {
		super(game);
	}

	@Override
	protected void loadExercises() throws IOException, BrokenWorldFileException {
		addExercise(new Environment(getGame(), this));
		
		Lecture instructions = addExercise(new Instructions(getGame(), this));
		addExercise(new InstructionsDrawG(getGame(), this), instructions);
		
		//Lecture conditions = 
		addExercise(new Conditions(getGame(), this));

		Lecture loopWhile = addExercise(new LoopWhile(getGame(), this));
		addExercise(new BaggleSeeker(getGame(), this), loopWhile);
		
		Lecture vars = addExercise(new Variables(getGame(), this));
		addExercise(new RunFour(getGame(), this), vars);
		addExercise(new RunHalf(getGame(), this), vars);
		addExercise(new BDR(getGame(), this),vars);
		addExercise(new BDR2(getGame(), this),vars);
		
		Lecture loopFor = addExercise(new LoopFor(getGame(), this));
		addExercise(new LoopStairs(getGame(), this), loopFor);
		addExercise(new LoopCourse(getGame(), this),loopFor);
		addExercise(new LoopCourseForest(getGame(), this),loopFor);
		
		Lecture loopDoWhile = addExercise(new LoopDoWhile(getGame(), this));
		addExercise(new Poucet1(getGame(), this), loopDoWhile);
		addExercise(new Poucet2(getGame(), this), loopDoWhile);
		
		Lecture methodsVoid = addExercise(new Methods(getGame(), this));
		  addExercise(new MethodsDogHouse(getGame(), this),methodsVoid);
  		  addExercise(new PictureMono1(getGame(), this),methodsVoid);
		  addExercise(new PictureMono2(getGame(), this),methodsVoid);
		  addExercise(new PictureMono3(getGame(), this),methodsVoid);
		 	 	  
 	 	Lecture methodReturns = addExercise(new MethodsReturning(getGame(), this));
		  addExercise(new SlugTracking(getGame(), this),methodReturns);
		  addExercise(new SlugHunting(getGame(), this),methodReturns);
		
		Lecture methodArg = addExercise(new MethodsArgs(getGame(), this));
		  addExercise(new SlugSnail(getGame(), this), methodArg);
		  addExercise(new MethodsPicture(getGame(), this), methodArg);
		  addExercise(new MethodsPictureLarge(getGame(), this), methodArg);
 	 	  addExercise(new PatternPicture(getGame(), this), methodArg);
 	 	  addExercise(new FlowerPot(getGame(), this), methodArg);
 	 	  addExercise(new FlowerCase(getGame(), this), methodArg);
	
		  // First serie of boolean fun
		  Lecture bat = addExercise(new SleepIn(getGame(), this));
		  addExercise(new MonkeyTrouble(getGame(), this),bat);
		  addExercise(new NearHundred(getGame(), this),bat);
		  addExercise(new SumDouble(getGame(), this),bat);
		  addExercise(new Diff21(getGame(), this),bat);
		  addExercise(new ParotTrouble(getGame(), this),bat);
		  addExercise(new Makes10(getGame(), this),bat);
		  addExercise(new PosNeg(getGame(), this),bat);
		  addExercise(new IcyHot(getGame(), this),bat);
		  addExercise(new In1020(getGame(), this),bat);
		  addExercise(new HasTeen(getGame(), this),bat);
		  addExercise(new LoneTeen(getGame(), this),bat);
		  addExercise(new CountTeen(getGame(), this),bat);
		  addExercise(new Close10(getGame(), this),bat);
		  addExercise(new In3050(getGame(), this),bat);
		  addExercise(new Max1020(getGame(), this),bat);
		  addExercise(new LastDigit(getGame(), this),bat);

		  // Second serie of boolean fun
		  bat = addExercise(new AlarmClock(getGame(), this));
		  addExercise(new AnswerCell(getGame(), this),bat);
		  addExercise(new BlueTicket(getGame(), this),bat);
		  addExercise(new CaughtSpeeding(getGame(), this),bat);
		  addExercise(new CigarParty(getGame(), this),bat);
		  addExercise(new DateFashion(getGame(), this),bat);
		  addExercise(new GreenTicket(getGame(), this),bat);
		  addExercise(new In1To10(getGame(), this),bat);
		  addExercise(new InOrder(getGame(), this),bat);
		  addExercise(new InOrderEqual(getGame(), this),bat);
		  addExercise(new LastDigit2(getGame(), this),bat);
		  addExercise(new LessBy10(getGame(), this),bat);
		  addExercise(new MaxMod5(getGame(), this),bat);
		  addExercise(new NearTen(getGame(), this),bat);
		  addExercise(new RedTicket(getGame(), this),bat);
		  addExercise(new ShareDigit(getGame(), this),bat);
		  addExercise(new SortaSum(getGame(), this),bat);
		  addExercise(new FizzBuzz(getGame(), this), bat);
		  addExercise(new SquirrelPlay(getGame(), this),bat);
		  addExercise(new TeaParty(getGame(), this),bat);
		  addExercise(new TeenSum(getGame(), this),bat);
		  addExercise(new TwoAsOne(getGame(), this),bat);
		  addExercise(new WithoutDoubles(getGame(), this),bat);

		  
		// arrays exercises 
		Lecture arrays = addExercise(new Array1(getGame(), this));
		addExercise(new Array2(getGame(), this),arrays);
		addExercise(new IndexOfValue(getGame(), this),arrays);
		addExercise(new OccurrenceOfValue(getGame(), this),arrays);
		addExercise(new AverageValue(getGame(), this),arrays);
		addExercise(new MaxValue(getGame(), this),arrays);
		addExercise(new IndexOfMaxValue(getGame(), this),arrays);
		addExercise(new Extrema(getGame(), this),arrays);
		addExercise(new SecondMaxValue(getGame(), this),arrays);
		addExercise(new ArrayCount9(getGame(), this),arrays);
		addExercise(new ArrayFront9(getGame(), this),arrays);
		addExercise(new Array123(getGame(), this),arrays);
		addExercise(new Array667(getGame(), this),arrays);
		addExercise(new NoTriples(getGame(), this),arrays);
		addExercise(new Has271(getGame(), this),arrays);
		addExercise(new Golomb(getGame(), this),arrays);
		addExercise(new Island(getGame(), this),arrays);

		
		// 2D traversals
		Lecture snake = addExercise(new Snake(getGame(), this));
		addExercise(new TraversalByColumn(getGame(), this),snake);
		addExercise(new TraversalByLine(getGame(), this),snake);
		addExercise(new TraversalZigZag(getGame(), this),snake);
		addExercise(new TraversalDiagonal(getGame(), this),snake);
		
		// Other exercises
		addExercise(new Moria(getGame(), this));

		
	}
}
