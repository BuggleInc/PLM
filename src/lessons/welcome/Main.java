package lessons.welcome;

import jlm.core.model.lesson.Lecture;
import jlm.core.model.lesson.Lesson;
import lessons.welcome.array.AverageValue;
import lessons.welcome.array.IndexOfMaxValue;
import lessons.welcome.array.IndexOfValue;
import lessons.welcome.array.MaxValue;
import lessons.welcome.array.OccurrenceOfValue;
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
import lessons.welcome.bool2.AlarmClock;
import lessons.welcome.bool2.AnswerCell;
import lessons.welcome.bool2.BlueTicket;
import lessons.welcome.bool2.CaughtSpeeding;
import lessons.welcome.bool2.CigarParty;
import lessons.welcome.bool2.DateFashion;
import lessons.welcome.bool2.GreenTicket;
import lessons.welcome.bool2.In1To10;
import lessons.welcome.bool2.InOrder;
import lessons.welcome.bool2.InOrderEqual;
import lessons.welcome.bool2.LastDigit2;
import lessons.welcome.bool2.LessBy10;
import lessons.welcome.bool2.MaxMod5;
import lessons.welcome.bool2.NearTen;
import lessons.welcome.bool2.RedTicket;
import lessons.welcome.bool2.ShareDigit;
import lessons.welcome.bool2.SortaSum;
import lessons.welcome.bool2.SquirrelPlay;
import lessons.welcome.bool2.TeaParty;
import lessons.welcome.bool2.TeenSum;
import lessons.welcome.bool2.TwoAsOne;
import lessons.welcome.bool2.WithoutDoubles;

public class Main extends Lesson {

	@Override
	protected void loadExercises() {
		Lecture environment = addExercise(new Environment(this));
		Lecture basics = addExercise(new Basics(this), environment);
		
		addExercise(new BasicsDrawG(this), basics);
		
		Lecture conditions = addExercise(new Conditions(this), basics);
		
		Lecture loopWhile = addExercise(new LoopWhile(this), conditions);
		addExercise(new BaggleSeeker(this), loopWhile);

		Lecture variables = addExercise(new Variables(this), loopWhile);
		
		Lecture loopFor = addExercise(new LoopFor(this), variables );
		addExercise(new LoopDoWhile(this), loopFor);
		
		Lecture methodsVoid = addExercise(new Methods(this), loopFor);
		
		addExercise(new MethodsDogHouse(this),methodsVoid);
		addExercise(new MethodsReturning(this),methodsVoid);
		
		Lecture methods = addExercise(new MethodsArgs(this));
		addExercise(new MethodsPicture(this));
		addExercise(new MethodsPicture2(this));
		addExercise(new MethodsPicture3(this));
		addExercise(new MethodsPicture4(this));
				
		addExercise(new BDR(this),conditions);
		addExercise(new BDR2(this));
		
		addExercise(new SlugHunting(this),methods);
		addExercise(new Snake(this));
		
		addExercise(new Array(this),methods);
		Lecture arrays = addExercise(new Array2(this));
		
		/* Turmites exercises */	
		addExercise(new lessons.welcome.cells.Langton(this), arrays);
		addExercise(new lessons.welcome.cells.LangtonColors(this));
		addExercise(new lessons.welcome.cells.HelloTurmite(this));
		addExercise(new lessons.welcome.cells.TurmiteCreator(this));
		
		/* Arrays exercises */ 
		addExercise(new IndexOfValue(this),arrays);
		addExercise(new OccurrenceOfValue(this));
		addExercise(new AverageValue(this));
		addExercise(new MaxValue(this));
		addExercise(new IndexOfMaxValue(this));

		/* Traversal exercises */
		addExercise(new TraversalByColumn(this),arrays);
		addExercise(new TraversalByLine(this));
		addExercise(new TraversalZigZag(this));
		addExercise(new TraversalDiagonal(this));

		/* First serie of boolean fun */
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

		/* Second serie of boolean fun */
		addExercise(new AlarmClock(this),conditions);
		addExercise(new AnswerCell(this));
		addExercise(new BlueTicket(this));
		addExercise(new CaughtSpeeding(this));
		addExercise(new CigarParty(this));
		addExercise(new DateFashion(this));
		addExercise(new GreenTicket(this));
		addExercise(new In1To10(this));
		addExercise(new InOrder(this));
		addExercise(new InOrderEqual(this));
		addExercise(new LastDigit2(this));
		addExercise(new LessBy10(this));
		addExercise(new MaxMod5(this));
		addExercise(new NearTen(this));
		addExercise(new RedTicket(this));
		addExercise(new ShareDigit(this));
		addExercise(new SortaSum(this));
		addExercise(new SquirrelPlay(this));
		addExercise(new TeaParty(this));
		addExercise(new TeenSum(this));
		addExercise(new TwoAsOne(this));
		addExercise(new WithoutDoubles(this));
	}

}
