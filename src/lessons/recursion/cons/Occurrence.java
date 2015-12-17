package lessons.recursion.cons;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;
import plm.universe.cons.ConsExercise;
import plm.universe.cons.ConsWorld;
import plm.universe.cons.RecList;

public class Occurrence extends ConsExercise {

	public Occurrence(Game game, Lesson lesson) {
		super(game, lesson);
		
		BatWorld myWorld = new ConsWorld(game, "Occurence");
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 2, 4}), 1);
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 2, 4}), 2);
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3, 4}), 42);
		myWorld.addTest(VISIBLE,   data(new int[]{1, 1, 1}), 1);
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 1, 3, 2}), 2) ;
		myWorld.addTest(INVISIBLE, data(new int[]{2, 4, 6, 8, 10}), -2) ;
		myWorld.addTest(INVISIBLE, data(new int[]{}), 42) ;

		templatePython("occurences", new String[]{"RecList", "Int"},
				"def occurences(list, val):\n",
				"  if list == None:\n" +
				"    return 0;\n"+
				"  if list.head == val:\n"+
				"    return 1 + occurences(list.tail, val)\n"+
				"  return occurences(list.tail, val)\n");
		templateScala("occurences", new String[] {"List[Int]", "Int"}, 
				"def occurences(l:List[Int], v:Int): Int = {\n",
 			 	"  l match {\n" +
				"    case a::b if a==v => 1 + occurences(b,v)\n"+
				"    case a::b         =>     occurences(b,v)\n"+
				"    case _    => 0\n"+
				"  }\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( occurences( (RecList)t.getParameter(0), (int)t.getParameter(1) ) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	int occurences(RecList seq, int val) {
		/* BEGIN SOLUTION */
		if (seq == null)
			return 0;
		if (seq.head == val)
			return 1 + occurences(seq.tail, val);
		return occurences(seq.tail,val);
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
