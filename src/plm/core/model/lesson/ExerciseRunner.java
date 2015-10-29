package plm.core.model.lesson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.xnap.commons.i18n.I18n;

import plm.core.PLMCompilerException;
import plm.core.lang.LangJava;
import plm.core.model.LogHandler;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.Exercise.WorldKind;
import plm.core.model.session.SourceFile;
import plm.core.model.session.TemplatedSourceFileFactory;
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

	public void run(Exercise exo, LangJava programmingLanguage, String code) {
		// FIXME: Handle ExecutionProgress

		Vector<World> currentWorlds = exo.getWorlds(WorldKind.CURRENT);

		Map<String, String> sources = new TreeMap<String, String>();
		TemplatedSourceFileFactory sourceFileFactory = new TemplatedSourceFileFactory(logger, i18n);
		SourceFile sf = sourceFileFactory.newSourceFromFile(exo.getId(), programmingLanguage, "");
		sf.setBody(code);
		String codeSource = sf.getCompilableContent(null, StudentOrCorrection.STUDENT);
		sources.put(exo.getId(), codeSource);

		lastResult = new ExecutionProgress(programmingLanguage);
		
		/*
		 *  Compilation time
		 */
		try {
			mutateEntities(programmingLanguage,currentWorlds, exo.getId(), sources);
		} catch (PLMCompilerException e) {
			lastResult.setCompilationError(e.getMessage());
			e.printStackTrace();
		}

		/*
		 * Execution time
		 */
		exo.reset();
		for(World w : currentWorlds) {
			runEntities(w, programmingLanguage, runnerVect, lastResult);
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

		/*
		 * Check time
		 */
		for(int i=0; i<currentWorlds.size(); i++) {
			World currentWorld = currentWorlds.get(i);
			World answerWorld = exo.getWorlds(WorldKind.ANSWER).get(i);
			checkWorld(currentWorld, answerWorld);
		}
	}
	
	public void runDemo(Exercise exo, LangJava programmingLanguage) {
		// FIXME: Factorize this code

		exo.reset();
		for(World w : exo.getWorlds(WorldKind.ANSWER)) {
			runEntities(w, programmingLanguage, runnerVect, null);
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

	public void mutateEntities(LangJava programmingLanguage, Vector<World> worlds, String newClassName, Map<String, String> sources) throws PLMCompilerException {
		programmingLanguage.compileExo(sources, logger, i18n);
		for(World w : worlds) {
			List<Entity> entities = w.getEntities();
			List<Entity> newEntities = programmingLanguage.mutateEntities(newClassName, entities);
			w.setEntities(newEntities);
		}
	}

	private void runEntities(World w, LangJava programmingLanguage, List<Thread> runnerVect, final ExecutionProgress progress) {
		for (final Entity entity : w.getEntities()) {
			Thread runner = new Thread(new Runnable() {
				public void run() {
					programmingLanguage.runEntity(entity, progress, i18n);
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
		boolean pass = true;
		lastResult.commonErrorText = "";
		lastResult.commonErrorID = -1;
		if (lastResult.outcome == ExecutionProgress.outcomeKind.PASS) {
			currentWorld.notifyWorldUpdatesListeners();

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
				String diff = answerWorld.diffTo(currentWorld);
				lastResult.executionError += i18n.tr("The world ''{0}'' differs",currentWorld.getName());
				if (diff != null) 
					lastResult.executionError += ":\n"+diff;
				lastResult.executionError += "\n------------------------------------------\n";
				pass = false;
			} else {
				lastResult.passedTests++;
			}
			if (pass)
				lastResult.outcome = ExecutionProgress.outcomeKind.PASS;
			else 
				lastResult.outcome = ExecutionProgress.outcomeKind.FAIL;
		}
	}
}