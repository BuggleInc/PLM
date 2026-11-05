package plm.test.simple.test;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import plm.core.PLMCompilerException;
import plm.core.lang.CompilerMessage;
import plm.core.lang.ProgrammingLanguage;
import plm.core.lang.ProgrammingLanguage.Compiled;
import plm.core.model.BrokenProgrammingLanguageException;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.Lesson;
import plm.core.model.lesson.RunOutcome;
import plm.core.model.session.SourceFile;
import plm.core.model.tracking.GitTracker;
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
    g.setGitTracker(Mockito.mock(GitTracker.class)); // disable all progress trackers (git, etc)
    g.removeSessionKit();
    g.setBatchExecution();
    if (!g.isDebugEnabled()) {
      g.switchDebug();
    }
    g.setProgrammingLanguage(pl);

    l   = new Main();
    exo = (SimpleExercise)l.getExercise("SimpleExercise");

    l.setCurrentExercise(exo);
    g.setCurrentLesson(l);
    g.setProgrammingLanguage(pl);
    g.setLocale(new Locale("en"));
  }

  /** Puts the code in the only source file of the exercise */
  protected void setEditorContent(String code) { exo.setEditorContent(pl, code); }

  /** The first message of the compiler is an error (or a warning) surely located on that line of the editor */
  protected void assertMessageOnLine(int line, boolean warning, PLMCompilerException e)
  {
    int editorLines       = exo.getEditorContent(pl).split("\\R", -1).length;
    CompilerMessage first = CompilerMessage.parse(e.getMessage(), pl.editorFileName(exo), e.getLineShift(), editorLines).get(0);
    Assertions.assertEquals(new CompilerMessage(line, true, warning, first.text()), first, e.getMessage());
  }

  @Test public void testNewSourceRefusesATabThatIsNotAnIdentifier()
  {
    Exercise invalid = new SimpleExercise(exo.getLesson(), "Invalid") {
      {
        tabName = "bad name";
      }
    };
    Assertions.assertThrows(IllegalArgumentException.class, () -> invalid.newSource(pl, new SourceFile()));
  }

  @BeforeEach public void setUp() throws Exception { exo.reset(); }

  @Test public void testSolutionShouldCompil() throws PLMCompilerException { pl.compileExo(exo, null, StudentOrCorrection.CORRECTION); }

  @Test public abstract void testSolutionShouldExecuteProperly() throws PLMCompilerException, InterruptedException;

  @Test public abstract void testSolutionShouldPass() throws PLMCompilerException, InterruptedException;

  @Test public void testOutOfBoundsErrorRisingCodeShouldNotExecuteProperly() throws PLMCompilerException, InterruptedException
  {
    setEditorContent(generateOutOfBoundsErrorCode());
    Compiled compiled = pl.compileExo(exo, null, StudentOrCorrection.STUDENT);

    RunOutcome result = exo.run(new ArrayList<Future<?>>(), pl, compiled);

    if (result.executionError == null || result.executionError.equals("")) {
      Assertions.fail(getClass().getName().replace("Test", "Entity") + " should not execute properly but throw an error...\n");
    }
  }

  @Test public void testNullPointerErrorRisingCodeShouldNotExecuteProperly() throws PLMCompilerException, InterruptedException
  {
    setEditorContent(generateNullPointerErrorCode());
    Compiled compiled = pl.compileExo(exo, null, StudentOrCorrection.STUDENT);

    RunOutcome result = exo.run(new ArrayList<Future<?>>(), pl, compiled);

    if (result.executionError == null || result.executionError.equals("")) {
      Assertions.fail(getClass().getName().replace("Test", "Entity") + " should not execute properly but throw an error...\n");
    }
  }
  @Test public void testExceptionRisingCodeShouldNotExecuteProperly() throws PLMCompilerException, InterruptedException
  {
    setEditorContent(generateExceptionRaisingCode());
    Compiled compiled = pl.compileExo(exo, null, StudentOrCorrection.STUDENT);

    RunOutcome result = exo.run(new ArrayList<Future<?>>(), pl, compiled);

    if (result.executionError == null || result.executionError.equals("")) {
      Assertions.fail(getClass().getName().replace("Test", "Entity") + " should not execute properly but throw an exception...\n");
    }
  }

  @Test public void testWrongCodeShouldNotPass() throws PLMCompilerException, InterruptedException
  {
    setEditorContent(generateWrongCode());
    Compiled compiled = pl.compileExo(exo, null, StudentOrCorrection.STUDENT);

    RunOutcome result = exo.run(new ArrayList<Future<?>>(), pl, compiled);

    exo.check(result);

    if (result.isPassed())
      Assertions.fail(getClass().getName().replace("Test", "Entity") + (" should not pass this exercise..."));
  }

  @Test public void testSolutionFollowedByErrorShouldNotPass() throws PLMCompilerException, InterruptedException
  {
    setEditorContent(generateSolutionFollowedByError());
    Compiled compiled = pl.compileExo(exo, null, StudentOrCorrection.STUDENT);

    RunOutcome result = exo.run(new ArrayList<Future<?>>(), pl, compiled);

    exo.check(result);

    if (result.isPassed()) {
      Assertions.fail(getClass().getName().replace("Test", "Entity") + (" should not pass this "
                                                                        + "exercise..."));
    }
  }

  /** Without picky mode, a warning does not fail the compilation, and is located on its line of the editor */
  @Test public void testWarningsOfASuccessfulCompilation() throws InterruptedException
  {
    setEditorContent(generateWarningCode());
    RunOutcome outcome = exo.compileRunCheck(null, StudentOrCorrection.STUDENT, pl, false, new ArrayList<Future<?>>(), () -> {});
    Assertions.assertTrue(outcome.isPassed(), outcome.compilationError + outcome.executionError);
    Assertions.assertTrue(outcome.compilerMessages.stream().anyMatch(message -> message.line() == 2 && message.sure() && message.warning()),
                          outcome.compilerMessages.toString());
  }

  /** In picky mode, the same warning fails the compilation */
  @Test public void testWarningsFailThePickyCompilation() throws InterruptedException
  {
    setEditorContent(generateWarningCode());
    RunOutcome outcome = exo.compileRunCheck(null, StudentOrCorrection.STUDENT, pl, true, new ArrayList<Future<?>>(), () -> {});
    Assertions.assertTrue(outcome.isCompilationError(), outcome.executionError);
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

  // Used to generate a code passing the exercise, but with a warning of the compiler on its second line
  public abstract String generateWarningCode();

  public abstract String generateSolutionFollowedByError();
}
