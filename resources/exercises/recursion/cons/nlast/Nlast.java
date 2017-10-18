package recursion.cons.nlast;

import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;
import plm.universe.cons.ConsExercise;
import plm.universe.cons.ConsWorld;

public class Nlast extends ConsExercise {

	public Nlast(FileUtils fileUtils) {
		super("Nlast");
		
		BatWorld myWorld = new ConsWorld(fileUtils, "nlast");
		myWorld.addTest(BatTest.VISIBLE,   (new int[]{1, 2, 3, 4}), 3);
		myWorld.addTest(BatTest.VISIBLE,   (new int[]{1, 2, 3, 4}), 2);
		myWorld.addTest(BatTest.VISIBLE,   (new int[]{1, 1, 1}), 3);
		myWorld.addTest(BatTest.VISIBLE,   (new int[]{1, 2, 1, 3, 2}), 0);
		myWorld.addTest(BatTest.INVISIBLE, (new int[]{2, 4, 6, 8, 10}), 12);
		myWorld.addTest(BatTest.INVISIBLE, (new int[]{6}), 1);
		myWorld.addTest(BatTest.INVISIBLE, (new int[]{}),  1);
		myWorld.addTest(BatTest.INVISIBLE, (new int[]{}),  0);

		setup(myWorld);
	}
}
