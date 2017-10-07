package recursion.cons.plusone;

import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;
import plm.universe.cons.ConsExercise;
import plm.universe.cons.ConsWorld;

public class PlusOne extends ConsExercise {

	public PlusOne() {
		super("PlusOne", "PlusOne");
		
		BatWorld myWorld = new ConsWorld("plusOne");
		myWorld.addTest(BatTest.VISIBLE,   (new int[]{1, 2, 3}));
		myWorld.addTest(BatTest.VISIBLE,   (new int[]{1, 1, 1}));
		myWorld.addTest(BatTest.VISIBLE,   (new int[]{1, 2, 1, 3})) ;
		myWorld.addTest(BatTest.INVISIBLE, (new int[]{2, 4, 6, 8, 10})) ;
		myWorld.addTest(BatTest.INVISIBLE, (new int[]{})) ;
		myWorld.addTest(BatTest.INVISIBLE, (new int[]{-2, -4, -6, -8, -10})) ;

		setup(myWorld);
	}
}
