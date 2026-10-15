package plm.core.model.lesson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.Future;
import plm.core.PLMCompilerException;
import plm.core.PLMEntityNotFound;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.lesson.Lesson.LoadingOutcome;
import plm.core.model.session.SourceFile;
import plm.core.utils.FileUtils;
import plm.universe.BrokenWorldFileException;
import plm.universe.Entity;
import plm.universe.World;

public abstract class ExerciseTemplated extends Exercise {

  protected String worldFileName = getClass().getCanonicalName(); /* Name of the save files */

  public ExerciseTemplated(Lesson lesson) { super(lesson, null); }
  public ExerciseTemplated(Lesson lesson, String basename) { super(lesson, basename); }

  /** The languages for which an entity file exists, to be loaded when first needed */
  private final Set<ProgrammingLanguage> entityLanguages = new LinkedHashSet<>(); // in the order of ProgrammingLanguageManager.langs

  @Override protected void loadSourceFiles(ProgrammingLanguage lang)
  {
    if (!entityLanguages.contains(lang))
      return;
    try {
      newSourceFromFile(lang, tabName, nameOfCorrectionEntity());
    } catch (NoSuchEntityException | PLMCompilerException e) {
      // Whatever the reason, this is a real authoring bug in the exercise, as the entity file did exist when the exercise got loaded
      throw new RuntimeException(Game.i18n.tr("Exercise {0} ({1}): the {2} entity is broken: {3}", getName(), getId(), lang, e.getMessage()), e);
    }
  }

  public void newSourceFromFile(ProgrammingLanguage lang, String name, String filename) throws NoSuchEntityException, PLMCompilerException
  {
    String shownFilename = filename.replaceAll("\\.", "/") + "." + lang.getExt();
    StringBuffer sb      = null;
    try {
      sb = FileUtils.readContentAsText(filename, lang.getExt(), false);
    } catch (IOException ex) {
      throw new NoSuchEntityException(Game.i18n.tr("Source file {0}.{1} not found.", filename.replaceAll("\\.", "/"), lang.getExt()));
    }

    SourceFile source = EntityTemplateParser.parse(sb.toString(), lang, name, shownFilename);
    if (source.getRemote() == null)
      throw new PLMCompilerException(Game.i18n.tr("{0}: cannot guess which RemoteXxx universe this entity belongs to. Please fix your entity.", shownFilename),
                                     null);
    newSource(lang, source);
  }

  protected final void setup(World w) { setup(new World[] {w}); }
  protected <W extends World> void setup(W[] ws)
  {
    boolean foundALanguage = false;

    /* Sanity check for broken lessons: every world must come with at least one entity */
    for (World w : ws)
      if (w.getEntities().isEmpty())
        throw new RuntimeException("Every world in every exercise must have at least one entity when calling setup(). Please fix your exercise.");

    setupWorlds(ws);

    for (ProgrammingLanguage lang : Game.getInstance().getProgrammingLanguageManager().langs) {
      if (FileUtils.exists(nameOfCorrectionEntity(), lang.getExt())) {
        entityLanguages.add(lang); // the entity itself is only loaded and parsed when needed, see loadSourceFiles()
        super.addProgLanguage(lang);
        foundALanguage = true;
        if (Game.getInstance().isDebugEnabled() && !Game.getInstance().isBatchExecution())
          System.out.println("Found suitable templating entity " + nameOfCorrectionEntity() + " in " + lang);
      } else {
        if (lang.isPython() || lang.isScala() || lang.isJava())
          System.out.println("No templating entity found: " + nameOfCorrectionEntity() + "." + lang.getExt());

        if (getProgLanguages().contains(lang))
          throw new RuntimeException(Game.i18n.tr("Exercise {0} is said to be compatible with language "
                                                      + "{1}, but there is no entity for this language: {2}",
                                                  getName(), lang, nameOfCorrectionEntity() + "." + lang.getExt()));
        /* Ok, this language does not work for this exercise but didn't promise anything. I can deal with
         * it */
      }
    }
    if (!foundALanguage)
      throw new RuntimeException(Game.i18n.tr("{0}: No entity found. You should fix your paths and settings.", getName()));

    computeAnswer();
  }

  protected void computeAnswer()
  {
    /* captured now, as the user may switch language meanwhile; falls back to the first language of this exercise if it lacks the current one */
    ProgrammingLanguage current = Game.getInstance().getProgrammingLanguage();
    ProgrammingLanguage java    = Game.getInstance().getProgrammingLanguageManager().JAVA;
    final ProgrammingLanguage lang;
    if (entityLanguages.contains(current))
      // Prefer not to switch the language
      lang = current;
    else if (entityLanguages.contains(java))
      // Prefer Java if we must switch, but only if it's possible
      lang = java;
    else {
      // Use the first language of that exercise if Java is not possible
      lang = entityLanguages.iterator().next();
    }

    Runnable task                  = new Runnable() {
      @Override public void run()
      {
        try {
          doComputeAnswer(lang);
        } catch (PLMEntityNotFound ex) {
          getLesson().setLoadingOutcomeState(LoadingOutcome.FAIL);
          throw ex; // Game.waitInitThreads() logs it (via the Future's ExecutionException)
        }
      }
    };
    Game.addInitThread(task);
  }

