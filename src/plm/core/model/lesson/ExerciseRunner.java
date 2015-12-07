package plm.core.model.lesson;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.xnap.commons.i18n.I18n;

import plm.core.PLMCompilerException;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.LogHandler;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.Exercise.WorldKind;
import plm.core.model.session.SourceFile;
import plm.universe.Entity;
import plm.universe.World;

public class ExerciseRunner {

	private LogHandler logger;
	private I18n i18n;
	private List<Thread> runnerVect = new ArrayList<Thread>();
	private ExecutionProgress lastResult;

	public ExerciseRunner(LogHandler logger, I18n i18n) {
		this.logger = logger;
		this.i18n = i18n;
	}

	public void setI18n(I18n i18n) {
		this.i18n = i18n;
	}

	public ExecutionProgress run(Exercise exo, ProgrammingLanguage progLang, String code) {
		// FIXME: Handle ExecutionProgress

		Vector<World> currentWorlds = exo.getWorlds(WorldKind.CURRENT);
		lastResult = new ExecutionProgress(progLang, i18n);

		exo.reset();

		/*
		 *  Compilation time
		 */
		SourceFile sf = exo.getDefaultSourceFile(progLang);
		sf.setBody(code);
		try {
			mutateEntities(exo, sf, progLang, StudentOrCorrection.STUDENT);
		} catch (PLMCompilerException e) {
			e.printStackTrace();
			return lastResult;
		}

		/*
		 * Execution time
		 */
		for(World w : currentWorlds) {
			runEntities(w, progLang, runnerVect, lastResult);
		}
		while (runnerVect.size()>0) {
			try {
				Thread t = runnerVect.get(0); // leave the thread into the set so that it remains interruptible
				t.join();
				runnerVect.remove(t);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if(lastResult.outcome == ExecutionProgress.outcomeKind.FAIL) {
			return lastResult;
		}

		/*
		 * Check time
		 */
		int i=0;
		while(i<currentWorlds.size()) {
			World currentWorld = currentWorlds.get(i);
			World answerWorld = exo.getWorlds(WorldKind.ANSWER).get(i);
			checkWorld(currentWorld, answerWorld);
			i++;
		}

		return lastResult;
	}

	public void runDemo(Exercise exo, ProgrammingLanguage progLang) {
		// FIXME: Factorize this code

		exo.reset();
		for(World w : exo.getWorlds(WorldKind.ANSWER)) {
			runEntities(w, progLang, runnerVect, null);
		}
		while (runnerVect.size()>0) {
			try {
				Thread t = runnerVect.get(0);
				t.join();
				runnerVect.remove(t);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void mutateEntities(Exercise exo, SourceFile sourceFile, ProgrammingLanguage progLang, StudentOrCorrection whatToCompile) throws PLMCompilerException {
		progLang.compileExo(sourceFile, lastResult, whatToCompile, logger, i18n);

		WorldKind worldKind = null;
		switch(whatToCompile) {
		case STUDENT:
			worldKind = WorldKind.CURRENT;
			break;
		case CORRECTION:
			worldKind = WorldKind.ANSWER;
			break;
		default:
			break;
		}

		for(World w : exo.getWorlds(worldKind)) {
			List<Entity> entities = w.getEntities();
			List<Entity> newEntities = progLang.mutateEntities(exo.getId(), sourceFile, whatToCompile, entities);
			w.setEntities(newEntities);
		}
	}

	private void runEntities(World w, ProgrammingLanguage progLang, List<Thread> runnerVect, final ExecutionProgress progress) {
		for (final Entity entity : w.getEntities()) {
			Thread runner = new Thread(new Runnable() {
				public void run() {
					progLang.runEntity(entity, progress, i18n);
				}
			});

			Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {
			    public void uncaughtException(Thread th, Throwable ex) {
			        
			    	if(ex instanceof ThreadDeath) {
			    		String msg = "You interrupted the execution, did you fall into an infinite loop ?\n"
			    				+ "Your program must stop by itself to successfully pass the exercise.\n";
				        progress.setExecutionError(i18n.tr(msg));
				        progress.outcome = ExecutionProgress.outcomeKind.FAIL;
			    	}
			    }
			};

			// So that we can still stop it from the AWT Thread, even if an infinite loop occurs
			runner.setPriority(Thread.MIN_PRIORITY);
			runner.setUncaughtExceptionHandler(h);
			runner.start();
			runnerVect.add(runner);
		}
	}

	private void checkWorld(World currentWorld, World answerWorld) {
		lastResult.commonErrorText = "";
		lastResult.commonErrorID = -1;
		lastResult.totalTests++;

		if (!currentWorld.winning(answerWorld)) {
			// FIXME: Enable again commonErrors
			/*
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
			*/
			String diff = answerWorld.diffTo(currentWorld, i18n);
			lastResult.executionError += i18n.tr("The world ''{0}'' differs",currentWorld.getName());
			if (diff != null) 
				lastResult.executionError += ":\n"+diff;
			lastResult.executionError += "\n------------------------------------------\n";
			lastResult.outcome = ExecutionProgress.outcomeKind.FAIL;
		} else {
			lastResult.passedTests++;
		}
	}
}