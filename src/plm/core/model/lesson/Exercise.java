package plm.core.model.lesson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import plm.core.PLMCompilerException;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.LogWriter;
import plm.core.model.session.SourceFile;
import plm.universe.World;

public abstract class Exercise extends Lecture {
  public static enum WorldKind { INITIAL, CURRENT, ANSWER }
  public static enum StudentOrCorrection { STUDENT, CORRECTION }

  protected String tabName = getClass().getSimpleName(); /* Name of the tab in the editor, also the name of the entity source file and of its saved code */

  public String nameOfCorrectionEntity()
  { // This will be redefined by TurtleArt to reduce the amount of code
    return getClass().getCanonicalName() + "Entity";
  }

  public String getTabName() { return tabName; }

  protected Map<ProgrammingLanguage, List<SourceFile>> sourceFiles = new HashMap<ProgrammingLanguage, List<SourceFile>>();

  protected Vector<World> currentWorld; /* the one displayed */
  protected Vector<World> initialWorld; /* the one used to reset the previous on each run */
  protected Vector<World> answerWorld;  /* the one current should look like to pass the test */

  public RunOutcome lastResult;

  public I18n i18n = I18nFactory.getI18n(getClass(), "org.plm.i18n.Messages", Game.getInstance().getLocale(), I18nFactory.FALLBACK);

  public Exercise(Lesson lesson, String basename) { super(lesson, basename); }

  public void setupWorlds(World[] w)
  {
    currentWorld = new Vector<World>(w.length);
    initialWorld = new Vector<World>(w.length);
    answerWorld  = new Vector<World>(w.length);
    for (int i = 0; i < w.length; i++) {
      if (w[i] == null)
        throw new RuntimeException("Broken exercise " + getId() + ": world " + i + " is null!");
      currentWorld.add(w[i].copy());
      initialWorld.add(w[i].copy());
      answerWorld.add(w[i].copy());
    }
  }

  public abstract void run(List<Future<?>> runnerVect, ProgrammingLanguage lang, String executable) throws InterruptedException;
  public abstract void runDemo(List<Future<?>> runnerVect, ProgrammingLanguage lang) throws InterruptedException;

  /**
   * Submit one task per entity of every world of {@code kind} to the shared bounded pool ({@link Game#submitEntityTask}),
   * add each to {@code runnerVect} (so {@code LessonRunner.stopAll()} can find and {@code cancel(true)} them from
   * another thread while this call is still blocked below), then wait for all of them to finish.
   *
   * A cancelled task (the user clicked "Stop") is not treated as a failure. Any other exception thrown by
   * {@code runEntity()} is unwrapped from its {@link ExecutionException} wrapper and re-thrown as-is, so that a
   * genuine bug surfaces to whoever called this (a test, or the GUI code that catches it broadly so it doesn't
   * take down the interface).
   */
  public void runAll(WorldKind kind, List<Future<?>> runnerVect, RunOutcome progress, ProgrammingLanguage lang, String executable) throws InterruptedException
  {
    for (World w : getWorlds(kind)) {
      w.doDelay();
      w.runEntities(runnerVect, progress, lang, executable);
    }

    for (Future<?> f : new ArrayList<Future<?>>(runnerVect)) {
      try {
        f.get();
      } catch (InterruptedException ie) {
        /* Interrupted while waiting (e.g. test timeout): cancel the runners so that they kill their student processes. */
        runnerVect.forEach(r -> r.cancel(true));
        throw ie;
      } catch (CancellationException ce) {
        /* Stopped on purpose (LessonRunner.stopAll()); not a failure. */
      } catch (ExecutionException ee) {
        Throwable cause = ee.getCause();
        if (cause instanceof RuntimeException)
          throw (RuntimeException)cause;
        throw new RuntimeException(cause);
      } finally {
        runnerVect.remove(f);
      }
    }
  }

  public void check()
  {
    boolean pass = true;
    if (lastResult.outcome == RunOutcome.kind.PASS) {
      for (int i = 0; i < currentWorld.size(); i++) {
        currentWorld.get(i).notifyWorldUpdatesListeners();

        lastResult.totalTests++;

        if (!currentWorld.get(i).winning(answerWorld.get(i))) {
          String diff = answerWorld.get(i).diffTo(currentWorld.get(i), lastResult.language);
          lastResult.executionError += i18n.tr("The world ''{0}'' differs", currentWorld.get(i).getName());
          if (diff != null)
            lastResult.executionError += ":\n" + diff;
          lastResult.executionError += "\n";
          pass = false;
        } else {
          lastResult.passedTests++;
        }
      }
      if (pass)
        lastResult.outcome = RunOutcome.kind.PASS;
      else
        lastResult.outcome = RunOutcome.kind.FAIL;
    }
  }
  /** Reset the current worlds to the state of the initial worlds */
  public void reset(ProgrammingLanguage lang)
  {
    lastResult = new RunOutcome(lang);

    for (int i = 0; i < initialWorld.size(); i++)
      currentWorld.get(i).reset(initialWorld.get(i));
  }

