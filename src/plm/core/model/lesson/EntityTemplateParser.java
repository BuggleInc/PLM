package plm.core.model.lesson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import plm.core.PLMCompilerException;
import plm.core.lang.LanguageExtraction;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;

/**
 * Parses the BEGIN/END TEMPLATE/SOLUTION/SOLUTIONHELPER/IMPORT/REMOTE markers out of one entity file's raw content, as described in the CONTRIBUTING.md file.
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
   */
  public static TemplatedEntity parse(String content, ProgrammingLanguage lang, String name, String shownFilename) throws PLMCompilerException
  {
    String[] lines             = rewriteDeclarations(content.split("\n"), name, lang);
    SplitResult split          = split(lines, shownFilename);
    List<Segment> segments     = split.segments();
    StringBuilder correctionSb = new StringBuilder();
    for (String line : lines)
      correctionSb.append(line).append("\n");
    String correction = correctionSb.toString();

    /* The tail starts with a \n so that Python sees it as a new block, even if the student left indented blank lines at the end of the template.
     * Both head and tail are narrowed down to the BEGIN/END REMOTE markers when present (see split()); absent REMOTE, they default to the whole file. */
    String head      = text(segments, Kind.HEAD);
    String tail      = "\n" + text(segments, Kind.TAIL);
    boolean hasBegin = Arrays.stream(lines).anyMatch(l -> l.contains("BEGIN TEMPLATE") || l.contains("BEGIN SOLUTION"));
    if (lang.isC() && hasBegin && !head.contains("#line"))
      head += "#line 1 \"" + name + ".c\" \n";

    String initialContent = text(segments, Kind.TEMPLATE);
    String imports        = text(segments, Kind.IMPORT);

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

    // Step 2 (see CONTRIBUTING.md, "From correction entity to compilable source: templating"): each language's own
    // extract() re-parses `correction` with its own marker syntax, so this parser stays unaware of it -- see
    // TemplatedEntity's javadoc.
    LanguageExtraction extraction = lang.extract(correction, template, split.correctionBody(), imports);
    return new TemplatedEntity(initialContent, template, split.correctionBody(), correction, imports, extraction);
  }

  /**
   * Where a run of lines sits relative to the markers. SOLUTIONHELPER only exists inside the template; elsewhere
   * solution-helper lines are plain HEAD/TAIL. IMPORT lines are never part of HEAD, TEMPLATE nor TAIL, wherever they
   * are written. IGNORED is TAIL content written after END REMOTE closes: still part of the raw file/correction, but
   * outside what {@code head}/{@code tail} expose once REMOTE has narrowed them.
   */
  enum Kind { HEAD, TEMPLATE, SOLUTION, SOLUTIONHELPER, IMPORT, TAIL, IGNORED }

  /** Consecutive lines of one {@link Kind}, each ended by a \n. Marker lines belong to no segment. */
  record Segment(Kind kind, String text) {}

  /** {@link #split}'s result: the {@link Kind}-partitioned segments, plus the raw (markers included) TEMPLATE/SOLUTION span. */
  record SplitResult(List<Segment> segments, String correctionBody) {}

  private enum Marker {
    BEGIN_REMOTE("BEGIN REMOTE"),
    END_REMOTE("END REMOTE"),
    BEGIN_TEMPLATE("BEGIN TEMPLATE"),
    END_TEMPLATE("END TEMPLATE"),
    // SOLUTION HELPER must be listed before SOLUTION to ensure that SOLUTIONHELPER are not read as SOLUTION by error
    BEGIN_SOLUTIONHELPER("BEGIN SOLUTIONHELPER"),
    END_SOLUTIONHELPER("END SOLUTIONHELPER"),
    BEGIN_SOLUTION("BEGIN SOLUTION"),
    END_SOLUTION("END SOLUTION"),
    BEGIN_IMPORT("BEGIN IMPORT"),
    END_IMPORT("END IMPORT");

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
   * nested, there is at most one TEMPLATE and one SOLUTION (any number of IMPORT sections), and no BEGIN marker comes
   * after the end of the template. Also accumulates {@code correctionBody}, the raw (markers included) span from BEGIN
   * TEMPLATE to END TEMPLATE, or from BEGIN SOLUTION to END SOLUTION when there is no template.
   *
   * BEGIN/END REMOTE is optional and, when present, narrows {@code Kind.HEAD}/{@code Kind.TAIL} down to what's written
   * between the markers instead of defaulting to the whole file: everything accumulated in HEAD before BEGIN REMOTE is
   * dropped (languages that need a per-compile wrapper -- Java/Scala/Python -- use this to exclude their own
   * package/imports/class-declaration boilerplate, since their own wrapper replaces it), and TAIL stops being collected
   * the moment END REMOTE closes. REMOTE must fully enclose (or exactly match) the templated region: it can only open
   * before BEGIN TEMPLATE/SOLUTION and can only close once that region has fully closed (Phase.AFTER), so a REMOTE that
   * would only partially overlap the template is rejected the same way an ill-formed TEMPLATE/SOLUTION pair is.
   *
   * @throws RuntimeException on the first ill-formed marker
   */
  static SplitResult split(String[] lines, String shownFilename)
  {
    List<Segment> segments = new ArrayList<>();
    StringBuilder current  = new StringBuilder();
    Kind currentKind       = Kind.HEAD;

    Phase phase              = Phase.BEFORE;
    boolean inSolution       = false;
    boolean seenSolution     = false;
    boolean inSolutionHelper = false;
    boolean inImport         = false;
    boolean inRemote         = false;
    boolean remoteSeen       = false;
    boolean remoteClosed     = false;

    StringBuilder correctionBody  = new StringBuilder();
    boolean correctionBodyStarted = false;
    boolean correctionBodyEnded   = false;

    for (int i = 0; i < lines.length; i++) {
      Marker marker = Marker.of(lines[i]);
      boolean legal = true;

      if (!correctionBodyStarted && marker != null && (marker == Marker.BEGIN_TEMPLATE || (marker == Marker.BEGIN_SOLUTION && phase == Phase.BEFORE)))
        correctionBodyStarted = true;
      if (correctionBodyStarted && !correctionBodyEnded)
        correctionBody.append(lines[i]).append("\n");
      if (marker != null && (marker == Marker.END_TEMPLATE || (marker == Marker.END_SOLUTION && phase == Phase.BEFORE)))
        correctionBodyEnded = true;

      if (marker != null) {
        switch (marker) {
          case BEGIN_REMOTE:
            legal = phase == Phase.BEFORE && !inRemote && !remoteSeen && !inImport;
            if (legal) {
              // Drop whatever HEAD content was accumulated so far: only what comes after BEGIN REMOTE counts as head.
              segments.removeIf(s -> s.kind() == Kind.HEAD);
              if (currentKind == Kind.HEAD)
                current = new StringBuilder();
            }
            inRemote   = true;
            remoteSeen = true;
            break;
          case END_REMOTE:
            legal        = inRemote && phase == Phase.AFTER; // must fully enclose the templated region, not close mid-way
            inRemote     = false;
            remoteClosed = true; // stop collecting TAIL content from here on
            break;
          case BEGIN_TEMPLATE:
            legal = phase == Phase.BEFORE && !inSolution && !inSolutionHelper && !inImport && !seenSolution;
            phase = Phase.IN_TEMPLATE;
            break;
          case END_TEMPLATE:
            legal = phase == Phase.IN_TEMPLATE && !inSolution && !inSolutionHelper && !inImport;
            phase = Phase.AFTER;
            break;
          case BEGIN_SOLUTION:
            legal        = phase != Phase.AFTER && !inSolution && !seenSolution && !inSolutionHelper && !inImport;
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
            legal            = !inSolutionHelper && !inSolution && !inImport;
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
        }
        if (!legal)
          throw new RuntimeException(Game.i18n.tr("{0}, line {1}: unexpected \"{2}\". Please fix your entity.", shownFilename, i + 1, marker.text));
      }

      Kind kind = inImport                                           ? Kind.IMPORT
                  : inSolution                                       ? Kind.SOLUTION
                  : (inSolutionHelper && phase == Phase.IN_TEMPLATE) ? Kind.SOLUTIONHELPER
                  : phase == Phase.BEFORE                            ? Kind.HEAD
                  : phase == Phase.IN_TEMPLATE                       ? Kind.TEMPLATE
                  : remoteClosed                                     ? Kind.IGNORED
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

    if (phase == Phase.IN_TEMPLATE || inSolution || inSolutionHelper || inImport || inRemote)
      throw new RuntimeException(Game.i18n.tr("{0}: end of file reached inside a BEGIN/END block. Please fix your entity.", shownFilename));
    if (!correctionBodyStarted)
      throw new RuntimeException(Game.i18n.tr("{0}: neither BEGIN/END TEMPLATE nor BEGIN/END SOLUTION found. Please fix your entity.", shownFilename));
    return new SplitResult(segments, correctionBody.toString());
  }
}
