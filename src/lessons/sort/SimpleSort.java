package lessons.sort;

import jlm.lesson.Lesson;
import universe.sort.SortExercise;
import universe.sort.SortingWorld;

public class SimpleSort extends SortExercise {

	public SimpleSort(Lesson lesson) {
		super(lesson);
		name = "Algorithmes de tri simples";
		SortingWorld[] myWorlds = new SortingWorld[2];
		myWorlds[0] = new SortingWorld("Functional test",10);
		myWorlds[1] = new SortingWorld("Performance test");

		addAlgo(myWorlds, new BubbleSort(), "BubbleSort");  
		addAlgo(myWorlds, new SelectionSort(), "SelectionSort");  
		addAlgo(myWorlds, new InsertionSort(), "InsertionSort");  
		addAlgo(myWorlds, new ShellSort(), "ShellSort");
		
		setup(myWorlds);
	}
}
