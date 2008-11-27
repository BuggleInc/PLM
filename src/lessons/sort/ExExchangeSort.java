package lessons.sort;

import jlm.lesson.Lesson;
import universe.sort.SortingExercise;
import universe.sort.SortingWorld;

public class ExExchangeSort extends SortingExercise {

	public ExExchangeSort(Lesson lesson) {
		super(lesson);
		name = "Tri à bulle et variantes";
		SortingWorld[] myWorlds = new SortingWorld[2];
		myWorlds[0] = new SortingWorld("Functional test",10);
		myWorlds[1] = new SortingWorld("Performance test",200);

		addEntityKind(myWorlds, new AlgBubbleSort(), "BubbleSort");  
		addEntityKind(myWorlds, new AlgBubbleSort2(), "BubbleSort2");  
		addEntityKind(myWorlds, new AlgBubbleSort3(), "BubbleSort3");  
		addEntityKind(myWorlds, new AlgCocktailSort(), "CocktailSort");  
		addEntityKind(myWorlds, new AlgCocktailSort2(), "CocktailSort2");  
		addEntityKind(myWorlds, new AlgCocktailSort3(), "CocktailSort3");  
		addEntityKind(myWorlds, new AlgCombSort(), "CombSort");  
		addEntityKind(myWorlds, new AlgCombSort11(), "CombSort11");  
//		addEntityKind(myWorlds, new AlgSelectionSort(), "SelectionSort");  
//		addEntityKind(myWorlds, new AlgInsertionSort(), "InsertionSort");  
//		addEntityKind(myWorlds, new AlgShellSort(), "ShellSort");
		
		setup(myWorlds);
	}
}
