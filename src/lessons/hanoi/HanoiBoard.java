package lessons.hanoi;

import universe.hanoi.HanoiEntity;
import universe.hanoi.HanoiWorld;
import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;

public class HanoiBoard extends ExerciseTemplated {

	public HanoiBoard(Lesson lesson) {
		super(lesson);
				
		/* Create initial situation */
		HanoiWorld[] myWorlds = new HanoiWorld[3];
		myWorlds[0] = new HanoiWorld("Hanoi(0,1)", 8);
		myWorlds[0].setParameter(new Object[] {0,1});		
		myWorlds[1] = new HanoiWorld("Hanoi(0,2)", 8);
		myWorlds[1].setParameter(new Object[] {0,2});		
		myWorlds[2] = new HanoiWorld("Hanoi(1,2)", 0, 8, 0);
		myWorlds[2].setParameter(new Object[] {1,2});		
		for (int i=0;i<myWorlds.length;i++) {
			new HanoiEntity("worker",myWorlds[i]);
		}
		
		setup(myWorlds);
	}
}
