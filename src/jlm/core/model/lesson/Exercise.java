package jlm.core.model.lesson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import jlm.core.CompilerJava;
import jlm.core.CompilerScala;
import jlm.core.JLMCompilerException;
import jlm.core.model.Game;
import jlm.core.model.LogWriter;
import jlm.core.model.ProgrammingLanguage;
import jlm.universe.Entity;
import jlm.universe.World;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;


public abstract class Exercise extends Lecture {
	public static enum WorldKind {INITIAL,CURRENT, ANSWER}

	protected Map<ProgrammingLanguage, List<SourceFile>> sourceFiles= new HashMap<ProgrammingLanguage, List<SourceFile>>();
	
	public Map<String, Class<Object>> compiledClasses = new TreeMap<String, Class<Object>>(); /* list of entity classes defined in the lesson */

	/* to make sure that the subsequent version of the same class have different names, in order to bypass the cache of the class loader */
	private static final String packageNamePrefix = "jlm.runtime";
	private int packageNameSuffix = 0;

	protected Vector<World> currentWorld; /* the one displayed */
	protected Vector<World> initialWorld; /* the one used to reset the previous on each run */
	protected Vector<World> answerWorld;  /* the one current should look like to pass the test */

	protected Map<String, String> runtimePatterns = new TreeMap<String, String>();

	public ExecutionProgress lastResult;
	
	public I18n i18n = I18nFactory.getI18n(getClass(),"org.jlm.i18n.Messages",Game.getInstance().getLocale(), I18nFactory.FALLBACK);

	public Exercise(Lesson lesson) {
		super(lesson);
	}
	
	public void setupWorlds(World[] w) {
		currentWorld = new Vector<World>(w.length);
		initialWorld = new Vector<World>(w.length);
		answerWorld  = new Vector<World>(w.length);
		for (int i=0; i<w.length; i++) {
			currentWorld.add( w[i].copy() );
			initialWorld.add( w[i].copy() );
			answerWorld. add( w[i].copy() );
		}
	}
	
	public abstract void run(List<Thread> runnerVect);	
	public abstract void runDemo(List<Thread> runnerVect);	
	
