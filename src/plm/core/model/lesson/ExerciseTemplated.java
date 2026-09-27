package plm.core.model.lesson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import plm.core.PLMCompilerException;
import plm.core.PLMEntityNotFound;
import plm.core.lang.LanguageExtraction;
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

  public void newSourceFromFile(ProgrammingLanguage lang, String name, String filename) throws NoSuchEntityException, PLMCompilerException
  {
    newSourceFromFile(lang, name, filename, "");
  }
  public void newSourceFromFile(ProgrammingLanguage lang, String name, String filename, String patternString) throws NoSuchEntityException, PLMCompilerException
  {
    String shownFilename = filename.replaceAll("\\.", "/") + "." + lang.getExt();
    StringBuffer sb      = null;
    try {
      sb = FileUtils.readContentAsText(filename, lang.getExt(), false);
    } catch (IOException ex) {
      throw new NoSuchEntityException(Game.i18n.tr("Source file {0}.{1} not found.", filename.replaceAll("\\.", "/"), lang.getExt()));
    }

    TemplatedEntity parsed = EntityTemplateParser.parse(sb.toString(), lang, name, shownFilename, patternString);
    // Step 2 (see CONTRIBUTING.md, "From correction entity to compilable source: templating"), computed right here
    // rather than inside EntityTemplateParser.parse() -- see TemplatedEntity's javadoc.
    LanguageExtraction extraction = lang.extract(parsed.correction(), parsed.template(), parsed.imports(), parsed.helpers(), name);
    newSource(lang, name, new TemplatedEntity(parsed.initialContent(), parsed.template(), parsed.correction(), parsed.imports(), parsed.helpers(), extraction));
  }

  protected final void setup(World w) { setup(new World[] {w}); }
  protected <W extends World> void setup(W[] ws)
  {
    boolean foundALanguage = false;

    /* Sanity check for broken lessons: the tab name is used as the compiled class name and must be valid */
    for (String forbidden : new String[] {"'", "\""}) {
      Matcher matcher = Pattern.compile(forbidden).matcher(tabName);
      if (matcher.matches())
        throw new RuntimeException(tabName + " is not a valid java identifier (forbidden char: " + forbidden + "). "
                                   + "Your exercise uses a broken tabName.");
    }

    /* Sanity check for broken lessons: every world must come with at least one entity */
    for (World w : ws)
      if (w.getEntities().isEmpty())
        throw new RuntimeException("Every world in every exercise must have at least one entity when calling setup(). Please fix your exercise.");

    setupWorlds(ws);

    for (ProgrammingLanguage lang : Game.getInstance().getProgrammingLanguageManager().langs) {
      boolean foundThisLanguage = false;
      String searchedName       = null;
      for (SourceFile sf : getSourceFilesList(lang)) {
        if (searchedName == null) { // lazy initialization if there is any sourcefile to parse
          Pattern p = Pattern.compile(".*?([^.]*)$");
          Matcher m = p.matcher(nameOfCorrectionEntity());
          if (m.matches())
            searchedName = m.group(1);
          p            = Pattern.compile("Entity$");
          m            = p.matcher(searchedName);
          searchedName = m.replaceAll("");
        }
        if (Game.getInstance().isDebugEnabled())
          System.out.println("Saw " + sf.getName() + " in " + lang.getLang() + ", searched for " + searchedName + " or " + tabName +
                             " while checking for the need of creating a new tab");
        if (sf.getName().equals(searchedName) || sf.getName().equals(tabName))
          foundThisLanguage = true;
      }
      if (!foundThisLanguage) {
        try {
          newSourceFromFile(lang, tabName, nameOfCorrectionEntity());
          super.addProgLanguage(lang);
          foundALanguage = true;
          if (Game.getInstance().isDebugEnabled() && !Game.getInstance().isBatchExecution())
            System.out.println("Found suitable templating entity " + nameOfCorrectionEntity() + " in " + lang);

        } catch (NoSuchEntityException e) {
          if (lang.isPython() || lang.isScala() || lang.isJava())
            System.out.println("No templating entity found: " + e);

          if (getProgLanguages().contains(lang))
            throw new RuntimeException(Game.i18n.tr("Exercise {0} is said to be compatible with language "
                                                        + "{1}, but there is no entity for this language: {2}",
                                                    getName(), lang, e.toString()));
          /* Ok, this language does not work for this exercise but didn't promise anything. I can deal with
           * it */
        } catch (PLMCompilerException e) {
          // Unlike NoSuchEntityException above, this means the entity file exists but is malformed in a way that
          // breaks step 2's extraction (e.g. Scala's getCorrectedTemplate() rejecting an ill-formed template) --
          // always a real authoring bug in the exercise, not just "this language isn't offered", so always loud.
          throw new RuntimeException(Game.i18n.tr("Exercise {0} ({1}): the {2} entity is broken: {3}", getName(), getId(), lang, e.getMessage()), e);
        }
      } else {
        foundALanguage = true;
      }
    }
    if (!foundALanguage)
      throw new RuntimeException(Game.i18n.tr("{0}: No entity found. You should fix your paths and settings.", getName()));

    computeAnswer();
  }

  protected void computeAnswer()
  {
    final String id = this.getId();
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
        RunOutcome progress = new RunOutcome();

        String path = null;
        try {
          path = compile(Game.getInstance().getOutputWriter(), StudentOrCorrection.CORRECTION, Game.getInstance().getProgrammingLanguage());
        } catch (PLMCompilerException e) {
          System.err.println("Severe error: the correction of exercise " + id + " cannot be compiled in " +
                             Game.getInstance().getProgrammingLanguage().getLang() + ". Please go fix your PLM.");
          e.printStackTrace();
          Game.getInstance().setState(Game.GameState.COMPILATION_ENDED);
          Game.getInstance().setState(Game.GameState.EXECUTION_ENDED);
        }

        for (World aw : answerWorld) {
          for (Entity ent : aw.getEntities())
            Game.getInstance().getProgrammingLanguage().runEntity(ent, progress, path);
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
    if (lastResult == null)
      lastResult = new RunOutcome();

    runAll(WorldKind.CURRENT, runnerVect, lastResult, lang, executable);
  }

  @Override public void runDemo(List<Future<?>> runnerVect, ProgrammingLanguage lang) throws InterruptedException
  {
    RunOutcome ignored = new RunOutcome();

    for (int i = 0; i < initialWorld.size(); i++) {
      answerWorld.get(i).reset(initialWorld.get(i));
      answerWorld.get(i).doDelay();
    }
    String executable;
    try {
      executable = compile(Game.getInstance().getOutputWriter(), StudentOrCorrection.CORRECTION, lang);
    } catch (PLMCompilerException e) {
      System.err.println("Severe error: the correction of exercise " + getId() + " cannot be compiled in " + lang.getLang() + ". Please go fix your PLM.");
      e.printStackTrace();
      return;
    }

    runAll(WorldKind.ANSWER, runnerVect, ignored, lang, executable);
  }
}
