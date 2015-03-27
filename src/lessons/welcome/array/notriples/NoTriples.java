package lessons.welcome.array.notriples;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class NoTriples extends BatExercise {
	public NoTriples(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "noTriples");
		myWorld.addTest(VISIBLE, (Object)new int[] {1, 1, 2, 2, 1}) ;
		myWorld.addTest(VISIBLE, (Object)new int[] {1, 1, 2, 2, 2, 1}) ;
		myWorld.addTest(VISIBLE, (Object)new int[] {1, 1, 1, 2, 2, 2, 1}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {1, 1, 2, 2, 1, 2, 1}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {1, 2, 1}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {1, 1, 1}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {1, 1}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {1}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {}) ;

		templatePython("noTriples", new String[]{"Array[Int]"},
				"def noTriples(nums):\n",
				"  count=0\n"+
				"  for i in range( len(nums)-2 ):\n"+
				"    if (nums[i] == nums[i+1]) and (nums[i+1] == nums[i+2]):\n"+
				"      return False\n"+
				"  return True\n");
		templateScala("noTriples", new String[]{"Array[Int]"}, 
				"def noTriples(nums:Array[Int]): Boolean = {\n",
				"  var count=0\n"+
				"  for (i <- 0 to nums.length-3)\n"+
				"    if ( (nums(i) == nums(i+1)) && (nums(i+1) == nums(i+2)) )\n"+
				"      return false\n"+
				"  return true\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( noTriples((int[])t.getParameter(0)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	boolean noTriples(int[] nums) {
		/* BEGIN SOLUTION */
		// Iterate < length-2, so can use i+1 and i+2 in the loop.
		// Return false immediately if every seeing a triple.
		for (int i=0; i < (nums.length-2); i++) {
			int first = nums[i];
			if (nums[i+1]==first && nums[i+2]==first) return false;
		}

		// If we get here ... no triples.
		return true;
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
