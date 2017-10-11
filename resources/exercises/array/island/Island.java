package array.island;

import plm.core.model.lesson.ExerciseTemplated;
import plm.universe.bat.BatTest;
import plm.universe.bat.BatWorld;

public class Island extends ExerciseTemplated {
	
	public Island() {
		super("Island", "Island");

		BatWorld myWorld = new BatWorld("island");
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {0,1,2,2,1,0,1,2,2,1,0}) ;
		myWorld.addTest(BatTest.VISIBLE, (Object)new int[] {0,1,2,3,4,3,2,1,2,3,4,3,2,1,0}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {0,0,0,0,0,0}) ;
		myWorld.addTest(BatTest.INVISIBLE, (Object)new int[] {0,0,2,4,0,0}) ;
		
		setup(myWorld);
	}
}
