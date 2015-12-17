package lessons.recursion.cons;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;
import plm.universe.cons.ConsExercise;
import plm.universe.cons.ConsWorld;
import plm.universe.cons.RecList;

public class Nth extends ConsExercise {

	public Nth(Game game, Lesson lesson) {
		super(game, lesson);
		
		BatWorld myWorld = new ConsWorld(game, "nth");
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3, 4}), 1);
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3, 4}), 2);
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3, 4}), 3);
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3, 4}), 4);
		myWorld.addTest(VISIBLE,   data(new int[]{1, 1, 1}), 2);
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 1, 3, 2}), 5);
		myWorld.addTest(INVISIBLE, data(new int[]{2, 4, 6, 8, 10}), 2);
		myWorld.addTest(INVISIBLE, data(new int[]{6}), 1);

		templatePython("nth", new String[]{"RecList", "Int"},
				"def nth(list, n):\n",
				"  if n == 1:\n"+
				"    return list.head\n"+
				"  return nth(list.tail, n-1)\n");
		templateScala("nth", new String[] {"List[Int]", "Int"}, 
				"def nth(l:List[Int], n:Int): Int = {\n",
 			 	"  if (n == 1) return l.head\n" +
				"  else        return nth(l.tail, n-1)\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( nth( (RecList)t.getParameter(0), (Integer)t.getParameter(1) ) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	int nth(RecList seq, int n) {
		/* BEGIN SOLUTION */
		if (n == 1) return seq.head;
		return nth(seq.tail, n-1);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
