package lessons.recursion.cons;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;
import plm.universe.cons.ConsExercise;
import plm.universe.cons.ConsWorld;
import plm.universe.cons.RecList;

public class Min extends ConsExercise {

	public Min(Game game, Lesson lesson) {
		super(game, lesson);
		
		BatWorld myWorld = new ConsWorld(game, "min");
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3, 4}));
		myWorld.addTest(VISIBLE,   data(new int[]{1, 1, 1}));
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 1, 3, 2}));
		myWorld.addTest(INVISIBLE, data(new int[]{2, 4, 6, 8, 10}));
		myWorld.addTest(INVISIBLE, data(new int[]{6}));

		templatePython("min", new String[]{"RecList"},
				"def min(list):\n",
				"  ptr = list.tail\n"+
				"  v = list.head\n"+
				"  while ptr != None:\n"+
				"     if ptr.head < v:\n"+
				"        v = ptr.head\n"+
				"     ptr = ptr.tail\n"+
				"  return v\n");
		templateScala("min", new String[] {"List[Int]"}, 
				"def min(l:List[Int]): Int = {\n",
 			 	"  def min2(l:List[Int], v:Int): Int = {\n" +
				"    if (l==Nil) return v\n"+
				"    if (l.head < v) return min2(l.tail, l.head)\n"+
				"    return min2(l.tail, v)\n"+
				"  }\n"+
				"  return min2(l.tail, l.head)\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( min( (RecList)t.getParameter(0) ) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	int min(RecList seq) {
		/* BEGIN SOLUTION */
		int v = seq.head;
		RecList ptr = seq;
		while (ptr != null) {
			if (ptr.head < v)
				v = ptr.head;
			ptr = ptr.tail;
		}
		return v;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
