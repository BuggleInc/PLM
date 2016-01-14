package plm.core.model.lesson;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

	public static int DEFAULT_NUMBER_OF_TRIES = 10;
	public static long DEFAULT_WAITING_TIME = 1000;
	
	private LogHandler logger;
	private I18n i18n;
	private List<Thread> runnerVect = new ArrayList<Thread>();
	private boolean executionStopped = false;

	private int maxNumberOfTries = DEFAULT_NUMBER_OF_TRIES;
	private long waitingTime = DEFAULT_WAITING_TIME;

	public ExerciseRunner(LogHandler logger, I18n i18n) {
		this.logger = logger;
		this.i18n = i18n;
	}

	public void setI18n(I18n i18n) {
		this.i18n = i18n;
	}

	public CompletableFuture<ExecutionProgress> run(Exercise exo, ProgrammingLanguage progLang, String code) {
		// FIXME: Clean this function
		// FIXME: Don't share lastResult with mutatesEntities() and runEntities()

		final CompletableFuture<ExecutionProgress> future = new CompletableFuture<>();

		Vector<World> currentWorlds = exo.getWorlds(WorldKind.CURRENT);
		ExecutionProgress lastResult = new ExecutionProgress(progLang, i18n);

		exo.reset();

		/*
		 *  Compilation time
		 */
		SourceFile sf = exo.getDefaultSourceFile(progLang);
		sf.setBody(code);
		try {
			mutateEntities(exo, sf, progLang, StudentOrCorrection.STUDENT, lastResult);
		} catch (PLMCompilerException e) {
			e.printStackTrace();
			return CompletableFuture.completedFuture(lastResult);
		}

		/*
		 *  Execution time
		 */
		ExecutorService mainExecutor = Executors.newCachedThreadPool();

		Runnable runCode = new Runnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {

				executionStopped = false;

				// Start entities in separate threads
				for(int i=0; i<currentWorlds.size(); i++) {
					World w = currentWorlds.get(i);
					runEntities(w, progLang, runnerVect, lastResult);
				}

				int numberOfTries = maxNumberOfTries;

				// Watch threads to timeout them if needed
				// Also watch if user asked to stop the execution
				while(!executionStopped && numberOfTries > 0 && runnerVect.size()>0) {
					Thread t = runnerVect.get(0);

					try {
						t.join(waitingTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(t.isAlive()) {
						// The task is not done yet
						numberOfTries--;
					} else  {
						numberOfTries = maxNumberOfTries;
						runnerVect.remove(0);
					}
				}

				/*
				 *  Assessment time
				 */
				if(executionStopped || numberOfTries == 0) {
					for(Thread t : runnerVect) {
						t.stop();
					}
					runnerVect.clear();
					if(executionStopped) {
						lastResult.setStopError();
						executionStopped = false;
					}
					else if(numberOfTries == 0) {
						lastResult.setTimeoutError();
					}
					future.complete(lastResult);
				} else {
					int i=0;
					while(lastResult.outcome == ExecutionProgress.outcomeKind.PASS && i<currentWorlds.size()) {
						World currentWorld = currentWorlds.get(i);
						World answerWorld = exo.getWorlds(WorldKind.ANSWER).get(i);
						checkWorld(currentWorld, answerWorld, progLang, lastResult);
						i++;
					}
					future.complete(lastResult);
				}
			}
		};

		mainExecutor.submit(runCode);

		return future;
	}

	public void runDemo(Exercise exo, ProgrammingLanguage progLang) {
		// FIXME: Factorize this code

		exo.reset();
		for(World w : exo.getWorlds(WorldKind.ANSWER)) {
			runEntities(w, progLang, runnerVect, null);
		}
		while(runnerVect.size()>0) {
			try {
				Thread t = runnerVect.get(0);
				t.join();
				runnerVect.remove(t);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void mutateEntities(Exercise exo, SourceFile sourceFile, ProgrammingLanguage progLang, StudentOrCorrection whatToCompile, ExecutionProgress progress) throws PLMCompilerException {
		progLang.compileExo(sourceFile, progress, whatToCompile, logger, i18n);

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

			// So that we can still stop it from the AWT Thread, even if an infinite loop occurs
			runner.setPriority(Thread.MIN_PRIORITY);
			runner.start();
			runnerVect.add(runner);
		}
	}

	private void checkWorld(World currentWorld, World answerWorld, ProgrammingLanguage progLang, ExecutionProgress progress) {
		progress.commonErrorText = "";
		progress.commonErrorID = -1;
		progress.totalTests++;

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
			String diff = answerWorld.diffTo(currentWorld, i18n, progLang);
			progress.executionError += i18n.tr("The world ''{0}'' differs",currentWorld.getName());
			if (diff != null) 
				progress.executionError += ":\n"+diff;
			progress.executionError += "\n------------------------------------------\n";
			progress.outcome = ExecutionProgress.outcomeKind.FAIL;
		} else {
			progress.passedTests++;
		}
	}

	public void stopExecution() {
		executionStopped = true;
	}

	public void setMaxNumberOfTries(int maxNumberOfTries) {
		this.maxNumberOfTries = maxNumberOfTries;
	}

	public void setWaitingTime(long waitingTime) {
		this.waitingTime = waitingTime;
	}
}