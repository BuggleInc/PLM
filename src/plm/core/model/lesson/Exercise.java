package plm.core.model.lesson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import plm.core.CompilerJava;
import plm.core.CompilerScala;
import plm.core.PLMCompilerException;
import plm.core.model.Game;
import plm.core.model.LogWriter;
import plm.core.model.ProgrammingLanguage;
import plm.core.model.session.SourceFile;
import plm.core.model.session.SourceFileRevertable;
import plm.core.utils.FileUtils;
import plm.universe.Entity;
import plm.universe.World;


public abstract class Exercise extends Lecture {
	public static enum WorldKind {INITIAL,CURRENT, ANSWER}
	public static enum StudentOrCorrection {STUDENT, CORRECTION}

	protected String tabName = getClass().getSimpleName();/* Name of the tab in editor -- must be a valid java identifier */


	protected Map<ProgrammingLanguage, List<SourceFile>> sourceFiles= new HashMap<ProgrammingLanguage, List<SourceFile>>();

	public Map<String, Class<Object>> compiledClasses = new TreeMap<String, Class<Object>>(); /* list of entity classes defined in the lesson */

	/* to make sure that the subsequent version of the same class have different names, in order to bypass the cache of the class loader */
	private static final String packageNamePrefix = "plm.runtime";
	private int packageNameSuffix = 0;

	protected Vector<World> currentWorld; /* the one displayed */
	protected Vector<World> initialWorld; /* the one used to reset the previous on each run */
	protected Vector<World> answerWorld;  /* the one current should look like to pass the test */

	protected Map<String, String> runtimePatterns = new TreeMap<String, String>();

	public ExecutionProgress lastResult;

	public I18n i18n = I18nFactory.getI18n(getClass(),"org.plm.i18n.Messages",Game.getInstance().getLocale(), I18nFactory.FALLBACK);

	public Exercise(Lesson lesson,String basename) {
		super(lesson,basename);
	}

	/** Converts {@code "foo.bar.baz"} to {@code "foo.bar.Scalabaz"}. */
	protected static String prependScalaToLastComponent(String path) {
		String[] components = path.split("\\.");
		StringBuilder result = new StringBuilder();
		int last = components.length - 1;
		for (int i = 0; i < last; i++) {
			result.append(components[i] + ".");
		}
		result.append("Scala" + components[last]);
		return result.toString();
	}

