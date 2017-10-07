package recursion.cons.increasing;

import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;
import plm.universe.cons.ConsExercise;
import plm.universe.cons.ConsWorld;
import plm.universe.cons.RecList;

public class Increasing extends ConsExercise {

	public Increasing() {
		super("Increasing", "Increasing");
		
		BatWorld myWorld = new ConsWorld("increasing");
		myWorld.addTest(BatTest.VISIBLE,   (new int[]{1, 2, 3, 4}));
		myWorld.addTest(BatTest.VISIBLE,   (new int[]{1, 1, 1}));
		myWorld.addTest(BatTest.VISIBLE,   (new int[]{1, 2, 1, 3, 2}));
		myWorld.addTest(BatTest.INVISIBLE, (new int[]{2, 4, 6, 8, 10}));
		myWorld.addTest(BatTest.INVISIBLE, (new int[]{6}));

		setup(myWorld);
	}
}
