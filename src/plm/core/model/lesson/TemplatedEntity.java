package plm.core.model.lesson;

/**
 * Result of parsing one {@code XxxEntity.<ext>} source file's {@code BEGIN}/{@code END} {@code TEMPLATE}/{@code SOLUTION}/
 * {@code REMOTE} markers (see {@link EntityTemplateParser} and CONTRIBUTING.md, "From correction entity
 * to compilable source: templating").
 *
 * Computed once, at lesson-load time, language-agnostic beyond a few flattening/marker-syntax quirks (see
 * {@link EntityTemplateParser}); each language's {@code compileExo()} then wraps the templates the way it needs.
 *
 * @param initialContent what the student sees in the editor the first time (templateHead + templateTail)
 * @param template        head + "$body" + tail; substituting $body at compile time rebuilds a compilable source. head/tail
 *                         default to the whole file before/after the templated region, narrowed down to the BEGIN/END REMOTE
 *                         markers when the entity declares them (see {@link EntityTemplateParser#split}); used for the student code
 * @param correctionTemplate same as {@code template} but with the SOLUTION sections of head/tail kept, to compile the correction
 * @param correctionBody  the raw (markers included) BEGIN/END TEMPLATE span, or BEGIN/END SOLUTION when there is no
 *                         template; the {@code $body} value used for {@code StudentOrCorrection.CORRECTION}
 * @param correction      the whole entity file, unchanged except for the class/package name rewrite
 * @param remote          the RemoteXxx universe guessed by {@code lang.getRemote(correction)}, or null if none
 * @param imports         the lines found between BEGIN IMPORT and END IMPORT markers (any number of sections), which are not part of
 *                         {@code template} nor {@code initialContent}, but are still in {@code correction}
 */
public record TemplatedEntity(String initialContent, String template, String correctionTemplate, String correctionBody, String correction, String remote, String imports)
{
}
