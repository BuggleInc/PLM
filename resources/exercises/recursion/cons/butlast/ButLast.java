package recursion.cons.butlast;

import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;
import plm.universe.cons.ConsExercise;
import plm.universe.cons.ConsWorld;

public class ButLast extends ConsExercise {

	public ButLast(FileUtils fileUtils) {
		super("ButLast", "ButLast");
		
		BatWorld myWorld = new ConsWorld(fileUtils, "butLast");
		myWorld.addTest(BatTest.VISIBLE,   new int[]{1, 2, 3, 4});
		myWorld.addTest(BatTest.VISIBLE,   new int[]{1, 1, 1}, 1);
		myWorld.addTest(BatTest.VISIBLE,   new int[]{1, 2, 1, 3, 2});
		myWorld.addTest(BatTest.INVISIBLE, new int[]{2, 4, 6, 8, 10});
		myWorld.addTest(BatTest.INVISIBLE, new int[]{6});

		setup(myWorld);
	}
}
