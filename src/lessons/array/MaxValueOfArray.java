package lessons.array;

import java.util.Random;

import jlm.core.model.lesson.Lesson;
import jlm.universe.array.ArrayExercise;
import jlm.universe.array.ArrayWorld;

public class MaxValueOfArray extends ArrayExercise {

	public MaxValueOfArray(Lesson lesson) {
		super(lesson);
		
		ArrayWorld[] myWorlds = new ArrayWorld[2];
		myWorlds[0] = new ArrayWorld("Toy array", 10);
		myWorlds[0].setValues( new int[] { 2, -3, 1, 17, -13, 5, 3, 1, 9, 18 } );
		myWorlds[1] = new ArrayWorld("Bigger Array", 1);
		
		int[] tab = new int[30];
		Random r = new Random();
		for (int i=0; i<tab.length; i++) {
			tab[i] = r.nextInt(35);
		}
		myWorlds[1].setValues(tab);

		addEntityKind(myWorlds, new MaxComputation(), "MaxComputation");  
		
		setup(myWorlds);
	}
}