  /** Sets the answer worlds: loaded from the cache files when possible, computed by the correction entity otherwise */
  private void doComputeAnswer(ProgrammingLanguage lang)
  {
    Game.getInstance().statusArgAdd(getClass().getSimpleName());

    Vector<World> newAnswer   = new Vector<World>(answerWorld);
    Vector<Integer> toCompute = loadCachedAnswers(newAnswer);
    if (!toCompute.isEmpty())
      computeAnswers(toCompute, lang);
    answerWorld = newAnswer;

    Game.getInstance().statusArgRemove(getClass().getSimpleName());
  }

  /**
   * Replaces in answers the worlds that can be read from the cache files, and returns the ranks of the others (randomized, missing
   * or broken), that must be computed.
   */
  private Vector<Integer> loadCachedAnswers(Vector<World> answers)
  {
    boolean haveIO   = answerWorld.get(0).haveIO();
    boolean useCache = haveIO && Game.getProperty(Game.PROP_ANSWER_CACHE, "true", true).equalsIgnoreCase("true");
    if (haveIO && !useCache)
      System.out.println(
          i18n.tr("Recompute the answer of {0} despite the cache file, as requested by the property {1}.", worldFileName, Game.PROP_ANSWER_CACHE));

    Vector<Integer> toCompute = new Vector<Integer>();
    for (int rank = 0; rank < answerWorld.size(); rank++) {
      World aw = answerWorld.get(rank);
      if (!useCache || aw.isRandomized()) {
        toCompute.add(rank);
        continue;
      }
      String name = worldFileName + "-answer" + rank;
      try {
        World nw = aw.readFromFile(name);
        nw.setAnswerWorld();
        answers.set(rank, nw);
      } catch (BrokenWorldFileException bwfe) {
        System.err.println(i18n.tr("World {0} is broken ({1}). Recompute it.", name, bwfe.getLocalizedMessage()));
        toCompute.add(rank);
      } catch (FileNotFoundException fnf) {
        System.err.println(i18n.tr("Cache file {0} is missing. Recompute it.", name));
        toCompute.add(rank);
      } catch (IOException ioe) {
        System.err.println(i18n.tr("IO exception while reading world {0} ({1}). Recompute it.", name, ioe.getLocalizedMessage()));
        toCompute.add(rank);
      }
    }
    return toCompute;
  }

  /** Compiles the correction entity in that language. Reports the error and returns null if it fails */
  private String compileCorrection(ProgrammingLanguage lang)
  {
    try {
      return lang.compileExo(this, Game.getInstance().getOutputWriter(), StudentOrCorrection.CORRECTION);
    } catch (PLMCompilerException e) {
      System.err.println("Severe error: the correction of exercise " + getId() + " cannot be compiled in " + lang.getLang() + ". Please go fix your PLM.");
      e.printStackTrace();
      return null;
    }
  }

  /** Runs the correction entity on the answer worlds of the given ranks, and writes the cache files of the cacheable ones */
  private void computeAnswers(Vector<Integer> ranks, ProgrammingLanguage lang)
  {
    RunOutcome progress = new RunOutcome(lang);

    String path = compileCorrection(lang);
    if (path == null) {
      Game.getInstance().setState(Game.GameState.COMPILATION_ENDED);
      Game.getInstance().setState(Game.GameState.EXECUTION_ENDED);
      return;
    }

    for (int rank : ranks) {
      World aw = answerWorld.get(rank);
      for (Entity ent : aw.getEntities())
        lang.runEntity(ent, progress, path);
      aw.setAnswerWorld();

      if (aw.haveIO() && !aw.isRandomized())
        writeAnswerFile(rank);
    }
  }

  /** Writes the cache file of the answer world of that rank, for next time */
  private void writeAnswerFile(int rank)
  {
    String name = ("src/" + worldFileName + "-answer" + rank).replaceAll("\\.", "/") + ".map";
    if (!new File(name).getParentFile().canWrite()) {
      System.err.println(i18n.tr("Cannot write answer world of {0}. Please check the permissions.", name));
      return;
    }
    try {
      answerWorld.get(rank).writeToFile(new File(name));
    } catch (Exception e) {
      System.err.println(i18n.tr("Error while writing answer world of {0}:", name));
      e.printStackTrace();
    }
  }

  @Override public RunOutcome run(List<Future<?>> runnerVect, ProgrammingLanguage lang, String executable) throws InterruptedException
  {
    RunOutcome result = new RunOutcome(lang);
    runAll(WorldKind.CURRENT, runnerVect, result, lang, executable);
    return result;
  }

  @Override public void runDemo(List<Future<?>> runnerVect, ProgrammingLanguage lang) throws InterruptedException
  {
    RunOutcome ignored = new RunOutcome(lang);

    for (int i = 0; i < initialWorld.size(); i++) {
      answerWorld.get(i).reset(initialWorld.get(i));
      answerWorld.get(i).doDelay();
    }
    String executable = compileCorrection(lang);
    if (executable != null)
      runAll(WorldKind.ANSWER, runnerVect, ignored, lang, executable);
  }
}