	public void check() {
		for (int i=0; i<currentWorld.size(); i++) {
			currentWorld.get(i).notifyWorldUpdatesListeners();
			
			lastResult.totalTests++;

			if (!currentWorld.get(i).equals(answerWorld.get(i))) {
				String diff = answerWorld.get(i).diffTo(currentWorld.get(i));
				lastResult.details += i18n.tr("The world ''{0}'' differs",currentWorld.get(i).getName());
				if (diff != null) 
					lastResult.details += ":\n"+diff;
				lastResult.details += "\n";
			} else {
				lastResult.passedTests++;
			}
		}
	}
	/** Reset the current worlds to the state of the initial worlds */
	public void reset() {
		lastResult = new ExecutionProgress();

		for (int i=0; i<initialWorld.size(); i++) 
			currentWorld.get(i).reset(initialWorld.get(i));
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
	private final CompilerJava compiler = new CompilerJava(Arrays.asList(new String[] { "-target", "1.6" }));


	/**
	 * Generate Java source from the user function
	 * @throws JLMCompilerException 
	 */
	public void compileAll(LogWriter out) throws JLMCompilerException {
		/* Make sure each run generate a new package to avoid that the loader cache prevent the reloading of the newly generated class */
		packageNameSuffix++;
		runtimePatterns.put("\\$package", "package "+packageName()+";");

		/* Do the compile (but only if the current language is Java or Scala: scripts are not compiled of course)
		 * Instead, scripting languages get the source code as text directly from the sourceFiles 
		 */
		if (Game.getProgrammingLanguage().equals(Game.JAVA)) {
			/* Prepare the source files */
			Map<String, String> sources = new TreeMap<String, String>();
			if (sourceFiles.get(Game.JAVA) != null)
				for (SourceFile sf: sourceFiles.get(Game.JAVA)) 
					sources.put(className(sf.getName()), sf.getCompilableContent(runtimePatterns)); 
			
			if (sources.isEmpty()) 
				return;
			
			try {
				DiagnosticCollector<JavaFileObject> errs = new DiagnosticCollector<JavaFileObject>();			
				compiledClasses = compiler.compile(sources, errs);

				out.log(errs);
			} catch (JLMCompilerException e) {
				System.err.println(Game.i18n.tr("Compilation error:"));
				out.log(e.getDiagnostics());
				lastResult = ExecutionProgress.newCompilationError(e.getDiagnostics());

				if (Game.getInstance().isDebugEnabled() && sourceFiles.get(Game.JAVA) != null)
					for (SourceFile sf: sourceFiles.get(Game.JAVA)) 
						System.out.println("Source file "+sf.getName()+":"+sf.getCompilableContent(runtimePatterns)); 

				throw e;
			}
		} else if (Game.getProgrammingLanguage().equals(Game.SCALA)) {
			List<SourceFile> sfs = sourceFiles.get(Game.SCALA);
			if (sfs == null || sfs.isEmpty())
				return;
			try {
				CompilerScala.getInstance().reset();
				for (SourceFile sf : sfs) 
					CompilerScala.getInstance().compile(className(sf.getName()), sf.getCompilableContent(runtimePatterns), sf.getOffset());
			} catch (JLMCompilerException e) {
				System.err.println(Game.i18n.tr("Compilation error:"));
				out.log(e.getMessage());
				lastResult = ExecutionProgress.newCompilationError(e.getMessage());
				
				throw e;
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
	public int getSourceFileCount(ProgrammingLanguage lang) {
		return getSourceFilesList(lang).size();
	}	
	public SourceFile getSourceFile(ProgrammingLanguage lang, int i) {
		return getSourceFilesList(lang).get(i);
	}

	public void newSource(ProgrammingLanguage lang, String name, String initialContent, String template,int offset) {
		getSourceFilesList(lang).add(new SourceFileRevertable(name, initialContent, template, offset));
	}

	protected void mutateEntities(Vector<World> worlds, String newClassName) {
		ProgrammingLanguage lang = Game.getProgrammingLanguage();
		/* Sanity check for broken lessons: the entity name must be a valid Java identifier */
		String[] forbidden = new String[] {"'","\""};
		for (String stringPattern : forbidden) {
			Pattern pattern = Pattern.compile(stringPattern);
			Matcher matcher = pattern.matcher(newClassName);
			
			if (matcher.matches())
				throw new RuntimeException(newClassName+" is not a valid java identifier (forbidden char: "+stringPattern+"). "+
						"Does your exercise use a broken tabname or entityname?");
		}

		for (World current:worlds) {
			ArrayList<Entity> newEntities = new ArrayList<Entity>();
			for (Entity old : current.getEntities()) {
				/* This is never called with lightbot entities, no need to deal with it here */
				if (lang.equals(Game.JAVA) || lang.equals(Game.SCALA)) {
					/* Instantiate a new entity of the new type */
					Entity ent;
					try {
						if (lang.equals(Game.JAVA))
							ent = (Entity) compiledClasses.get(className(newClassName)).newInstance();
						else
							ent = (Entity)CompilerScala.getInstance().findClass(className(newClassName)).newInstance();
							
					} catch (InstantiationException e) {
						throw new RuntimeException("Cannot instanciate entity of type "+className(newClassName), e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException("Illegal access while instanciating entity of type "+className(newClassName), e);
					} catch (NullPointerException e) {
						/* this kind of entity was not written by student. try to get it from default class loader, or complain if it also fails */
						try {
							ent = (Entity)compiler.loadClass(newClassName).newInstance(); 
						} catch (Exception e2) {
							throw new RuntimeException("Cannot find an entity of name "+className(newClassName)+" or "+newClassName+". Broken lesson.", e2);
						}
					}
					/* change fields of new entity to copy old one */
					ent.copy(old);
					ent.initDone();
					/* Add new entity to the to be returned entities set */
					newEntities.add(ent);
				} else { 
					/* In scripting, we don't need to actually mutate the entity, just set the script to be interpreted later.
					 * Also, since the classloader don't cross our way, don't mess with package names to force reloads. In other words, don't use className() in here!! 
					 */	
					
					boolean foundScript = false;
					for (SourceFile sf : sourceFiles.get(lang)) {
						if (newClassName.equals(sf.name)) {
							old.setScript(lang, sf.getCompilableContent());
							old.setScriptOffset(lang, sf.getOffset());
							foundScript = true;
						} 
					}
					if (!foundScript) {
						StringBuffer sb = new StringBuffer();
						for (SourceFile sf: sourceFiles.get(lang)) {
							sb.append(sf.name+", ");
						}
						throw new RuntimeException(getClass().getName()+": Cannot retrieve the script for "+newClassName+". Known scripts: "+sb+"(EOL)");						
					}
				}
			}
			if (lang.equals(Game.JAVA) || lang.equals(Game.SCALA)) 
				current.setEntities(newEntities);
		}
	}
			
	public Vector<World> getWorlds(WorldKind kind) {
		switch (kind) {
		case INITIAL: return initialWorld;
		case CURRENT: return currentWorld;
		case ANSWER:  return answerWorld;
		default: throw new RuntimeException("Unhandled kind of world: "+kind);
		}
	}
	
	public int getWorldCount() {
		return this.initialWorld.size();
	}
	
	/** Returns the current world number index 
	 * @see #getAnswerOfWorld(int)
	 */
	public World getWorld(int index) {// FIXME: rename to getCurrentWorld or KILLME
		return this.currentWorld.get(index);
	}
	
	public int indexOfWorld(World w) {
		int index = 0;
		do {
			if (this.currentWorld.get(index) == w)
				return index;
			index++;
		} while (index < this.currentWorld.size());
		
		throw new RuntimeException("World not found (please report this bug)");
	}
	
	public World getAnswerOfWorld(int index) { // FIXME: rename or KILLME
		return this.answerWorld.get(index);
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
}