  /**
   * All the steps of the "Run" button, shared by the GUI and the tests: resets the worlds, compiles the code of {@code whatToCompile}, runs it
   * on every world, then checks the result. The worlds are neither reset nor checked in creative mode.
   * @param runners receives the entity tasks, so that another thread can stop them
   * @param onCompiled called once the compilation succeeded, right before the execution
   */
  public void compileRunCheck(LogWriter out, StudentOrCorrection whatToCompile, ProgrammingLanguage lang, List<Future<?>> runners, Runnable onCompiled)
      throws PLMCompilerException, InterruptedException
  {
    boolean creative = Game.getInstance().isCreativeEnabled();

    /* Reset */
    if (creative)
      lastResult = new RunOutcome(lang);
    else
      reset(lang);

    /* Compilation, then source-level API gating. Every compilation error is reported here, whichever language raised it */
    String executable;
    try {
      executable = lang.compileExo(this, out, whatToCompile);
      if (whatToCompile == StudentOrCorrection.STUDENT)
        verifySource(getSourceFile(lang, 0).getEditorContent());
    } catch (PLMCompilerException e) {
      System.err.println(Game.i18n.tr("Compilation error:"));
      System.err.println(e.getMessage());
      lastResult = RunOutcome.newCompilationError(lang, e.getMessage());
      if (out != null)
        out.log(lastResult.compilationError); // display the same error as in the ExerciseFailedDialog
      throw e;
    }

    onCompiled.run(); // Change the footer message in the GUI

    /* Execution */
    run(runners, lang, executable);

    /* Check */
    if (!creative)
      check();
  }

  /**
   * Extra verification of the student's code, run after a successful compilation and before the execution. Exercises override it to
   * refuse code that compiles but misses the pedagogical point. The message of the exception is shown as a compilation error.
   * @param code the content of the editor
   */
  protected void verifySource(String code) throws PLMCompilerException {}

  /** get the list of source files for a given language, or create (and load) it if not existent yet */
  public synchronized List<SourceFile> getSourceFilesList(ProgrammingLanguage lang)
  {
    List<SourceFile> res = sourceFiles.get(lang);
    if (res == null) {
      res = new ArrayList<SourceFile>();
      sourceFiles.put(lang, res);
      loadSourceFiles(lang);
    }
    if (res.size() > 1)
      throw new IllegalStateException("For now, it's impossible to have more than one entity script in a given exercise.");
    return res;
  }
  public int getSourceFileCount(ProgrammingLanguage lang) { return getSourceFilesList(lang).size(); }
  public SourceFile getSourceFile(ProgrammingLanguage lang, int i) { return getSourceFilesList(lang).get(i); }

  /** The source files of this language that were already loaded: never loads anything, unlike {@link #getSourceFilesList} */
  public synchronized List<SourceFile> getLoadedSourceFiles(ProgrammingLanguage lang) { return sourceFiles.getOrDefault(lang, List.of()); }

  /** Called right after the list of source files of this language got created, to fill it if this exercise knows how */
  protected void loadSourceFiles(ProgrammingLanguage lang) {}

  public void newSource(ProgrammingLanguage lang, SourceFile source)
  {
    getSourceFilesList(lang).add(source);
  }

  public Vector<World> getWorlds(WorldKind kind)
  {
    switch (kind) {
      case INITIAL:
        return initialWorld;
      case CURRENT:
        return currentWorld;
      case ANSWER:
        return answerWorld;
      default:
        throw new RuntimeException("Unhandled kind of world: " + kind);
    }
  }

  public int getWorldCount() { return this.initialWorld.size(); }

  /**
   * Returns the current world number index
   * @see #getAnswerOfWorld(int)
   */
  public World getWorld(int index)
  { // FIXME: rename to getCurrentWorld or KILLME
    return this.currentWorld.get(index);
  }

  public int indexOfWorld(World w)
  {
    int index = 0;
    do {
      if (this.currentWorld.get(index) == w)
        return index;
      index++;
    } while (index < this.currentWorld.size());

    throw new RuntimeException("World not found (please report this bug)");
  }

  public World getAnswerOfWorld(int index)
  { // FIXME: rename or KILLME
    return this.answerWorld.get(index);
  }

  public String toString() { return getName(); }

  /* setters and getter of the programming language that this exercise accepts */
  private Set<ProgrammingLanguage> progLanguages = new HashSet<ProgrammingLanguage>();

  public Set<ProgrammingLanguage> getProgLanguages() { return progLanguages; }
  protected void addProgLanguage(ProgrammingLanguage newL) { progLanguages.add(newL); }
}
