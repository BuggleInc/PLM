package lessons.recursion.cons;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;
import plm.universe.cons.ConsExercise;
import plm.universe.cons.ConsWorld;
import plm.universe.cons.RecList;

public class IsMember extends ConsExercise {

	public IsMember(Game game, Lesson lesson) {
		super(game, lesson);
		
		BatWorld myWorld = new ConsWorld(game, "isMember");
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3, 4}), 1);
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3, 4}), 2);
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3, 4}), 42);
		myWorld.addTest(VISIBLE,   data(new int[]{1, 1, 1}), 1);
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 1, 3, 2}), 2) ;
		myWorld.addTest(INVISIBLE, data(new int[]{2, 4, 6, 8, 10}), -2) ;
		myWorld.addTest(INVISIBLE, data(new int[]{}), 42) ;

		templatePython("isMember", new String[]{"RecList", "Int"},
				"def isMember(list, val):\n",
				"  if list == None:\n" +
				"    return False;\n"+
				"  if list.head == val:\n"+
				"    return True\n"+
				"  return isMember(list.tail, val)\n");
		templateScala("isMember", new String[] {"List[Int]", "Int"}, 
				"def isMember(l:List[Int], v:Int): Boolean = {\n",
 			 	"  l match {\n" +
				"    case a::b if a==v => true\n"+
				"    case a::b         => isMember(b,v)\n"+
				"    case _    => false\n"+
				"  }\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( isMember( (RecList)t.getParameter(0), (int)t.getParameter(1) ) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	Boolean isMember(RecList seq, int val) {
		/* BEGIN SOLUTION */
		if (seq == null)
			return false;
		if (seq.head == val)
			return true;
		return isMember(seq.tail,val);
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
