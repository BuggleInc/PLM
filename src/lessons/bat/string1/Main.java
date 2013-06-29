package lessons.bat.string1;

import jlm.core.model.lesson.Lesson;
import lessons.bat.string1.AltPairs;
import lessons.bat.string1.StringBits;
import lessons.bat.string1.FrontTimes;
import lessons.bat.string1.Last2;
import lessons.bat.string1.StringMatch;
import lessons.bat.string1.StringSplosion;
import lessons.bat.string1.StringX;
import lessons.bat.string1.StringTimes;
import lessons.bat.string1.StringYak;

public class Main extends Lesson {

	@Override
	protected void loadExercises() {
		addExercise(new StringTimes(this));
		addExercise(new FrontTimes(this));
		addExercise(new StringBits(this));
		addExercise(new StringSplosion(this));
		addExercise(new Last2(this));
		addExercise(new StringMatch(this));
		addExercise(new StringX(this));
		addExercise(new AltPairs(this));
		addExercise(new StringYak(this));
	}

}