	protected String nameOfCorrectionEntity(ProgrammingLanguage lang) {
		String entityName = getClass().getCanonicalName() + "Entity";
		return lang.equals(Game.SCALA) ? prependScalaToLastComponent(entityName) : entityName;
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
			
			if (!currentWorld.get(i).winning(answerWorld.get(i))) {
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
	private final CompilerJava compiler = new CompilerJava(Arrays.asList(new String[] { }));


	/**
	 * Generate Java source from the user function
	 * @param out 
	 * 			where to display our errors
	 * @param whatToCompile
	 * 			either STUDENT's provided data or CORRECTION entity 
	 * @throws PLMCompilerException 
	 */
	public void compileAll(LogWriter out, StudentOrCorrection whatToCompile) throws PLMCompilerException {
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
					sources.put(className(sf.getName()), sf.getCompilableContent(runtimePatterns,whatToCompile)); 

			if (sources.isEmpty()) 
				return;

			try {
				DiagnosticCollector<JavaFileObject> errs = new DiagnosticCollector<JavaFileObject>();			
				compiledClasses = compiler.compile(sources, errs);

				if (out != null)
					out.log(errs);
			} catch (PLMCompilerException e) {
				System.err.println(Game.i18n.tr("Compilation error:"));
				lastResult = ExecutionProgress.newCompilationError(e.getDiagnostics());
				if (out != null)
					out.log(lastResult.compilationError); // display the same error as in the ExerciseFailedDialog

				if (Game.getInstance().isDebugEnabled() && sourceFiles.get(Game.JAVA) != null)
					for (SourceFile sf: sourceFiles.get(Game.JAVA)) 
						System.out.println("Source file "+sf.getName()+":"+sf.getCompilableContent(runtimePatterns,whatToCompile)); 

				throw e;
			}
		} else if (Game.getProgrammingLanguage().equals(Game.SCALA)) {
			List<SourceFile> sfs = sourceFiles.get(Game.SCALA);
			if (sfs == null || sfs.isEmpty()) {
				String msg = getName()+": No source to compile";
				System.err.println(msg);
				PLMCompilerException e = new PLMCompilerException(msg, null, null);
				lastResult = ExecutionProgress.newCompilationError(e.getMessage());				
				throw e;
			}

			try {
				CompilerScala.getInstance().reset();
				for (SourceFile sf : sfs) {
					System.out.println("name : "+tabName);
					CompilerScala.getInstance().compile(className(sf.getName()), sf.getCompilableContent(runtimePatterns,whatToCompile), sf.getOffset());
				}
			} catch (PLMCompilerException e) {
				System.err.println(Game.i18n.tr("Compilation error:"));
				System.err.println(e.getMessage());
				lastResult = ExecutionProgress.newCompilationError(e.getMessage());

				throw e;
			}
		}else if(Game.getProgrammingLanguage().equals(Game.C)){
			List<SourceFile> sfs = sourceFiles.get(Game.C);
			if (sfs == null || sfs.isEmpty()) {
				String msg = getName()+": No source to compile";
				System.err.println(msg);
				PLMCompilerException e = new PLMCompilerException(msg, null, null);
				lastResult = ExecutionProgress.newCompilationError(e.getMessage());				
				throw e;
			}

			for (SourceFile sf : sfs){
				//TODO GIANNINI recuperer le body et faire des trucs avec pour la compilation

				String code = sf.getCompilableContent(runtimePatterns,whatToCompile); 
				//System.out.println(code);
				
				Runtime runtime = Runtime.getRuntime();

				final StringBuffer resCompilationErr=new StringBuffer();
				try {
					
					File saveDir = new File(Game.getSavingLocation()+"/bin");
					if(!saveDir.exists()){
						saveDir.mkdir();
					}
					String saveDirPath = saveDir.getAbsolutePath();
					
					File exec = new File(saveDirPath+"/prog");
					if(exec.exists()){
						exec.delete();
					}
					String execPath = exec.getAbsolutePath();
					
					//compile the RemoteBuggle
					String compileRemote="";
					String link="";
					if(code.contains("RemoteBuggle")){
						compileRemote = "gcc -g -Wall -c langages/c/src/RemoteBuggle.c -I langages/c/include/ -o "+saveDirPath+"/RemoteBuggle.o";
						link = "gcc "+saveDirPath+"/current.o "+saveDirPath+"/RemoteBuggle.o -o "+execPath;
					}else if(code.contains("RemoteTurtle")){
						compileRemote = "gcc -g -Wall -c langages/c/src/RemoteTurtle.c -I langages/c/include/ -o "+saveDirPath+"/RemoteTurtle.o";
						link = "gcc "+saveDirPath+"/current.o "+saveDirPath+"/RemoteTurtle.o -o "+execPath;
					}else if(code.contains("RemoteSort")){
						compileRemote = "gcc -g -Wall -c langages/c/src/RemoteSort.c -I langages/c/include/ -o "+saveDirPath+"/RemoteSort.o";
						link = "gcc "+saveDirPath+"/current.o "+saveDirPath+"/RemoteSort.o -o "+execPath;
					}else if(code.contains("RemoteFlag")){
						compileRemote = "gcc -g -Wall -c langages/c/src/RemoteFlag.c -I langages/c/include/ -o "+saveDirPath+"/RemoteFlag.o";
						link = "gcc "+saveDirPath+"/current.o "+saveDirPath+"/RemoteFlag.o -o "+execPath;
					}else if(code.contains("RemoteBaseball")){
						compileRemote = "gcc -g -Wall -c langages/c/src/RemoteBaseball.c -I langages/c/include/ -o "+saveDirPath+"/RemoteBaseball.o";
						link = "gcc "+saveDirPath+"/current.o "+saveDirPath+"/RemoteBaseball.o -o "+execPath;
					}else if(code.contains("RemotePancake")){
						compileRemote = "gcc -g -Wall -c langages/c/src/RemotePancake.c -I langages/c/include/ -o "+saveDirPath+"/RemotePancake.o ";
						link = "gcc "+saveDirPath+"/current.o "+saveDirPath+"/RemotePancake.o -o "+execPath;
					}else if(code.contains("RemoteHanoi")){
						compileRemote = "gcc -g -Wall -c langages/c/src/RemoteHanoi.c -I langages/c/include/ -o "+saveDirPath+"/RemoteHanoi.o ";
						link = "gcc "+saveDirPath+"/current.o "+saveDirPath+"/RemoteHanoi.o -o "+execPath;
					}
					
					//compile the current code
					String compileCode = "gcc -g -c -x c -o "+saveDirPath+"/current.o -I langages/c/include/ -Wall - ";

					
					String[] arg1 = {"/bin/sh","-c",compileRemote+" ; "+compileCode+" ; "+link};
					//String[] arg1 = {"/bin/sh","-c","gcc -x c -o "+execPath+" -Wall - "};
					
					
					
					final Process process = runtime.exec(arg1);
					final BufferedWriter bwriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
					bwriter.write(code);
					bwriter.close();
					Thread reader = new Thread() {
						public void run() {
							try {
								BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
								String line = "";
								try {
									while((line = reader.readLine()) != null ) {
										resCompilationErr.append(line+"\n");
									}
								} finally {
									reader.close();
								}
							} catch(IOException ioe) {
								ioe.printStackTrace();
							}
						}
					};
					reader.run();
					
					Thread error = new Thread() {
						public void run() {
							try {
								BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
								String line = "";
								try {
									while((line = reader.readLine()) != null ) {
										resCompilationErr.append(line+"\n");
									}
								} finally {
									reader.close();
								}
							} catch(IOException ioe) {
								ioe.printStackTrace();
							}
						}
					};
					error.run();
					
					


					process.waitFor();
					
					if(resCompilationErr.length()>0){
						//TODO GIANNINI parse the error message and verify Warning
						System.out.println("=========="+sf.getName());
						PLMCompilerException e = new PLMCompilerException(resCompilationErr.toString(), null, null);
						System.err.println(Game.i18n.tr("Compilation error:"));
						System.err.println(e.getMessage());
						lastResult = ExecutionProgress.newCompilationError(e.getMessage());

						throw e;
					}
					

				} catch (IOException ioe) {
					// TODO Auto-generated catch block
					ioe.printStackTrace();
				} catch(InterruptedException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}




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

	public void newSource(ProgrammingLanguage lang, String name, String initialContent, String template,int offset,String correctionCtn) {
		getSourceFilesList(lang).add(new SourceFileRevertable(name, initialContent, template, offset,correctionCtn));
	}

	public void mutateEntities(WorldKind kind, StudentOrCorrection whatToMutate) {
		ProgrammingLanguage lang = Game.getProgrammingLanguage();
		String newClassName = (whatToMutate == StudentOrCorrection.STUDENT ? tabName : nameOfCorrectionEntity(lang));

		Vector<World> worlds;
		switch (kind) {
		case INITIAL: worlds = initialWorld; break;
		case CURRENT: worlds = currentWorld; break;
		case ANSWER:  worlds = answerWorld;  break;
		default: throw new RuntimeException("kind is invalid: "+kind);
		}

		/* Sanity check for broken lessons: the entity name must be a valid Java identifier */
		if (Game.getProgrammingLanguage().equals(Game.JAVA)) {
			String[] forbidden = new String[] {"'","\""};
			for (String stringPattern : forbidden) {
				Pattern pattern = Pattern.compile(stringPattern);
				Matcher matcher = pattern.matcher(newClassName);

				if (matcher.matches())
					throw new RuntimeException(newClassName+" is not a valid java identifier (forbidden char: "+stringPattern+"). "+
							"Does your exercise use a broken tabname or entityname?");
			}
		}

		for (World current:worlds) {
			ArrayList<Entity> newEntities = new ArrayList<Entity>();
			for (Entity old : current.getEntities()) {
				/* This is never called with lightbot entities, no need to deal with it here */
				if (lang.equals(Game.JAVA) || lang.equals(Game.SCALA) || lang.equals(Game.C)) {


					/* Instantiate a new entity of the new type */
					Entity ent;
					try {
						if (lang.equals(Game.JAVA)){
							ent = (Entity) compiledClasses.get(className(newClassName)).newInstance();
						}else if(lang.equals(Game.SCALA)){
							ent = (Entity)CompilerScala.getInstance().findClass(className(newClassName)).newInstance();
						}else if(lang.equals(Game.C)){
							//TODO GIANNINI Faire quelque chose ici pour l'entite					
							ent= (Entity)Class.forName(nameOfCorrectionEntity(lang)).newInstance();

						}else{
							ent=null;
						}
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
					} catch (ClassNotFoundException e) {
						//TODO GIANNINI
						throw new RuntimeException("Cannot instanciate entity of type "+className(newClassName), e);
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

					if (whatToMutate == StudentOrCorrection.STUDENT) {
						boolean foundScript = false;

						for (SourceFile sf : sourceFiles.get(lang)) {
							if (newClassName.equals(sf.getName())) {
								old.setScript(lang, sf.getCompilableContent(whatToMutate));
								old.setScriptOffset(lang, sf.getOffset());
								foundScript = true;
							} 
						}
						if (!foundScript) {
							StringBuffer sb = new StringBuffer();
							for (SourceFile sf: sourceFiles.get(lang)) 
								sb.append(sf.getName()+", ");

							System.err.println(getClass().getName()+": Cannot retrieve the script for "+newClassName+". Known scripts: "+sb+"(EOL)");
							throw new RuntimeException(getClass().getName()+": Cannot retrieve the script for "+newClassName+". Known scripts: "+sb+"(EOL)");						
						}
					} else { // whatToMutate == StudentOrCorrection.CORRECTION
						for (World aw : worlds) {
							for (Entity ent: aw.getEntities()) {
								StringBuffer sb = null;
								try {
									sb = FileUtils.readContentAsText(nameOfCorrectionEntity(lang), lang.getExt(), false);
								} catch (IOException ex) {
									throw new RuntimeException(Game.i18n.tr("Cannot compute the answer from file {0}.{1} since I cannot read it (error was: {2}).", nameOfCorrectionEntity(lang), lang.getExt(),ex.getLocalizedMessage()));
								}


								ent.setScript(lang, sb.toString());
							}
						}
					}
				}
			}
			if (lang.equals(Game.JAVA) || lang.equals(Game.SCALA) || lang.equals(Game.C)) 
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

