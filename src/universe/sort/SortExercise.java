package universe.sort;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;
import jlm.universe.Entity;
import jlm.universe.World;

public class SortExercise extends ExerciseTemplated {

	public SortExercise(Lesson lesson) {
		super(lesson);
		entitiesNames = new ArrayList<String>();
		tabsNames = new ArrayList<String>();
	}

	protected void addAlgo(SortingWorld w, SortingEntity se, String name) {
		w.addEntity(se);
		se.setWorld(w);
		se.setName(name);
		entitiesNames.add(se.getClass().getName());
		tabsNames.add("My"+name);
		newSourceFromFile("My"+name, se.getClass().getName().replace('.',File.separatorChar)+".java"); //FIXME: potential bug under windows
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
