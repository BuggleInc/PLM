package plm.test.session;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import plm.core.lang.ProgrammingLanguageManager;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.Lesson;
import plm.core.model.session.ISourceFileListener;
import plm.core.model.session.SessionDB;
import plm.core.utils.FileUtils;

/** What the student wrote is stored by exercise, language and source name, independently from each other. */
public class SessionDBTest {
  static ProgrammingLanguageManager langs;
  SessionDB db = new SessionDB();

  @BeforeAll static void initGame()
  {
    FileUtils.setLocale(new Locale("en"));
    langs = Game.getInstance().programmingLanguageManager;
  }

  static Lecture exercise(String id)
  {
    Lecture exo = Mockito.mock(Lecture.class);
    Mockito.when(exo.getId()).thenReturn(id);
    return exo;
  }

  @Test public void testNothingSetYet() { Assertions.assertNull(db.getBody(exercise("exo"), langs.JAVA)); }

  @Test public void testSetThenGetThenOverwrite()
  {
    Lecture exo = exercise("exo");
    db.setBody(exo, langs.JAVA, "first");
    Assertions.assertEquals("first", db.getBody(exo, langs.JAVA));
    db.setBody(exo, langs.JAVA, "second");
    Assertions.assertEquals("second", db.getBody(exo, langs.JAVA));
  }

  @Test public void testBodiesAreIndependent()
  {
    Lecture exo = exercise("exo");
    db.setBody(exo, langs.JAVA, "java");
    db.setBody(exo, langs.PYTHON, "python");
    db.setBody(exercise("exo2"), langs.JAVA, "exo2");

    Assertions.assertEquals("java", db.getBody(exo, langs.JAVA));
    Assertions.assertEquals("python", db.getBody(exo, langs.PYTHON));
    Assertions.assertEquals("exo2", db.getBody(exercise("exo2"), langs.JAVA));
    Assertions.assertNull(db.getBody(exo, langs.SCALA));
  }

  /** The tabs are not the business of the DB: the Python templating reads them the way python does */
  @Test public void testBodyIsStoredAsIs()
  {
    Lecture exo = exercise("exo");
    db.setBody(exo, langs.PYTHON, "if x:\n\ty = \"a\tb\"\n");
    Assertions.assertEquals("if x:\n\ty = \"a\tb\"\n", db.getBody(exo, langs.PYTHON));
  }

  @Test public void testRemoveBodiesForgetsOnlyThatExercise()
  {
    Lecture exo = exercise("exo");
    db.setBody(exo, langs.JAVA, "java");
    db.setBody(exo, langs.PYTHON, "python");
    db.setBody(exercise("exo2"), langs.JAVA, "exo2");

    db.removeBodies(exo);

    Assertions.assertNull(db.getBody(exo, langs.JAVA));
    Assertions.assertNull(db.getBody(exo, langs.PYTHON));
    Assertions.assertEquals("exo2", db.getBody(exercise("exo2"), langs.JAVA));
  }

  @Test public void testListenerIsToldOfSetsAndRemovals()
  {
    Lecture exo                  = exercise("exo");
    ISourceFileListener listener = Mockito.mock(ISourceFileListener.class);
    db.setBodyListener(exo, langs.JAVA, listener);

    db.setBody(exo, langs.JAVA, "code");
    db.setBody(exo, langs.PYTHON, "other language: not that listener");
    db.removeBodies(exo);
    Mockito.verify(listener, Mockito.times(2)).sourceFileContentHasChanged();

    db.removeBodyListener(exo, langs.JAVA);
    db.setBody(exo, langs.JAVA, "code");
    Mockito.verify(listener, Mockito.times(2)).sourceFileContentHasChanged();
  }

  static Lecture exerciseOf(String lessonId, String id)
  {
    Lesson lesson = Mockito.mock(Lesson.class);
    Mockito.when(lesson.getId()).thenReturn(lessonId);
    Lecture exo = exercise(id);
    Mockito.when(exo.getLesson()).thenReturn(lesson);
    return exo;
  }

