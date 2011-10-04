package jlm.universe.sort;

import java.awt.Color;
import java.util.Iterator;

import jlm.core.model.lesson.ExecutionProgress;
import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.Entity;
import jlm.universe.World;

public class SortingExercise extends ExerciseTemplated {

	public SortingExercise(Lesson lesson) {
		super(lesson);
	}

	@Override
	protected void setup(World[] ws) {		
		worldDuplicate(ws);
		computeAnswer();
	}

	@Override
	public void check() {
		lastResult = new ExecutionProgress();
		for (int w=0;w<answerWorld.length;w++) {
			Iterator<Entity> itAnswer = answerWorld[w].entities();
			Iterator<Entity> itCurrent = currentWorld[w].entities();
			while (itAnswer.hasNext()) {
				boolean res = true;
				SortingEntity ans = (SortingEntity) itAnswer.next();
				SortingEntity cur = (SortingEntity) itCurrent.next();
				if (ans.getWriteCount() != cur.getWriteCount()) {
					lastResult.details += "Algorithm "+ans.getName()+" did "+cur.getWriteCount()+" writes instead of "+ans.getWriteCount()+" in world "+answerWorld[w].getName()+"\n";
					res = false;
				}
				if (ans.getReadCount() != cur.getReadCount()) {
					lastResult.details += "Algorithm "+ans.getName()+" did "+cur.getReadCount()+" reads instead of "+ans.getReadCount()+" in world "+answerWorld[w].getName()+"\n";
					res = false;
				}
				for (int i=1;i<cur.values.length;i++)  
					if (cur.values[i-1]>cur.values[i]) {
						lastResult.details += "Algorithm "+ans.getName()+" failed to sort the data"+" in world "+answerWorld[w].getName()+"\n";
						res=false;
						break;
					}
				for (int i=0;i<cur.values.length;i++)  
					if (cur.values[i]!=i) 
						cur.color[i] = Color.red;
					else
						cur.color[i] = Color.blue;
				cur.getWorld().getView()[0].repaint();
				
				lastResult.totalTests++;
				if (res) 
					lastResult.passedTests++;
			}
		}
	}
}
