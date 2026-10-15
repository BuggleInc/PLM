package plm.core.model.lesson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.swing.JScrollPane;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import plm.core.PLMCompilerException;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.LogWriter;
import plm.core.model.session.EntityFileSegments;
import plm.core.model.session.SourceFile;
import plm.core.ui.JavaEditorPanel;
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

  public abstract RunOutcome run(List<Future<?>> runnerVect, ProgrammingLanguage lang, String executable) throws InterruptedException;
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

  public void check(RunOutcome result)
  {
    boolean pass = true;
    if (result.outcome == RunOutcome.kind.PASS) {
      for (int i = 0; i < currentWorld.size(); i++) {
        currentWorld.get(i).notifyWorldUpdatesListeners();

        result.totalTests++;

        if (!currentWorld.get(i).winning(answerWorld.get(i))) {
          String diff = answerWorld.get(i).diffTo(currentWorld.get(i), result.language);
          result.executionError += i18n.tr("The world ''{0}'' differs", currentWorld.get(i).getName());
          if (diff != null)
            result.executionError += ":\n" + diff;
          result.executionError += "\n";
          pass = false;
        } else {
          result.passedTests++;
        }
      }
      if (pass)
        result.outcome = RunOutcome.kind.PASS;
      else
        result.outcome = RunOutcome.kind.FAIL;
    }
  }
  /** Reset the current worlds to the state of the initial worlds */
  public void reset()
  {
    for (int i = 0; i < initialWorld.size(); i++)
      currentWorld.get(i).reset(initialWorld.get(i));
  }

  /**
   * All the steps of the "Run" button, shared by the GUI and the tests: resets the worlds, generates the template, compiles the
   * code of {@code whatToCompile}, runs it on every world, then checks the result. The worlds are neither reset nor checked in
   * creative mode.
   * @param runners receives the entity tasks, so that another thread can stop them
   * @param onCompiled called once the compilation succeeded, right before the execution
   * @return the outcome of the run, which is a compilation error if the compilation (or the source-level check) failed
   */
  public RunOutcome compileRunCheck(LogWriter out, StudentOrCorrection whatToCompile, ProgrammingLanguage lang, List<Future<?>> runners, Runnable onCompiled)
      throws InterruptedException
  {
    boolean creative = Game.getInstance().isCreativeEnabled();

    /* Reset */
    if (!creative)
      reset();

    /* Compilation, then source-level API gating. Every compilation error is reported here. */
    String executable;
    try {
      executable = lang.compileExo(this, out, whatToCompile);
      if (whatToCompile == StudentOrCorrection.STUDENT)
        verifySource(getEditorContent(lang));
    } catch (PLMCompilerException e) {
      System.err.println(Game.i18n.tr("Compilation error:"));
      System.err.println(e.getMessage());
      if (Game.getInstance().isDebugEnabled())
        for (String file : e.getClassNames())
          System.out.println("File involved in the compilation: " + file);
      RunOutcome error = RunOutcome.newCompilationError(lang, e.getMessage());
      if (out != null)
        out.log(error.compilationError); // display the same error as in the ExerciseFailedDialog
      return error;
    }

    onCompiled.run(); // Change the footer message in the GUI

    /* Execution */
    RunOutcome result = run(runners, lang, executable);

    /* Check */
    if (!creative)
      check(result);
    return result;
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

  /** What the student sees in the editor: what they wrote, or the initial body if they did not change anything */
  public String getEditorContent(ProgrammingLanguage lang)
  {
    String body = Game.getInstance().studentWork.getBody(this, lang);
    return body != null ? body : getSourceFile(lang, 0).getInitialBody();
  }

  /** Does not load the source file, which would parse the entity: this is how the saved code is restored at startup, for all exercises */
  public void setEditorContent(ProgrammingLanguage lang, String content) { Game.getInstance().studentWork.setBody(this, lang, content); }

  /** Puts back, in every language, the editor content that the student saw the first time */
  public void revertEditorContents() { Game.getInstance().studentWork.removeBodies(this); }

  /** The editor of this exercise */
  public JScrollPane getEditorPanel(ProgrammingLanguage lang) { return new JavaEditorPanel(this, lang); }

  /** The pieces of the program to compile: the correction's, or the student's wrapped around the current editor content */
  public EntityFileSegments getSegments(ProgrammingLanguage lang, SourceFile sf, StudentOrCorrection whatToCompile)
  {
    return whatToCompile == StudentOrCorrection.CORRECTION ? sf.getCorrectionSegments() : sf.getStudentSegments(getEditorContent(lang));
  }

  /** The source files of this language that were already loaded: never loads anything, unlike {@link #getSourceFilesList} */
  public synchronized List<SourceFile> getLoadedSourceFiles(ProgrammingLanguage lang) { return sourceFiles.getOrDefault(lang, List.of()); }

  /** Called right after the list of source files of this language got created, to fill it if this exercise knows how */
  protected void loadSourceFiles(ProgrammingLanguage lang) {}

  public void newSource(ProgrammingLanguage lang, SourceFile source)
  {
    if (!tabName.matches("[A-Za-z_][A-Za-z0-9_]*"))
      throw new IllegalArgumentException("The tab name of " + getId() + " must be an identifier, as it names files and keys the student's code: '" + tabName +
                                         "'");
    if (!source.getName().equals(tabName))
      throw new IllegalArgumentException("The source file of " + getId() + " is named '" + source.getName() + "' instead of '" + tabName +
                                         "', the name of its tab");
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
   * Serializes the randomized initial worlds in the .map format, by rank.
   * Reports and logs need them, as they cannot be rebuilt to reproduce what the student saw.
   */
  public Map<Integer, String> serializeRandomizedWorlds()
  {
    Map<Integer, String> res = new TreeMap<Integer, String>();
    for (int rank = 0; rank < initialWorld.size(); rank++)
      if (initialWorld.get(rank).isRandomized()) {
        StringWriter out = new StringWriter();
        try (BufferedWriter bw = new BufferedWriter(out)) {
          initialWorld.get(rank).writeToFile(bw);
        } catch (IOException e) {
          e.printStackTrace();
        }
        res.put(rank, out.toString());
      }
    return res;
  }

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
