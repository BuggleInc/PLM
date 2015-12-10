package lessons.welcome.array.search;

import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Extrema extends BatExercise {
	public Extrema(Game game, Lesson lesson) {
		super(game, lesson);

		BatWorld myWorld = new BatWorld(game, "extrema");
		myWorld.addTest(VISIBLE, (Object)new int[] {1, 2, 7, 1}) ;
		myWorld.addTest(VISIBLE, (Object)new int[] {1}) ;
		myWorld.addTest(VISIBLE, (Object)new int[] {1, 2}) ;
		myWorld.addTest(VISIBLE, (Object)new int[] {1, 2, 3}) ;
		myWorld.addTest(VISIBLE, (Object)new int[] {1, 3}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {1, 1, 1, 1, 1, 1, 1, 1}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {1, 2, 7, 1, 7, 9, 10, 1}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {10, 4, 1, 2, 7, 1, 0}) ;
		myWorld.addTest(INVISIBLE, (Object)new int[] {45, -34, 0, -42, 72, -42, 72}) ;
		
		

		templatePython("extrema", new String[] {"Array[Int]"},
				"def extrema(nums):\n",
				"  if (len(nums)>0) :\n"+
				"    min=nums[0]\n"+
				"    max=nums[0]\n"+
				"    for i in range( len(nums)-1):\n"+
				"      if (nums[i+1]<min) :\n"+
				"      	 min=nums[i+1]\n"+
				"      if (nums[i+1]>max) :\n"+
				"      	 max=nums[i+1]\n"+
				"    return max-min\n" +
				"  else :\n" +
				"    return 0\n");
		templateScala("extrema", new String[] {"Array[Int]"}, 
				"def extrema(nums:Array[Int]): Int = {\n",
				"  if (nums.length>0){\n"+
				"    var min=nums(0)\n"+
				"    var max=nums(0)\n"+
				"    for (i <- 1 to nums.length-1){\n"+
				"      if (nums(i)<min) \n"+
				"      	 min=nums(i)\n"+
				"      if (nums(i)>max) \n"+
				"      	 max=nums(i)\n" +
				"	 }\n"+
				"    return max-min\n" +
				"  }else\n" +
				"    return 0\n" +
				"}\n");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( extrema((int[])t.getParameter(0)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	int extrema(int[] nums) {
		/* BEGIN SOLUTION */
		if(nums.length>0){
			int min=nums[0];
			int max=nums[0];
			for(int i=1;i<nums.length;i++){
				if(nums[i]<min){
					min=nums[i];
				}
				if(nums[i]>max){
					max=nums[i];
				}
			}
			return max-min;
		}else{
			return 0;
		}
		
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}

