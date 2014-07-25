package lessons.welcome.array.island;

import plm.core.model.lesson.Lesson;
import plm.universe.bat.BatExercise;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Island extends BatExercise{
	
	public Island(Lesson lesson) {
		super(lesson);

		BatWorld myWorld = new BatWorld("island");
		myWorld.addTest(VISIBLE, (Object)new int[] {0,1,2,2,1,0,1,2,2,1,0}) ;
		myWorld.addTest(VISIBLE, (Object)new int[] {0,1,2,3,4,3,2,1,2,3,4,3,2,1,0}) ;
		myWorld.addTest(VISIBLE, (Object)new int[] {0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0}) ;
		myWorld.addTest(VISIBLE, (Object)new int[] {0,0,0,0,0,0}) ;
		

		templatePython("island", 
				"def island(num):\n",
				"  nbisland=0\n" +
				"  for i in range(len(num)-1):\n" +
				"    if num[i]<num[i+1]:\n" +
				"      nbisland=nbisland+1\n" +
				"  return nbisland\n"
				);
		templateScala("island", new String[] {"Array[Int]"}, 
				"def island(num:Array[Int]): Int = {\n",
				"  var nbisland=0;\n" +
				"  for (i -> 1 to num.length-1){\n" +
				"    if num(i)<num(i+1) nbisland=nbisland+1;\n" +
				"  }\n" +
				"  return nbisland\n"+
				"}");
		setup(myWorld);
	}

	public void run(BatTest t) {
		/* BEGIN SKEL */
		t.setResult( island((int[])t.getParameter(0)) );
		/* END SKEL */
	}

	/* BEGIN TEMPLATE */
	int island(int[] num) {
		/* BEGIN SOLUTION */
		int nbisland=0;
		for(int i=0;i<num.length-1;i++){
			if(num[i]<num[i+1]){
				nbisland++;
			}
		}
		return nbisland;
		/* END SOLUTION */
	}
	/* END TEMPLATE */

}
