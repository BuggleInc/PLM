package plm.core.lang;

/**
 * Marker for the per-language, once-computed extraction records ({@code JvmExtraction}, {@code PythonExtraction},
 * {@code TemplatedRemoteLang.SimpleExtraction}) returned by {@link ProgrammingLanguage#extract(String, String, String)}.
 * {@link SourceFile} only needs these two accessors to build a compilable source; the rest of each record ({@code
 * remote}, {@code runFunction}, {@code dependency}, ...) stays language-specific, read back by that language's own
 * {@code compileExo()} via a cast (see e.g. {@code LangJava.compileExo()}).
 *
 * @see plm.core.model.session.SourceFile
 */
public interface LanguageExtraction {
  /** The "head + $body + tail" shape to fill in; {@code null} if this SourceFile isn't compilable at all (e.g. LightBot). */
  String template();

  /** The `$body` value to use for {@code StudentOrCorrection.CORRECTION} (STUDENT always uses the SourceFile's own body). */
  String correctionBody();
}
