package lessons.lightbot;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;
import jlm.universe.World;

public class LightBotExercise extends ExerciseTemplated {

	public LightBotExercise(Lesson lesson) {
		super(lesson);
	}

	@Override
	protected void setup(World[] ws) {		
		worldDuplicate(ws);
		computeAnswer();
	}

	@Override
	public boolean check() {
		boolean result = false;
		for (int w=0;w<answerWorld.length;w++) {
			
		}
		return result;	
	}
}
