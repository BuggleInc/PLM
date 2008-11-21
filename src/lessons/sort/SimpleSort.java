package lessons.sort;

import jlm.lesson.Lesson;
import universe.sort.SortExercise;
import universe.sort.SortingWorld;

public class SimpleSort extends SortExercise {

	public SimpleSort(Lesson lesson) {
		super(lesson);
		name = "Algorithmes de tri simples";
		SortingWorld myWorld = new SortingWorld();

		addAlgo(myWorld, new BubbleSort(), "BubbleSort");  
		addAlgo(myWorld, new SelectionSort(), "SelectionSort");  
		addAlgo(myWorld, new InsertionSort(), "InsertionSort");  
		addAlgo(myWorld, new ShellSort(), "ShellSort");  
		setup(myWorld);
	}
}
