package plm.core.model.session;

import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JScrollPane;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.ui.JavaEditorPanel;

public class SourceFile {

  protected String name;
  private String body;
  private String correction;
  private final String template;
  private final String correctionTemplate;
  private final String correctionBody;
  private final String imports;
  private final String remote;
  private ISourceFileListener listener = null;

  /**
   * Computed once, at lesson-load time, by {@code EntityTemplateParser} (see CONTRIBUTING.md, "From correction entity to
   * compilable source: templating"); {@code null} where there is nothing to template.
   *
   * @param initialBody        what the student sees in the editor the first time
   * @param correction         the whole entity file, unchanged except for the class/package name rewrite
   * @param template           head + "$body" + tail, for the student's code; substituting $body rebuilds a compilable source
   * @param correctionTemplate same as {@code template} but keeping the SOLUTION sections of head/tail, for the correction
   * @param correctionBody     the raw (markers included) BEGIN/END TEMPLATE span, or BEGIN/END SOLUTION when there is no template;
   *                           the {@code $body} value used for {@code StudentOrCorrection.CORRECTION}
   * @param imports            the lines found between BEGIN IMPORT and END IMPORT markers: not part of the templates nor of
   *                           the initial body, but still in {@code correction}
   * @param remote             the RemoteXxx universe guessed by {@code lang.getRemote(correction)}, or null if none
   */
  public SourceFile(String name, String initialBody, String correction, String template, String correctionTemplate, String correctionBody, String imports,
                    String remote)
  {
    this.name               = name;
    this.body               = initialBody;
    this.correction         = correction;
    this.template           = template;
    this.correctionTemplate = correctionTemplate;
    this.correctionBody     = correctionBody;
    this.imports            = imports;
    this.remote             = remote;
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

  public String getTemplate() { return template; }
  public String getCorrectionTemplate() { return correctionTemplate; }
  public String getCorrectionBody() { return correctionBody; }
  public String getImports() { return imports; }
  public String getRemote() { return remote; }

  /**
   * The result of {@link #getCompilableContent(String, Map, StudentOrCorrection)}: the compilable source text, plus how many
   * lines of it come before the student/correction body's own first line (see {@code offset} there).
   */
  public record CompilableContent(String content, int offset) {}

  /**
   * Returns the source text that we should compile, alongside the {@code $body} offset computed along the way.
   *
   * The template (if any) is the correction one, which keeps the solution-helper sections of head/tail, or the student one
   * otherwise. It has its {@code $body} placeholder substituted last, after every other {@code runtimePattern}
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
  public CompilableContent getCompilableContent(String template, Map<String, String> runtimePatterns, StudentOrCorrection whatToRetrieve)
  {
    String res = template != null ? template : this.body;

    if (runtimePatterns != null)
      for (Entry<String, String> pattern : runtimePatterns.entrySet())
        res = res.replaceAll(pattern.getKey(), pattern.getValue());

    int offset = 0;
    if (template != null) {
      int bodyIndex = res.indexOf("$body");
      for (int i = 0; i < bodyIndex; i++)
        if (res.charAt(i) == '\n')
          offset++;

      String bodyContent = whatToRetrieve == StudentOrCorrection.CORRECTION ? correctionBody : this.body;
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
