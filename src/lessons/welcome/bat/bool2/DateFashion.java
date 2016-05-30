/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */

package lessons.welcome.bat.bool2;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class DateFashion extends BatExercise {
	public DateFashion(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "dateFashion");
		myWorld.addTest(VISIBLE, 5, 10) ;
		myWorld.addTest(VISIBLE, 5, 2) ;
		myWorld.addTest(VISIBLE, 5, 5) ;
		myWorld.addTest(INVISIBLE, 3, 3) ;
		myWorld.addTest(INVISIBLE, 10, 2) ;
		myWorld.addTest(INVISIBLE, 2, 9) ;
		myWorld.addTest(INVISIBLE, 9, 9) ;
		myWorld.addTest(INVISIBLE, 10, 5) ;
		myWorld.addTest(INVISIBLE, 2, 2) ;
		myWorld.addTest(INVISIBLE, 3, 7) ;
		myWorld.addTest(INVISIBLE, 2, 7) ;
		myWorld.addTest(INVISIBLE, 6, 2) ;

		templatePython("dateFashion", new String[]{"Int","Int"},
				"def dateFashion(you, date):\n",
				"	if (you <= 2 or date <= 2):\n"+
				"		return 0\n"+
				"	elif (you >= 8 or date >= 8):\n"+
				"		return 2\n"+
				"	else:\n"+
				"		return 1\n");
		templateScala("dateFashion",new String[]{"Int","Int"}, 
				"def dateFashion(you:Int, date:Int):Int = {\n",
				"	if (you <= 2 || date <= 2) \n"+
				"		return 0\n"+
				"	else if (you >= 8 || date >= 8)\n"+
				"		return 2\n"+
				"	else\n"+
				"		return 1\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( dateFashion((Integer)t.getParameter(0), (Integer)t.getParameter(1)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	int dateFashion(int you, int date) {
		/* BEGIN SOLUTION */
		if (you <= 2 || date <= 2)
			return 0;
		else if (you >= 8 || date >= 8)
			return 2;
		else
			return 1;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
