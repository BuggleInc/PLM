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
 * student/correction source (the run() method, its dependencies, its imports...) into a language-specific template,
 * then to compile and run as an external process.
 */
public abstract class TemplatedRemoteLang extends RemoteExecutionLang {

  public TemplatedRemoteLang(String lang, String ext, ImageIcon i) { super(lang, ext, i); }

  /**
   * Extract every section of {@code code} delimited by a (possibly repeated) {@code beginMarker}/{@code endMarker}
   * pair, concatenated with a newline after each occurrence. Used to pull out the "/* BEGIN DEPENDENCY *&#47;
   * ... /* END DEPENDENCY *&#47;"-style sections that Java and Scala exercises use to mark code the template must
   * carry over verbatim (dependencies, extra imports).
   */
  protected static String extractMarkedSection(String code, String beginMarker, String endMarker)
  {
    StringBuilder section = new StringBuilder();
    for (int i = 0; i < code.length(); i++) {
      if (!code.startsWith(beginMarker, i))
        continue;
      int begin = i + beginMarker.length();
      int end   = code.indexOf(endMarker, i);
      section.append(code, begin, end).append("\n");
      i = end + endMarker.length();
    }
    return section.toString();
  }

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
  protected static String getRemote(String code)
  {
    if (code.contains("plm.test.simple"))
      return "RemoteSimple";
    if (code.contains(".bat."))
      return "RemoteBat";
    if (code.contains(".cons."))
      return "RemoteCons";
    if (code.contains("Buggle"))
      return "RemoteBuggle";
    if (code.contains("Langton") || code.contains("Turmite"))
      return "RemoteTurmite";
    if (code.contains("Turtle"))
      return "RemoteTurtle";
    if (code.contains("Flag"))
      return "RemoteFlag";
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
  protected static String getRemoteOrFail(String code, String langName, Exercise exo, DiagnosticCollector<JavaFileObject> diagnostic)
      throws PLMCompilerException
  {
    String remote = getRemote(code);
    if (remote == null) {
      PLMCompilerException e = new PLMCompilerException("This universe is not implemented in " + langName + ".", null, diagnostic);
      exo.lastResult         = RunOutcome.newCompilationError(e.getMessage());
      throw e;
    }
    return remote;
  }

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

  /* to make sure that the subsequent version of the same class have different names, in order to bypass the cache of the class loader */
  /* FIXME: the exercise ID should be used now that the student code is executed in a remote process. There is no class cache to bypass anymore */
  protected static final String packageNamePrefix = "plm.runtime";
  protected int packageNameSuffix                 = 0;
  protected String packageName() { return packageNamePrefix + packageNameSuffix; }
}
