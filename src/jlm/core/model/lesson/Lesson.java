package jlm.core.model.lesson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jlm.core.model.FileUtils;
import jlm.universe.BrokenWorldFileException;


public abstract class Lesson {
	private String name;
	private String id;
	protected String about = "(no information provided by the lesson)";
	protected ArrayList<Lecture> lectures = new ArrayList<Lecture>();
	
	protected Vector<Lecture> rootLectures = new Vector<Lecture>(); /* To display the graph */
	
	protected Lecture currentExercise;
	
	final static String LessonHeader = "<head>\n" + "  <meta content=\"text/html; charset=UTF-8\" />\n"
	+ "  <style>\n"
	+ "    body { font-family: tahoma, \"Times New Roman\", serif; font-size:10px; margin:10px; }\n"
	+ "    code { background:#EEEEEE; }\n" + "    pre { background: #EEEEEE;\n" + "          margin: 5px;\n"
	+ "          padding: 6px;\n" + "          border: 1px inset;\n" + "          width: 640px;\n"
	+ "          overflow: auto;\n" + "          text-align: left;\n"
	+ "          font-family: \"Courrier New\", \"Courrier\", monospace; }\n"
	+ "   .comment { background:#EEEEEE;\n" + "              font-family: \"Times New Roman\", serif;\n"
	+ "              color:#00AA00;\n" + "              font-style: italic; }\n" + "  </style>\n" + "</head>\n";

	public Lesson() {
		id = getClass().getCanonicalName();
		Pattern namePattern = Pattern.compile(".Main$");
		Matcher nameMatcher = namePattern.matcher(id);
		id = nameMatcher.replaceAll("");
		
		namePattern = Pattern.compile("^lessons.");
		nameMatcher = namePattern.matcher(id);
		id = nameMatcher.replaceAll("");
		
		try {
			loadExercises(); /* FIXME: remove this line when session savers can deal with laziness */
		} catch (IOException e) {
			System.err.println("Cannot load the exercises. This lesson is severely broken..");
			e.printStackTrace();
		} catch (BrokenWorldFileException e) {
			System.err.println("Cannot load the exercises. This lesson is severely broken..");
			e.printStackTrace();
		} 
	}
	public String getId() {
		return id;
	}
	
	private boolean aboutLoaded = false;

	public void resetAboutLoaded() {
		this.aboutLoaded = false;
	}
	
	private void loadAboutAndName() {
		aboutLoaded = true;		/* read it */
		String filename = getClass().getCanonicalName().replace('.',File.separatorChar);
		StringBuffer sb = null;
		try {
			sb = FileUtils.readContentAsText(filename,"html",true);
		} catch (IOException ex) {
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

	Lecture rootExo, lastAdded;
	@Deprecated
	public Lecture getRootExo() {
		return rootExo;
	}
	public Vector<Lecture> getRootLectures() {
		return rootLectures;
	}
	public Lecture addExercise(Lecture exo) {
		lectures.add(exo);		
		rootLectures.add(exo);
		if (rootExo == null) {
			rootExo = exo;
		}
		lastAdded = exo;
		return exo;
	}
	public Lecture addExercise(Lecture exo, Lecture previousExo) {
		lectures.add(exo);
		
		if (rootExo == null) {
			rootExo = exo;
		}
		lastAdded = exo;
		previousExo.dependingLectures.add(exo);
		return exo;
	}
	public Lecture getCurrentExercise() {
		if (this.currentExercise == null && lectures.size() > 0) {
			this.currentExercise = lectures.get(0);
		}
		if (currentExercise == null)
			System.out.print("There is only "+lectures.size()+" lectures so far in the lesson "+getName());
		return this.currentExercise;
	}

	abstract protected void loadExercises() throws IOException, BrokenWorldFileException;

	public void setCurrentExercise(Lecture exo) {
		this.currentExercise = exo;
	}
	
	public int exerciseCount() {
		return this.lectures.size();
	}

	public List<Lecture> exercises() {
		return this.lectures;
	}
	public Lecture getExercise(String name) {
		String searchedName = getClass().getPackage().getName()+"."+name;
		for (Lecture l: lectures) {
			if (l.getClass().getCanonicalName().equals(searchedName))
				return l;
		}
		return null;
	}
	@Deprecated
	public Lecture getExercise(int index) { // FIXME: killme
		return this.lectures.get(index); 
	}
	public int getExerciseCount() {
		return this.lectures.size();
	}
}