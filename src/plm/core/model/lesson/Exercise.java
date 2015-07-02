package plm.core.model.lesson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import plm.core.PLMCompilerException;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.LogWriter;
import plm.core.model.session.SourceFile;
import plm.core.model.session.SourceFileRevertable;
import plm.universe.World;


public abstract class Exercise extends Lecture {
	public static enum WorldKind {INITIAL, CURRENT, ANSWER, ERROR}
	public static enum StudentOrCorrection {STUDENT, CORRECTION, ERROR}

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

	protected Vector<World> currentWorld; /* the one displayed */
	protected Vector<World> initialWorld; /* the one used to reset the previous on each run */
	protected Vector<World> answerWorld;  /* the one current should look like to pass the test */
	protected Vector<Vector<World>> commonErrors = new Vector<Vector<World>>();


	public ExecutionProgress lastResult;

	public I18n i18n = I18nFactory.getI18n(getClass(),"org.plm.i18n.Messages",Game.getInstance().getLocale(), I18nFactory.FALLBACK);

	public Exercise(Lesson lesson,String basename) {
		super(lesson,basename);
	}

	public void setupWorlds(World[] w, ArrayList<File> f) {
		currentWorld = new Vector<World>(w.length);
		initialWorld = new Vector<World>(w.length);
		answerWorld  = new Vector<World>(w.length);
		Vector<World> errorWorld   = new Vector<World>(w.length);
		for (int i=0; i<w.length; i++) {
			if (w[i] == null) 
				throw new RuntimeException("Broken exercise "+getId()+": world "+i+" is null!");
			currentWorld.add( w[i].copy() );
			initialWorld.add( w[i].copy() );
			answerWorld. add( w[i].copy() );
			if(f!=null) {
				for(int j = 0 ; j < f.size() ; j++) {
					errorWorld = new Vector<World>(w.length);
					errorWorld.add(w[i].copy());
					addError(errorWorld);
				}
			}
		}
	}
	
	public void addError(Vector<World> errorWorld) {
		commonErrors.add(errorWorld);
	}

	public abstract void run(List<Thread> runnerVect);	
	public abstract void runDemo(List<Thread> runnerVect);	

	public void check() {
		boolean pass = true;
		if (lastResult.outcome == ExecutionProgress.outcomeKind.PASS) {
			for (int i=0; i<currentWorld.size(); i++) {
				currentWorld.get(i).notifyWorldUpdatesListeners();

				lastResult.totalTests++;

				if (!currentWorld.get(i).winning(answerWorld.get(i))) {
					for(int j = 0 ; j < commonErrors.size() ; j++) {
						System.out.println(currentWorld.size()+" > "+commonErrors.size()+" - "+commonErrors.get(j).size());
						if(currentWorld.get(i).winning((commonErrors.get(j)).get(i))) {
							lastResult.executionError += "Nombre de fichiers leurre : "+commonErrors.size()+"\n\nYou just had to write \"forward();\" to win this exercise...\n\n";
							break;
						} else {
							System.out.println("Failed : "+j);
						}
					}
					String diff = answerWorld.get(i).diffTo(currentWorld.get(i));
					lastResult.executionError += i18n.tr("The world ''{0}'' differs",currentWorld.get(i).getName());
					if (diff != null) 
						lastResult.executionError += ":\n"+diff;
					lastResult.executionError += "\n";
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
		lastResult = new ExecutionProgress();

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
	public void compileAll(LogWriter out, StudentOrCorrection whatToCompile) throws PLMCompilerException {
		/* Do the compile (but only if the current language is Java or Scala: scripts are not compiled of course)
		 * Instead, scripting languages get the source code as text directly from the sourceFiles 
		 */
		Game.getProgrammingLanguage().compileExo(this, out, whatToCompile);
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
		return getSourceFilesList(lang).get(i);
	}

	public void newSource(ProgrammingLanguage lang, String name, String initialContent, String template,int offset,String correctionCtn,String errorCtn) {
		getSourceFilesList(lang).add(new SourceFileRevertable(name, initialContent, template, offset,correctionCtn,errorCtn));
	}

	public void mutateEntities(WorldKind kind, StudentOrCorrection whatToMutate, int nbError) {
		ProgrammingLanguage lang = Game.getProgrammingLanguage();

		Vector<World> worlds = null;
		if(nbError == -1) {
			switch (kind) {
			case INITIAL: worlds = initialWorld; break;
			case CURRENT: worlds = currentWorld; break;
			case ANSWER:  worlds = answerWorld;  break;
			default: throw new RuntimeException("kind is invalid: "+kind);
			}
		} else {
			if(kind==WorldKind.ERROR) {
				if(nbError != -1) {
					worlds = commonErrors.get(nbError);
				} 
			} else {
				throw new RuntimeException("kind is invalid: "+kind);
			}
		}

		/* Sanity check for broken lessons: the entity name must be a valid Java identifier */
		if (Game.getProgrammingLanguage().equals(Game.JAVA)) {
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
				current.setEntities( lang.mutateEntities(this, current.getEntities(), whatToMutate, nbError) );			
			}
		} catch (PLMCompilerException e) {
			lastResult = ExecutionProgress.newCompilationError(e.getLocalizedMessage());
		}
	}

	public Vector<World> getWorlds(WorldKind kind, int nbError) {
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
}

