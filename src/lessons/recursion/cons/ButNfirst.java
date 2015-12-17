package lessons.recursion.cons;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;
import plm.universe.cons.ConsExercise;
import plm.universe.cons.ConsWorld;
import plm.universe.cons.RecList;

public class ButNfirst extends ConsExercise {

	public ButNfirst(Game game, Lesson lesson) {
		super(game, lesson);
		
		BatWorld myWorld = new ConsWorld(game, "butNfirst");
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3, 4}), 3);
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3, 4}), 2);
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 1, 3, 2}), 0);
		myWorld.addTest(INVISIBLE,   data(new int[]{1, 1, 1}), 3);
		myWorld.addTest(INVISIBLE, data(new int[]{2, 4, 6, 8, 10}), 12);
		myWorld.addTest(INVISIBLE, data(new int[]{6}), 1);
		myWorld.addTest(INVISIBLE, data(new int[]{}),  1);
		myWorld.addTest(INVISIBLE, data(new int[]{}),  0);

		templatePython("butNfirst", new String[]{"RecList","Int"},
				"def butNfirst(list, n):\n",
				"  if list == None or n == 0:\n"+
				"    return list\n"+
				"  return butNfirst(list.tail, n-1)\n");
		templateScala("butNfirst", new String[] {"List[Int]","Int"}, 
				"def butNfirst(l:List[Int], n:Int): List[Int] = {\n",
				"  if (n==0 || l==Nil) l\n"+
 			 	"  else                butNfirst(l.tail, n-1)\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( butNfirst( (RecList)t.getParameter(0), (Integer)t.getParameter(1) ) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	RecList butNfirst(RecList seq, int n) {
		/* BEGIN SOLUTION */
		if (seq == null || n==0)
			return seq;
		return butNfirst(seq.tail, n-1);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
