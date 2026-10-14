package plm.test.simple.test;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import plm.core.PLMCompilerException;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.BrokenProgrammingLanguageException;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.Lesson;
import plm.core.model.lesson.RunOutcome;
import plm.core.model.session.SourceFile;
import plm.core.utils.FileUtils;
import plm.test.simple.Main;
import plm.test.simple.SimpleExercise;

public abstract class SimpleExerciseTest {

  protected ProgrammingLanguage pl;
  protected Lesson l;
  protected SimpleExercise exo;

  public SimpleExerciseTest(ProgrammingLanguage pl) throws BrokenProgrammingLanguageException
  {
    this.pl = pl;

    FileUtils.setLocale(new Locale("en"));
    Game g = Game.getInstance();
    g.getProgressTrackerListeners().clear(); // disable all progress trackers (git, etc)
    g.removeSessionKit();
    g.setBatchExecution();
    if (!g.isDebugEnabled()) {
      g.switchDebug();
    }
    g.setProgramingLanguage(pl);

    l   = new Main();
    exo = (SimpleExercise)l.getExercise("SimpleExercise");

    l.setCurrentExercise(exo);
    g.setCurrentLesson(l);
    g.setProgramingLanguage(pl);
    g.setLocale(new Locale("en"));
  }

  /** Puts the code in the only source file of the exercise */
  protected void setEditorContent(String code) { exo.setEditorContent(pl, exo.getSourceFile(pl, 0), code); }

  /** The source files are named after the tab, since the code of the student is saved under that name */
  @Test public void testNewSourceRefusesANameThatIsNotTheTab()
  {
    IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> exo.newSource(pl, new SourceFile("Other")));
    Assertions.assertTrue(e.getMessage().contains("SimpleExercise"), e.getMessage());
  }

  @Test public void testNewSourceRefusesATabThatIsNotAnIdentifier()
  {
    Exercise invalid = new SimpleExercise(exo.getLesson(), "Invalid") {
      {
        tabName = "bad name";
      }
    };
    Assertions.assertThrows(IllegalArgumentException.class, () -> invalid.newSource(pl, new SourceFile("bad name")));
  }

  @BeforeEach public void setUp() throws Exception { exo.reset(); }

  @Test public void testSolutionShouldCompil() throws PLMCompilerException { pl.compileExo(exo, null, StudentOrCorrection.CORRECTION); }

  @Test public abstract void testSolutionShouldExecuteProperly() throws PLMCompilerException, InterruptedException;

  @Test public abstract void testSolutionShouldPass() throws PLMCompilerException, InterruptedException;

  @Test public void testOutOfBoundsErrorRisingCodeShouldNotExecuteProperly() throws PLMCompilerException, InterruptedException
  {
    setEditorContent(generateOutOfBoundsErrorCode());
    String executable = pl.compileExo(exo, null, StudentOrCorrection.STUDENT);

    RunOutcome result = exo.run(new ArrayList<Future<?>>(), pl, executable);

    if (result.executionError == null || result.executionError.equals("")) {
      Assertions.fail(getClass().getName().replace("Test", "Entity") + " should not execute properly but throw an error...\n");
    }
  }

  @Test public void testNullPointerErrorRisingCodeShouldNotExecuteProperly() throws PLMCompilerException, InterruptedException
  {
    setEditorContent(generateNullPointerErrorCode());
    String executable = pl.compileExo(exo, null, StudentOrCorrection.STUDENT);

    RunOutcome result = exo.run(new ArrayList<Future<?>>(), pl, executable);

    if (result.executionError == null || result.executionError.equals("")) {
      Assertions.fail(getClass().getName().replace("Test", "Entity") + " should not execute properly but throw an error...\n");
    }
  }
  @Test public void testExceptionRisingCodeShouldNotExecuteProperly() throws PLMCompilerException, InterruptedException
  {
    setEditorContent(generateExceptionRaisingCode());
    String executable = pl.compileExo(exo, null, StudentOrCorrection.STUDENT);

    RunOutcome result = exo.run(new ArrayList<Future<?>>(), pl, executable);

    if (result.executionError == null || result.executionError.equals("")) {
      Assertions.fail(getClass().getName().replace("Test", "Entity") + " should not execute properly but throw an exception...\n");
    }
  }

  @Test public void testWrongCodeShouldNotPass() throws PLMCompilerException, InterruptedException
  {
    setEditorContent(generateWrongCode());
    String executable = pl.compileExo(exo, null, StudentOrCorrection.STUDENT);

    RunOutcome result = exo.run(new ArrayList<Future<?>>(), pl, executable);

    exo.check(result);

    if (result.outcome == RunOutcome.kind.PASS)
      Assertions.fail(getClass().getName().replace("Test", "Entity") + (" should not pass this exercise..."));
  }

  @Test public void testSolutionFollowedByErrorShouldNotPass() throws PLMCompilerException, InterruptedException
  {
    setEditorContent(generateSolutionFollowedByError());
    String executable = pl.compileExo(exo, null, StudentOrCorrection.STUDENT);

    RunOutcome result = exo.run(new ArrayList<Future<?>>(), pl, executable);

    exo.check(result);

    if (result.outcome == RunOutcome.kind.PASS) {
      Assertions.fail(getClass().getName().replace("Test", "Entity") + (" should not pass this "
                                                                        + "exercise..."));
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
}
