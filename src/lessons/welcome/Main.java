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
import lessons.welcome.bool1.diff21.Diff21;
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
		addExercise(new Environment(this));
		Lecture basics = addExercise(new Basics(this));
		
		addExercise(new BasicsDrawG(this), basics);
		
		Lecture conditions = addExercise(new Conditions(this));
		addExercise(new BDR(this),conditions);
		addExercise(new BDR2(this),conditions);
		
		Lecture loopWhile = addExercise(new LoopWhile(this));
		addExercise(new BaggleSeeker(this), loopWhile);

		addExercise(new Variables(this));
		
		Lecture loopFor = addExercise(new LoopFor(this));
		addExercise(new LoopDoWhile(this), loopFor);
		
		Lecture methodsVoid = addExercise(new Methods(this));
		
		  addExercise(new MethodsDogHouse(this),methodsVoid);
		
  		  Lecture picture = addExercise(new MethodsPicture(this),methodsVoid);
		  addExercise(new MethodsPicture2(this),picture);
		  addExercise(new MethodsPicture3(this),picture);
 	 	  addExercise(new MethodsPicture4(this),picture);
 	 	  
		addExercise(new MethodsReturning(this));
		
		addExercise(new MethodsArgs(this));		
				
		/* Slug hunting exercises */
		Lecture slug = addExercise(new SlugHunting(this));
		addExercise(new SlugTracking(this),slug);
		addExercise(new Snake(this),slug);
		
		/* arrays exercises */
		Lecture arrays = addExercise(new Array(this));
		addExercise(new Array2(this),arrays);
		addExercise(new IndexOfValue(this),arrays);
		addExercise(new OccurrenceOfValue(this),arrays);
		addExercise(new AverageValue(this),arrays);
		addExercise(new MaxValue(this),arrays);
		addExercise(new IndexOfMaxValue(this),arrays);
		
		/* Traversal exercises */
		Lecture traverse = addExercise(new TraversalByColumn(this));
		addExercise(new TraversalByLine(this),traverse);
		addExercise(new TraversalZigZag(this),traverse);
		addExercise(new TraversalDiagonal(this),traverse);
		
		/* Turmites exercises */	
		Lecture langton = addExercise(new Langton(this));//, arrays);
		addExercise(new LangtonColors(this),langton);
		addExercise(new HelloTurmite(this),langton);
		addExercise(new TurmiteCreator(this),langton);
		
		/* First serie of boolean fun */
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

		/* Second serie of boolean fun */
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
		addExercise(new SquirrelPlay(this),bat);
		addExercise(new TeaParty(this),bat);
		addExercise(new TeenSum(this),bat);
		addExercise(new TwoAsOne(this),bat);
		addExercise(new WithoutDoubles(this),bat);
	}

}
