package plm.core.model.session;

public class SourceFile {

  private final String remote;
  private final String imports;
  private final int bodyIndent;
  private final EntityFileSegments student;
  private final EntityFileSegments correction;

  /**
   * Computed once, when the entity is first needed, by {@code EntityTemplateParser} (see CONTRIBUTING.md, "From correction entity to
   * compilable source: templating"); {@code null} where there is nothing to template.
   *
   * @param remote     the RemoteXxx universe guessed by {@code EntityTemplateParser}, or null if none
   * @param imports    the lines found between BEGIN IMPORT and END IMPORT markers: not part of the segments, but still in
   *                   the entity file
   * @param bodyIndent how many spaces the templated region is indented by in the entity: the editor content is flush left instead
   * @param student    pre/post wrapping the student's code, and the body the student sees in the editor the first time
   * @param correction pre/post wrapping the correction's body, which keeps the SOLUTION sections and the raw BEGIN/END
   *                   TEMPLATE span (or BEGIN/END SOLUTION when there is no template), markers included
   */
  public SourceFile(String remote, String imports, int bodyIndent, EntityFileSegments student, EntityFileSegments correction)
  {
    this.remote     = remote;
    this.imports    = imports;
    this.bodyIndent = bodyIndent;
    this.student    = student;
    this.correction = correction;
  }

  /** A source file that is not templated. Lightbot uses it. */
  public SourceFile() { this(null, null, 0, new EntityFileSegments("", "", ""), null); }

  /** The body the student sees in the editor the first time */
  public String getInitialBody() { return student.body(); }

  /** The pieces of the correction to compile */
  public EntityFileSegments getCorrectionSegments() { return correction; }

  /** The pieces of the student's program to compile, wrapped around {@code body} */
  public EntityFileSegments getStudentSegments(String body) { return student.withBody(body); }

  public String getRemote() { return remote; }
  public String getImports() { return imports; }
  public int getBodyIndent() { return bodyIndent; }
}
