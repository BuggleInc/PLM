package plm.core.model.lesson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import plm.core.PLMCompilerException;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.LogHandler;
import plm.core.model.session.SourceFile;
import plm.core.model.session.SourceFileRevertable;
import plm.core.utils.FileUtils;
import plm.universe.World;


public abstract class Exercise extends Lecture {
	public static enum WorldKind {INITIAL, CURRENT, ANSWER, ERROR}
	public static enum StudentOrCorrection {STUDENT, CORRECTION, ERROR}
	
	private int nbError;

	protected String tabName = getClass().getSimpleName();/* Name of the tab in editor -- must be a valid java identifier */

	public String nameOfCorrectionEntity() { // This will be redefined by TurtleArt to reduce the amount of code
		return getClass().getCanonicalName() + "Entity";
	}

	public String nameOfCommonError(int i) {
		return getClass().getCanonicalName() + "CommonErr" + i;
	}

	public String getTabName() {
		return tabName;
	}

	protected Map<ProgrammingLanguage, List<SourceFile>> sourceFiles= new HashMap<ProgrammingLanguage, List<SourceFile>>();
	private Map<ProgrammingLanguage, SourceFile> defaultSourceFiles = new HashMap<ProgrammingLanguage, SourceFile>();

	protected Vector<World> currentWorld; /* the one displayed */
	protected Vector<World> initialWorld; /* the one used to reset the previous on each run */
	protected Vector<World> answerWorld;  /* the one current should look like to pass the test */
	protected Vector<Vector<World>> commonErrors = new Vector<Vector<World>>();


	public ExecutionProgress lastResult;

	public Exercise(String id, String name) {
		setId(id);
		setName(name);
	}
	
	public Exercise(Game game, Lesson lesson,String basename) {
		super(game, lesson,basename);
	}

	public void initWorlds(int size) {
		currentWorld = new Vector<World>(size);
		initialWorld = new Vector<World>(size);
		answerWorld  = new Vector<World>(size);
	}
	
	public void setupWorlds(World[] w, int size) {
		initWorlds(w.length);
		Vector<World> errorWorld   = new Vector<World>(w.length);
		for (int i=0; i<w.length; i++) {
			if (w[i] == null) 
				throw new RuntimeException("Broken exercise "+getId()+": world "+i+" is null!");
			currentWorld.add( w[i].copy() );
			initialWorld.add( w[i].copy() );
			answerWorld.add( w[i].copy() );
		}
		for(int j = 0 ; j < size ; j++) { //size : nombre de fichiers d'erreur
			errorWorld = new Vector<World>(w.length);
			for (int i=0; i<w.length; i++) {
				if (w[i] == null) 
					throw new RuntimeException("Broken exercise "+getId()+": world "+i+" is null!");
				errorWorld.add(w[i].copy());
			}
			commonErrors.add(errorWorld);
		}
	}

	public abstract void run(List<Thread> runnerVect);	
	public abstract void runDemo(List<Thread> runnerVect);	

	public void check() {
		boolean pass = true;
		lastResult.commonErrorText = "";
		lastResult.commonErrorID = -1;
		if (lastResult.outcome == ExecutionProgress.outcomeKind.PASS) {
			for (int i=0; i<currentWorld.size(); i++) {
				currentWorld.get(i).notifyWorldUpdatesListeners();

				lastResult.totalTests++;

				if (!currentWorld.get(i).winning(answerWorld.get(i))) {
					for(int j = 0 ; j < commonErrors.size() ; j++) {
						if(currentWorld.get(i).winning((commonErrors.get(j)).get(i))) { //winning do an equals, but it is the same
							String path = Game.JAVA.nameOfCommonError(this, j).replaceAll("\\.", "/");
							try {
								StringBuffer sb = FileUtils.readContentAsText(path, getGame().getLocale(), "html", true);
								lastResult.commonErrorText = sb.toString();
								lastResult.commonErrorID = j;
							} catch (IOException e) {
								e.printStackTrace();
							} 
							break;
						}
					}
					String diff = answerWorld.get(i).diffTo(currentWorld.get(i));
					lastResult.executionError += getGame().i18n.tr("The world ''{0}'' differs",currentWorld.get(i).getName());
					if (diff != null) 
						lastResult.executionError += ":\n"+diff;
					lastResult.executionError += "\n------------------------------------------\n";
					pass = false;
				} else {
					lastResult.passedTests++;
				}
			}
			if (pass)
				lastResult.outcome = ExecutionProgress.outcomeKind.PASS;
			else 
				lastResult.outcome = ExecutionProgress.outcomeKind.FAIL;
		}
	}
	/** Reset the current worlds to the state of the initial worlds */
	public void reset() {
		//lastResult = new ExecutionProgress(getGame().getProgrammingLanguage());

		for (int i=0; i<initialWorld.size(); i++) 
			currentWorld.get(i).reset(initialWorld.get(i));
	}

