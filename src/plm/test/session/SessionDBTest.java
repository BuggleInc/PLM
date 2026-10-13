package plm.test.session;

import java.util.Locale;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import plm.core.lang.ProgrammingLanguageManager;
import plm.core.model.Game;
import plm.core.model.lesson.Lecture;
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

  @Test public void testNothingSetYet() { Assertions.assertNull(db.getBody(exercise("exo"), langs.JAVA, "Entity")); }

  @Test public void testSetThenGetThenOverwrite()
  {
    Lecture exo = exercise("exo");
    db.setBody(exo, langs.JAVA, "Entity", "first");
    Assertions.assertEquals("first", db.getBody(exo, langs.JAVA, "Entity"));
    db.setBody(exo, langs.JAVA, "Entity", "second");
    Assertions.assertEquals("second", db.getBody(exo, langs.JAVA, "Entity"));
  }

  @Test public void testBodiesAreIndependent()
  {
    Lecture exo = exercise("exo");
    db.setBody(exo, langs.JAVA, "Entity", "java");
    db.setBody(exo, langs.PYTHON, "Entity", "python");
    db.setBody(exo, langs.JAVA, "Other", "other");
    db.setBody(exercise("exo2"), langs.JAVA, "Entity", "exo2");

    Assertions.assertEquals("java", db.getBody(exo, langs.JAVA, "Entity"));
    Assertions.assertEquals("python", db.getBody(exo, langs.PYTHON, "Entity"));
    Assertions.assertEquals("other", db.getBody(exo, langs.JAVA, "Other"));
    Assertions.assertEquals("exo2", db.getBody(exercise("exo2"), langs.JAVA, "Entity"));
    Assertions.assertNull(db.getBody(exo, langs.SCALA, "Entity"));
  }

  /** The tabs are not the business of the DB: the Python templating reads them the way python does */
  @Test public void testBodyIsStoredAsIs()
  {
    Lecture exo = exercise("exo");
    db.setBody(exo, langs.PYTHON, "Entity", "if x:\n\ty = \"a\tb\"\n");
    Assertions.assertEquals("if x:\n\ty = \"a\tb\"\n", db.getBody(exo, langs.PYTHON, "Entity"));
  }

  @Test public void testRemoveBodiesForgetsOnlyThatExercise()
  {
    Lecture exo = exercise("exo");
    db.setBody(exo, langs.JAVA, "Entity", "java");
    db.setBody(exo, langs.PYTHON, "Entity", "python");
    db.setBody(exercise("exo2"), langs.JAVA, "Entity", "exo2");

    db.removeBodies(exo);

    Assertions.assertNull(db.getBody(exo, langs.JAVA, "Entity"));
    Assertions.assertNull(db.getBody(exo, langs.PYTHON, "Entity"));
    Assertions.assertEquals("exo2", db.getBody(exercise("exo2"), langs.JAVA, "Entity"));
  }

  @Test public void testListenerIsToldOfSetsAndRemovals()
  {
    Lecture exo                  = exercise("exo");
    ISourceFileListener listener = Mockito.mock(ISourceFileListener.class);
    db.setBodyListener(exo, langs.JAVA, "Entity", listener);

    db.setBody(exo, langs.JAVA, "Entity", "code");
    db.setBody(exo, langs.PYTHON, "Entity", "other language: not that listener");
    db.removeBodies(exo);
    Mockito.verify(listener, Mockito.times(2)).sourceFileContentHasChanged();

    db.removeBodyListener(exo, langs.JAVA, "Entity");
    db.setBody(exo, langs.JAVA, "Entity", "code");
    Mockito.verify(listener, Mockito.times(2)).sourceFileContentHasChanged();
  }
}
