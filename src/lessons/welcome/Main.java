package lessons.welcome;

import jlm.core.model.lesson.Lecture;
import jlm.core.model.lesson.Lesson;
import lessons.welcome.array.averagevalue.AverageValue;
import lessons.welcome.array.basics.Array;
import lessons.welcome.array.basics2.Array2;
import lessons.welcome.array.indexof.maxvalue.IndexOfMaxValue;
import lessons.welcome.array.indexof.value.IndexOfValue;
import lessons.welcome.array.maxvalue.MaxValue;
import lessons.welcome.array.occurenceofvalue.OccurrenceOfValue;
import lessons.welcome.baggleseeker.BaggleSeeker;
import lessons.welcome.basics.Basics;
import lessons.welcome.basicsdrawg.BasicsDrawG;
import lessons.welcome.bdr.basics.BDR;
import lessons.welcome.bdr.extended.BDR2;
import lessons.welcome.bool1.close10.Close10;
import lessons.welcome.bool1.countteen.CountTeen;
import lessons.welcome.bool1.dif21.Diff21;
import lessons.welcome.bool1.hasteen.HasTeen;
import lessons.welcome.bool1.icyhot.IcyHot;
import lessons.welcome.bool1.in1020.In1020;
import lessons.welcome.bool1.in3050.In3050;
import lessons.welcome.bool1.lastdigit.LastDigit;
import lessons.welcome.bool1.loneteen.LoneTeen;
import lessons.welcome.bool1.makes10.Makes10;
import lessons.welcome.bool1.max1020.Max1020;
import lessons.welcome.bool1.monkeytrouble.MonkeyTrouble;
import lessons.welcome.bool1.nearhundred.NearHundred;
import lessons.welcome.bool1.parottrouble.ParotTrouble;
import lessons.welcome.bool1.posneg.PosNeg;
import lessons.welcome.bool1.sleepin.SleepIn;
import lessons.welcome.bool1.sumdouble.SumDouble;
import lessons.welcome.bool2.alarmclock.AlarmClock;
import lessons.welcome.bool2.answercell.AnswerCell;
import lessons.welcome.bool2.caughtspeeding.CaughtSpeeding;
import lessons.welcome.bool2.datefashion.DateFashion;
import lessons.welcome.bool2.in1to10.In1To10;
import lessons.welcome.bool2.inorder.InOrder;
import lessons.welcome.bool2.inorderequals.InOrderEqual;
import lessons.welcome.bool2.lastdigit2.LastDigit2;
import lessons.welcome.bool2.lessby10.LessBy10;
import lessons.welcome.bool2.maxmod5.MaxMod5;
import lessons.welcome.bool2.nearten.NearTen;
import lessons.welcome.bool2.party.cigar.CigarParty;
import lessons.welcome.bool2.party.tea.TeaParty;
import lessons.welcome.bool2.sharedigit.ShareDigit;
import lessons.welcome.bool2.sortasum.SortaSum;
import lessons.welcome.bool2.squirrelplay.SquirrelPlay;
import lessons.welcome.bool2.teensum.TeenSum;
import lessons.welcome.bool2.ticket.blue.BlueTicket;
import lessons.welcome.bool2.ticket.green.GreenTicket;
import lessons.welcome.bool2.ticket.red.RedTicket;
import lessons.welcome.bool2.twoasone.TwoAsOne;
import lessons.welcome.bool2.withoutdoubles.WithoutDoubles;
import lessons.welcome.conditions.Conditions;
import lessons.welcome.environment.Environment;
import lessons.welcome.loop.dowhileloop.LoopDoWhile;
import lessons.welcome.loop.forloop.LoopFor;
import lessons.welcome.loop.whileloop.LoopWhile;
import lessons.welcome.methods.args.MethodsArgs;
import lessons.welcome.methods.basics.Methods;
import lessons.welcome.methods.doghouse.MethodsDogHouse;
import lessons.welcome.methods.picture.MethodsPicture;
import lessons.welcome.methods.picture2.MethodsPicture2;
import lessons.welcome.methods.picture3.MethodsPicture3;
import lessons.welcome.methods.picture4.MethodsPicture4;
import lessons.welcome.methods.returning.MethodsReturning;
import lessons.welcome.slug.hunting.SlugHunting;
import lessons.welcome.slug.tracking.SlugTracking;
import lessons.welcome.snake.Snake;
import lessons.welcome.traversal.column.TraversalByColumn;
import lessons.welcome.traversal.diagonal.TraversalDiagonal;
import lessons.welcome.traversal.line.TraversalByLine;
import lessons.welcome.traversal.zigzag.TraversalZigZag;
import lessons.welcome.turmites.helloturmite.HelloTurmite;
import lessons.welcome.turmites.langton.Langton;
import lessons.welcome.turmites.langtoncolors.LangtonColors;
import lessons.welcome.turmites.turmitecreator.TurmiteCreator;
import lessons.welcome.variables.Variables;

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
		addExercise(new SlugTracking(this));
		addExercise(new Snake(this));
		
		addExercise(new Array(this),methods);
		Lecture arrays = addExercise(new Array2(this));
		
		/* Turmites exercises */	
		addExercise(new Langton(this), arrays);
		addExercise(new LangtonColors(this));
		addExercise(new HelloTurmite(this));
		addExercise(new TurmiteCreator(this));
		
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
