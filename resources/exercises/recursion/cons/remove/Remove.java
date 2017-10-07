package recursion.cons.remove;



import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;
import plm.universe.cons.ConsExercise;
import plm.universe.cons.ConsWorld;
import plm.universe.cons.RecList;

public class Remove extends ConsExercise {

	public Remove() {
		super("Remove", "Remove");

		BatWorld myWorld = new ConsWorld("remove");
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
