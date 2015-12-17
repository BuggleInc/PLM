package lessons.recursion.cons;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;
import plm.universe.cons.ConsExercise;
import plm.universe.cons.ConsWorld;
import plm.universe.cons.RecList;

public class Reverse extends ConsExercise {

	public Reverse(Game game, Lesson lesson) {
		super(game, lesson);
		
		BatWorld myWorld = new ConsWorld(game, "reverse");
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3}));
		myWorld.addTest(VISIBLE,   data(new int[]{1, 1, 1}));
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 1, 3})) ;
		myWorld.addTest(INVISIBLE, data(new int[]{2, 4, 6, 8, 10})) ;
		myWorld.addTest(INVISIBLE, data(new int[]{})) ;
		myWorld.addTest(INVISIBLE, data(new int[]{-2, -4, -6, -8, -10})) ;

		// unrecursed version, as it's impossible to add functions to BatExercises
		
		templatePython("reverse", new String[]{"RecList"},
				"def reverse(list):\n",
				"  A = None\n"+
				"  B = list\n"+
				"  while B != None:\n"+
				"     A = cons (B.head, A)\n"+
				"     B = B.tail\n"+
				"  return A\n");
		templateScala("reverse", new String[] {"List[Int]"}, 
				"def reverse(l:List[Int]): List[Int] = {\n",
				"  def lambda(l:List[Int], tmp:List[Int]):List[Int] = {"+
				"     if (l == Nil) return tmp\n"+
				"     return lambda(l.tail, l.head::tmp)\n"+
				"  }\n"+
				"  lambda(l, Nil)\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( reverse((RecList)t.getParameter(0)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	RecList reverse(RecList seq) {
		/* BEGIN SOLUTION */
		RecList A = null;
		RecList B = seq;
		while (B != null) {
			A = cons (B.head, A);
			B = B.tail;
		}
		return A;
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
