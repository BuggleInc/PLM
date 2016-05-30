package plm.test.simple.test;

import static org.junit.Assert.fail;

import java.util.Locale;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import plm.core.PLMCompilerException;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.Exercise.WorldKind;
import plm.core.model.lesson.Lesson;
import plm.test.simple.SimpleExercise;
import plm.universe.Entity;
import plm.universe.World;

public abstract class SimpleExerciseTest {
	
	protected ProgrammingLanguage pl;
	protected Lesson l;
	protected SimpleExercise exo;
	
	public Game game;
	
	public SimpleExerciseTest() {}
	
	public SimpleExerciseTest(Game game, ProgrammingLanguage pl) {
		this.pl = pl;
		
		this.game = game;
		
		game.getProgressSpyListeners().clear(); // disable all progress spies (git, etc)
		game.removeSessionKit();
		game.setBatchExecution();
		
		if(!game.isDebugEnabled()) {
			game.switchDebug();
		}
		game.addLesson("plm.test.simple");
		game.switchLesson("plm.test.simple", true);
		exo = (SimpleExercise) game.getCurrentLesson().getCurrentExercise();
		game.setProgramingLanguage(pl);
		game.setLocale(new Locale("en"));
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testSolutionShouldCompil() {
		try {
			exo.compileAll(null, StudentOrCorrection.CORRECTION);
		} catch (PLMCompilerException e) {
			e.printStackTrace();
		}
		if(exo.lastResult.compilationError!=null && !exo.lastResult.compilationError.equals("")) {
			fail(getClass().getName().replace("Test", "Entity") +" should compile and not throw the following error:\n"+exo.lastResult.compilationError);
		}
	}
		
	@Test
	public abstract void testSolutionShouldExecuteProperly() throws PLMCompilerException;
	
	@Test
	public abstract void testSolutionShouldPass() throws PLMCompilerException;
	
	@Test
	public void testOutOfBoundsErrorRisingCodeShouldNotExecuteProperly() throws PLMCompilerException {
		exo.getSourceFile(pl, 0).setBody(generateOutOfBoundsErrorCode());
		exo.compileAll(null, StudentOrCorrection.STUDENT);
		exo.setNbError(-1);
		exo.mutateEntities(WorldKind.CURRENT, StudentOrCorrection.STUDENT);
		
		for (World w : exo.getWorlds(WorldKind.CURRENT)) {
			for (Entity ent: w.getEntities()) {
				pl.runEntity(ent,exo.lastResult, getGame().i18n);
			}
		}
		
		if(exo.lastResult.executionError==null || exo.lastResult.executionError.equals("")) {
			fail(getClass().getName().replace("Test", "Entity") +" should not execute properly but throw an error...\n");
		}
	}
	
	@Test
	public void testNullPointerErrorRisingCodeShouldNotExecuteProperly() throws PLMCompilerException {
		exo.getSourceFile(pl, 0).setBody(generateNullPointerErrorCode());
		exo.compileAll(null, StudentOrCorrection.STUDENT);
		exo.setNbError(-1);
		exo.mutateEntities(WorldKind.CURRENT, StudentOrCorrection.STUDENT);
		
		for (World w : exo.getWorlds(WorldKind.CURRENT)) {
			for (Entity ent: w.getEntities()) {
				pl.runEntity(ent,exo.lastResult, getGame().i18n);
			}
		}
		
		if(exo.lastResult.executionError==null || exo.lastResult.executionError.equals("")) {
			fail(getClass().getName().replace("Test", "Entity") +" should not execute properly but throw an error...\n");
		}
	}
	@Test
	public void testExceptionRisingCodeShouldNotExecuteProperly() throws PLMCompilerException {
		exo.getSourceFile(pl, 0).setBody(generateExceptionRaisingCode());
		exo.compileAll(null, StudentOrCorrection.STUDENT);
		exo.setNbError(-1);
		exo.mutateEntities(WorldKind.CURRENT, StudentOrCorrection.STUDENT);
		
		for (World w : exo.getWorlds(WorldKind.CURRENT)) {
			for (Entity ent: w.getEntities()) {
				pl.runEntity(ent,exo.lastResult, getGame().i18n);
			}
		}
		
		if(exo.lastResult.executionError==null || exo.lastResult.executionError.equals("")) {
			fail(getClass().getName().replace("Test", "Entity") +" should not execute properly but throw an exception...\n");
		}
	}
	
	@Test
	public void testWrongCodeShouldNotPass() throws PLMCompilerException {
		exo.getSourceFile(pl, 0).setBody(generateWrongCode());
		exo.compileAll(null, StudentOrCorrection.STUDENT);
		exo.setNbError(-1);
		exo.mutateEntities(WorldKind.CURRENT, StudentOrCorrection.STUDENT);
		
		for (World w : exo.getWorlds(WorldKind.CURRENT)) {
			for (Entity ent: w.getEntities()) {
				pl.runEntity(ent,exo.lastResult, getGame().i18n);
			}
		}
		
		exo.check();
		
		if(exo.lastResult.outcome == ExecutionProgress.outcomeKind.PASS) {
			fail(getClass().getName().replace("Test", "Entity") +" should not pass this exercise...");
		}
	}

	@Test
	public void testSolutionFollowedByErrorShouldNotPass() throws PLMCompilerException {
		exo.getSourceFile(pl, 0).setBody(generateSolutionFollowedByError());
		exo.compileAll(null, StudentOrCorrection.STUDENT);
		exo.setNbError(-1);
		exo.mutateEntities(WorldKind.CURRENT, StudentOrCorrection.STUDENT);
		
		for (World w : exo.getWorlds(WorldKind.CURRENT)) {
			for (Entity ent: w.getEntities()) {
				pl.runEntity(ent,exo.lastResult, getGame().i18n);
			}
		}
		
		exo.check();
		
		if(exo.lastResult.outcome == ExecutionProgress.outcomeKind.PASS) {
			fail(getClass().getName().replace("Test", "Entity") +" should not pass this exercise...");
		}
	}
	
	// Used to generate compilation error for each programming languages tested
	public abstract String generateSyntaxErrorCode();
	public abstract String generateVariableErrorCode();
	
	// Used to generate execution error for each programming languages tested
	public abstract String generateNullPointerErrorCode();
	public abstract String generateOutOfBoundsErrorCode();
	public abstract String generateExceptionRaisingCode();

	// Used to generate a code throwing no errors but not passing the exercise
	public abstract String generateWrongCode();
	
	public abstract String generateSolutionFollowedByError();
	
	public Game getGame() {
		return game;
	}
}
