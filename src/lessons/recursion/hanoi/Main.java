package lessons.recursion.hanoi;

import plm.core.model.Game;
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.Lesson;

public class Main extends Lesson {
	public Main(Game game) {
		super(game);
	}

	@Override
	protected void loadExercises() {
		addExercise(new HanoiBoard(getGame(), this));
		
		Lecture interleaved = addExercise(new InterleavedHanoi(getGame(), this));
		addExercise(new SplitHanoi1(getGame(), this),interleaved);
		addExercise(new SplitHanoi2(getGame(), this),interleaved);
		addExercise(new SplitHanoi3(getGame(), this),interleaved);
		
		Lecture linear = addExercise(new LinearHanoi(getGame(), this));
		addExercise(new LinearTwinHanoi(getGame(), this),linear);
		
		Lecture tricolor = addExercise(new TricolorHanoi1(getGame(), this));
		addExercise(new TricolorHanoi2(getGame(), this),tricolor);
		addExercise(new TricolorHanoi3(getGame(), this),tricolor);
		
		addExercise(new CyclicHanoi(getGame(), this));
		
		addExercise(new IterativeHanoi(getGame(), this));
	}
}
