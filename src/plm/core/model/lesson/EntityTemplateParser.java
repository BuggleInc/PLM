package plm.core.model.lesson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Pattern;
import plm.core.PLMCompilerException;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.session.SourceFile;
import plm.core.utils.Indentation;

/**
 * Parses the BEGIN/END TEMPLATE/SOLUTION/IMPORT/REMOTE markers out of one entity file's raw content, as described in the CONTRIBUTING.md file.
 * This results in a {@link SourceFile}.
 */
public class EntityTemplateParser {

  private EntityTemplateParser() {} // not instantiable, only static helpers

  /**
   * @param content        the raw content of the entity file, as read from disk
   * @param lang           the language this entity file is written in
   * @param name           the name of the resulting source file
   * @param shownFilename  the human-readable file name, only used in warning/error messages
   */
  public static SourceFile parse(String content, ProgrammingLanguage lang, String name, String shownFilename) throws PLMCompilerException
  {
    String[] lines             = content.split("\n");
    SplitResult split          = split(lines, shownFilename);
    List<Segment> segments     = split.segments();
    StringBuilder correctionSb = new StringBuilder();
    for (String line : lines)
      correctionSb.append(line).append("\n");
    String correction = correctionSb.toString();

    /* The tail starts with a \n so that Python sees it as a new block, even if the student left indented blank lines at the end of the template.
     * Both head and tail are narrowed down to the BEGIN/END REMOTE markers when present (see split()); absent REMOTE, they default to the whole file. */
    String head           = text(segments, false, Kind.HEAD);
    String tail           = "\n" + text(segments, false, Kind.TAIL);
    String correctionHead = text(segments, true, Kind.HEAD);
    String correctionTail = "\n" + text(segments, true, Kind.TAIL);

    String initialContent = text(segments, false, Kind.TEMPLATE);
    String imports        = text(segments, false, Kind.IMPORT);

    String template           = head + "$body" + tail;
    String correctionTemplate = correctionHead + "$body" + correctionTail;

    /* The editor starts flush left: remove the indentation shared by the whole templated region (solution included) from the initial
     * content. Python is indentation-sensitive, so LangPython.compileExo() indents the student's code back by bodyIndent: its tabs are
     * expanded as python reads them, to not mix up with spaces. Elsewhere, tabs are simply converted to spaces. */
    String templateRegion = text(segments, true, Kind.TEMPLATE);
    if (lang.isPython()) {
      initialContent = Indentation.expandLeadingTabs(initialContent);
      templateRegion = Indentation.expandLeadingTabs(templateRegion);
    } else {
      initialContent = initialContent.replaceAll("\t", "    ");
      templateRegion = templateRegion.replaceAll("\t", "    ");
    }
    int bodyIndent = minLeadingSpaces(templateRegion);
    initialContent = removeLeadingSpaces(initialContent, bodyIndent);

    return new SourceFile(name, initialContent, correction, template, correctionTemplate, split.correctionBody(), imports, lang.getRemote(correction),
                          bodyIndent);
  }

  /**
   * Where a run of lines sits relative to the markers. IMPORT lines are never part of HEAD, TEMPLATE nor TAIL, wherever
   * they are written. IGNORED is TAIL content written after END REMOTE closes: still part of the raw file/correction, but
   * outside what {@code head}/{@code tail} expose once REMOTE has narrowed them.
   */
  enum Kind { HEAD, TEMPLATE, IMPORT, TAIL, IGNORED }

  /**
   * Consecutive lines of one {@link Kind}, each ended by a \n. Marker lines belong to no segment. {@code solution} is set
   * for lines written between BEGIN/END SOLUTION: they are kept for the correction but not for the student.
   */
  record Segment(Kind kind, String text, boolean solution) {}

  /** {@link #split}'s result: the {@link Kind}-partitioned segments, plus the raw (markers included) TEMPLATE/SOLUTION span. */
  record SplitResult(List<Segment> segments, String correctionBody) {}

