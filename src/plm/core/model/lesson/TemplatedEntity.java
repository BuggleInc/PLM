package plm.core.model.lesson;

/**
 * Result of parsing one {@code XxxEntity.<ext>} source file's {@code BEGIN}/{@code END} {@code TEMPLATE}/{@code SOLUTION}/
 * {@code HIDDEN} markers (see {@link EntityTemplateParser} and CONTRIBUTING.md, "From correction entity to compilable
 * source: templating").
 *
 * This only captures step 1 of the templating pipeline (computed once, at lesson-load time). The per-language extraction
 * done once per {@code SourceFile} by each {@code ProgrammingLanguage} (run() body, dependencies, imports, remote-world
 * hint, corrected template shape -- step 2) is a separate, cached record of its own (e.g. {@code JvmExtraction},
 * {@code PythonExtraction}), not part of this one.
 *
 * @param initialContent what the student sees in the editor the first time (templateHead + templateTail)
 * @param template        head + "$body" + tail; substituting $body at compile time rebuilds a compilable source
 * @param offset          number of lines in head, used to translate a compiler error's line number back to the
 *                         student's own editor coordinates
 * @param correction      the whole entity file, unchanged except for the class/package name rewrite; re-parsed by
 *                         each language's own compileExo() to extract dependencies/imports/run()
 */
public record TemplatedEntity(String initialContent, String template, int offset, String correction) {}
