package lessons.recursion.cons;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;
import plm.universe.cons.ConsExercise;
import plm.universe.cons.ConsWorld;
import plm.universe.cons.RecList;

public class AllDifferent extends ConsExercise {

	public AllDifferent(Game game, Lesson lesson) {
		super(game, lesson);
		
		BatWorld myWorld = new ConsWorld(game, "allDifferent");
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3, 4}));
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 2, 4}));
		myWorld.addTest(VISIBLE,   data(new int[]{1, 1, 1}));
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 4, 3, 1})) ;
		myWorld.addTest(INVISIBLE, data(new int[]{2, 4, 6, 8, 10})) ;
		myWorld.addTest(INVISIBLE, data(new int[]{})) ;

		templatePython("allDifferent", new String[]{"RecList"},
				"def allDifferent(list):\n",
				"  if list == None:\n" +
				"    return True;\n"+
				"  ptr = list.tail\n" +
				"  while ptr != None and ptr.head != list.head:\n"+
				"    ptr = ptr.tail\n"+
				"  if ptr != None:\n"+
				"    return False\n"+
				"  return allDifferent(list.tail)\n");
		templateScala("allDifferent", new String[] {"List[Int]"}, 
				"def allDifferent(l:List[Int]): Boolean = {\n",
				"  if (l == Nil)                 return true\n" +
 			 	"  if (isMember(l.tail, l.head)) return false\n" +
				"  return allDifferent(l.tail)\n"+
				"}\n" +
				"def isMember(l:List[Int], v:Int): Boolean = {\n"+
 			 	"  if (l == Nil)    return false\n" +
				"  if (v == l.head) return true\n"+
				"  return isMember(l.tail, v)\n"+
				"}");

		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( allDifferent( (RecList)t.getParameter(0) ) );
		/* END SKEL */
	}
	
	/* BEGIN TEMPLATE */
	boolean allDifferent(RecList seq) {
		/* BEGIN SOLUTION */
		if (seq == null)
			return true;
		/* inline compute isMember */
		RecList ptr = seq.tail;
		while (ptr != null && ptr.head != seq.head) 
			ptr = ptr.tail;
		if (ptr != null) 
			return false;
		/* end isMember */
		return allDifferent(seq.tail);
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
