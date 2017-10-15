package recursion.cons.remove;



import plm.core.utils.FileUtils;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;
import plm.universe.cons.ConsExercise;
import plm.universe.cons.ConsWorld;

public class Remove extends ConsExercise {

	public Remove(FileUtils fileUtils) {
		super("Remove", "Remove");

		BatWorld myWorld = new ConsWorld(fileUtils, "remove");
		myWorld.addTest(BatTest.VISIBLE,   (new int[]{1, 2, 3}),    1);
		myWorld.addTest(BatTest.VISIBLE,   (new int[]{1, 1, 2}),    1);
		myWorld.addTest(BatTest.VISIBLE,   (new int[]{1, 2, 1, 3}), 3);
		myWorld.addTest(BatTest.INVISIBLE, (new int[]{2, 4, 6, 8, 10}), 10) ;
		myWorld.addTest(BatTest.INVISIBLE, (new int[]{1, 1, 1}),  1);
		myWorld.addTest(BatTest.INVISIBLE, (new int[]{}), 1) ;
		myWorld.addTest(BatTest.INVISIBLE, (new int[]{-2, -4, -6, -8, -10}), -4) ;

		setup(myWorld);
	}
}
