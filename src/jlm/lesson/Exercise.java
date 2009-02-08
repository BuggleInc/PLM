package jlm.lesson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import jlm.core.InMemoryCompiler;
import jlm.core.LogWriter;
import jlm.exception.BrokenLessonException;
import jlm.exception.JLMCompilerException;
import jlm.universe.Entity;
import jlm.universe.World;


public abstract class Exercise {
	public boolean debug = false; /* whether to debug this particular exo */

	protected boolean done = false; /** indicate whether this Exercise was successfully done or not */

	public String name = "<no name>"; 
	public String mission = "";  /** The text to display to present the lesson */
	protected List<RevertableSourceFile> sourceFiles; /** All the editable source files */
	protected List<SourceFile> hiddenSourceFiles; /** Other source files, that the compiler should use, but the user shouldn't see */
	Map<String, Class<Object>> compiledClasses = new TreeMap<String, Class<Object>>(); /* list of entity classes defined in the lesson */
	protected List<String> fileNames;

	/* to make sure that the subsequent version of the same class have different names, in order to bypass the cache of the class loader */
	private static final String packageNamePrefix = "jlm.runtime";
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
	
	/*
	public World[] getCurrentWorld(){
		return currentWorld;
	}
	*/
	
	public List<World> getCurrentWorld() {
		return Arrays.asList(currentWorld);
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
			currentWorld[i] = w[i].copy();
			initialWorld[i] = w[i].copy();
			answerWorld[i]  = w[i].copy();
		}
	}
	
	public abstract void run(List<Thread> runnerVect);	
	public abstract void runDemo(List<Thread> runnerVect);	
	
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
	// Create a compiler of classes (using java 1.6)
	private final InMemoryCompiler compiler = new InMemoryCompiler(
			getClass().getClassLoader(), Arrays.asList(new String[] { "-target", "1.6" }));

	/**
	 * Generate Java source from the user function
	 * @throws JLMCompilerException 
	 */
	public void compileAll(LogWriter out) throws JLMCompilerException {
		compiledClasses = new TreeMap<String, Class<Object>>();

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
			compiledClasses = compiler.compile(sources, errs);

			out.log(errs);
		} catch (JLMCompilerException e) {
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

	protected void mutateEntities(World[] worlds, ArrayList<String> newClasseNames){
		for (World current:worlds) {
			ArrayList<Entity> newEntities = new ArrayList<Entity>();
			Iterator<Entity> it = current.entities();
			for (String name : newClasseNames) {
				/* Get the next existing entity */
				if (!it.hasNext()) 
					throw new BrokenLessonException("Too much arguments provided to mutateEntities");
				Entity old = it.next();

				/* Instanciate a new entity of the new type */
				Entity ent;
				try {
					ent = (Entity)compiledClasses.get(className(name)).newInstance();
					//Logger.log("Exercise:mutateEntities to "+className(name), b.toString());
				} catch (InstantiationException e) {
					throw new RuntimeException("Cannot instanciate entity of type "+className(name), e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException("Illegal access while instanciating entity of type "+className(name), e);
				} catch (NullPointerException e) {
					/* this kind of entity was not written by student. try to get it from default class loader, or complain if it also fails */
					try {
						ent = (Entity)compiler.loadClass(name).newInstance(); 
					} catch (Exception e2) {
						throw new RuntimeException("Cannot find an entity of name "+className(name)+" or "+name+". Broken lesson.", e2);
					}
				}

				/* change fields of new entity to copy old one */
				ent.copy(old);
				ent.initDone();

				/* Add new entity to the to be returned entities set */
				newEntities.add(ent);
			}
			if (it.hasNext())
				throw new BrokenLessonException("Not enough arguments provided to mutateEntities");
			current.setEntities(newEntities);
		}
	}
	protected void mutateEntity(World[] worlds, String newClasseName){
		ArrayList<String> names= new ArrayList<String>();
		for (int i=0; i<currentWorld[0].getEntityCount(); i++)
			names.add(newClasseName);
		mutateEntities(worlds, names);
	}

	protected void mutateEntities(ArrayList<String> newClasseNames){
		mutateEntities(currentWorld, newClasseNames);
	}

	protected void mutateEntity(String newClasseName){		
		mutateEntity(currentWorld, newClasseName);
	}

	public Exercise(Lesson lesson) {
		super();
		this.lesson = lesson;
		sourceFiles = new ArrayList<RevertableSourceFile>();
		hiddenSourceFiles = new ArrayList<SourceFile>();
		runtimePatterns = new TreeMap<String, String>();
		fileNames = new ArrayList<String>();
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
		
	/*
	public World[] getAnswerWorld() {
		return answerWorld;
	}
	*/
	
	public List<World> getAnswerWorld() {
		return Arrays.asList(answerWorld);
	}
	
	
	/*
	public World[] getInitialWorld() {
		return initialWorld;
	}
	*/
	
	public List<World> getInitialWorld() {
		return Arrays.asList(this.initialWorld);
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
		
		throw new RuntimeException("World not found (please report this bug)");
	}
	
	public World getAnswerOfWorld(int index) {
		return this.answerWorld[index];
	}
	
}

