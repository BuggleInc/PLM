package plm.core.lang;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.swing.ImageIcon;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import plm.core.PLMCompilerException;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.RunOutcome;

/**
 * Ancestor of the all programming languages, in charge of generating student code by injecting extracted pieces of the
 * student/correction source (the run() method, its helpers, its imports...) into a language-specific template,
 * then to compile and run as an external process.
 */
public abstract class TemplatedRemoteLang extends RemoteExecutionLang {

  public TemplatedRemoteLang(String lang, String ext, ImageIcon i) { super(lang, ext, i); }

  /**
   * Return [start, end) of a method's own text (its declaration line through its brace-matched closing '}') within
   * code, searching for the given declaration keyword (e.g. "void run(" for Java, "def run(" for Scala) -- or null if
   * that keyword doesn't appear at all. This brace-based algorithm is the default for every language except Python,
   * whose blocks are indentation-delimited instead; see LangPython's override.
   *
   * This is offsets, not a substring, so callers can test containment against another region (e.g. a templated
   * region) without caring how many characters of incidental whitespace separate two markers.
   */
  protected int[] extractRunSpan(String code, String runKeyword)
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

  /**
   * The method's own text (declaration through closing brace/block), or "" if runKeyword doesn't appear in code at
   * all. Built generically on top of (the possibly overridden) {@link #extractRunSpan}, so languages only need to
   * override the span logic, not this.
   */
  protected String extractRunFunction(String code, String runKeyword)
  {
    int[] span = extractRunSpan(code, runKeyword);
    return span == null ? "" : code.substring(span[0], span[1]);
  }

  /**
   * Guess which RemoteXxx universe an exercise belongs to. Shared by Java, Scala and C.
   * Python instead requires an explicit "from RemoteXxx import *" line (see its own getRemote()).
   */
  protected String getRemote(String code)
  {
    if (code.contains("setObjectif") || code.contains("RemoteSimple"))
      return "RemoteSimple";
    if (code.contains(".bat."))
      return "RemoteBat";
    if (code.contains(".cons.") || code.contains("#include \"universe/RecList.h"))
      return "RemoteCons";
    if (code.contains("Buggle"))
      return "RemoteBuggle";
    if (code.contains("Langton") || code.contains("Turmite"))
      return "RemoteTurmite";
    if (code.contains("Turtle"))
      return "RemoteTurtle";
    if (code.contains("Flag"))
      return "RemoteDutchFlag";
    if (code.contains("Baseball"))
      return "RemoteBaseball";
    if (code.contains("Pancake"))
      return "RemotePancake";
    if (code.contains("Hanoi"))
      return "RemoteHanoi";
    if (code.contains("Sort"))
      return "RemoteSort";
    if (code.contains("Lander"))
      return "RemoteLander";

    return null;
  }

  /**
   * Same as {@link #getRemote(String)}, but fails uniformly (PLMCompilerException thrown + {@code exo.lastResult} set
   * to a compilation error) when {@code code}'s universe couldn't be guessed.
   * {@code diagnostic} may be null (Python and C have no javac-style DiagnosticCollector to attach).
   */
  /**
   * Fails the compile with a clear message if {@code remote} is null (the RemoteXxx universe couldn't be guessed from
   * the correction, see {@link #getRemote}), otherwise returns it unchanged.
   */
  protected String checkRemoteOrFail(String remote, String langName, Exercise exo, DiagnosticCollector<JavaFileObject> diagnostic) throws PLMCompilerException
  {
    if (remote == null) {
      PLMCompilerException e = new PLMCompilerException("This universe is not implemented in " + langName + ".", null, diagnostic);
      exo.lastResult         = RunOutcome.newCompilationError(e.getMessage());
      throw e;
    }
    return remote;
  }

