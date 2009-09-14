package lessons.bat;

import jlm.lesson.Lesson;
import jlm.universe.World;
import universe.bat.BatExercise;
import universe.bat.BatWorld;

public class SleepIn extends BatExercise {
	
	public SleepIn(Lesson lesson) {
		super(lesson);
		
		World[] myWorlds = new BatWorld[4];
		myWorlds[0] = new BatWorld(VISIBLE,  false,false);
		myWorlds[1] = new BatWorld(VISIBLE,  true,false);
		myWorlds[2] = new BatWorld(INVISIBLE, false,true);
		myWorlds[3] = new BatWorld(INVISIBLE, true,true);

		
		setup(myWorlds,"sleepIn", new SleepInEntity());
	}

	
	
}
