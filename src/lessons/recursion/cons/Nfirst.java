package lessons.recursion.cons;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;
import plm.universe.cons.ConsExercise;
import plm.universe.cons.ConsWorld;
import plm.universe.cons.RecList;

public class Nfirst extends ConsExercise {

	public Nfirst(Game game, Lesson lesson) {
		super(game, lesson);
		
		BatWorld myWorld = new ConsWorld(game, "nfirst");
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3, 4}), 1);
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3, 4}), 2);
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3, 4}), 3);
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3, 4}), 4);
		myWorld.addTest(VISIBLE,   data(new int[]{1, 1, 1}), 2);
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 1, 3, 2}), 5);
		myWorld.addTest(INVISIBLE, data(new int[]{2, 4, 6, 8, 10}), 2);
		myWorld.addTest(INVISIBLE, data(new int[]{6}), 1);
		myWorld.addTest(INVISIBLE, data(new int[]{1, 2, 3, 4}), 0);
		myWorld.addTest(INVISIBLE, data(new int[]{}), 0);

		templatePython("nfirst", new String[]{"RecList", "Int"},
				"def nfirst(list, n):\n",
				"  if n == 0:\n"+
				"    return None\n"+
				"  return cons(list.head, nfirst(list.tail, n-1))\n");
		templateScala("nfirst", new String[] {"List[Int]", "Int"}, 
				"def nfirst(l:List[Int], n:Int): List[Int] = {\n",
 			 	"  if (n == 0) return Nil\n" +
				"  else        return l.head :: nfirst(l.tail, n-1)\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( nfirst( (RecList)t.getParameter(0), (Integer)t.getParameter(1) ) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	RecList nfirst(RecList seq, int n) {
		/* BEGIN SOLUTION */
		if (n == 0) return null;
		return cons( seq.head, nfirst(seq.tail, n-1));
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
