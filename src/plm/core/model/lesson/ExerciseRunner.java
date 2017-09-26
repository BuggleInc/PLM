package plm.core.model.lesson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import plm.core.PLMCompilerException;
import plm.core.PLMEntityNotFound;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.I18nManager;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.Exercise.WorldKind;
import plm.core.model.session.SourceFile;
import plm.universe.Entity;
import plm.universe.World;

public class ExerciseRunner {

	private Locale locale;

	final long timeoutMilli = 3000;

	public ExerciseRunner(Locale locale) {
		this.locale = locale;
	}

	public void setI18n(Locale locale) {
		this.locale = locale;
	}

	public CompletableFuture<ExecutionProgress> run(final Exercise exo, final ProgrammingLanguage progLang, String code) {
		// FIXME: Clean this function
		// FIXME: Don't share lastResult with mutatesEntities() and runEntities()

		final CompletableFuture<ExecutionProgress> future = new CompletableFuture<>();

		final ExecutionProgress lastResult = new ExecutionProgress(progLang, locale);

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
		} catch (PLMEntityNotFound e) {
			e.printStackTrace();
			String msg = "Your program failed to start. One possible cause is some code laying around, out of any function or method. Please make sure that all your code (but your function declaraitons) is written in the run() method.";
			lastResult.setExecutionError(msg);
			return CompletableFuture.completedFuture(lastResult);
		}

		/*
		 *  Execution time
		 *  
		 *  build one future per world in charge of executing this, 
		 *  and then chain another one to compare if it's the correct solution
		 */
		int worldCount = exo.getWorlds(WorldKind.CURRENT).size(); 
		CompletableFuture<?>[] worldRunners = new CompletableFuture[worldCount];
		for(int i=0; i<worldCount; i++) {
			World currentWorld = exo.getWorlds(WorldKind.CURRENT).get(i);
			World answerWorld =  exo.getWorlds(WorldKind.ANSWER).get(i);
			worldRunners[i] = currentWorld.runEntities(progLang, lastResult, locale, timeoutMilli)
					            .thenRun( () -> checkWorld(currentWorld, answerWorld, progLang, lastResult) );
		}

		
		/*
		 *  After all worlds are done executing, we should return the ExecutionProgress as a result
		 */
		return CompletableFuture.allOf(worldRunners)
				.thenApply(unused -> lastResult);
	}

	public void runDemo(Exercise exo, ProgrammingLanguage progLang) {

		exo.reset();
		CompletableFuture<?>[] worldRunners =
					exo.getWorlds(WorldKind.ANSWER)
							.stream()
							.map(world -> world.runEntities(progLang, null, locale, timeoutMilli))
							.toArray(CompletableFuture[]::new);

		try {
			CompletableFuture.allOf(worldRunners).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}			
	}

	public void mutateEntities(Exercise exo, SourceFile sourceFile, ProgrammingLanguage progLang, StudentOrCorrection whatToCompile, ExecutionProgress progress) throws PLMCompilerException {
		progLang.compileExo(sourceFile, progress, whatToCompile, locale);

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

		String[] bits = exo.getId().split("\\.");
		String entityName = bits[bits.length - 1];

		for(World w : exo.getWorlds(worldKind)) {
			List<Entity> entities = w.getEntities();
			List<Entity> newEntities = progLang.mutateEntities(entityName, sourceFile, whatToCompile, entities);
			w.setEntities(newEntities);
		}
	}

	private void checkWorld(World currentWorld, World answerWorld, ProgrammingLanguage progLang, ExecutionProgress progress) {
		progress.commonErrorText = "";
		progress.commonErrorID = -1;
		progress.totalTests++;

		if (!answerWorld.winning(currentWorld)) {
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
			progress.executionError += I18nManager.getI18n(locale).tr("The world ''{0}'' differs",currentWorld.getName());
			if (diff != null) 
				progress.executionError += ":\n"+diff;
			progress.executionError += "\n------------------------------------------\n";
			progress.outcome = ExecutionProgress.outcomeKind.FAIL;
		} else {
			progress.passedTests++;
		}
	}
}
