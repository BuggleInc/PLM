package jlm.lesson;

import java.util.ArrayList;
import java.util.List;

import jlm.core.Game;


public class Lesson {
	public String name;
	protected String about = "(no information provided by the lesson)";
	private ArrayList<Exercise> exercises = new ArrayList<Exercise>();
	
	private Exercise currentExercise;
	
	protected boolean sequential = true;
	/** if true, one must succeed in first exercise before trying next ones */

	public Lesson() {
	}

	public String getName() {
		return this.name;
	}
	public String getAbout(){
		return about;
	}

	public boolean isSequential() {		
		String seq = Game.getProperties().getProperty(getClass().getCanonicalName()+".sequential");
		if (seq != null) {
			return ! seq.equals("false"); // sequential by default
		} else {
			return this.sequential;
		}
	}
	
	public void setSequential(boolean enabled) {
		this.sequential = enabled;
	}	
	
	public void addExercise(Exercise exo) {
		exercises.add(exo);
		//currentExercise = exercises.get(exerciseCount()-1);
		//	sequential = false;
	}
	
	public Exercise getCurrentExercise() {
		if (this.currentExercise == null && exercises.size() > 0) {
			this.currentExercise = exercises.get(0);
		}
		return this.currentExercise;
	}

	public void setCurrentExercise(Exercise exo) {
		this.currentExercise = exo;
	}
	
	public int exerciseCount() {
		return this.exercises.size();
	}

	public List<Exercise> exercises() {
		return this.exercises;
	}
	
	public Exercise getExercise(int index) {
		return this.exercises.get(index);
	}

	public boolean isAccessible(Exercise exo) {
		if (isSequential()) {
			int index = this.exercises.indexOf(exo);
			if (index == 0)
				return true;
			if (index > 0)
				return this.exercises.get(index-1).isSuccessfullyPassed();
			return false;
		} else {
			return true;
		}
	}

	public boolean isSuccessfullyCompleted() {
		// TODO: too lazy, to add a boolean 'completed' which is updated when a test is passed 
		if (isSequential()) {
			return this.exercises.get(this.exercises.size()-1).isSuccessfullyPassed();			
		} else {
			for (Exercise exo : this.exercises) {
				if (!exo.isSuccessfullyPassed())
					return false;
			}
			return true;
		}
	}
}
