package universe.sort;

import java.awt.Color;
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
		boolean result = true;
		for (int w=0;w<answerWorld.length;w++) {
			Iterator<Entity> itAnswer = answerWorld[w].entities();
			Iterator<Entity> itCurrent = currentWorld[w].entities();
			while (itAnswer.hasNext()) {
				SortingEntity ans = (SortingEntity) itAnswer.next();
				SortingEntity cur = (SortingEntity) itCurrent.next();
				if (ans.getWriteCount() != cur.getWriteCount())
					result=false;
				if (ans.getReadCount() != cur.getReadCount())
					result=false;
				for (int i=1;i<cur.values.length;i++)  
					if (cur.values[i-1]>cur.values[i]) 
						result=false;
				for (int i=0;i<cur.values.length;i++)  
					if (cur.values[i]!=i) 
						cur.color[i] = Color.red;
					else
						cur.color[i] = Color.blue;
				cur.getWorld().getView().repaint();
			}
		}
		return result;	
	}
}
