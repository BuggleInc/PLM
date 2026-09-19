package plm.core.model.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;
import javax.swing.JScrollPane;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.Lesson;
import plm.core.ui.JavaEditorPanel;

public class SourceFile {

  protected String name;
  private final String template;
  private String body;
  private int offset;
  private String correction;
  private ISourceFileListener listener = null;
  private final Map<Class<?>, Object> onceCache = new HashMap<>();

  public SourceFile(String name, String initialBody, String template, int _offset, String _correctionCtn)
  {
    this.name       = name;
    this.body       = initialBody;
    this.offset     = _offset;
    this.correction = _correctionCtn;
    this.template   = template;
  }

  public String getName() { return this.name; }

  public String getBody() { return this.body; }

  public void setBody(String text, ProgrammingLanguage lang)
  {
    if (lang.isPython())
      body = text.replaceAll("\\t", "    ");
    else
      body = text;
    notifyListener();
  }
  public void setCorrection(String c) { this.correction = c; }
  public String getCorrection() { return this.correction; }

  /**
   * Lazily computes and caches one value per SourceFile instance, keyed by its class. Intended for languages whose
   * per-compile extraction out of {@link #correction} (run()/dependency/imports/corrected template...) is a pure
   * function of it -- {@code correction} never changes between compiles of the same SourceFile, so it only needs
   * computing once. See CONTRIBUTING.md, "From correction entity to compilable source: templating".
   */
  @SuppressWarnings("unchecked") public <T> T cached(Class<T> type, Supplier<T> compute) { return (T)onceCache.computeIfAbsent(type, k -> compute.get()); }

  /** Functional counterpart of {@link Supplier} that may throw a checked exception, for {@link #cachedOrThrow}. */
  public interface ThrowingSupplier<T, E extends Exception> {
    T get() throws E;
  }

  /**
   * Same as {@link #cached}, for a per-language extraction that can itself fail to parse {@code correction} (e.g.
   * Scala's getCorrectedTemplate(), which validates the template is well-formed). {@code computeIfAbsent} can't be
   * reused here since its lambda parameter can't declare checked exceptions.
   */
  @SuppressWarnings("unchecked") public <T, E extends Exception> T cachedOrThrow(Class<T> type, ThrowingSupplier<T, E> compute) throws E
  {
    if (!onceCache.containsKey(type))
      onceCache.put(type, compute.get());
    return (T)onceCache.get(type);
  }

  public String getCompilableContent(StudentOrCorrection whatToRetrieve) { return getCompilableContent(null, whatToRetrieve); }

  public String getCompilableContent(Map<String, String> runtimePatterns, StudentOrCorrection whatToRetrieve)
  {
    return getCompilableContent(runtimePatterns, whatToRetrieve, this.template);
  }

