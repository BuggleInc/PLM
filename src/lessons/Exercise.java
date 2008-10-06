package lessons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import jlm.bugglequest.AbstractBuggle;
import jlm.bugglequest.BuggleCompiler;
import jlm.bugglequest.LogWriter;
import jlm.bugglequest.World;
import jlm.exception.BrokenLessonException;
import jlm.exception.BuggleCompilerException;


public abstract class Exercise {
	//public static final boolean debug = false; /** well behaved exercises use a working solution as initial body when this is true */
	public boolean debugThis = false; /* whether to debug this particular exo */

	protected boolean done = false; /** indicate whether this Exercise was successfully done or not */

	public String name = "<no name>"; 
	public String mission = "";  /** The text to display to present the lesson */
	protected Vector<RevertableSourceFile> sourceFiles; /** All the editable source files */
	protected Vector<SourceFile> hiddenSourceFiles; /** Other source files, that the compiler should use, but the user shouldn't see */
	Map<String, Class<AbstractBuggle>> compiledBuggleClasses = new TreeMap<String, Class<AbstractBuggle>>(); /* list of buggle classes defined in the lesson */
	protected Vector<String> fileNames;

	/* to make sure that the subsequent version of the same class have different names, in order to bypass the cache of the class loader */
	private static final String packageNamePrefix = "bugglequest.runtime";
	private int packageNameSuffix = 0;

	protected World [] currentWorld; /* the one displayed */
	protected World [] initialWorld; /* the one used to reset the previous on each run */
	protected World [] answerWorld;  /* the one current should look like to pass the test */

	protected Map<String, String> runtimePatterns;


	private Lesson lesson; 

	public String getName() {
		return this.name;
	}

	public void successfullyPassed() {
		this.done = true;
	}
	
	public void failed() {
		this.done = false;
	}

	public boolean isSuccessfullyPassed() {
		return this.done;
	}

	public Lesson getLesson() {
		return this.lesson;
	}

	public String getMission() {
		return this.mission;
	}

	public abstract void reset();
	
	public World[] getCurrentWorld(){
		return currentWorld;
	}
	@Deprecated
	public void worldDuplicate(World w) {
		World[] ws = new World[1];
		ws[0] = w;
		
		worldDuplicate(ws);
	}

	public void worldDuplicate(World[] w) {
		currentWorld = new World[w.length];
		initialWorld = new World[w.length];
		answerWorld  = new World[w.length];
		for (int i=0; i<w.length; i++) {
			currentWorld[i] = new World(w[i]);
			initialWorld[i] = new World(w[i]);
			answerWorld[i]  = new World(w[i]);
		}
	}
	
	public abstract void run(Vector<Thread> runnerVect);	
	public abstract void runDemo(Vector<Thread> runnerVect);	
	
	public boolean check() throws Exception {
		for (int i=0; i<currentWorld.length; i++) {
			if (!currentWorld[i].equals(answerWorld[i]))
				return false;
		}
		return true;
	}

	/*
	 * +++++++++++++++++++++++++
	 * Compilation related stuff
	 * +++++++++++++++++++++++++
	 * 
	 */
	// Create a compiler of Buggle classes (using java 1.6)
	private final BuggleCompiler compiler = new BuggleCompiler(
			getClass().getClassLoader(), Arrays.asList(new String[] { "-target", "1.6" }));

	/**
	 * Generate Java source from the user function
	 * @throws BuggleCompilerException 
	 */
	public void compileAll(LogWriter out) throws BuggleCompilerException {
		compiledBuggleClasses = new TreeMap<String, Class<AbstractBuggle>>();

		/* Make sure each run generate a new package to avoid that the loader cache prevent the reloading of the newly generated class */
		packageNameSuffix++;
		runtimePatterns.put("\\$package", "package "+packageName()+";");

		/* Prepare the source files */
		Map<String, CharSequence> sources = new TreeMap<String, CharSequence>();
		for (SourceFile sf: sourceFiles) {
			sources.put(className(sf.getName()), sf.getCompilableContent(runtimePatterns));
		}
		for (SourceFile sf: hiddenSourceFiles)
			sources.put(className(sf.getName()), sf.getCompilableContent(runtimePatterns)); 			

		/* Do the compile */
		try {
			DiagnosticCollector<JavaFileObject> errs = new DiagnosticCollector<JavaFileObject>();
			compiledBuggleClasses = compiler.compile(sources, errs);

			out.log(errs);
		} catch (BuggleCompilerException e) {
			out.log(e.getDiagnostics());
			throw e;
		}
	}
	
	private String packageName(){
		return packageNamePrefix + packageNameSuffix;
	}
	private String className(String name) {
		return packageName() + "." + name;
	}
	/* ***
	 * Functions to add new source files in child classes, ie stuff to be written by the student
	 */
	public void newFrozenSource(String name, String content) {
		SourceFile sf = new SourceFile(name, content);
		hiddenSourceFiles.add(sf);
	}
	
