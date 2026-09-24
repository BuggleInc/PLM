package plm.core.model.lesson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;

/**
 * Parses the BEGIN/END TEMPLATE/SOLUTION/HIDDEN/IMPORT/DEPENDENCY markers out of one entity file's raw content, as described in the CONTRIBUTING.md file.
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
    String[] lines             = rewriteDeclarations(content.split("\n"), name);
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
    String dependencies   = text(segments, Kind.DEPENDENCY);
    String headContent;
    if (lang.isPython() || lang.isScala() || lang.isC()) {
      headContent = head;
    } else {
      /* Java only: flatten head onto a single physical line, so that $body always starts right there too -- its first line
       * predictably ends up on the same compiled line as head, giving offset 0 below. A "//" comment in head would then
       * swallow everything joined after it on that line (including $body itself), so line comments must be stripped first;
       * this must not touch correction/initialContent/imports/dependencies, which stay a faithful copy of the file. */
      headContent = head.replaceAll("//.*", "").replaceAll("\r\n", " ").replaceAll("\n", " "); // remove Windows and Linux EOF
    }

    String template = (headContent + "$body" + tail);
    /* How many physical lines of the compiled file come before $body's own first line: head's line count when kept as is,
     * or 0 when flattened above (head and $body's first line then share the same physical line). */
    int offset = (lang.isPython() || lang.isScala() || lang.isC()) ? headContent.split("\n").length : 0;

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
    return new TemplatedEntity(initialContent, template, offset, correction, imports, dependencies, null);
  }

  /**
   * Where a run of lines sits relative to the markers. HIDDEN only exists inside the template; elsewhere hidden lines are plain HEAD/TAIL.
   * IMPORT and DEPENDENCY lines are never part of HEAD, TEMPLATE nor TAIL, wherever they are written.
   */
  enum Kind { HEAD, TEMPLATE, SOLUTION, HIDDEN, IMPORT, DEPENDENCY, TAIL }

  /** Consecutive lines of one {@link Kind}, each ended by a \n. Marker lines belong to no segment. */
  record Segment(Kind kind, String text) {}

  private enum Marker {
    BEGIN_TEMPLATE("BEGIN TEMPLATE"),
    END_TEMPLATE("END TEMPLATE"),
    BEGIN_SOLUTION("BEGIN SOLUTION"),
    END_SOLUTION("END SOLUTION"),
    BEGIN_HIDDEN("BEGIN HIDDEN"),
    END_HIDDEN("END HIDDEN"),
    BEGIN_IMPORT("BEGIN IMPORT"),
    END_IMPORT("END IMPORT"),
    BEGIN_DEPENDENCY("BEGIN DEPENDENCY"),
    END_DEPENDENCY("END DEPENDENCY");

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

  /** Rewrites the first class declaration to use {@code name}, and the first package line to {@code $package}. Marker lines are left alone. */
  private static String[] rewriteDeclarations(String[] lines, String name)
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
      } else if (!packageDone && res[i].contains("package")) {
        res[i]      = "$package ";
        packageDone = true;
      }
    }
    return res;
  }

  /**
   * Splits the lines into segments, checking that markers are well-formed on the way: BEGIN/END pairs are matched and not
   * nested, there is at most one TEMPLATE and one SOLUTION (any number of IMPORT/DEPENDENCY sections), and no BEGIN marker comes after the end of the template.
   *
   * @throws RuntimeException on the first ill-formed marker
   */
  static List<Segment> split(String[] lines, String shownFilename)
  {
    List<Segment> segments = new ArrayList<>();
    StringBuilder current  = new StringBuilder();
    Kind currentKind       = Kind.HEAD;

    Phase phase          = Phase.BEFORE;
    boolean inSolution   = false;
    boolean seenSolution = false;
    boolean inHidden     = false;
    boolean inImport     = false;
    boolean inDependency = false;

    for (int i = 0; i < lines.length; i++) {
      Marker marker = Marker.of(lines[i]);
      boolean legal = true;
      if (marker != null) {
        switch (marker) {
          case BEGIN_TEMPLATE:
            legal = phase == Phase.BEFORE && !inSolution && !inHidden && !inImport && !inDependency && !seenSolution;
            phase = Phase.IN_TEMPLATE;
            break;
          case END_TEMPLATE:
            legal = phase == Phase.IN_TEMPLATE && !inSolution && !inHidden && !inImport && !inDependency;
            phase = Phase.AFTER;
            break;
          case BEGIN_SOLUTION:
            legal        = phase != Phase.AFTER && !inSolution && !seenSolution && !inHidden && !inImport && !inDependency;
            inSolution   = true;
            seenSolution = true;
            break;
          case END_SOLUTION:
            legal      = inSolution;
            inSolution = false;
            if (phase == Phase.BEFORE) // solution without template: the tail starts right after it
              phase = Phase.AFTER;
            break;
          case BEGIN_HIDDEN:
            legal    = !inHidden && !inSolution && !inImport && !inDependency;
            inHidden = true;
            break;
          case END_HIDDEN:
            legal    = inHidden;
            inHidden = false;
            break;
          case BEGIN_IMPORT:
            legal    = !inImport && !inSolution && !inHidden;
            inImport = true;
            break;
          case END_IMPORT:
            legal    = inImport;
            inImport = false;
            break;
          case BEGIN_DEPENDENCY:
            legal        = !inDependency && !inImport && !inSolution && !inHidden;
            inDependency = true;
            break;
          case END_DEPENDENCY:
            legal        = inDependency;
            inDependency = false;
            break;
        }
        if (!legal)
          throw new RuntimeException(Game.i18n.tr("{0}, line {1}: unexpected \"{2}\". Please fix your entity.", shownFilename, i + 1, marker.text));
      }

      Kind kind = inDependency                               ? Kind.DEPENDENCY
                  : inImport                                 ? Kind.IMPORT
                  : inSolution                               ? Kind.SOLUTION
                  : (inHidden && phase == Phase.IN_TEMPLATE) ? Kind.HIDDEN
                  : phase == Phase.BEFORE                    ? Kind.HEAD
                  : phase == Phase.IN_TEMPLATE               ? Kind.TEMPLATE
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

    if (phase == Phase.IN_TEMPLATE || inSolution || inHidden || inImport || inDependency)
      throw new RuntimeException(Game.i18n.tr("{0}: end of file reached inside a BEGIN/END block. Please fix your entity.", shownFilename));
    return segments;
  }
}
