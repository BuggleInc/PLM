package plm.core.lang;

/**
 * The per-language pieces extracted once from one entity file (step 2, see CONTRIBUTING.md), eagerly at lesson-load time,
 * as returned by {@link ProgrammingLanguage#extract(String, String, String, String, String)}. The same record is used by
 * every language; fields that a language doesn't need are left as-is or {@code null}. {@link plm.core.model.session.SourceFile}
 * only reads the templates and the body; each language's own {@code compileExo()} reads the rest back from
 * {@code SourceFile.getExtraction()}.
 *
 * @param remote             the guessed RemoteXxx universe, or null if it couldn't be guessed ({@code checkRemoteOrFail} turns that into a
 *                           compile failure); always null for C, which has no remote glue guessing
 * @param rawImports         the content of any BEGIN/END IMPORT section(s), as split out by the entity parser, NOT the full $imports
 *                           replacement compileExo() builds; only read by Java and Scala
 * @param template           the "head + $body + tail" shape to fill in for the student's code, wrapped the way the language needs
 * @param correctionTemplate same shape, but with the solution-helper sections of head/tail kept: used to compile the correction
 * @param correctionBody     the {@code $body} value to use for {@code StudentOrCorrection.CORRECTION}
 *                           (STUDENT always uses the SourceFile's own body)
 */
public record LanguageExtraction(String remote, String rawImports, String template, String correctionTemplate, String correctionBody) {}
