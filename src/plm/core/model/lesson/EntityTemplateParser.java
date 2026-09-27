package plm.core.model.lesson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import plm.core.PLMCompilerException;
import plm.core.lang.LanguageExtraction;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;

/**
 * Parses the BEGIN/END TEMPLATE/SOLUTION/SOLUTIONHELPER/IMPORT/HELPER markers out of one entity file's raw content, as described in the CONTRIBUTING.md file.
 * This results in a {@link TemplatedEntity} reccord.
 */
public class EntityTemplateParser {

  private EntityTemplateParser() {} // not instantiable, only static helpers

  /**
   * @param content        the raw content of the entity file, as read from disk
   * @param lang           the language this entity file is written in
   * @param name           the class name to substitute in the file's own class declaration; the package line, if any, is rewritten to the fixed "generated"
   *     instead
   * @param shownFilename  the human-readable file name, only used in warning/error messages
   * @param patternString  optional {@code s/regex/replacement/;...} rewrites applied to template/initialContent
   */
  public static TemplatedEntity parse(String content, ProgrammingLanguage lang, String name, String shownFilename, String patternString)
      throws PLMCompilerException
  {
    String[] lines             = rewriteDeclarations(content.split("\n"), name, lang);
    List<Segment> segments     = split(lines, shownFilename);
    StringBuilder correctionSb = new StringBuilder();
    for (String line : lines)
      correctionSb.append(line).append("\n");
    String correction = correctionSb.toString();

    /* The tail starts with a \n so that Python sees it as a new block, even if the student left indented blank lines at the end of the template */
    String head      = text(segments, Kind.HEAD);
    String tail      = "\n" + text(segments, Kind.TAIL);
    boolean hasBegin = Arrays.stream(lines).anyMatch(l -> l.contains("BEGIN TEMPLATE") || l.contains("BEGIN SOLUTION"));
    if (lang.isC() && hasBegin && !head.contains("#line"))
      head += "#line 1 \"" + name + ".c\" \n";

    String initialContent = text(segments, Kind.TEMPLATE);
    String imports        = text(segments, Kind.IMPORT);
    String helpers        = text(segments, Kind.HELPER);

    String template = head + "$body" + tail;

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

    // Step 2 (see CONTRIBUTING.md, "From correction entity to compilable source: templating"): each language's own
    // extract() re-parses `correction` with its own marker syntax, so this parser stays unaware of it -- see
    // TemplatedEntity's javadoc.
    LanguageExtraction extraction = lang.extract(correction, template, imports, helpers, name);
    return new TemplatedEntity(initialContent, template, correction, imports, helpers, extraction);
  }

  /**
   * Where a run of lines sits relative to the markers. SOLUTIONHELPER only exists inside the template; elsewhere
   * solution-helper lines are plain HEAD/TAIL. IMPORT and HELPER lines are never part of HEAD, TEMPLATE nor TAIL,
   * wherever they are written.
   */
  enum Kind { HEAD, TEMPLATE, SOLUTION, SOLUTIONHELPER, IMPORT, HELPER, TAIL }

  /** Consecutive lines of one {@link Kind}, each ended by a \n. Marker lines belong to no segment. */
  record Segment(Kind kind, String text) {}

  private enum Marker {
    BEGIN_TEMPLATE("BEGIN TEMPLATE"),
    END_TEMPLATE("END TEMPLATE"),
    // SOLUTION HELPER must be listed before SOLUTION to ensure that SOLUTIONHELPER are not read as SOLUTION by error
    BEGIN_SOLUTIONHELPER("BEGIN SOLUTIONHELPER"),
    END_SOLUTIONHELPER("END SOLUTIONHELPER"),
    BEGIN_SOLUTION("BEGIN SOLUTION"),
    END_SOLUTION("END SOLUTION"),
    BEGIN_IMPORT("BEGIN IMPORT"),
    END_IMPORT("END IMPORT"),
    BEGIN_HELPER("BEGIN HELPER"),
    END_HELPER("END HELPER");

    final String text;
    Marker(String text) { this.text = text; }

    /** The marker written on this line, or null. Markers are expected alone on their line. */
    static Marker of(String line)
    {
      for (Marker m : values())
        if (line.contains(m.text))
          return m;
      return null;
    }
  }

  private enum Phase { BEFORE, IN_TEMPLATE, AFTER }

  private static final Pattern CLASS_DECLARATION = Pattern.compile("\\bclass\\s+\\w+");

  /** Concatenates the text of all segments of the given kinds, in file order. */
  private static String text(List<Segment> segments, Kind... kinds)
  {
    EnumSet<Kind> wanted = EnumSet.copyOf(Arrays.asList(kinds));
    StringBuilder sb     = new StringBuilder();
    for (Segment s : segments)
      if (wanted.contains(s.kind()))
        sb.append(s.text());
    return sb.toString();
  }

