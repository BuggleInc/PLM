package jlm.core.model.lesson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jlm.core.model.Game;
import jlm.core.model.Reader;


public abstract class Lesson {
	private String name;
	protected String about = "(no information provided by the lesson)";
	protected ArrayList<Exercise> exercises = new ArrayList<Exercise>();
	
	protected Exercise currentExercise;
	protected boolean exercisesLoaded = false; /* for lazy loading of exercises */
	
	final static String LessonHeader = "<head>\n" + "  <meta content=\"text/html; charset=UTF-8\" />\n"
	+ "  <style>\n"
	+ "    body { font-family: tahoma, \"Times New Roman\", serif; font-size:10px; margin:10px; }\n"
	+ "    code { background:#EEEEEE; }\n" + "    pre { background: #EEEEEE;\n" + "          margin: 5px;\n"
	+ "          padding: 6px;\n" + "          border: 1px inset;\n" + "          width: 640px;\n"
	+ "          overflow: auto;\n" + "          text-align: left;\n"
	+ "          font-family: \"Courrier New\", \"Courrier\", monospace; }\n"
	+ "   .comment { background:#EEEEEE;\n" + "              font-family: \"Times New Roman\", serif;\n"
	+ "              color:#00AA00;\n" + "              font-style: italic; }\n" + "  </style>\n" + "</head>\n";

	
	
	protected boolean sequential = false;
	/** if true, one must succeed in first exercise before trying next ones */

	public Lesson() {
		loadExercises(); /* FIXME: remove this line when session saver can deal with laziness */
	}

	private boolean aboutLoaded = false;
	private void loadAboutAndName() {
		aboutLoaded = true;
		
		/* read it */
		String filename = getClass().getCanonicalName().replace('.',File.separatorChar);
		StringBuffer sb = Reader.fileToStringBuffer(filename,"html",true);
		if (sb==null) {
			about = "File "+filename+".html not found.";
			name = filename;
			return;
		}

		String str = sb.toString();
		
		/* search the mission name */
		Pattern p =  Pattern.compile("<h[123]>([^<]*)<");
		Matcher m = p.matcher(str);
		if (!m.find())
			System.out.println("Cannot find the name of mission in "+filename+".html");
		name = m.group(1);
		
		/* get the mission explanation */
		about = "<html>"+LessonHeader+"<body>\n"+str+"</body>\n</html>\n";		
	}
	public String getName() {
		if (!aboutLoaded)
			loadAboutAndName();
		return this.name;
	}
	public String getAbout(){
		if (!aboutLoaded) {
			loadAboutAndName();
		}
		return about;
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
	}
	
	public Exercise getCurrentExercise() {
		if (!exercisesLoaded) {
			System.out.println("Load exercises of lesson "+getName());
			loadExercises();
		}
		if (this.currentExercise == null && exercises.size() > 0) {
			this.currentExercise = exercises.get(0);
		}
		return this.currentExercise;
	}

	abstract protected void loadExercises();

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
	public int getExerciseCount() {
		return this.exercises.size();
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
		if (!exercisesLoaded)
			return false;
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