  /**
   * The `$body` value to substitute in {@code template} for {@code StudentOrCorrection.CORRECTION}, as derived from
   * {@link #correction} by {@link #getCompilableContent}'s shorter overloads: the text between whichever of
   * BEGIN/END TEMPLATE or BEGIN/END SOLUTION exists in {@code correction} (comment-delimited, `/* ... *&#47;`-style --
   * i.e. Java/Scala/C's marker syntax), markers included.
   */
  private String deriveCorrectionBody()
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
   * Same as the full 4-argument {@link #getCompilableContent}, using this SourceFile's own step-1 {@link #template}
   * (Java/Scala re-derive their own per-compile shape instead -- see LangJava/LangScala.compileExo()) and this
   * SourceFile's own {@link #deriveCorrectionBody} (Python computes its own instead -- see LangPython.compileExo()).
   */
  public String getCompilableContent(Map<String, String> runtimePatterns, StudentOrCorrection whatToRetrieve, String template)
  {
    return getCompilableContent(runtimePatterns, whatToRetrieve, template, deriveCorrectionBody());
  }

  /**
   * Returns the source text that we should compile. Pure function of its arguments: unlike a plain {@code $body}
   * mutable field, {@code template} is never stored back onto this SourceFile, so compiling STUDENT right after
   * CORRECTION (or vice-versa, or Java right after Scala on an unrelated SourceFile) can never see a stale shape
   * left over by a previous call.
   * @param runtimePatterns
   * 			some last-minute replacement to do (such as package name adjustment)
   * @param whatToRetrieve
   * 			whether we want to retrieve the student-provided content or the correction
   * @param template
   * 			the "head + $body + tail" shape to fill in. Java/Scala rebuild their own on every compile (their `run()`
   * 			may or may not overlap with the templated region, which changes the shape); Python and C always pass this
   * 			SourceFile's own step-1 {@link #template} (see the 3-argument overload).
   * @param correctionBody
   * 			the `$body` value to use for {@code StudentOrCorrection.CORRECTION} (ignored for STUDENT, which always uses
   * 			this SourceFile's own {@link #body}). Callers whose marker syntax or CORRECTION-body rule differs from
   * 			{@link #deriveCorrectionBody}'s (Java/Scala/C's `/* ... *&#47;`-style TEMPLATE/SOLUTION markers) -- currently
   * 			only Python, whose markers are `#`-style comments and whose CORRECTION body isn't always the plain
   * 			marker-delimited slice -- compute their own instead of relying on it (see LangPython.compileExo()).
   * @return
   */
  public String getCompilableContent(Map<String, String> runtimePatterns, StudentOrCorrection whatToRetrieve, String template, String correctionBody)
  {
    String res;

    if (whatToRetrieve == StudentOrCorrection.CORRECTION) {
      res = template.replace("$body", correctionBody + " \n");
    } else if (template != null) {
      res = template.replaceAll("\\$body", this.body + " \n");

    } else {
      res = this.body;
    }
    if (runtimePatterns != null)
      for (Entry<String, String> pattern : runtimePatterns.entrySet()) {
        res = res.replaceAll(pattern.getKey(), pattern.getValue());
        // This is a trap to find issue #42 that I fail to reproduce
        if (pattern.getValue().contains("\n") && !pattern.getKey().equals("\\$run")) {
          System.out.println("Damn! I integrated a pattern being more than one line long, line numbers will be wrong."
                             + "Please repport this bug (alongside with the following informations) as it will help us fixing our issue #42!");
          System.out.println("pattern key: " + pattern.getKey());
          System.out.println("pattern value: " + pattern.getValue());

          Lesson lesson = Game.getInstance().getCurrentLesson();
          String exo    = lesson == null ? "unknown" : lesson.getCurrentExercise().getName();
          System.out.println("Exercise: " + exo);

          System.out.println("PLM version: " + Game.getProperty("plm.major.version", "internal", false) + " (" +
                             Game.getProperty("plm.major.version", "internal", false) + "." + Game.getProperty("plm.minor.version", "", false) + ")");
          System.out.println("Java version: " + System.getProperty("java.version") + " (VM version: " + System.getProperty("java.vm.version") + ")");
          System.out.println("System: " + System.getProperty("os.name") + " (version: " + System.getProperty("os.version") +
                             "; arch: " + System.getProperty("os.arch") + ")");
        }
      }
    return res.replaceAll("\\xa0", " "); // Kill those damn \160 chars, which are non-breaking spaces (got them from copy/pasting source examples?)
  }

  public void setListener(ISourceFileListener l) { this.listener = l; }

  public void removeListener() { this.listener = null; }

  public void notifyListener()
  {
    if (this.listener != null)
      this.listener.sourceFileContentHasChanged();
  }

  @Override public int hashCode()
  {
    final int PRIME = 31;
    int result      = 1;
    result          = PRIME * result + ((body == null) ? 0 : body.hashCode());
    return result;
  }

  @Override public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final SourceFile other = (SourceFile)obj;
    if (body == null) {
      if (other.body != null)
        return false;
    } else if (!body.equals(other.body))
      return false;
    return true;
  }

  public JScrollPane getEditorPanel(ProgrammingLanguage lang) { return new JavaEditorPanel(this, lang); }

  public int getOffset() { return offset; }
}
