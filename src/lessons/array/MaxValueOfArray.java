package lessons.array;

import jlm.lesson.Lesson;
import universe.array.ArrayExercise;
import universe.array.ArrayWorld;

public class MaxValueOfArray extends ArrayExercise {

	public MaxValueOfArray(Lesson lesson) {
		super(lesson);
		
		ArrayWorld[] myWorlds = new ArrayWorld[2];
		myWorlds[0] = new ArrayWorld("Toy array", 10);
		myWorlds[0].setValues( new int[] { 2, -3, 1, 17, -13, 5, 3, 1, 9, 18 } );
		myWorlds[1] = new ArrayWorld("Bigger Array", 30);

		addEntityKind(myWorlds, new MaxComputation(), "MaxComputation");  
		
		setup(myWorlds);
	}
}




