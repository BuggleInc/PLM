package plm.core.model.lesson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

  /**
   * Returns [start, end) of a method's own text (its declaration line through its brace-matched closing '}') within code,
   * searching for the given declaration keyword (e.g. "void run(" for Java/C) -- or null if that keyword doesn't appear
   * at all. For Python, whose blocks are indentation-delimited rather than brace-delimited, use
   * extractRunSpanIndentBased() below instead.
   *
   * This is offsets, not a substring, so callers can test containment against another region (e.g. a templated region)
   * without caring how many characters of incidental whitespace happen to separate two markers: what matters is whether
   * the method's real brace-matched span contains, is contained by, or is disjoint from that region.
   */
  public static int[] extractRunSpan(String code, String runKeyword)
  {
    int startRun = code.indexOf(runKeyword);
    if (startRun == -1)
      return null;

    int beginOfRunLine = code.substring(0, startRun).lastIndexOf('\n');
    if (beginOfRunLine == -1)
      beginOfRunLine = 0;

    int i       = code.indexOf('{', startRun) + 1;
    int bracket = 1;
    for (; i < code.length() && bracket > 0; i++) {
      if (code.charAt(i) == '{')
        bracket++;
      if (code.charAt(i) == '}')
        bracket--;
    }
    return new int[] {beginOfRunLine, i};
  }

  /** The method's own text (declaration through closing brace), or "" if runKeyword doesn't appear in code at all. */
  public static String extractRunFunction(String code, String runKeyword)
  {
    int[] span = extractRunSpan(code, runKeyword);
    return span == null ? "" : code.substring(span[0], span[1]);
  }

  /**
   * Python counterpart of extractRunSpan() above: Python has no braces, so a function's body is delimited by
   * indentation instead -- it ends at the first subsequent non-blank line indented no more than the "def" line itself
   * (or at end of file). Tabs and spaces are counted as plain characters (not expanded), which only matters if a single
   * file mixes the two inconsistently -- not a case seen in any exercise file so far.
   */
  public static int[] extractRunSpanIndentBased(String code, String runKeyword)
  {
    int startRun = code.indexOf(runKeyword);
    if (startRun == -1)
      return null;

    int beginOfRunLine = code.substring(0, startRun).lastIndexOf('\n');
    beginOfRunLine      = beginOfRunLine == -1 ? 0 : beginOfRunLine + 1;

    int defIndent = startRun - beginOfRunLine;

    int lineEnd = code.indexOf('\n', startRun);
    if (lineEnd == -1)
      lineEnd = code.length();

    int pos = lineEnd + 1;
    int end = lineEnd;
    while (pos <= code.length()) {
      int nextLineEnd = code.indexOf('\n', pos);
      if (nextLineEnd == -1)
        nextLineEnd = code.length();

      String line    = code.substring(pos, nextLineEnd);
      String trimmed = line.strip();

      if (!trimmed.isEmpty()) {
        int indent = 0;
        while (indent < line.length() && (line.charAt(indent) == ' ' || line.charAt(indent) == '\t'))
          indent++;
        if (indent <= defIndent)
          break;
      }

      end = nextLineEnd;
      if (nextLineEnd == code.length())
        break;
      pos = nextLineEnd + 1;
    }

    return new int[] {beginOfRunLine, end};
  }

  /** Indentation-based counterpart of extractRunFunction() above, for Python. */
  public static String extractRunFunctionIndentBased(String code, String runKeyword)
  {
    int[] span = extractRunSpanIndentBased(code, runKeyword);
    return span == null ? "" : code.substring(span[0], span[1]);
  }

  protected String worldFileName = getClass().getCanonicalName(); /* Name of the save files */

  public ExerciseTemplated(Lesson lesson) { super(lesson, null); }
  public ExerciseTemplated(Lesson lesson, String basename) { super(lesson, basename); }

  public void newSourceFromFile(ProgrammingLanguage lang, String name, String filename) throws NoSuchEntityException
  {
    newSourceFromFile(lang, name, filename, "");
  }
  public void newSourceFromFile(ProgrammingLanguage lang, String name, String filename, String patternString) throws NoSuchEntityException
  {

    String shownFilename = filename.replaceAll("\\.", "/") + "." + lang.getExt();
    StringBuffer sb      = null;
    try {
      sb = FileUtils.readContentAsText(filename, lang.getExt(), false);
    } catch (IOException ex) {
      throw new NoSuchEntityException(Game.i18n.tr("Source file {0}.{1} not found.", filename.replaceAll("\\.", "/"), lang.getExt()));
    }

    String content;
    if (lang.isJava()) {
      /* Remove line comments since at some point, we put everything on one line only,
       * so this would comment the end of the template and break everything */
      Pattern lineCommentPattern = Pattern.compile("//.*$", Pattern.MULTILINE);
      Matcher lineCommentMatcher = lineCommentPattern.matcher(sb.toString());
      content                    = lineCommentMatcher.replaceAll("");
    } else {
      content = sb.toString();
    }

    /* Extract the template, the initial content and the solution out of the file */
    int state                 = 0;
    int savedState            = 0;
    StringBuffer head         = new StringBuffer(); /* before the template (state 0) */
    StringBuffer templateHead = new StringBuffer(); /* in template before solution (state 1) */
    StringBuffer solution     = new StringBuffer(); /* the solution (state 2) */
    StringBuffer templateTail = new StringBuffer(); /* in template after solution (state 3) */
    StringBuffer tail =
        new StringBuffer("\n");                            /* after the template (state 4)
                                                            *   This contains a preliminar \n to help python understanding that the following is not in the same block.
                                                            *   Not doing Without it, we would have issues if the student puts some empty lines with the indentation marker at tail
                                                            */
    StringBuffer skel                = new StringBuffer(); /* within BEGIN/END SKEL */
    StringBuffer correction          = new StringBuffer(); /* the unchanged content, but the package and className modification */
    boolean containsLinePreprocessor = false;

    boolean seenTemplate = false; // whether B/E SOLUTION seems included within B/E TEMPLATE
    for (String line : content.split("\n")) {
      // if (this.debug)
      //	System.out.println(state+"->"+line);

      switch (state) {
        case 0: /* initial content */
          if (line.contains("class ")) {
            String modified = line.replaceAll("class \\S*", "class " + name);
            head.append(modified);
            correction.append(modified + "\n");
          } else if (line.contains("package")) {
            head.append("$package \n");
            correction.append("$package \n");
          } else if (line.contains("#line") && lang.isC()) {
            containsLinePreprocessor = true;
            head.append(line + "\n");
          } else if (line.contains("BEGIN TEMPLATE")) {
            if (!containsLinePreprocessor && lang.isC()) {
              head.append("#line 1 \"" + name + ".c\" \n");
              containsLinePreprocessor = true;
            }
            correction.append(line + "\n");
            seenTemplate = true;
            state        = 1;
          } else if (line.contains("BEGIN SOLUTION")) {
            if (!containsLinePreprocessor && lang.isC()) {
              head.append("#line 1 \"" + name + ".c\" \n");
              containsLinePreprocessor = true;
            }
            correction.append(line + "\n");
            state = 2;
          } else if (line.contains("BEGIN SKEL")) {
            correction.append(line + "\n");
            savedState = state;
            state      = 6;
          } else {
            correction.append(line + "\n");
            head.append(line + "\n");
          }
          break;
        case 1: /* template head */
          correction.append(line + "\n");
          if (line.contains("BEGIN TEMPLATE")) {
            System.out.println(i18n.tr("{0}: BEGIN TEMPLATE within the template. Please fix your entity.", shownFilename));
            state = 4;
          } else if (line.contains("public class ")) {
            templateHead.append(line.replaceAll("public class \\S*", "public class " + name) + "\n");
          } else if (line.contains("END TEMPLATE")) {
            state = 4;
          } else if (line.contains("BEGIN SOLUTION")) {
            state = 2;
          } else if (line.contains("BEGIN HIDDEN")) {
            savedState = 1;
            state      = 5;
          } else if (line.contains("BEGIN SKEL")) {
            savedState = state;
            state      = 6;
          } else {
            templateHead.append(line + "\n");
          }
          break;
        case 2: /* solution */
          correction.append(line + "\n");
          if (line.contains("END TEMPLATE")) {
            System.out.println(i18n.tr("{0}: BEGIN SOLUTION is closed with END TEMPLATE. Please fix your entity.", shownFilename));
            state = 4;
          } else if (line.contains("END SOLUTION")) {
            if (seenTemplate)
              state = 3;
            else
              state = 4; // Jump directly to end of template
          } else if (line.contains("BEGIN SKEL")) {
            savedState = state;
            state      = 6;
          } else {
            solution.append(line + "\n");
          }
          break;
        case 3: /* template tail */
          correction.append(line + "\n");
          if (line.contains("END TEMPLATE")) {
            if (!seenTemplate)
              System.out.println(i18n.tr("{0}: END TEMPLATE with no matching BEGIN TEMPLATE. Please fix your entity.", shownFilename));

            state = 4;
          } else if (line.contains("BEGIN SOLUTION")) {
            throw new RuntimeException(i18n.tr("{0}: Begin solution in template tail. Change it to BEGIN HIDDEN.", shownFilename));
          } else if (line.contains("BEGIN SKEL")) {
            savedState = state;
            state      = 6;
          } else if (line.contains("BEGIN HIDDEN")) {
            savedState = 3;
            state      = 5;
          } else {
            templateTail.append(line + "\n");
          }
          break;
        case 4: /* end of file */
          correction.append(line + "\n");
          if (line.contains("BEGIN SKEL")) {
            savedState = state;
            state      = 6;

          } else {
            if (line.contains("END TEMPLATE"))
              if (!seenTemplate)
                System.out.println(i18n.tr("{0}: END TEMPLATE with no matching BEGIN TEMPLATE. Please fix your entity.", shownFilename));

            tail.append(line + "\n");
          }
          break;
        case 5: /* Hidden but not bodied */
          correction.append(line + "\n");
          if (line.contains("END HIDDEN")) {
            state = savedState;
          }
          break;
        case 6: /* skeleton */
          correction.append(line + "\n");
          if (line.contains("END SKEL")) {
            state = savedState;
          } else {
            skel.append(line + "\n");
          }
          break;
        default:
          throw new RuntimeException(i18n.tr("Parser error in file {0}. This is a parser bug (state={1}), please report.", filename, state));
      }
    }
    if (state == 3) {
      if (seenTemplate)
        System.out.println(i18n.tr("{0}: End of file unexpected after the solution but within the template. Please fix your entity.", shownFilename, state));
    } else if (state != 4)
      System.out.println(i18n.tr("{0}: End of file unexpected (state: {1}). Did you forget to close your template or solution? Please fix your entity.",
                                 shownFilename, state));

    String initialContent = templateHead.toString() + templateTail.toString();
    String skelContent;
    String headContent;
    if (lang.isPython() || lang.isScala() || lang.isC()) {
      skelContent = skel.toString();
      headContent = head.toString();
    } else {
      skelContent = skel.toString().replaceAll("\r\n", " ").replaceAll("\n", " "); // remove Windows and Linux EOF
      headContent = head.toString().replaceAll("\r\n", " ").replaceAll("\n", " "); // remove Windows and Linux EOF
    }

    String template = (headContent + "$body" + tail);
    int offset      = headContent.split("\n").length;

    /* Remove the unnecessary leading spaces from the initial content */
    Pattern newLinePattern = Pattern.compile("\n", Pattern.MULTILINE);
    if (!lang.isPython()) {
      initialContent = initialContent.replaceAll("\t", "    ");
      String[] ctn   = newLinePattern.split(initialContent);
      /* Compute the minimal amount of leading spaces on all lines */
      int minAmountOfLeadingSpace = -1;
      for (String line : ctn) {
        if (line.equals(""))
          continue;
        int len = 0;
        for (char c : line.toCharArray())
          if (c == ' ') {
            len++;
          } else {
            break;
          }
        if (minAmountOfLeadingSpace == -1 || len < minAmountOfLeadingSpace)
          minAmountOfLeadingSpace = len;
      }
      if (minAmountOfLeadingSpace > 0) {
        /* Remove that amount of leading spaces on all lines, and rebuilds initialContent */
        StringBuffer sbCtn = new StringBuffer();
        for (String line : ctn)
          if (line.equals(""))
            sbCtn.append("\n");
          else
            sbCtn.append(line.substring(minAmountOfLeadingSpace) + "\n");
        /* Rebuild the initial content */
        initialContent = sbCtn.toString();
      }
    }

    /* remove any \n from template to not desynchronize line numbers between compiler and editor
     * Python: We should obviously not change blank signs in Python
     * Scala: no need since our compiler's front-end is aware of these offsets */
    if (lang.isJava()) {
      Matcher newLineMatcher = newLinePattern.matcher(template);
      template               = newLineMatcher.replaceAll(" ");
    }

    /* Apply all requested rewrites, if any */
    if (patternString != null) {
      Map<String, String> patterns = new HashMap<String, String>();
      for (String pattern : patternString.split(";")) {
        String[] parts = pattern.split("/");
        if (parts.length != 1 || !parts[0].equals("")) {
          if (parts.length != 3 || !parts[0].equals("s"))
            throw new RuntimeException("Malformed pattern for file " + name + ": '" + pattern + "' (from '" + patterns + "')");

          if (Game.getInstance().isDebugEnabled())
            System.out.println("Replace all " + parts[1] + " to " + parts[2]);
          template       = template.replaceAll(parts[1], parts[2]);
          initialContent = initialContent.replaceAll(parts[1], parts[2]);
          skelContent    = skelContent.replaceAll(parts[1], parts[2]);
        }
      }
    }

    /*if (this.debug) {
            System.out.println("<<<<<<<<template:"+template);
            System.out.println("<<<<<<<<debugCtn:"+debugContent);
            System.out.println("<<<<<<<<initialContent:"+initialContent);
        System.out.println("<<<<<<<<Skel: "+skelContent);
    }*/
    newSource(lang, name, initialContent, template, offset, correction.toString());
  }

  protected final void setup(World w) { setup(new World[] {w}); }
  protected <W extends World> void setup(W[] ws)
  {
    boolean foundALanguage = false;
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
          if (Game.getInstance().isDebugEnabled())
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
    final String id                   = this.getId();
    Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {
      public void uncaughtException(Thread th, Throwable ex)
      {
        if (ex instanceof PLMEntityNotFound) {
          getLesson().setLoadingOutcomeState(LoadingOutcome.FAIL);
        }
        System.err.println("Uncaught exception while computing answer: " + ex);
        ex.printStackTrace();
      }
    };
    Thread t = new Thread() {
      @Override public void run()
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

        try {
          executeAll(Game.getInstance().getOutputWriter(), WorldKind.ANSWER, StudentOrCorrection.CORRECTION);
        } catch (PLMCompilerException e) {
          System.err.println("Severe error: the correction of exercise " + id + " cannot be compiled in " +
                             Game.getInstance().getProgrammingLanguage().getLang() + ". Please go fix your PLM.");
          e.printStackTrace();
          Game.getInstance().setState(Game.GameState.COMPILATION_ENDED);
          Game.getInstance().setState(Game.GameState.EXECUTION_ENDED);
        }

        for (World aw : answerWorld) {
          for (Entity ent : aw.getEntities())
            Game.getInstance().getProgrammingLanguage().runEntity(ent, progress);
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
    t.setUncaughtExceptionHandler(h);
    Game.addInitThread(t);
    t.start();
  }

  @Override public void run(List<Thread> runnerVect)
  {
    if (lastResult == null)
      lastResult = new RunOutcome();

    mutateEntities(WorldKind.CURRENT, StudentOrCorrection.STUDENT);

    for (World cw : getWorlds(WorldKind.CURRENT)) {
      cw.doDelay();
      cw.runEntities(runnerVect, lastResult);
    }
  }

  @Override public void runDemo(List<Thread> runnerVect)
  {
    RunOutcome ignored = new RunOutcome();

    for (int i = 0; i < initialWorld.size(); i++) {
      answerWorld.get(i).reset(initialWorld.get(i));
      answerWorld.get(i).doDelay();
    }
    try {
      executeAll(Game.getInstance().getOutputWriter(), WorldKind.ANSWER, StudentOrCorrection.CORRECTION);
    } catch (PLMCompilerException e) {
      System.err.println("Severe error: the correction of exercise " + getId() + " cannot be compiled in " +
                         Game.getInstance().getProgrammingLanguage().getLang() + ". Please go fix your PLM.");
      e.printStackTrace();
      return;
    }

    for (World aw : getWorlds(WorldKind.ANSWER))
      aw.runEntities(runnerVect, ignored);
  }
}
