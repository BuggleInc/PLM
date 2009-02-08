package lessons.recursion;

import jlm.lesson.Lesson;

public class Main extends Lesson {
	public Main() {
		super();	
		name = "Algorithmes récursifs";
		about = "Cette leçon vous permet d'expérimenter avec des algorithmes récursifs. " +
				"<p>Si vous cherchez d'autres algorithmes récursifs,  un exercice sur les tris récursifs " +
				"(en particulier QuickSort et MergeSort) est prévu à l'avenir dans la leçon sur les tris.";

		sequential = false;
		addExercise(new Square(this));
		addExercise(new Circle(this));
		addExercise(new Star(this));
		addExercise(new Spiral(this));
		addExercise(new SpiralUse(this));
		addExercise(new Tree(this));
	}
}
