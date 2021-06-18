package lessons.recursion.cons;

import lessons.recursion.cons.universe.ConsExercise;
import lessons.recursion.cons.universe.ConsWorld;
import lessons.recursion.cons.universe.RecList;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class ButLast extends ConsExercise {

	public ButLast(Lesson lesson) {
		super(lesson);
		
		BatWorld myWorld = new ConsWorld("butLast");
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 3, 4}));
		myWorld.addTest(VISIBLE,   data(new int[]{1, 1, 1}));
		myWorld.addTest(VISIBLE,   data(new int[]{1, 2, 1, 3, 2}));
		myWorld.addTest(INVISIBLE, data(new int[]{2, 4, 6, 8, 10}));
		myWorld.addTest(INVISIBLE, data(new int[]{6}));

		templatePython("butLast", new String[]{"RecList"},
				"def butLast(list):\n",
				"  if list.tail == None:\n"+
				"    return None\n"+
				"  return cons(list.head,butLast(list.tail))\n");
		templateScala("butLast", new String[] {"List[Int]"}, 
				"def butLast(l:List[Int]): List[Int] = {\n",
 			 	"  l match {\n" +
				"    case a::b if b==Nil => Nil\n"+
				"    case a::b           => a::butLast(b)\n"+
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
	RecList last(RecList seq) {
		/* BEGIN SOLUTION */
		if (seq.tail == null)
			return null;
		return cons(seq.head, last(seq.tail));
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
