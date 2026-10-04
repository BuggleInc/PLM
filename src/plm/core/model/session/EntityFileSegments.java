package plm.core.model.session;

/**
 * A compilable source split around the editable region: {@code pre + body + post} is the full program to inject in the template
 * (that is composed of the class declaration if needed).
 *
 * @param pre  what comes before the body
 * @param body the editable region
 * @param post what comes after the body
 */
public record EntityFileSegments(String pre, String body, String post)
{

  public EntityFileSegments withBody(String newBody)
  {
    return new EntityFileSegments(pre, newBody, post);
  }
}
