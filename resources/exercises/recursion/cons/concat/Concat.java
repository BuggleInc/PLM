package recursion.cons.concat;

import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;
import plm.universe.cons.ConsExercise;
import plm.universe.cons.ConsWorld;

public class Concat extends ConsExercise {

	public Concat() {
		super("Concat", "Concat");
		
		BatWorld myWorld = new ConsWorld("concat");
		myWorld.addTest(BatTest.VISIBLE,   (new int[]{1, 2, 3}),    (new int[]{11, 12, 13}));
		myWorld.addTest(BatTest.VISIBLE,   (new int[]{1, 2, 3}),    (new int[]{1, 1, 1}));
		myWorld.addTest(BatTest.VISIBLE,   (new int[]{1, 2, 3}),    (new int[]{}));
		myWorld.addTest(BatTest.VISIBLE,   (new int[]{1, 2, 1, 3}), (new int[]{64,36})) ;
		myWorld.addTest(BatTest.INVISIBLE, (new int[]{2, 4, 6, 8, 10}), (new int[]{72,35})) ;
		myWorld.addTest(BatTest.INVISIBLE, (new int[]{}), (new int[]{3,5,8})) ;
		myWorld.addTest(BatTest.INVISIBLE, (new int[]{}), (new int[]{})) ;
		myWorld.addTest(BatTest.INVISIBLE, (new int[]{-2, -4, -6, -8, -10}), (new int[]{2,4,6,8,10})) ;

		setup(myWorld);
	}
}
