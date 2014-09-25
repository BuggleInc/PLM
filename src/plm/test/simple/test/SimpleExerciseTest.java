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
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.Exercise.WorldKind;
import plm.core.model.lesson.Lesson;
import plm.core.utils.FileUtils;
import plm.test.simple.Main;
import plm.test.simple.SimpleExercise;
import plm.universe.Entity;
import plm.universe.World;

public abstract class SimpleExerciseTest {
	
	protected ProgrammingLanguage pl;
	protected Lesson l;
	protected SimpleExercise exo;
	
	public SimpleExerciseTest(ProgrammingLanguage pl) {
		this.pl = pl;
		
		FileUtils.setLocale(new Locale("en"));
		Game g = Game.getInstance();
		g.getProgressSpyListeners().clear(); // disable all progress spies (git, etc)
		g.removeSessionKit();
		g.setBatchExecution();
		if(!g.isDebugEnabled()) {
			g.switchDebug();
		}
		g.setProgramingLanguage(pl);
		
		l = new Main();
		exo = (SimpleExercise) l.getExercise("SimpleExercise");

		l.setCurrentExercise(exo);
		g.setCurrentLesson(l);
		g.setProgramingLanguage(pl);
		g.setLocale(new Locale("en"));
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		exo.reset();
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
	
	// FIXME: 	Currently always passing since the execution errors
	//			are signaled using the compilationError field
	@Test
	public void testSolutionShouldExecuteProperly() throws PLMCompilerException {
		exo.compileAll(null, StudentOrCorrection.CORRECTION);
		exo.mutateEntities(WorldKind.CURRENT, StudentOrCorrection.STUDENT);
		
		for (World w : exo.getWorlds(WorldKind.CURRENT)) {
			for (Entity ent: w.getEntities()) {
				pl.runEntity(ent,exo.lastResult);
			}
		}
		
		if(exo.lastResult.executionError!=null && !exo.lastResult.executionError.equals("")) {
			fail(getClass().getName().replace("Test", "Entity") +" should execute properly and not throw the following error:\n"+exo.lastResult.executionError);
		}
	}
	
	// FIXME: 	Currently always failing since the execution errors
	//			are signaled using the compilationError field
	@Test
	public void testBadCodeShouldNotExecuteProperly() throws PLMCompilerException {
		exo.getSourceFile(pl, 0).setBody(generateRuntimeExceptionRisingCode());
		exo.compileAll(null, StudentOrCorrection.STUDENT);
		exo.mutateEntities(WorldKind.CURRENT, StudentOrCorrection.STUDENT);
		
		for (World w : exo.getWorlds(WorldKind.CURRENT)) {
			for (Entity ent: w.getEntities()) {
				pl.runEntity(ent,exo.lastResult);
			}
		}
		
		if(exo.lastResult.executionError==null || exo.lastResult.executionError.equals("")) {
			fail(getClass().getName().replace("Test", "Entity") +" should not execute properly but throw an error...\n");
		}
	}
	
	public abstract String generateRuntimeExceptionRisingCode();
	public abstract String generateCompilationExceptionRisingCode();
}
