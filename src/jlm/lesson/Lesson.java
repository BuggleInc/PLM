package jlm.lesson;

import java.util.ArrayList;
import java.util.List;

import jlm.core.Game;


public class Lesson {
	public String name;
	protected String about = "(no information provided by the lesson)";
	protected ArrayList<Exercise> exercises = new ArrayList<Exercise>();
	
	protected Exercise currentExercise;
	
	final static String LessonHeader = "<head>\n" + "  <meta content=\"text/html; charset=UTF-8\" />\n"
	+ "  <style>\n"
	+ "    body { font-family: tahoma, \"Times New Roman\", serif; font-size:10px; margin:10px; }\n"
	+ "    code { background:#EEEEEE; }\n" + "    pre { background: #EEEEEE;\n" + "          margin: 5px;\n"
	+ "          padding: 6px;\n" + "          border: 1px inset;\n" + "          width: 640px;\n"
	+ "          overflow: auto;\n" + "          text-align: left;\n"
	+ "          font-family: \"Courrier New\", \"Courrier\", monospace; }\n"
	+ "   .comment { background:#EEEEEE;\n" + "              font-family: \"Times New Roman\", serif;\n"
	+ "              color:#00AA00;\n" + "              font-style: italic; }\n" + "  </style>\n" + "</head>\n";

	
	
	protected boolean sequential = true;
	/** if true, one must succeed in first exercise before trying next ones */

	public Lesson() {
	}

	public String getName() {
		return this.name;
	}
	public String getAbout(){
		return "<html>"+LessonHeader+"<body><h1>"+getName()+"</h1>"+about+"</body></html>";
	}

	public boolean isSequential() {		
		String seq = Game.getProperty(getClass().getCanonicalName()+".sequential");
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
