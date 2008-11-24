package lessons.sort;

import jlm.lesson.Lesson;
import universe.sort.SortingExercise;
import universe.sort.SortingWorld;

public class SimpleSort extends SortingExercise {

	public SimpleSort(Lesson lesson) {
		super(lesson);
		name = "Algorithmes de tri simples";
		SortingWorld[] myWorlds = new SortingWorld[2];
		myWorlds[0] = new SortingWorld("Functional test",10);
		myWorlds[1] = new SortingWorld("Performance test");

		addEntityKind(myWorlds, new BubbleSort(), "BubbleSort");  
		addEntityKind(myWorlds, new SelectionSort(), "SelectionSort");  
		addEntityKind(myWorlds, new InsertionSort(), "InsertionSort");  
		addEntityKind(myWorlds, new ShellSort(), "ShellSort");
		
		setup(myWorlds);
	}
}