  /**
   * The `$body` value to substitute in {@code template} for {@code StudentOrCorrection.CORRECTION}: the text between
   * whichever of BEGIN/END TEMPLATE or BEGIN/END SOLUTION exists in {@code correction} (comment-delimited,
   * `/* ... *&#47;`-style -- i.e. Java/Scala/C's marker syntax), markers included. Used by every language but Python
   * (whose markers and CORRECTION-body rule differ, see {@code LangPython.extract()}).
   */
  protected static String deriveCorrectionBody(String correction, String name)
  {
    final String BEGIN_TEMPLATE = "/* BEGIN TEMPLATE */";
    final String END_TEMPLATE   = "/* END TEMPLATE */";
    final String BEGIN_SOLUTION = "/* BEGIN SOLUTION */";
    final String END_SOLUTION   = "/* END SOLUTION */";

    String beginMarker;
    String endMarker;
    if (correction.contains(BEGIN_TEMPLATE) && correction.contains(END_TEMPLATE)) {
      /* Normal case: the correction entity explicitly delimits the templated region */
      beginMarker = BEGIN_TEMPLATE;
      endMarker   = END_TEMPLATE;
    } else if (correction.contains(BEGIN_SOLUTION) && correction.contains(END_SOLUTION)) {
      /* No BEGIN/END TEMPLATE: the whole run() is graded, only BEGIN/END SOLUTION delimit it. */
      beginMarker = BEGIN_SOLUTION;
      endMarker   = END_SOLUTION;
    } else {
      throw new RuntimeException("Broken exercise: neither BEGIN/END TEMPLATE nor BEGIN/END SOLUTION exist in file " + name);
    }

    return correction.substring(Math.max(correction.indexOf(beginMarker), 0),
                                Math.min(correction.indexOf(endMarker) + endMarker.length() + 1, correction.length()));
  }

  /**
   * {@link LanguageExtraction} for a language that, unlike Java/Scala/Python, never rebuilds its own per-compile
   * template: currently only {@code LangC}, which just reuses step 1's template unchanged alongside the derived
   * correction body.
   */
  public record SimpleExtraction(String template, String correctionBody) implements LanguageExtraction {}

  /**
   * Read a classloader resource at {@code path} (relative to the classpath root) as a UTF-8 string. Low-level
   * primitive behind {@link #loadRemoteFile} and LangC's own resource reading.
   */
  protected static String readClasspathResource(String path) throws IOException
  {
    try (InputStream in = TemplatedRemoteLang.class.getClassLoader().getResourceAsStream(path)) {
      if (in == null)
        throw new IOException("Resource '" + path + "' does not exist.");
      return new String(in.readAllBytes(), StandardCharsets.UTF_8);
    }
  }

  /**
   * Load the raw content of a "RemoteXxx" universe-glue file (e.g. RemoteBuggle.java/.scala/.py), shipped as a
   * classloader resource under "resources/langages/&lt;langDir&gt;/". {@code remoteName} is normalized the same way in
   * every caller: null/empty defaults to plain "Remote", "Remote" is prepended if missing, and {@code extension} is
   * appended if missing. Package-declaration handling (Java/Scala only) is left to the caller, since Python has none.
   */
  protected static String loadRemoteFile(String remoteName, String langDir, String extension)
  {
    String remote = (remoteName == null || remoteName.isEmpty()) ? "Remote" + extension : remoteName;
    if (!remote.startsWith("Remote"))
      remote = "Remote" + remote;
    if (!remote.endsWith(extension))
      remote = remote + extension;

    String path = "resources/langages/" + langDir + "/" + remote;
    try {
      return readClasspathResource(path);
    } catch (IOException e) {
      throw new IllegalArgumentException("Remote '" + path + "' do not exist (argument passed: '" + remoteName + "').");
    }
  }

  /**
   * Unique-enough package name for one compile, derived from the exercise id and which side gets compiled
   * (STUDENT/CORRECTION) -- instead of a shared, manually-incremented counter (the old packageNameSuffix), which
   * raced across concurrent compiles whenever this (singleton) language instance served two threads at once: one
   * thread could read the workspace directory name right as another thread bumped the counter again, so the jar
   * ended up in a directory named after one value but with a manifest naming another.
   *
   * This name is stable across compiles of the same exercise and the same side: reusing it is normally harmless,
   * since nothing changed. The one case it does not cover is an entity file edited on disk without restarting PLM
   * (there is no live-reload of entities otherwise, so this is a developer-only corner case): the old id-plus-a-
   * hash-of-the-source scheme picked up such changes automatically by landing in a fresh directory; this one does
   * not, so pick "clean workspace" (or restart PLM) after editing an entity file, or delete the stale directory by
   * hand under the language's own tempFolder.
   */
  protected String packageNameForExercise(Exercise exo, Exercise.StudentOrCorrection whatToCompile)
  {
    String id = exo.getId().replaceAll("[^a-zA-Z0-9]", "_");
    return packageNamePrefix + id + "_" + whatToCompile;
  }

  protected static final String packageNamePrefix = "plm.runtime";
}