	@Deprecated
	public void newSource(String name, String initialContent) {
		newSource(name, initialContent, null, "");
	}
	public void newSource(String name, String initialContent, String template) {
		newSource(name, initialContent, template, "");
	}
	public void newSource(String name, String initialContent, String template, String patterns) {
		Map<String, String> pat = new TreeMap<String, String>();
		for (String pattern: patterns.split(";")) {
			String[] parts = pattern.split("/");
			if (parts.length != 1 || !parts[0].equals("")) {
				if (parts.length != 3 || !parts[0].equals("s")) 
					throw new RuntimeException("Malformed pattern for file "+name+": '"+ pattern+"' (from '"+patterns+"')");
				pat.put(parts[1], parts[2]);
			}
		}
		sourceFiles.add(new RevertableSourceFile(name, initialContent, template, pat));
		fileNames.add(name);
	}

	protected void mutateBuggles(World[] worlds, ArrayList<String> newClasseNames){
		for (World current:worlds) {
			ArrayList<AbstractBuggle> newBuggles = new ArrayList<AbstractBuggle>();
			Iterator<AbstractBuggle> it = current.buggles();
			for (String name : newClasseNames) {
				/* Get the next existing buggle */
				if (!it.hasNext()) 
					throw new BrokenLessonException("Too much arguments provided to mutateBuggle");
				AbstractBuggle old = it.next();

				/* Instanciate a new buggle of the new type */
				AbstractBuggle b;
				try {
					b = compiledBuggleClasses.get(className(name)).newInstance();
					//Logger.log("Exercise:mutateBuggles to "+className(name), b.toString());
				} catch (InstantiationException e) {
					throw new RuntimeException("Cannot instanciate buggle of type "+className(name), e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException("Illegal access while instanciating buggle of type "+className(name), e);
				} catch (NullPointerException e) {
					/* this kind of buggle was not written by student. try to get it from default class loader, or complain if it also fails */
					try {
						b = compiler.loadClass(name).newInstance(); 
					} catch (Exception e2) {
						throw new RuntimeException("Cannot find a buggle of name "+className(name)+" or "+name+". Broken lesson.", e2);
					}
				}

				/* change fields of new buggle to copy old one */
				b.setWorld(old.getWorld());
				b.setPos(old.getX(), old.getY());
				b.setDirection(old.getDirection());
				b.setColor(old.getColor());
				b.setBrushColor(old.getBrushColor());
				b.setName(old.getName());
				b.initDone();

				/* Add new buggle to the to be returned buggles set */
				newBuggles.add(b);
			}
			if (it.hasNext())
				throw new BrokenLessonException("Not enough arguments provided to mutateBuggle");
			current.setBuggles(newBuggles);
		}
	}
	protected void mutateBuggle(World[] worlds, String newClasseName){
		ArrayList<String> names= new ArrayList<String>();
		for (int i=0; i<currentWorld[0].bugglesCount(); i++)
			names.add(newClasseName);
		mutateBuggles(worlds, names);
	}

	protected void mutateBuggles(ArrayList<String> newClasseNames){
		mutateBuggles(currentWorld, newClasseNames);
	}

	protected void mutateBuggle(String newClasseName){		
		mutateBuggle(currentWorld, newClasseName);
	}

	public Exercise(Lesson lesson) {
		super();
		this.lesson = lesson;
		sourceFiles = new Vector<RevertableSourceFile>();
		hiddenSourceFiles = new Vector<SourceFile>();
		runtimePatterns = new TreeMap<String, String>();
		fileNames = new Vector<String>();
	}

	public String[] getSourceFilesNames() {
		String[] res = new String[sourceFiles.size()];
		int i = 0;
		for (SourceFile sf: sourceFiles) {
			res[i] = sf.getName();
			i++;
		}
		return res;
	}
		
	public World[] getAnswerWorld() {
		return answerWorld;
	}
	
	public World[] getInitialWorld() {
		return initialWorld;
	}

	public int publicSourceFileCount() {
		return this.sourceFiles.size();
	}
	
	public RevertableSourceFile getPublicSourceFile(int i) {
		return this.sourceFiles.get(i);
	}
	
	public int worldCount() {
		return this.initialWorld.length;
	}
	
	public World getWorld(int index) {
		return this.currentWorld[index];
	}
	
	public int indexOfWorld(World w) {
		int index = 0;
		do {
			if (this.currentWorld[index] == w)
				return index;
			index++;
		} while (index < this.currentWorld.length);
		
		throw new RuntimeException("WTF ?"); // FIXEM: not nice ;)
	}
	
	public World getAnswerOfWorld(int index) {
		return this.answerWorld[index];
	}
}