  private enum Marker {
    BEGIN_REMOTE("BEGIN REMOTE"),
    END_REMOTE("END REMOTE"),
    BEGIN_TEMPLATE("BEGIN TEMPLATE"),
    END_TEMPLATE("END TEMPLATE"),
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

  /** The smallest number of leading spaces among the non blank lines of {@code text}, or 0 if there is none. */
  private static int minLeadingSpaces(String text)
  {
    int min = -1;
    for (String line : text.split("\n")) {
      if (line.isBlank())
        continue;
      int len = 0;
      while (len < line.length() && line.charAt(len) == ' ')
        len++;
      if (min == -1 || len < min)
        min = len;
    }
    return Math.max(min, 0);
  }

  /** Removes (at most) {@code n} leading spaces from every line of {@code text}, which ends with a \n if it was not empty. */
  private static String removeLeadingSpaces(String text, int n)
  {
    if (n == 0)
      return text;
    StringBuilder sb = new StringBuilder();
    for (String line : text.split("\n"))
      sb.append(line.substring(Math.min(n, line.length()))).append("\n");
    return sb.toString();
  }

  private enum Phase { BEFORE, IN_TEMPLATE, AFTER }

  /** Concatenates the text of all segments of the given kinds, in file order; solution segments only if {@code withSolutions}. */
  private static String text(List<Segment> segments, boolean withSolutions, Kind... kinds)
  {
    EnumSet<Kind> wanted = EnumSet.copyOf(Arrays.asList(kinds));
    StringBuilder sb     = new StringBuilder();
    for (Segment s : segments)
      if (wanted.contains(s.kind()) && (withSolutions || !s.solution()))
        sb.append(s.text());
    return sb.toString();
  }

