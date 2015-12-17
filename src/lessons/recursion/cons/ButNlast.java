package lessons.recursion.cons;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;
import plm.universe.cons.ConsExercise;
import plm.universe.cons.ConsWorld;
import plm.universe.cons.RecList;

public class ButNlast extends ConsExercise {

	public ButNlast(Game game, Lesson lesson) {
		super(game, lesson);
		
		BatWorld myWorld = new ConsWorld(game, "butNlast");
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3, 4}), 3);
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3, 4}), 2);
		myWorld.addTest(VISIBLE,   data(new int[]{1, 1, 1}), 3);
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 1, 3, 2}), 0);
		myWorld.addTest(INVISIBLE, data(new int[]{2, 4, 6, 8, 10}), 12);
		myWorld.addTest(INVISIBLE, data(new int[]{6}), 1);
		myWorld.addTest(INVISIBLE, data(new int[]{}),  1);
		myWorld.addTest(INVISIBLE, data(new int[]{}),  0);

		// Note that the correction is NOT in linear time :-/
		// It's very difficult to define extra functions in BatExercises
		
		templatePython("butNlast", new String[]{"RecList","Int"},
				"def butNlast(list, n):\n",
				"  if list == None or list.plmInsiderLength() <= n:\n"+
				"    return None\n"+
				"  return cons( list.head, butNlast(list.tail, n) )\n");
		templateScala("butNlast", new String[] {"List[Int]","Int"}, 
				"def butNlast(l:List[Int], n:Int): List[Int] = {\n",
				"  def nFirst(l:List[Int], n:Int): List[Int] = {\n"+
				"    if (n<=0 || l==Nil) return Nil\n"+
				"    return l.head::nFirst(l.tail, n-1)\n"+
				"  }\n"+
				"  return nFirst(l, l.size-n)\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( butNlast( (RecList)t.getParameter(0), (Integer)t.getParameter(1) ) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	RecList butNlast(RecList seq, int n) {
		/* BEGIN SOLUTION */
		if (seq == null || seq.plmInsiderLength() <= n)
			return null;
		return cons( seq.head, butNlast(seq.tail, n));
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
