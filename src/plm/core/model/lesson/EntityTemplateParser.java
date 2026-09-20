package plm.core.model.lesson;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;

/**
 * Parses the BEGIN/END TEMPLATE/SOLUTION/HIDDEN markers out of one entity file's raw content, as described in the CONTRIBUTING.md file.
 * This results in a {@link TemplatedEntity} reccord.
 */
public class EntityTemplateParser {

  private EntityTemplateParser() {} // not instantiable, only static helpers

  /**
   * @param content        the raw content of the entity file, as read from disk
   * @param lang           the language this entity file is written in
   * @param name           the class/package name to substitute in the file's own class/package declaration
   * @param shownFilename  the human-readable file name, only used in warning/error messages
   * @param patternString  optional {@code s/regex/replacement/;...} rewrites applied to template/initialContent
   */
  public static TemplatedEntity parse(String content, ProgrammingLanguage lang, String name, String shownFilename, String patternString)
  {
    if (lang.isJava()) {
      /* Remove line comments since at some point, we put everything on one line only,
       * so this would comment the end of the template and break everything */
      Pattern lineCommentPattern = Pattern.compile("//.*$", Pattern.MULTILINE);
      Matcher lineCommentMatcher = lineCommentPattern.matcher(content);
      content                    = lineCommentMatcher.replaceAll("");
    }

    /* Extract the template, the initial content and the solution out of the file */
    int state                 = 0;
    int savedState            = 0;
    StringBuffer head         = new StringBuffer(); /* before the template (state 0) */
    StringBuffer templateHead = new StringBuffer(); /* in template before solution (state 1) */
    StringBuffer solution     = new StringBuffer(); /* the solution (state 2) -- kept for parity, unused below */
    StringBuffer templateTail = new StringBuffer(); /* in template after solution (state 3) */
    StringBuffer tail =
        new StringBuffer("\n");                            /* after the template (state 4)
                                                            *   This contains a preliminar \n to help python understanding that the following is not in the same block.
                                                            *   Not doing Without it, we would have issues if the student puts some empty lines with the indentation marker at tail
                                                            */
    StringBuffer correction          = new StringBuffer(); /* the unchanged content, but the package and className modification */
    boolean containsLinePreprocessor = false;

    boolean seenTemplate = false; // whether B/E SOLUTION seems included within B/E TEMPLATE
    for (String line : content.split("\n")) {
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
          } else {
            correction.append(line + "\n");
            head.append(line + "\n");
          }
          break;
        case 1: /* template head */
          correction.append(line + "\n");
          if (line.contains("BEGIN TEMPLATE")) {
            System.out.println(Game.i18n.tr("{0}: BEGIN TEMPLATE within the template. Please fix your entity.", shownFilename));
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
          } else {
            templateHead.append(line + "\n");
          }
          break;
        case 2: /* solution */
          correction.append(line + "\n");
          if (line.contains("END TEMPLATE")) {
            System.out.println(Game.i18n.tr("{0}: BEGIN SOLUTION is closed with END TEMPLATE. Please fix your entity.", shownFilename));
            state = 4;
          } else if (line.contains("END SOLUTION")) {
            if (seenTemplate)
              state = 3;
            else
              state = 4; // Jump directly to end of template
          } else {
            solution.append(line + "\n");
          }
          break;
        case 3: /* template tail */
          correction.append(line + "\n");
          if (line.contains("END TEMPLATE")) {
            if (!seenTemplate)
              System.out.println(Game.i18n.tr("{0}: END TEMPLATE with no matching BEGIN TEMPLATE. Please fix your entity.", shownFilename));

            state = 4;
          } else if (line.contains("BEGIN SOLUTION")) {
            throw new RuntimeException(Game.i18n.tr("{0}: Begin solution in template tail. Change it to BEGIN HIDDEN.", shownFilename));
          } else if (line.contains("BEGIN HIDDEN")) {
            savedState = 3;
            state      = 5;
          } else {
            templateTail.append(line + "\n");
          }
          break;
        case 4: /* end of file */
          correction.append(line + "\n");
          if (line.contains("END TEMPLATE"))
            if (!seenTemplate)
              System.out.println(Game.i18n.tr("{0}: END TEMPLATE with no matching BEGIN TEMPLATE. Please fix your entity.", shownFilename));

          tail.append(line + "\n");
          break;
        case 5: /* Hidden but not bodied */
          correction.append(line + "\n");
          if (line.contains("END HIDDEN")) {
            state = savedState;
          }
          break;
        default:
          throw new RuntimeException(Game.i18n.tr("Parser error in file {0}. This is a parser bug (state={1}), please report.", shownFilename, state));
      }
    }
    if (state == 3) {
      if (seenTemplate)
        System.out.println(
            Game.i18n.tr("{0}: End of file unexpected after the solution but within the template. Please fix your entity.", shownFilename, state));
    } else if (state != 4)
      System.out.println(Game.i18n.tr("{0}: End of file unexpected (state: {1}). Did you forget to close your template or solution? Please fix your entity.",
                                      shownFilename, state));

    String initialContent = templateHead.toString() + templateTail.toString();
    String headContent;
    if (lang.isPython() || lang.isScala() || lang.isC()) {
      headContent = head.toString();
    } else {
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

    /* Java: remove any \n from template to not desynchronize line numbers between compiler and editor
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
        }
      }
    }

    // extraction (step 2) is not computed here: this parser stays unaware of any per-language marker syntax, see
    // ExerciseTemplated.newSourceFromFile() and TemplatedEntity's own javadoc.
    return new TemplatedEntity(initialContent, template, offset, correction.toString(), null);
  }
}
