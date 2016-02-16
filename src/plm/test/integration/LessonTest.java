package plm.test.integration;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import plm.core.log.LogHandler;

@RunWith(Parameterized.class)
public class LessonTest {
	public LessonTest() {}
	
	@Parameters
	static public Collection<Object[]> lessons() {
		List<Object[]> result = new LinkedList<Object[]>();
		// TODO: Re-implement me
		return result;
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
	
	// FIXME: Re-implement these tests in webPLM
	/*
	public Lesson loadLesson(String pl) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		// FIXME: Re-implement me
		return null;
	}
	
	@Test
	public void testJavaLesson() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Lesson lesson = loadLesson(Game.JAVA.getLang());
		assertTrue("An error arose while loading lesson "+lesson.getName()+"...", lesson.getLoadingOutcomeState() == LoadingOutcome.SUCCESS);
	}
	
	@Test
	public void testScalaLesson() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Lesson lesson = loadLesson(Game.SCALA.getLang());
		assertTrue("An error arose while loading lesson "+lesson.getName()+"...", lesson.getLoadingOutcomeState() == LoadingOutcome.SUCCESS);
	}
	
	@Test
	public void testPythonLesson() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Lesson lesson = loadLesson(Game.PYTHON.getLang());
		assertTrue("An error arose while loading lesson "+lesson.getName()+"...", lesson.getLoadingOutcomeState() == LoadingOutcome.SUCCESS);
	}
	*/
}