  @Test public void testPassedExercisesAreCountedPerLessonAndLanguage()
  {
    Lecture exo1 = exerciseOf("lesson", "exo1");
    Lecture exo2 = exerciseOf("lesson", "exo2");
    Assertions.assertFalse(db.getPassed(exo1, langs.JAVA));

    db.setPassed(exo1, langs.JAVA, true);
    db.setPassed(exo1, langs.JAVA, true); // nothing changes
    db.setPassed(exo2, langs.JAVA, true);
    db.setPassed(exo1, langs.PYTHON, true);

    Assertions.assertTrue(db.getPassed(exo1, langs.JAVA));
    Assertions.assertFalse(db.getPassed(exo2, langs.PYTHON));
    Assertions.assertEquals(2, db.getPassedExercises("lesson", langs.JAVA));
    Assertions.assertEquals(1, db.getPassedExercises("lesson", langs.PYTHON));

    db.setPassed(exo1, langs.JAVA, false);
    db.setPassed(exo1, langs.JAVA, false); // nothing changes
    Assertions.assertFalse(db.getPassed(exo1, langs.JAVA));
    Assertions.assertEquals(1, db.getPassedExercises("lesson", langs.JAVA));
  }

  @Test public void testRegisterLessonCountsTheExercisesOfEachLanguage()
  {
    Lesson lesson = Mockito.mock(Lesson.class);
    Mockito.when(lesson.getId()).thenReturn("lesson");
    Exercise exo1 = Mockito.mock(Exercise.class);
    Exercise exo2 = Mockito.mock(Exercise.class);
    for (Exercise exo : List.of(exo1, exo2))
      Mockito.when(exo.getLesson()).thenReturn(lesson);
    Mockito.when(exo1.getId()).thenReturn("exo1");
    Mockito.when(exo1.getProgLanguages()).thenReturn(Set.of(langs.JAVA, langs.PYTHON));
    Mockito.when(exo2.getId()).thenReturn("exo2");
    Mockito.when(exo2.getProgLanguages()).thenReturn(Set.of(langs.JAVA));
    Mockito.when(lesson.exercises()).thenReturn(List.of(exo1, exo2));
    db.setPassed(exo1, langs.JAVA, true);

    db.registerLesson(lesson);

    Assertions.assertEquals(2, db.getPossibleExercises("lesson", langs.JAVA));
    Assertions.assertEquals(1, db.getPassedExercises("lesson", langs.JAVA));
    Assertions.assertEquals(1, db.getPossibleExercises("lesson", langs.PYTHON));
    Assertions.assertEquals(0, db.getPassedExercises("lesson", langs.PYTHON));
  }

  @Test public void testLessonSummaryRoundTrip()
  {
    db.lessonSummaryParse("lesson", "{\"possibleJava\":5,\"passedJava\":2,\"possiblePython\":4}");

    SessionDB other = new SessionDB();
    other.lessonSummaryParse("lesson", db.lessonSummary("lesson"));

    Assertions.assertEquals(5, other.getPossibleExercises("lesson", langs.JAVA));
    Assertions.assertEquals(2, other.getPassedExercises("lesson", langs.JAVA));
    Assertions.assertEquals(4, other.getPossibleExercises("lesson", langs.PYTHON));
    Assertions.assertEquals(0, other.getPassedExercises("lesson", langs.PYTHON));
  }

  @Test public void testSummaryOfUnknownLessonIsEmpty() { Assertions.assertEquals("{}", db.lessonSummary("unknown")); }

  @Test public void testInvalidSummaryIsIgnored()
  {
    db.lessonSummaryParse("lesson", "{\"possibleJava\":5}");
    db.lessonSummaryParse("lesson", "this is not json");
    Assertions.assertEquals(5, db.getPossibleExercises("lesson", langs.JAVA));
  }
}
