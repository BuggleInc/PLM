package plm.core.model.lesson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import plm.core.HumanLangChangesListener;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.utils.FileUtils;
import plm.universe.BrokenWorldFileException;

public abstract class Lesson implements HumanLangChangesListener {

	private Game game;
	private String name;
	private String id;
	private String description;
	private String imgPath;
	
	public static enum LoadingOutcome {SUCCESS,FAIL}
	
	private LoadingOutcome LoadingOutcomeState = LoadingOutcome.SUCCESS;

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

	public Lesson(Game game) {
		this.game = game;
		id = getClass().getCanonicalName().replaceAll(".Main$","");
		id = id.replaceAll("^lessons.", "");
	}
	
	public void loadLesson() {
		try {
			loadExercises();
			Game.waitInitThreads();
		} catch (IOException e) {
			System.err.println("Cannot load the exercises. This lesson is severely broken..");
			e.printStackTrace();
		} catch (BrokenWorldFileException e) {
			System.err.println("Cannot load the exercises. This lesson is severely broken..");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.err.println("Interrupted while waiting for the lesson to load.");
			e.printStackTrace();
		} 
		/* Compute the lesson summary for the next time we start the PLM */
		for (ProgrammingLanguage lang: Game.programmingLanguages) {
			int possible = 0;
			int passed = 0;
			for (Lecture l: lectures) {
				if (l instanceof Exercise) {
					Exercise exo = (Exercise) l;
					if (exo.getProgLanguages().contains(lang)) {
						possible++;
						if (game.studentWork.getPassed(l, lang))
							passed++;
					}
				}
			}
			game.studentWork.setPassedExercises(id, lang, passed);
			game.studentWork.setPossibleExercises(id, lang, possible);
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
			sb = FileUtils.readContentAsText(filename,getGame().getLocale(),"html",true);
		} catch (IOException ex) {
			about = getGame().i18n.tr("File {0}.html not found.",filename);
			name = filename;
			return;
		}
		String str = sb.toString();
		
		/* search the mission name */
		Pattern p =  Pattern.compile("<h[123]>([^<]*)<");
		Matcher m = p.matcher(str);
		if (!m.find())
			getGame().getLogger().log(getGame().i18n.tr("Cannot find the name of mission in {0}.html",filename));
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
	public void setCurrentExercise(String exoName) {
		setCurrentExercise(getExercise(exoName));
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
			if (l.getClass().getCanonicalName().equals(searchedName) ||
					l.getClass().getCanonicalName().equals(name)||
					l.getId().equals(searchedName) || l.getId().equals(name)||
					l.getLocalId().equals(searchedName) || l.getLocalId().equals(name))
				return l;
		}
		return null;
	}
	public int getExerciseCount() {
		return this.lectures.size();
	}
	
	public LoadingOutcome getLoadingOutcomeState() {
		return LoadingOutcomeState;
	}
	public void setLoadingOutcomeState(LoadingOutcome loadingOutcomeState) {
		LoadingOutcomeState = loadingOutcomeState;
	}
	
	public String getDescription() {
		if(description == null) {
			setDescription("short_desc");
		}
		return description;
	}

	public void setDescription(String name) {
		String filename = "lessons" + File.separatorChar + id.replace('.',File.separatorChar)+ File.separatorChar + name;
		StringBuffer sb = null;
		try {
			sb = FileUtils.readContentAsText(filename, getGame().getLocale(), "html",true);
		} catch (IOException ex) {
			filename += ".html";
		}
		
		description = sb != null? sb.toString() : "";
	}

	public String getImgPath() {
		if(imgPath == null) {
			setImgPath("icon.png");
		}
		return imgPath;
	}

	public void setImgPath(String name) {
		imgPath = "lessons" + File.separatorChar + id.replace('.',File.separatorChar)+ File.separatorChar + name;
	}
	
	public void currentHumanLanguageHasChanged(Locale newLang) {
		loadAboutAndName();
		setDescription("short_desc");
	}
	
	public Game getGame() {
		return game;
	}
}