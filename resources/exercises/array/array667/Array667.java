package array.array667;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Array667 extends ExerciseTemplated {
	public Array667(Lesson lesson) {
		super("Array667");

		BatWorld myWorld = new BatWorld("array667");
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {6, 6, 2}) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {6, 6, 2, 6}) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {6, 7, 2, 6}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {6, 6, 2, 6, 7}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {1, 6, 3}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {6, 1}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {3, 6, 7, 6}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {3, 6, 6, 7}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {6, 3, 6, 6}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {6, 7, 6, 6}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {1, 2, 3, 5, 6}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {1, 2, 3, 6, 6}) ;

		templatePython("array667", new String[] {"Array[Int]"},
				"def array667(nums):\n",
				"  count=0\n"+
				"  for i in range( len(nums)-1):\n"+
				"    if (nums[i] == 6) and (nums[i+1]==6 or nums[i+1]==7):\n"+
				"      count += 1\n"+
				"  return count\n");
		templateScala("array667", new String[] {"Array[Int]"},
				"def array667(nums:Array[Int]): Int = {\n",
				"  var count=0\n"+
				"  for (i <- 0 to nums.length-2)\n"+
				"    if ((nums(i) == 6) && (nums(i+1)==6 || nums(i+1)==7))\n"+
				"      count += 1\n"+
				"  return count\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( array667((int[])t.getParameter(0)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	int array667(int[] nums) {
		/* BEGIN SOLUTION */
		int count = 0;
		// Note: iterate to length-1, so can use i+1 in the loop
		for (int i=0; i < (nums.length-1); i++) {
			if (nums[i] == 6) {
				if (nums[i+1] == 6 || nums[i+1] == 7) {
					count++;
				}
			}
		}
		return count;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
