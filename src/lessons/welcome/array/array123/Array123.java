package lessons.welcome.array.array123;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Array123 extends BatExercise {
	public Array123(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "array123");
		myWorld.addTest(VISIBLE, (Object)new int[] {1, 1, 2, 3, 1}) ;
		myWorld.addTest(VISIBLE, (Object)new int[] {1, 1, 2, 4, 1}) ;
		myWorld.addTest(VISIBLE, (Object)new int[] {1, 1, 2, 1, 2, 3}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {1, 1, 2, 1, 2, 1}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {1, 2, 3, 1, 2, 3}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {1, 2, 3}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {1, 1, 1}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {1, 1, 3}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {2, 2, 3}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {1, 2}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {1}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {}) ;

		templatePython("array123", new String[] {"Array[Int]"},
				"def array123(nums):\n",
				"  for i in range(len(nums)-2):\n" +
				"    if nums[i]==1  and  nums[i+1]==2  and  nums[i+2]==3:\n"+
				"      return True\n"+
				"  return False\n");
		templateScala("array123", new String[] {"Array[Int]"}, 
				"def array123(nums:Array[Int]): Boolean = {\n",
				"  for (i <- 0 to nums.length-3)\n" +
				"    if (nums(i)==1  &&  nums(i+1)==2  &&  nums(i+2)==3)\n"+
				"      return true\n"+
				"  return false\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( array123((int[])t.getParameter(0)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean array123(int[] nums) {
		/* BEGIN SOLUTION */
		// Note: iterate < length-2, so can use i+1 and i+2 in the loop
		for (int i=0; i < (nums.length-2); i++) {
			if (nums[i]==1 && nums[i+1]==2 && nums[i+2]==3) return true;
		}
		return false;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
