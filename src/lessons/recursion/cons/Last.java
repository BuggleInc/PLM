package lessons.recursion.cons;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;
import plm.universe.cons.ConsExercise;
import plm.universe.cons.ConsWorld;
import plm.universe.cons.RecList;

public class Last extends ConsExercise {

	public Last(Game game, Lesson lesson) {
		super(game, lesson);
		
		BatWorld myWorld = new ConsWorld(game, "last");
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3, 4}));
		myWorld.addTest(VISIBLE,   data(new int[]{1, 1, 1}));
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 1, 3, 2}));
		myWorld.addTest(INVISIBLE, data(new int[]{2, 4, 6, 8, 10}));
		myWorld.addTest(INVISIBLE, data(new int[]{6}));

		templatePython("last", new String[]{"RecList"},
				"def last(list):\n",
				"  if list.tail == None:\n"+
				"    return list.head\n"+
				"  return last(list.tail)\n");
		templateScala("last", new String[] {"List[Int]"}, 
				"def last(l:List[Int]): Int = {\n",
 			 	"  l match {\n" +
				"    case a::b if b==Nil => a\n"+
				"    case a::b            => last(b)\n"+
				"  }\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( last( (RecList)t.getParameter(0) ) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	int last(RecList seq) {
		/* BEGIN SOLUTION */
		if (seq.tail == null)
			return seq.head;
		return last(seq.tail);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
