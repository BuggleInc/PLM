package lessons.welcome.array.arrayfront9;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class ArrayFront9 extends BatExercise {
	public ArrayFront9(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "arrayFront9");
		myWorld.addTest(VISIBLE, (Object)new int[] {1, 2, 9, 3, 4}) ;
		myWorld.addTest(VISIBLE, (Object)new int[] {1, 2, 3, 4, 9}) ;
		myWorld.addTest(VISIBLE, (Object)new int[] {1, 2, 3, 4, 5}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {9, 2, 3}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {1, 9, 9}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {1, 2, 3}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {1, 9}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {5, 5}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {2}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {9}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {3, 9, 2, 3, 3}) ;

		templatePython("arrayFront9", new String[]{"Array[Int]"},
				"def arrayFront9(nums):\n",
				"  for i in range( min( len(nums), 4) ):\n" +
				"    if nums[i] == 9:\n" +
				"      return True\n" +
				"  return False\n");
		templateScala("arrayFront9", new String[]{"Array[Int]"},
				"def arrayFront9(nums:Array[Int]): Boolean = {\n",
				"  for (i <- 0 to Math.min(nums.length,4)-1)\n"+
				"    if (nums(i) == 9)\n" +
				"      return true\n" +
				"  return false\n"+
				"}");

		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( arrayFront9((int[])t.getParameter(0)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean arrayFront9(int[] nums) {
		/* BEGIN SOLUTION */
		// First figure the end for the loop
		int end = nums.length;
		if (end > 4) end = 4;

		for (int i=0; i<end; i++) {
			if (nums[i] == 9) return true;
		}

		return false;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
