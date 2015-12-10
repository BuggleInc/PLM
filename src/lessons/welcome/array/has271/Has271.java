package lessons.welcome.array.has271;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Has271 extends BatExercise {
	public Has271(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "has271");
		myWorld.addTest(VISIBLE, (Object)new int[] {1, 2, 7, 1}) ;
		myWorld.addTest(VISIBLE, (Object)new int[] {1, 2, 8, 1}) ;
		myWorld.addTest(VISIBLE, (Object)new int[] {2, 7, 1}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {3, 8, 2}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {2, 7, 3}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {2, 7, 4}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {2, 7, -1}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {2, 7, -2}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {4, 5, 3, 8, 0}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {2, 7, 5, 10, 4}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {2, 7, -2, 4, 9, 3}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {2, 7, 5, 10, 1}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {2, 7, -2, 4, 10, 2}) ;

		templatePython("has271", new String[] {"Array[Int]"},
				"import math\ndef has271(nums):\n",
				"  count=0\n"+
				"  for i in range( len(nums)-1):\n"+
				"    if (nums[i] + 5 == nums[i+1]) and (math.fabs(nums[i+2]-nums[i]+1)<=2):\n"+
				"      return True\n"+
				"  return False\n");
		templateScala("has271", new String[] {"Array[Int]"},
				"def has271(nums:Array[Int]): Boolean = {\n",
				"  var count=0\n"+
				"  for (i <- 0 to nums.length-2)\n"+
				"    if ((nums(i) + 5 == nums(i+1)) && (Math.abs(nums(i+2)-nums(i)+1)<=2))\n"+
				"      return true\n"+
				"  return false\n"+
				"}");

		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( has271((int[])t.getParameter(0)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean has271(int[] nums) {
		/* BEGIN SOLUTION */
		// Iterate < length-2, so can use i+1 and i+2 in the loop.
		// Return true immediately when seeing 271.
		for (int i=0; i < (nums.length-2); i++) {
			int val = nums[i];
			if (nums[i+1] == (val + 5) &&
					Math.abs(nums[i+2] - (val-1)) <= 2)  return true;
		}

		// If we get here ... none found.
		return false;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
