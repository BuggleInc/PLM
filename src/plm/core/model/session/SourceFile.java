package plm.core.model.session;

import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JScrollPane;
import plm.core.lang.ProgrammingLanguage;
import plm.core.ui.JavaEditorPanel;

public class SourceFile {

  protected String name;
  private final String initialBody;
  private String body;
  private final String template;
  private final String correctionTemplate;
  private final String correctionBody;
  private final String imports;
  private final String remote;
  private final int bodyIndent;
  private ISourceFileListener listener = null;

  /**
   * Computed once, when the entity is first needed, by {@code EntityTemplateParser} (see CONTRIBUTING.md, "From correction entity to
   * compilable source: templating"); {@code null} where there is nothing to template.
   *
   * @param initialBody        what the student sees in the editor the first time
   * @param template           head + "$body" + tail, for the student's code; substituting $body rebuilds a compilable source
   * @param correctionTemplate same as {@code template} but keeping the SOLUTION sections of head/tail, for the correction
   * @param correctionBody     the raw (markers included) BEGIN/END TEMPLATE span, or BEGIN/END SOLUTION when there is no template;
   *                           the {@code $body} value used for {@code StudentOrCorrection.CORRECTION}
   * @param imports            the lines found between BEGIN IMPORT and END IMPORT markers: not part of the templates nor of
   *                           the initial body, but still in {@code correction}
   * @param remote             the RemoteXxx universe guessed by {@code lang.getRemote(correction)}, or null if none
   * @param bodyIndent         how many spaces the templated region is indented by in the entity: the editor content is flush left instead
   */
  public SourceFile(String name, String initialBody, String template, String correctionTemplate, String correctionBody, String imports, String remote,
                    int bodyIndent)
  {
    this.name               = name;
    this.initialBody        = initialBody;
    this.body               = initialBody;
    this.template           = template;
    this.correctionTemplate = correctionTemplate;
    this.correctionBody     = correctionBody;
    this.imports            = imports;
    this.remote             = remote;
    this.bodyIndent         = bodyIndent;
  }

  /** A source file that is not templated: only its editable body matters. */
  public SourceFile(String name) { this(name, "", null, null, null, null, null, 0); }

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

  /** Puts back the body the student saw the first time */
  public void revert(ProgrammingLanguage lang) { setBody(this.initialBody, lang); }

  public String getTemplate() { return template; }
  public String getCorrectionTemplate() { return correctionTemplate; }
  public String getCorrectionBody() { return correctionBody; }
  public String getImports() { return imports; }
  public String getRemote() { return remote; }
  public int getBodyIndent() { return bodyIndent; }

  /**
   * The result of {@link #getCompilableContent(String, String, Map)}: the compilable source text, plus how many
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
   * @param template
   * 			the template to fill, or null to compile the body alone
   * @param body
   * 			what goes in place of {@code $body}: the student-provided content or the correction
   * @param runtimePatterns
   * 			some last-minute replacement to do (such as package name adjustment)
   * @return
   */
  public CompilableContent getCompilableContent(String template, String body, Map<String, String> runtimePatterns)
  {
    String res = template != null ? template : body;

    if (runtimePatterns != null)
      for (Entry<String, String> pattern : runtimePatterns.entrySet())
        res = res.replaceAll(pattern.getKey(), pattern.getValue());

    int offset = 0;
    if (template != null) {
      int bodyIndex = res.indexOf("$body");
      for (int i = 0; i < bodyIndex; i++)
        if (res.charAt(i) == '\n')
          offset++;

      res = res.replace("$body", body + " \n");
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
    result          = PRIME * result + ((initialBody == null) ? 0 : initialBody.hashCode());
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
    if (initialBody == null) {
      if (other.initialBody != null)
        return false;
    } else if (!initialBody.equals(other.initialBody))
      return false;
    return true;
  }

  public JScrollPane getEditorPanel(ProgrammingLanguage lang) { return new JavaEditorPanel(this, lang); }
}
