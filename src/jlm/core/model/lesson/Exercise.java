package jlm.core.model.lesson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import jlm.core.InMemoryCompiler;
import jlm.core.JLMCompilerException;
import jlm.core.model.Game;
import jlm.core.model.LogWriter;
import jlm.core.model.ProgrammingLanguage;
import jlm.universe.Entity;
import jlm.universe.World;


public abstract class Exercise  extends Lecture {
	public boolean debug = false; /* whether to debug this particular exo */

	protected Map<ProgrammingLanguage, List<SourceFile>> sourceFiles; /** All source files */
	
	public Map<String, Class<Object>> compiledClasses = new TreeMap<String, Class<Object>>(); /* list of entity classes defined in the lesson */
	public Map<ProgrammingLanguage, Map<String, String>> scriptSources = new TreeMap<ProgrammingLanguage, Map<String, String>>(); /* (Lang x scriptName |-> script source) list of scripts that are to be executed by entities when not running Java */

	/* to make sure that the subsequent version of the same class have different names, in order to bypass the cache of the class loader */
	private static final String packageNamePrefix = "jlm.runtime";
	private int packageNameSuffix = 0;

	protected World [] currentWorld; /* the one displayed */
	protected World [] initialWorld; /* the one used to reset the previous on each run */
	protected World [] answerWorld;  /* the one current should look like to pass the test */

	protected Map<String, String> runtimePatterns;

	public ExecutionProgress lastResult;
	
