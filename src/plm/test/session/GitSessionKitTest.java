package plm.test.session;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import plm.core.lang.ProgrammingLanguageManager;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson;
import plm.core.model.session.GitSessionKit;
import plm.core.model.session.SessionDB;
import plm.core.utils.FileUtils;
import plm.test.simple.Main;
import plm.test.simple.SimpleExercise;

/** The session that was saved in the repository of the user is restored into the SessionDB, without loading the entities. */
public class GitSessionKitTest {
  @TempDir Path path;

  private ProgrammingLanguageManager langs;
  private SessionDB db;
  private Lesson lesson;
  private SimpleExercise exo;
  private Path repo;

  @BeforeEach public void setUp() throws IOException
  {
    FileUtils.setLocale(new Locale("en"));
    langs  = Game.getInstance().programmingLanguageManager;
    db     = Game.getInstance().studentWork;
    lesson = new Main();
    exo    = (SimpleExercise)((Main)lesson).getExercise("SimpleExercise");
    repo   = Files.createDirectories(path.resolve(Game.getInstance().getUsers().getCurrentUser().getUserUUIDasString()));
  }

  @AfterEach public void tearDown()
  {
    db.setPassed(exo, langs.JAVA, false);
    db.setPassed(exo, langs.PYTHON, false);
    exo.revertEditorContents();
  }

  @Test public void testLoadLessonRestoresTheSavedCodeAndTheSuccesses() throws IOException
  {
    Files.writeString(repo.resolve(exo.getId() + ".java.code"), "saved code");
    Files.writeString(repo.resolve(exo.getId() + ".java.DONE"), "");
    Files.writeString(repo.resolve(exo.getId() + ".py.code"), "code that was never passed");

    GitSessionKit kit = new GitSessionKit(Game.getInstance());
    kit.loadAll(path.toFile()); // tells the kit which repository is the one of the user
    kit.loadLesson(path.toFile(), lesson);

    Assertions.assertEquals("saved code", db.getBody(exo, langs.JAVA));
    Assertions.assertTrue(db.getPassed(exo, langs.JAVA));
    Assertions.assertEquals("code that was never passed", db.getBody(exo, langs.PYTHON));
    Assertions.assertFalse(db.getPassed(exo, langs.PYTHON));
    Assertions.assertNull(db.getBody(exo, langs.SCALA)); // never saved
    Assertions.assertTrue(exo.getLoadedSourceFiles(langs.JAVA).isEmpty(), "restoring the code must not parse the entity");
  }

  @Test public void testLoadAllReadsTheLessonSummaries() throws IOException
  {
    Files.writeString(repo.resolve("some.lesson.summary"), "{\"possibleJava\":3,\"passedJava\":1}");

    new GitSessionKit(Game.getInstance()).loadAll(path.toFile());

    Assertions.assertEquals(3, db.getPossibleExercises("some.lesson", langs.JAVA));
    Assertions.assertEquals(1, db.getPassedExercises("some.lesson", langs.JAVA));
  }
}
