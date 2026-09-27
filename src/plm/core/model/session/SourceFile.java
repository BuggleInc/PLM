package plm.core.model.session;

import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JScrollPane;
import plm.core.lang.LanguageExtraction;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.ui.JavaEditorPanel;

public class SourceFile {

  protected String name;
  private final LanguageExtraction extraction;
  private String body;
  private String correction;
  private ISourceFileListener listener = null;

  public SourceFile(String name, String initialBody, LanguageExtraction extraction, String _correctionCtn)
  {
    this.name       = name;
    this.body       = initialBody;
    this.extraction = extraction;
    this.correction = _correctionCtn;
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
   * This SourceFile's per-language extraction (step 2, computed once, eagerly, at lesson-load time -- see
   * CONTRIBUTING.md, "From correction entity to compilable source: templating"), for the owning language's own
   * {@code compileExo()} to read its own fields back from via a cast (e.g. {@code (JvmExtraction)sf.getExtraction()}
   * in {@code LangJava.compileExo()}). Safe by construction: a given SourceFile is only ever populated by the one
   * language it was parsed for (see {@code Exercise.newSource()}), so the concrete type is always the one that
   * language's own {@code extract()} returns.
   */
  public LanguageExtraction getExtraction() { return extraction; }

  /**
   * The result of {@link #getCompilableContent(Map, StudentOrCorrection)}: the compilable source text, plus how many
   * lines of it come before the student/correction body's own first line (see {@code offset} there).
   */
  public record CompilableContent(String content, int offset) {}

  /**
   * Returns the source text that we should compile, alongside the {@code $body} offset computed along the way.
   *
   * The template (if any) has its {@code $body} placeholder substituted last, after every other {@code runtimePattern}
   * has been applied: a pattern's replacement text may itself span several lines, which shifts how many physical lines
   * come before {@code $body} in the final compiled file. The returned {@code offset} is the number of lines
   * separating the start of the generated file from {@code $body}'s own first line, so that a compiler error line
   * number can later be translated back into the student's own editor coordinates.
   *
   * @param runtimePatterns
   * 			some last-minute replacement to do (such as package name adjustment)
   * @param whatToRetrieve
   * 			whether we want to retrieve the student-provided content or the correction
   * @return
   */
  public CompilableContent getCompilableContent(Map<String, String> runtimePatterns, StudentOrCorrection whatToRetrieve)
  {
    String template = extraction == null ? null : extraction.template();
    String res      = template != null ? template : this.body;

    if (runtimePatterns != null)
      for (Entry<String, String> pattern : runtimePatterns.entrySet())
        res = res.replaceAll(pattern.getKey(), pattern.getValue());

    int offset = 0;
    if (template != null) {
      int bodyIndex = res.indexOf("$body");
      for (int i = 0; i < bodyIndex; i++)
        if (res.charAt(i) == '\n')
          offset++;

      String bodyContent = whatToRetrieve == StudentOrCorrection.CORRECTION ? extraction.correctionBody() : this.body;
      res                = res.replace("$body", bodyContent + " \n");
    }

    res = res.replaceAll("\\xa0", " "); // Kill those damn \160 chars, which are non-breaking spaces (got them from copy/pasting source examples?)
    return new CompilableContent(res, offset);
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
}
