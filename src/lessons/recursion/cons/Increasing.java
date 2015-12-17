package lessons.recursion.cons;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;
import plm.universe.cons.ConsExercise;
import plm.universe.cons.ConsWorld;
import plm.universe.cons.RecList;

public class Increasing extends ConsExercise {

	public Increasing(Game game, Lesson lesson) {
		super(game, lesson);
		
		BatWorld myWorld = new ConsWorld(game, "increasing");
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3, 4}));
		myWorld.addTest(VISIBLE,   data(new int[]{1, 1, 1}));
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 1, 3, 2}));
		myWorld.addTest(INVISIBLE, data(new int[]{2, 4, 6, 8, 10}));
		myWorld.addTest(INVISIBLE, data(new int[]{6}));

		templatePython("increasing", new String[]{"RecList"},
				"def increasing(list):\n",
				"  if list == None or list.tail == None:\n"+
				"    return True\n"+
				"  if list.head > list.tail.head:\n"+
				"    return False\n"+
				"  return increasing(list.tail)\n");
		templateScala("increasing", new String[] {"List[Int]"}, 
				"def increasing(l:List[Int]): Boolean = {\n",
				"  if (l == Nil || l.tail == Nil) return true\n" +
 			 	"  if (l.head > l.tail.head)      return false\n" +
				"  return increasing(l.tail)\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( increasing( (RecList)t.getParameter(0) ) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean increasing(RecList seq) {
		/* BEGIN SOLUTION */
		if (seq == null || seq.tail == null)
			return true;
		if (seq.head > seq.tail.head)
			return false;
		return increasing(seq.tail);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
