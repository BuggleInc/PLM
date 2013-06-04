package lessons.bat.string1;

import jlm.core.model.lesson.Lesson;
import lessons.bat.string1.altpairs.AltPairs;
import lessons.bat.string1.bits.StringBits;
import lessons.bat.string1.fronttimes.FrontTimes;
import lessons.bat.string1.last2.Last2;
import lessons.bat.string1.match.StringMatch;
import lessons.bat.string1.splosion.StringSplosion;
import lessons.bat.string1.stringx.StringX;
import lessons.bat.string1.times.StringTimes;
import lessons.bat.string1.yak.StringYak;

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