  /**
   * Splits the lines into segments, checking that markers are well-formed on the way: BEGIN/END pairs are matched and not
   * nested, there is at most one TEMPLATE (any number of IMPORT sections), and no BEGIN marker comes after the end of the
   * template. When there is a TEMPLATE, any number of SOLUTION sections may appear anywhere, as long as they do not
   * straddle another marker. When there is none, exactly one SOLUTION is expected and it plays the role of the template:
   * the student's code replaces it. Also accumulates {@code correctionBody}, the raw (markers included) span from BEGIN
   * TEMPLATE to END TEMPLATE, or from BEGIN SOLUTION to END SOLUTION when there is no template.
   *
   * BEGIN/END REMOTE is optional and, when present, narrows {@code Kind.HEAD}/{@code Kind.TAIL} down to what's written
   * between the markers instead of defaulting to the whole file: everything accumulated in HEAD before BEGIN REMOTE is
   * dropped (languages that need a per-compile wrapper -- Java/Scala/Python -- use this to exclude their own
   * package/imports/class-declaration boilerplate, since their own wrapper replaces it), and TAIL stops being collected
   * the moment END REMOTE closes. REMOTE must fully enclose (or exactly match) the templated region: it can only open
   * before BEGIN TEMPLATE/SOLUTION and can only close once that region has fully closed (Phase.AFTER), so a REMOTE that
   * would only partially overlap the template is rejected the same way an ill-formed TEMPLATE/SOLUTION pair is. A
   * SOLUTION written outside the templated region must lie within REMOTE when the file has one, as it would otherwise be
   * dropped silently.
   *
   * @throws RuntimeException on the first ill-formed marker
   */
  static SplitResult split(String[] lines, String shownFilename)
  {
    boolean hasTemplate = Arrays.stream(lines).anyMatch(l -> Marker.of(l) == Marker.BEGIN_TEMPLATE);
    Marker bodyStart    = hasTemplate ? Marker.BEGIN_TEMPLATE : Marker.BEGIN_SOLUTION;
    Marker bodyEnd      = hasTemplate ? Marker.END_TEMPLATE : Marker.END_SOLUTION;

    List<Segment> segments = new ArrayList<>();
    StringBuilder current  = new StringBuilder();
    Kind currentKind       = Kind.HEAD;
    boolean currentSol     = false;

    Phase phase                  = Phase.BEFORE;
    boolean inSolution           = false;
    boolean seenSolution         = false;
    boolean inImport             = false;
    boolean inRemote             = false;
    boolean remoteSeen           = false;
    boolean remoteClosed         = false;
    boolean solutionBeforeRemote = false; // a SOLUTION opened before any BEGIN REMOTE: illegal if a REMOTE comes later

    StringBuilder correctionBody  = new StringBuilder();
    boolean correctionBodyStarted = false;
    boolean correctionBodyEnded   = false;

    for (int i = 0; i < lines.length; i++) {
      Marker marker = Marker.of(lines[i]);
      boolean legal = true;

      if (!correctionBodyStarted && marker == bodyStart)
        correctionBodyStarted = true;
      if (correctionBodyStarted && !correctionBodyEnded)
        correctionBody.append(lines[i]).append("\n");
      if (marker == bodyEnd)
        correctionBodyEnded = true;

      if (marker != null) {
        switch (marker) {
          case BEGIN_REMOTE:
            legal = phase == Phase.BEFORE && !inRemote && !remoteSeen && !inImport && !inSolution && !solutionBeforeRemote;
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
            legal        = inRemote && phase == Phase.AFTER && !inSolution; // must fully enclose the templated region, not close mid-way
            inRemote     = false;
            remoteClosed = true; // stop collecting TAIL content from here on
            break;
          case BEGIN_TEMPLATE:
            legal = phase == Phase.BEFORE && !inSolution && !inImport;
            phase = Phase.IN_TEMPLATE;
            break;
          case END_TEMPLATE:
            legal = phase == Phase.IN_TEMPLATE && !inSolution && !inImport;
            phase = Phase.AFTER;
            break;
          case BEGIN_SOLUTION:
            legal        = !inSolution && !inImport && !remoteClosed && (hasTemplate || (!seenSolution && phase == Phase.BEFORE));
            inSolution   = true;
            seenSolution = true;
            if (!hasTemplate) // solution without template: it is the templated region
              phase = Phase.IN_TEMPLATE;
            else if (!inRemote && !remoteSeen)
              solutionBeforeRemote = true;
            break;
          case END_SOLUTION:
            legal      = inSolution;
            inSolution = false;
            if (!hasTemplate) // solution without template: the tail starts right after it
              phase = Phase.AFTER;
            break;
          case BEGIN_IMPORT:
            legal    = !inImport && !inSolution;
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

      Kind kind = inImport                     ? Kind.IMPORT
                  : phase == Phase.BEFORE      ? Kind.HEAD
                  : phase == Phase.IN_TEMPLATE ? Kind.TEMPLATE
                  : remoteClosed               ? Kind.IGNORED
                                               : Kind.TAIL;
      if (kind != currentKind || inSolution != currentSol) {
        if (current.length() > 0)
          segments.add(new Segment(currentKind, current.toString(), currentSol));
        current     = new StringBuilder();
        currentKind = kind;
        currentSol  = inSolution;
      }
      if (marker == null)
        current.append(lines[i]).append("\n");
    }
    if (current.length() > 0)
      segments.add(new Segment(currentKind, current.toString(), currentSol));

    if (phase == Phase.IN_TEMPLATE || inSolution || inImport || inRemote)
      throw new RuntimeException(Game.i18n.tr("{0}: end of file reached inside a BEGIN/END block. Please fix your entity.", shownFilename));
    if (!correctionBodyStarted)
      throw new RuntimeException(Game.i18n.tr("{0}: neither BEGIN/END TEMPLATE nor BEGIN/END SOLUTION found. Please fix your entity.", shownFilename));
    return new SplitResult(segments, correctionBody.toString());
  }
}