  /**
   * Rewrites the first class declaration to use {@code name}, and the first package line to the fixed "generated" (Java/Scala
   *  only: "import static X.*;" needs a real package, even though it does not need to be a per-exercise one, as we use separate
   *  directories and processes to ensure that runs never collide). Marker lines are left alone.
   */
  private static String[] rewriteDeclarations(String[] lines, String name, ProgrammingLanguage lang)
  {
    String[] res      = lines.clone();
    boolean classDone = false, packageDone = false;
    for (int i = 0; i < res.length; i++) {
      if (Marker.of(res[i]) != null)
        continue;
      Matcher m = CLASS_DECLARATION.matcher(res[i]);
      if (!classDone && m.find()) {
        res[i]    = m.replaceFirst("class " + Matcher.quoteReplacement(name));
        classDone = true;
      } else if (!packageDone && res[i].contains("package") && (lang.isJava() || lang.isScala())) {
        res[i]      = "package generated" + (lang.isScala() ? "" : ";");
        packageDone = true;
      }
    }
    return res;
  }

  /**
   * Splits the lines into segments, checking that markers are well-formed on the way: BEGIN/END pairs are matched and not
   * nested, there is at most one TEMPLATE and one SOLUTION (any number of IMPORT/HELPER sections), and no BEGIN marker comes after the end of the template.
   *
   * @throws RuntimeException on the first ill-formed marker
   */
  static List<Segment> split(String[] lines, String shownFilename)
  {
    List<Segment> segments = new ArrayList<>();
    StringBuilder current  = new StringBuilder();
    Kind currentKind       = Kind.HEAD;

    Phase phase              = Phase.BEFORE;
    boolean inSolution       = false;
    boolean seenSolution     = false;
    boolean inSolutionHelper = false;
    boolean inImport         = false;
    boolean inHelper         = false;

    for (int i = 0; i < lines.length; i++) {
      Marker marker = Marker.of(lines[i]);
      boolean legal = true;
      if (marker != null) {
        switch (marker) {
          case BEGIN_TEMPLATE:
            legal = phase == Phase.BEFORE && !inSolution && !inSolutionHelper && !inImport && !inHelper && !seenSolution;
            phase = Phase.IN_TEMPLATE;
            break;
          case END_TEMPLATE:
            legal = phase == Phase.IN_TEMPLATE && !inSolution && !inSolutionHelper && !inImport && !inHelper;
            phase = Phase.AFTER;
            break;
          case BEGIN_SOLUTION:
            legal        = phase != Phase.AFTER && !inSolution && !seenSolution && !inSolutionHelper && !inImport && !inHelper;
            inSolution   = true;
            seenSolution = true;
            break;
          case END_SOLUTION:
            legal      = inSolution;
            inSolution = false;
            if (phase == Phase.BEFORE) // solution without template: the tail starts right after it
              phase = Phase.AFTER;
            break;
          case BEGIN_SOLUTIONHELPER:
            legal            = !inSolutionHelper && !inSolution && !inImport && !inHelper;
            inSolutionHelper = true;
            break;
          case END_SOLUTIONHELPER:
            legal            = inSolutionHelper;
            inSolutionHelper = false;
            break;
          case BEGIN_IMPORT:
            legal    = !inImport && !inSolution && !inSolutionHelper;
            inImport = true;
            break;
          case END_IMPORT:
            legal    = inImport;
            inImport = false;
            break;
          case BEGIN_HELPER:
            legal    = !inHelper && !inImport && !inSolution && !inSolutionHelper;
            inHelper = true;
            break;
          case END_HELPER:
            legal    = inHelper;
            inHelper = false;
            break;
        }
        if (!legal)
          throw new RuntimeException(Game.i18n.tr("{0}, line {1}: unexpected \"{2}\". Please fix your entity.", shownFilename, i + 1, marker.text));
      }

      Kind kind = inHelper                                           ? Kind.HELPER
                  : inImport                                         ? Kind.IMPORT
                  : inSolution                                       ? Kind.SOLUTION
                  : (inSolutionHelper && phase == Phase.IN_TEMPLATE) ? Kind.SOLUTIONHELPER
                  : phase == Phase.BEFORE                            ? Kind.HEAD
                  : phase == Phase.IN_TEMPLATE                       ? Kind.TEMPLATE
                                                                     : Kind.TAIL;
      if (kind != currentKind) {
        if (current.length() > 0)
          segments.add(new Segment(currentKind, current.toString()));
        current     = new StringBuilder();
        currentKind = kind;
      }
      if (marker == null)
        current.append(lines[i]).append("\n");
    }
    if (current.length() > 0)
      segments.add(new Segment(currentKind, current.toString()));

    if (phase == Phase.IN_TEMPLATE || inSolution || inSolutionHelper || inImport || inHelper)
      throw new RuntimeException(Game.i18n.tr("{0}: end of file reached inside a BEGIN/END block. Please fix your entity.", shownFilename));
    return segments;
  }
}
