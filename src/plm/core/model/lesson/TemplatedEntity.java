package plm.core.model.lesson;

import plm.core.lang.LanguageExtraction;

/**
 * Result of parsing one {@code XxxEntity.<ext>} source file's {@code BEGIN}/{@code END} {@code TEMPLATE}/{@code SOLUTION}/
 * {@code HIDDEN} markers (see {@link EntityTemplateParser} and CONTRIBUTING.md, "From correction entity to compilable
 * source: templating"), plus that language's own extraction out of the resulting {@code correction} text.
 *
 * {@code initialContent}/{@code template}/{@code offset}/{@code correction} are step 1 (computed once, at lesson-load
 * time, language-agnostic beyond a few flattening/marker-syntax quirks -- see {@link EntityTemplateParser}).
 * {@code extraction} is step 2 (run() body, dependencies, imports, remote-world hint, corrected template shape --
 * language-specific, e.g. {@code JvmExtraction}/{@code PythonExtraction}), computed right after step 1 by
 * {@code ExerciseTemplated.newSourceFromFile()} (via {@code ProgrammingLanguage.extract()}) rather than by
 * {@link EntityTemplateParser} itself, so this class stays unaware of any per-language marker syntax. {@code null} for
 * languages that need no such extraction (C).
 *
 * @param initialContent what the student sees in the editor the first time (templateHead + templateTail)
 * @param template        head + "$body" + tail; substituting $body at compile time rebuilds a compilable source
 * @param offset          how many physical lines of the compiled file come before {@code $body}'s own first line (head's line
 *                         count, or 0 when head is flattened onto a single line, see {@code EntityTemplateParser}), used to
 *                         translate a compiler error's line number back to the student's own editor coordinates
 * @param correction      the whole entity file, unchanged except for the class/package name rewrite
 * @param imports         the lines found between BEGIN IMPORT and END IMPORT markers (any number of sections), which are not part of
 *                         {@code template} nor {@code initialContent}, but are still in {@code correction}
 * @param dependencies    the lines found between BEGIN DEPENDENCY and END DEPENDENCY markers (any number of sections), same rules as
 *                         {@code imports}
 * @param extraction      this language's own once-computed extraction out of {@code correction}, or {@code null}
 */
public record TemplatedEntity(String initialContent, String template, int offset, String correction, String imports, String dependencies,
                              LanguageExtraction extraction)
{
}
