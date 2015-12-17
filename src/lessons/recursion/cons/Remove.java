package lessons.recursion.cons;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;
import plm.universe.cons.ConsExercise;
import plm.universe.cons.ConsWorld;
import plm.universe.cons.RecList;

public class Remove extends ConsExercise {

	public Remove(Game game, Lesson lesson) {
		super(game, lesson);
		
		BatWorld myWorld = new ConsWorld(game, "remove");
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3}),    1);
		myWorld.addTest(VISIBLE,   data(new int[]{1, 1, 2}),    1);
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 1, 3}), 3);
		myWorld.addTest(INVISIBLE, data(new int[]{2, 4, 6, 8, 10}), 10) ;
		myWorld.addTest(INVISIBLE, data(new int[]{1, 1, 1}),  1);
		myWorld.addTest(INVISIBLE, data(new int[]{}), 1) ;
		myWorld.addTest(INVISIBLE, data(new int[]{-2, -4, -6, -8, -10}), -4) ;

		templatePython("remove", new String[]{"RecList","Int"},
				"def remove(list, v):\n",
				"  if list == None:\n" +
				"    return None;\n"+
				"  if list.head == v:\n"+
				"    return remove(list.tail, v)\n"+
				"  return cons(list.head, remove(list.tail, v))\n");
		templateScala("remove", new String[] {"List[Int]", "Int"}, 
				"def remove(l:List[Int], v:Int): List[Int] = {\n",
				"  l match {\n" +
				"    case a::_ if a==v => remove(l.tail, v)\n"+
				"    case a::b         => a::remove(b, v)\n"+
				"    case _            => Nil\n"+
				"  }\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( remove((RecList)t.getParameter(0), (Integer)t.getParameter(1)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	RecList remove(RecList seq, int v) {
		/* BEGIN SOLUTION */
		if (seq == null)
			return null;
		if (seq.head == v)
			return remove(seq.tail, v);
		return cons(seq.head, remove(seq.tail, v));
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
