package lessons.recursion.cons;

import java.util.Arrays;
import java.util.List;

import lessons.recursion.cons.universe.ConsExercise;
import lessons.recursion.cons.universe.ConsWorld;
import lessons.recursion.cons.universe.RecList;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Length extends ConsExercise {

	public Length(Lesson lesson) {
		super(lesson);
		
		BatWorld myWorld = new ConsWorld("length");
		myWorld.addTest(VISIBLE,   Arrays.asList(new Integer[]{1, 2, 3}));
		myWorld.addTest(VISIBLE,   Arrays.asList(new Integer[]{1, 1, 1}));
		myWorld.addTest(VISIBLE,   Arrays.asList(new Integer[]{1, 2, 1, 3})) ;
		myWorld.addTest(INVISIBLE, Arrays.asList(new Integer[]{2, 4, 6, 8, 10})) ;
		myWorld.addTest(INVISIBLE, Arrays.asList(new Integer[]{})) ;

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

	@SuppressWarnings("unchecked")
	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( length(RecList.fromList((List<Integer>)t.getParameter(0))) );
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
