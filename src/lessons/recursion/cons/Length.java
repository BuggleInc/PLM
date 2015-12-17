package lessons.recursion.cons;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;
import plm.universe.cons.ConsExercise;
import plm.universe.cons.ConsWorld;
import plm.universe.cons.RecList;

public class Length extends ConsExercise {

	public Length(Game game, Lesson lesson) {
		super(game, lesson);
		
		BatWorld myWorld = new ConsWorld(game, "length");
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3}));
		myWorld.addTest(VISIBLE,   data(new int[]{1, 1, 1}));
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 1, 3})) ;
		myWorld.addTest(INVISIBLE, data(new int[]{2, 4, 6, 8, 10})) ;
		myWorld.addTest(INVISIBLE, data(new int[]{})) ;

		templatePython("length", new String[]{"RecList"},
				"def length(list):\n",
				"  if list == None:\n" +
				"    return 0;\n"+
				"  return 1 + length(list.tail)\n");
		templateScala("length", new String[] {"List[Int]"}, 
				"def length(l:List[Int]): Int = {\n",
				"  l match {\n" +
				"    case a::b => 1+length(b)\n"+
				"    case _    => 0\n"+
				"  }\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( length( (RecList)t.getParameter(0) ) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	int length(RecList seq) {
		/* BEGIN SOLUTION */
		if (seq == null)
			return 0;
		return 1+length(seq.tail);
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
