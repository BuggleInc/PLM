package recursion.cons.alldifferent;

import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;
import plm.universe.cons.ConsExercise;
import plm.universe.cons.ConsWorld;

public class AllDifferent extends ConsExercise {

	public AllDifferent() {
		super("AllDifferent", "AllDifferent");

		BatWorld myWorld = new ConsWorld("allDifferent");
		myWorld.addTest(BatTest.VISIBLE,   new int[]{1, 2, 3, 4});
		myWorld.addTest(BatTest.VISIBLE,   new int[]{1, 2, 2, 4});
		myWorld.addTest(BatTest.VISIBLE,   new int[]{1, 1, 1});
		myWorld.addTest(BatTest.VISIBLE,   new int[]{1, 2, 4, 3, 1}) ;
		myWorld.addTest(BatTest.INVISIBLE, new int[]{2, 4, 6, 8, 10}) ;
		myWorld.addTest(BatTest.INVISIBLE, new int[]{}) ;

		setup(myWorld);
	}
}
