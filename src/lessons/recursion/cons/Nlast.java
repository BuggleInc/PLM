package lessons.recursion.cons;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;
import plm.universe.cons.ConsExercise;
import plm.universe.cons.ConsWorld;
import plm.universe.cons.RecList;

public class Nlast extends ConsExercise {

	public Nlast(Game game, Lesson lesson) {
		super(game, lesson);
		
		BatWorld myWorld = new ConsWorld(game, "nlast");
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
		
		templatePython("nlast", new String[]{"RecList","Int"},
				"def nlast(list, n):\n",
				"  if list == None or list.plmInsiderLength() <= n:\n"+
				"    return list\n"+
				"  return nlast(list.tail, n)\n");
		templateScala("nlast", new String[] {"List[Int]","Int"}, 
				"def nlast(l:List[Int], n:Int): List[Int] = {\n",
				"  def ButnFirst(l:List[Int], n:Int): List[Int] = {\n"+
				"    if (n<=0 || l==Nil) return l\n"+
				"    return ButnFirst(l.tail, n-1)\n"+
				"  }\n"+
				"  return ButnFirst(l, l.size-n)\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( nlast( (RecList)t.getParameter(0), (Integer)t.getParameter(1) ) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	RecList nlast(RecList seq, int n) {
		/* BEGIN SOLUTION */
		if (seq == null || seq.plmInsiderLength() <= n)
			return seq;
		return nlast(seq.tail, n);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
