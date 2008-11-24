package universe.sort;

import java.util.Iterator;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;
import jlm.universe.Entity;
import jlm.universe.World;

public class SortingExercise extends ExerciseTemplated {

	public SortingExercise(Lesson lesson) {
		super(lesson);
	}

	protected void setup(World[] ws) {		
		worldDuplicate(ws);
		computeAnswer();
	}

	@Override
	public boolean check() {
		Iterator<Entity> itAnswer = answerWorld[0].entities();
		Iterator<Entity> itCurrent = currentWorld[0].entities();
		while (itAnswer.hasNext()) {
			SortingEntity ans = (SortingEntity) itAnswer.next();
			SortingEntity cur = (SortingEntity) itCurrent.next();
			if (ans.getWriteCount() != cur.getWriteCount())
				return false;
			if (ans.getReadCount() != cur.getReadCount())
				return false;
			for (int i=1;i<cur.values.length;i++) 
				if (cur.values[i-1]>cur.values[i])
					return false;
		}
		return true;	
	}
}