	/**
	 * Generate Java source from the user function
	 * @param out 
	 * 			where to display our errors
	 * @param whatToCompile
	 * 			either STUDENT's provided data or CORRECTION entity 
	 * @throws PLMCompilerException 
	 * 
	 * FIXME: KILLME and use the compileExo of ProgrammingLanguage directly
	 */
	public void compileAll(LogHandler logger, StudentOrCorrection whatToCompile) throws PLMCompilerException {
		/* Do the compile (but only if the current language is Java or Scala: scripts are not compiled of course)
		 * Instead, scripting languages get the source code as text directly from the sourceFiles 
		 */
		getGame().getProgrammingLanguage().compileExo(this, logger, whatToCompile, getGame().i18n);
	}

	/** get the list of source files for a given language, or create it if not existent yet */
	public List<SourceFile> getSourceFilesList(ProgrammingLanguage lang) {
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
		if(i<getSourceFileCount(lang)) {
			return getSourceFilesList(lang).get(i);
		}
		return null;
	}

	public void newSource(ProgrammingLanguage lang, String name, String initialContent, String template,int offset,String correctionCtn, String errorCtn) {
		switch (lang.getLang()){
		case "Blockly":
			getSourceFilesList(lang).add(new SourceFileRevertable(getGame(), name, initialContent, template, offset, correctionCtn, errorCtn));
			getSourceFilesList(lang).add(new SourceFileRevertable(getGame(), name+"Blocks", initialContent, template, offset, correctionCtn, errorCtn));
			break;
		default:
			getSourceFilesList(lang).add(new SourceFileRevertable(getGame(), name, initialContent, template, offset, correctionCtn, errorCtn));
			break;
		}
	}

	public void mutateEntities(WorldKind kind, StudentOrCorrection whatToMutate) {
		ProgrammingLanguage lang = getGame().getProgrammingLanguage();

		Vector<World> worlds = null;
		switch (kind) {
		case INITIAL: worlds = initialWorld; break;
		case CURRENT: worlds = currentWorld; break;
		case ANSWER:  worlds = answerWorld;  break;
		case ERROR: worlds = commonErrors.get(nbError); break;
		default: throw new RuntimeException("kind is invalid: "+kind);
		}


		/* Sanity check for broken lessons: the entity name must be a valid Java identifier */
		if (getGame().getProgrammingLanguage().equals(Game.JAVA)) {
			String[] forbidden = new String[] {"'","\""};
			for (String stringPattern : forbidden) {
				Pattern pattern = Pattern.compile(stringPattern);
				Matcher matcher = pattern.matcher(tabName);

				if (matcher.matches())
					throw new RuntimeException(tabName+" is not a valid java identifier (forbidden char: "+stringPattern+"). "+
							"Your exercise uses a broken tabName.");
			}
		}

		try {
			for (World current:worlds) {
				if (current.getEntities().isEmpty())
					throw new RuntimeException("Every world in every exercise must have at least one entity when calling setup(). Please fix your exercise.");
				current.setEntities( lang.mutateEntities(this, current.getEntities(), whatToMutate, getGame().i18n, nbError) );
			}
		} catch (PLMCompilerException e) {
			lastResult = ExecutionProgress.newCompilationError(e.getLocalizedMessage(), lang);
		}
	}

	public Vector<World> getWorlds(WorldKind kind) {
		switch (kind) {
		case INITIAL: return initialWorld;
		case CURRENT: return currentWorld;
		case ANSWER:  return answerWorld;
		case ERROR:   if(nbError != -1) return commonErrors.get(nbError);
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

	public void currentHumanLanguageHasChanged(Locale newLang) {
		super.currentHumanLanguageHasChanged(newLang);
		initialWorld.get(0).resetAbout();
		initialWorld.get(0).getAbout();
	}
	
	public int getNbError() {
		return nbError;
	}
	
	public void setNbError(int nbError) {
		this.nbError = nbError;
	}

	public boolean isProgLangSupported(ProgrammingLanguage progLang) {
		return defaultSourceFiles.containsKey(progLang);
	}

	public void addDefaultSourceFile(ProgrammingLanguage progLang, SourceFile sourceFile) {
		defaultSourceFiles.put(progLang, sourceFile);
	}

	public SourceFile getDefaultSourceFile(ProgrammingLanguage progLang) {
		return defaultSourceFiles.get(progLang).clone();
	}
}

