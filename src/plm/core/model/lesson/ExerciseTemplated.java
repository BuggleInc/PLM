package plm.core.model.lesson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
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
  private final Set<ProgrammingLanguage> entityLanguages = new HashSet<>();

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
    final String id                = this.getId();
    final ProgrammingLanguage lang = Game.getInstance().getProgrammingLanguage(); /* captured now: the user may switch language meanwhile */
    Runnable task   = new Runnable() {
      @Override public void run()
      {
        try {
          doComputeAnswer();
        } catch (PLMEntityNotFound ex) {
          getLesson().setLoadingOutcomeState(LoadingOutcome.FAIL);
          throw ex; // Game.waitInitThreads() logs it (via the Future's ExecutionException)
        }
      }

      private void doComputeAnswer()
      {
        Game.getInstance().statusArgAdd(getClass().getSimpleName());
        boolean allFound = true;
        if (answerWorld.get(0).haveIO()) {
          if (Game.getProperty(Game.PROP_ANSWER_CACHE, "true", true).equalsIgnoreCase("true")) {
            Vector<World> newAnswer = new Vector<World>();
            int rank                = 0;
            for (World aw : answerWorld) {
              String name = worldFileName + "-answer" + (rank++);
              try {
                World nw = aw.readFromFile(name);
                nw.setAnswerWorld();
                newAnswer.add(nw);
              } catch (BrokenWorldFileException bwfe) {
                System.err.println(i18n.tr("World {0} is broken ({1}). Recompute all answer worlds.", name, bwfe.getLocalizedMessage()));
                allFound = false;
                break;
              } catch (FileNotFoundException fnf) {
                System.err.println(i18n.tr("Cache file {0} is missing. Recompute all answer worlds.", name, fnf.getLocalizedMessage()));
                allFound = false;
                break;
              } catch (IOException ioe) {
                System.err.println(i18n.tr("IO exception while reading world {0} ({1}). Recompute all answer worlds.", name, ioe.getLocalizedMessage()));
                allFound = false;
                break;
              }
            }
            if (allFound) {
              answerWorld = newAnswer;
              Game.getInstance().statusArgRemove(getClass().getSimpleName());
              return;
            }
          } else {
            System.out.println(
                i18n.tr("Recompute the answer of {0} despite the cache file, as requested by the property {1}.", worldFileName, Game.PROP_ANSWER_CACHE));
          }
        }

        /* I/O didn't work. We have to load the files manually */
        RunOutcome progress = new RunOutcome(lang);

        String path = null;
        try {
          path = lang.compileExo(ExerciseTemplated.this, Game.getInstance().getOutputWriter(), StudentOrCorrection.CORRECTION);
        } catch (PLMCompilerException e) {
          System.err.println("Severe error: the correction of exercise " + id + " cannot be compiled in " + lang.getLang() + ". Please go fix your PLM.");
          e.printStackTrace();
          Game.getInstance().setState(Game.GameState.COMPILATION_ENDED);
          Game.getInstance().setState(Game.GameState.EXECUTION_ENDED);
        }

        for (World aw : answerWorld) {
          for (Entity ent : aw.getEntities())
            lang.runEntity(ent, progress, path);
          aw.setAnswerWorld();
        }

        /* Try to write all files for next time */
        if (answerWorld.get(0).haveIO()) {
          int rank = 0;
          for (World aw : answerWorld) {
            String name = "src/" + worldFileName + "-answer" + (rank++);
            name        = name.replaceAll("\\.", "/") + ".map";
            if (new File(name).getParentFile().canWrite()) {
              try {
                aw.writeToFile(new File(name));
              } catch (Exception e) {
                System.err.println(i18n.tr("Error while writing answer world of {0}:", name));
                e.printStackTrace();
              }
            } else {
              System.err.println(i18n.tr("Cannot write answer world of {0}. Please check the permissions.", name));
            }
          }
        }
        Game.getInstance().statusArgRemove(getClass().getSimpleName());
      }
    };
    Game.addInitThread(task);
  }

  @Override public void run(List<Future<?>> runnerVect, ProgrammingLanguage lang, String executable) throws InterruptedException
  {
    runAll(WorldKind.CURRENT, runnerVect, lastResult, lang, executable);
  }

  @Override public void runDemo(List<Future<?>> runnerVect, ProgrammingLanguage lang) throws InterruptedException
  {
    RunOutcome ignored = new RunOutcome(lang);

    for (int i = 0; i < initialWorld.size(); i++) {
      answerWorld.get(i).reset(initialWorld.get(i));
      answerWorld.get(i).doDelay();
    }
    String executable;
    try {
      executable = lang.compileExo(this, Game.getInstance().getOutputWriter(), StudentOrCorrection.CORRECTION);
    } catch (PLMCompilerException e) {
      System.err.println("Severe error: the correction of exercise " + getId() + " cannot be compiled in " + lang.getLang() + ". Please go fix your PLM.");
      e.printStackTrace();
      return;
    }

    runAll(WorldKind.ANSWER, runnerVect, ignored, lang, executable);
  }
}