	public List<World> getCurrentWorld() {
		return Arrays.asList(currentWorld);
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
	
	public void check() throws Exception {
		lastResult = new ExecutionProgress();
		for (int i=0; i<currentWorld.length; i++) {
			currentWorld[i].notifyWorldUpdatesListeners();
			
			lastResult.totalTests++;

			if (!currentWorld[i].equals(answerWorld[i])) {
				String diff = answerWorld[i].diffTo(currentWorld[i]);
				lastResult.details += "The world '"+currentWorld[i].getName()+"' differs";
				if (diff != null) 
					lastResult.details += ":\n"+diff;
				lastResult.details += "\n";
			} else {
				lastResult.passedTests++;
			}
		}
	}
	public void reset() {
		for (int i=0; i<initialWorld.length; i++) 
			currentWorld[i].reset(initialWorld[i]);
	}

	/*
	 * +++++++++++++++++++++++++
	 * Compilation related stuff
	 * +++++++++++++++++++++++++
	 * 
	 */
	//TODO: why do we instantiate a compiler per exercise ? is there any way to re-use the same compiler. 
	//TODO: I tried to put it as static, but of course strange behaviors happen afterwards
	// Create a compiler of classes (using java 1.6)
	private final InMemoryCompiler compiler = new InMemoryCompiler(
			getClass().getClassLoader(), Arrays.asList(new String[] { "-target", "1.6" }));


	/**
	 * Generate Java source from the user function
	 * @throws JLMCompilerException 
	 */
	public void compileAll(LogWriter out) throws JLMCompilerException {
		/* Make sure each run generate a new package to avoid that the loader cache prevent the reloading of the newly generated class */
		packageNameSuffix++;
		runtimePatterns.put("\\$package", "package "+packageName()+";");

		/* Do the compile (but only if the current language is Java: scripts are not compiled of course) */
		if (Game.getProgrammingLanguage().equals(Game.JAVA)) {
			/* Prepare the source files */
			Map<String, CharSequence> sources = new TreeMap<String, CharSequence>();
			if (sourceFiles.get(Game.JAVA) != null)
				for (SourceFile sf: sourceFiles.get(Game.JAVA)) 
					sources.put(className(sf.getName()), sf.getCompilableContent(runtimePatterns)); 
			
			if (sources.isEmpty()) 
				return;
			
			try {
				DiagnosticCollector<JavaFileObject> errs = new DiagnosticCollector<JavaFileObject>();			
				compiler.compile(sources, errs);

				out.log(errs);
			} catch (JLMCompilerException e) {
				System.err.println("Compilation error:");
				out.log(e.getDiagnostics());
				lastResult = ExecutionProgress.newCompilationError(e.getDiagnostics());

				if (Game.getInstance().isDebugEnabled() && sourceFiles.get(Game.JAVA) != null)
					for (SourceFile sf: sourceFiles.get(Game.JAVA)) 
						System.out.println("Source file "+sf.getName()+":"+sf.getCompilableContent(runtimePatterns)); 

				throw e;
			}
		}
		
		/* Setup the scripts for the other languages */
		for (ProgrammingLanguage lang: getProgLanguages()) {
			if (!lang.equals(Game.JAVA) && !lang.equals(Game.LIGHTBOT)) {
				Map<String, String> scripts = new TreeMap<String, String>();
				
				for (SourceFile sf: sourceFiles.get(lang)) 
					scripts.put(className(sf.getName()), sf.getCompilableContent(null)); 
				
				scriptSources.put(lang,scripts); 
			}
		}
	}
	
	private String packageName(){
		return packageNamePrefix + packageNameSuffix;
	}
	public String className(String name) {
		return packageName() + "." + name;
	}
	/** get the list of source files for a given language, or create it if not existent yet */
	protected List<SourceFile> getSourceFilesList(ProgrammingLanguage lang) {
		List<SourceFile> res = sourceFiles.get(lang); 
		if (res == null) {
			res = new ArrayList<SourceFile>();
			sourceFiles.put(lang, res);
		}
		return res;
	}
	public int sourceFileCount(ProgrammingLanguage lang) {
		return getSourceFilesList(lang).size();
	}	
	public SourceFile getPublicSourceFile(ProgrammingLanguage lang, int i) {
		return getSourceFilesList(lang).get(i);
	}

	public void newSource(ProgrammingLanguage lang, String name, String initialContent, String template) {
		getSourceFilesList(lang).add(new SourceFileRevertable(name, initialContent, template));
	}
	
	protected void mutateEntities(World[] worlds, ArrayList<String> newClasseNames) {
		for (World current:worlds) {
			ArrayList<Entity> newEntities = new ArrayList<Entity>();
			Iterator<Entity> entityIterator = current.entities();
			for (String name : newClasseNames) {
				/* Sanity check for broken lessons: the entity name must be a valid Java identifier */
				String[] forbidden = new String[] {"'","\""};
				for (String stringPattern : forbidden) {
					Pattern pattern = Pattern.compile(stringPattern);
					Matcher matcher = pattern.matcher(name);
				
					if (matcher.matches())
						throw new RuntimeException(name+" is not a valid java identifier (forbidden char: "+stringPattern+"). "+
					     "Does your exercise use a broken tabname or entityname?");
				}

				/* Get the next existing entity */
				if (!entityIterator.hasNext()) 
					throw new BrokenLessonException("Too much class names ("+newClasseNames.size()+")"+
							"provided to mutateEntities compared to the amount of entities found "+
							"("+current.getEntityCount()+")");
				Entity old = entityIterator.next();

				if (Game.getProgrammingLanguage().equals(Game.JAVA) || 
						Game.getProgrammingLanguage().equals(Game.LIGHTBOT)) {
					/* Instantiate a new entity of the new type */
					Entity ent;
					try {
						ent = (Entity)compiledClasses.get(className(name)).newInstance();
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
				} else { /* In scripting, we don't need to actually mutate the entity, just set the script to be interpreted later */
					String script = scriptSources.get(Game.getProgrammingLanguage()).get(className(name));
					if (script == null) 
						throw new RuntimeException("Cannot retrieve the script for "+className(name));
					old.setScript(Game.getProgrammingLanguage(), script);
				}
			}
			if (entityIterator.hasNext())
				throw new BrokenLessonException("Not enough arguments provided to mutateEntities");
			if (Game.getProgrammingLanguage().equals(Game.JAVA)) 
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
		super(lesson);
		sourceFiles = new HashMap<ProgrammingLanguage, List<SourceFile>>();
		runtimePatterns = new TreeMap<String, String>();
	}
			
	public List<World> getAnswerWorld() {
		return Arrays.asList(answerWorld);
	}
	
	public List<World> getInitialWorld() {
		return Arrays.asList(this.initialWorld);
	}
	
	public int worldCount() {
		return this.initialWorld.length;
	}
	
	/** Returns the current world number index 
	 * @see #getAnswerOfWorld(int)
	 */
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
	
	public String toString() {
		return getName();
	}

	/* setters and getter of the programming language that this exercise accepts */ 
	private Set<ProgrammingLanguage> progLanguages = new HashSet<ProgrammingLanguage>();
	public Set<ProgrammingLanguage> getProgLanguages() {
		return progLanguages;
	}
	protected void addProgLanguage(ProgrammingLanguage newL) {
		progLanguages.add(newL);
	}
	public void addProgLanguage(ProgrammingLanguage[] newL) { 
		for (ProgrammingLanguage l:newL)
			addProgLanguage(l);
	}
	public void setProgLanguages(ProgrammingLanguage ... languages) {
		progLanguages = new HashSet<ProgrammingLanguage>();
		for (ProgrammingLanguage pl:languages) {
			progLanguages.add(pl);
		}
	}
}

