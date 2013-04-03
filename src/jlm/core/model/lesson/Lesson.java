package jlm.core.model.lesson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jlm.core.model.FileUtils;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Graph;


public abstract class Lesson {
	private String name;
	private String id;
	protected String about = "(no information provided by the lesson)";
	protected ArrayList<Lecture> lectures = new ArrayList<Lecture>();
	
	private Graph<Lecture,Integer> exercisesGraph = new DelegateForest<Lecture,Integer>();
		
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

	Factory<Integer> edgeFactory = new Factory<Integer>() {
		int i=0;
		public Integer create() {
			return i++;
		}};
	
	public Lesson() {
		loadExercises(); /* FIXME: remove this line when session savers can deal with laziness */
		
		id = getClass().getCanonicalName();
		Pattern namePattern = Pattern.compile(".Main$");
		Matcher nameMatcher = namePattern.matcher(id);
		id = nameMatcher.replaceAll("");

		namePattern = Pattern.compile("^lessons.");
		nameMatcher = namePattern.matcher(id);
		id = nameMatcher.replaceAll("");

	}
	
	public String getId() {
		return id;
	}
	
	private boolean aboutLoaded = false;
	private void loadAboutAndName() {
		aboutLoaded = true;

		/* read it */
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
	
	public String getAboutFileName() {
		//FIXME: file is loaded just to know if it is present?
		
		String filename = getClass().getCanonicalName().replace('.',File.separatorChar);
		
		try { 
		   StringBuffer sb = FileUtils.readContentAsText(filename,"md",true);
		} catch (Exception ex) {
			about = "File "+filename+".md not found.";
			name = filename;
			return about;		  
		}
		return filename;
	}

	Lecture rootExo, lastAdded;
	public Lecture getRootExo() {
		return rootExo;
	}
	public Lecture addExercise(Lecture exo) {
		lectures.add(exo);		
		getExercisesGraph().addVertex(exo);
		if (lastAdded != null) {
			getExercisesGraph().addEdge(edgeFactory.create(), lastAdded, exo);
		} 
		if (rootExo == null) {
			rootExo = exo;
		}
		lastAdded = exo;
		return exo;
	}
	public Lecture addExercise(Lecture exo, Lecture[] deps) {
		lectures.add(exo);
		
		getExercisesGraph().addVertex(exo);
		if (deps!=null) {
			for (Lecture d:deps) {
				getExercisesGraph().addEdge(edgeFactory.create(), d, exo);				
			}
		}
		if (rootExo == null) {
			rootExo = exo;
		}
		lastAdded = exo;
		return exo;
	}
	public Lecture addExercise(Lecture exo, Lecture dependency) {
		return addExercise(exo, new Lecture[] {dependency});
	}

	
	public Lecture getCurrentExercise() {
		if (this.currentExercise == null && lectures.size() > 0) {
			this.currentExercise = lectures.get(0);
		}
		if (currentExercise == null)
			System.out.print("There is only "+lectures.size()+" so far");
		return this.currentExercise;
	}

	abstract protected void loadExercises();

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

	/* Methods to retrieve the dependencies so that the lesson navigator can display them */
	public Graph<Lecture,Integer> getExercisesGraph() {
		return exercisesGraph;
	}
}
