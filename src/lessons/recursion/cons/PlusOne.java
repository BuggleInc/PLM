package lessons.recursion.cons;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;
import plm.universe.cons.ConsExercise;
import plm.universe.cons.ConsWorld;
import plm.universe.cons.RecList;

public class PlusOne extends ConsExercise {

	public PlusOne(Game game, Lesson lesson) {
		super(game, lesson);
		
		BatWorld myWorld = new ConsWorld(game, "plusOne");
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3}));
		myWorld.addTest(VISIBLE,   data(new int[]{1, 1, 1}));
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 1, 3})) ;
		myWorld.addTest(INVISIBLE, data(new int[]{2, 4, 6, 8, 10})) ;
		myWorld.addTest(INVISIBLE, data(new int[]{})) ;
		myWorld.addTest(INVISIBLE, data(new int[]{-2, -4, -6, -8, -10})) ;

		templatePython("plusOne", new String[]{"RecList"},
				"def plusOne(list):\n",
				"  if list == None:\n" +
				"    return None;\n"+
				"  return cons(list.head+1, plusOne(list.tail))\n");
		templateScala("plusOne", new String[] {"List[Int]"}, 
				"def plusOne(l:List[Int]): List[Int] = {\n",
				"  l match {\n" +
				"    case a::b => (a+1)::plusOne(b)\n"+
				"    case _    => Nil\n"+
				"  }\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( plusOne((RecList)t.getParameter(0)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	RecList plusOne(RecList seq) {
		/* BEGIN SOLUTION */
		if (seq == null)
			return null;
		return cons(seq.head+1, plusOne(seq.tail));
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
