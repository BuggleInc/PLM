package lessons.recursion.cons;

import lessons.recursion.cons.universe.ConsExercise;
import lessons.recursion.cons.universe.ConsWorld;
import lessons.recursion.cons.universe.RecList;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Concat extends ConsExercise {

	public Concat(Lesson lesson) {
		super(lesson);
		
		BatWorld myWorld = new ConsWorld("concat");
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3}),    data(new int[]{11, 12, 13}));
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3}),    data(new int[]{1, 1, 1}));
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3}),    data(new int[]{}));
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 1, 3}), data(new int[]{64,36})) ;
		myWorld.addTest(INVISIBLE, data(new int[]{2, 4, 6, 8, 10}), data(new int[]{72,35})) ;
		myWorld.addTest(INVISIBLE, data(new int[]{}), data(new int[]{3,5,8})) ;
		myWorld.addTest(INVISIBLE, data(new int[]{}), data(new int[]{})) ;
		myWorld.addTest(INVISIBLE, data(new int[]{-2, -4, -6, -8, -10}), data(new int[]{2,4,6,8,10})) ;

		// unrecursed version, as it's impossible to add functions to BatExercises
		
		templatePython("concat", new String[]{"RecList", "List[Int]"},
				"def concat(list1, list2):\n",
				/* revert list in A */
				"  A = None\n"+
				"  B = list1\n"+
				"  while B != None:\n"+
				"     A = cons (B.head, A)\n"+
				"     B = B.tail\n"+
				/* Add A in front of list2 */
				"  B = list2\n"+
				"  while A != None:\n"+
				"     B = cons(A.head, B)\n"+
				"     A = A.tail\n"+
				"  return B");
		templateScala("concat", new String[] {"List[Int]", "List[Int]"}, 
				"def concat(l1:List[Int],  l2:List[Int]): List[Int] = {\n",
				"  def reverse_helper(todo:List[Int], done:List[Int]):List[Int] = {"+
				"     if (todo == Nil) return done\n"+
				"     return reverse_helper(todo.tail, todo.head::done)\n"+
				"  }\n"+
				"  reverse_helper(  reverse_helper(l1, Nil), l2  )\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( concat((RecList)t.getParameter(0), (RecList)t.getParameter(1)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	RecList concat(RecList seq1, RecList seq2) {
		/* BEGIN SOLUTION */
		// Revert seq1 into A
		RecList A = null;
		RecList B = seq1;
		while (B != null) {
			A = cons (B.head, A);
			B = B.tail;
		}
		// add A at front of seq2 in B
		B = seq2;
		while (A != null) {
			B = cons (A.head, B);
			A = A.tail;
		}
		return B;
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
