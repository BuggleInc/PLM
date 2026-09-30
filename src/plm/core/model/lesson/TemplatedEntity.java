package plm.core.model.lesson;

import plm.core.lang.LanguageExtraction;

/**
 * Result of parsing one {@code XxxEntity.<ext>} source file's {@code BEGIN}/{@code END} {@code TEMPLATE}/{@code SOLUTION}/
 * {@code SOLUTIONHELPER}/{@code REMOTE} markers (see {@link EntityTemplateParser} and CONTRIBUTING.md, "From correction entity
 * to compilable source: templating"), plus that language's own extraction out of the resulting {@code correction} text.
 *
 * {@code initialContent}/{@code template}/{@code correctionBody}/{@code correction} are step 1 (computed once, at
 * lesson-load time, language-agnostic beyond a few flattening/marker-syntax quirks -- see {@link EntityTemplateParser}).
 * {@code extraction} is step 2 (run() body, imports, remote-world hint, corrected template shape --
 * language-specific), computed right after step 1, still inside
 * {@link EntityTemplateParser#parse} (via {@code ProgrammingLanguage.extract()}) even though that class otherwise
 * stays unaware of any per-language marker syntax. {@code null} for languages that need no such extraction (C).
 *
 * @param initialContent what the student sees in the editor the first time (templateHead + templateTail)
 * @param template        head + "$body" + tail; substituting $body at compile time rebuilds a compilable source. head/tail
 *                         default to the whole file before/after the templated region, narrowed down to the BEGIN/END REMOTE
 *                         markers when the entity declares them (see {@link EntityTemplateParser#split}); used for the student code
 * @param correctionTemplate same as {@code template} but with the SOLUTIONHELPER sections of head/tail kept, to compile the correction
 * @param correctionBody  the raw (markers included) BEGIN/END TEMPLATE span, or BEGIN/END SOLUTION when there is no
 *                         template; the {@code $body} value used for {@code StudentOrCorrection.CORRECTION}
 * @param correction      the whole entity file, unchanged except for the class/package name rewrite
 * @param imports         the lines found between BEGIN IMPORT and END IMPORT markers (any number of sections), which are not part of
 *                         {@code template} nor {@code initialContent}, but are still in {@code correction}
 * @param extraction      this language's own once-computed extraction out of {@code correction}, or {@code null}
 */
public record TemplatedEntity(String initialContent, String template, String correctionTemplate, String correctionBody, String correction, String imports,
                              LanguageExtraction extraction)
{
}
