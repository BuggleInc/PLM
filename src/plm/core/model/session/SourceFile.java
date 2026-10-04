package plm.core.model.session;

import javax.swing.JScrollPane;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.ui.JavaEditorPanel;

public class SourceFile {

  protected String name;
  private final EntityFileSegments student;
  private final EntityFileSegments correction;
  private String body;
  private final String imports;
  private final String remote;
  private final int bodyIndent;
  private ISourceFileListener listener = null;

  /**
   * Computed once, when the entity is first needed, by {@code EntityTemplateParser} (see CONTRIBUTING.md, "From correction entity to
   * compilable source: templating"); {@code null} where there is nothing to template.
   *
   * @param student    pre/post wrapping the student's code, and the body the student sees in the editor the first time
   * @param correction pre/post wrapping the correction's body, which keeps the SOLUTION sections and the raw BEGIN/END
   *                   TEMPLATE span (or BEGIN/END SOLUTION when there is no template), markers included
   * @param imports    the lines found between BEGIN IMPORT and END IMPORT markers: not part of the segments, but still in
   *                   the entity file
   * @param remote     the RemoteXxx universe guessed by {@code EntityTemplateParser}, or null if none
   * @param bodyIndent how many spaces the templated region is indented by in the entity: the editor content is flush left instead
   */
  public SourceFile(String name, EntityFileSegments student, EntityFileSegments correction, String imports, String remote, int bodyIndent)
  {
    this.name       = name;
    this.student    = student;
    this.correction = correction;
    this.body       = student.body();
    this.imports    = imports;
    this.remote     = remote;
    this.bodyIndent = bodyIndent;
  }

  /** A source file that is not templated: only its editable body matters. */
  public SourceFile(String name) { this(name, new EntityFileSegments("", "", ""), null, null, null, 0); }

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
  public void revert(ProgrammingLanguage lang) { setBody(student.body(), lang); }

  /** The pieces of the program to compile: the correction's, or the student's wrapped around the current editor content. */
  public EntityFileSegments getSegments(StudentOrCorrection whatToCompile)
  {
    return whatToCompile == StudentOrCorrection.CORRECTION ? correction : student.withBody(getBody());
  }

  public String getImports() { return imports; }
  public String getRemote() { return remote; }
  public int getBodyIndent() { return bodyIndent; }

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
    result          = PRIME * result + student.body().hashCode();
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
    return student.body().equals(other.student.body());
  }

  public JScrollPane getEditorPanel(ProgrammingLanguage lang) { return new JavaEditorPanel(this, lang); }
}
