package plm.core.lang;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import plm.core.PLMCompilerException;
import plm.core.lang.primitives.ExternalPrimitiveLanguage;
import plm.core.lang.primitives.PrimitiveMethod;
import plm.core.lang.primitives.PrimitiveParameter;
import plm.universe.Direction;
import plm.universe.Point;

/**
 * Ancestor of Java and Scala: both compile to a runnable jar and generate their ExternalPrimitiveLanguage glue the
 *  same way, only differing in the exact syntax produced.
 */
public abstract class JvmTemplatedLang extends TemplatedRemoteLang {

  public JvmTemplatedLang(String lang, String ext, ImageIcon i) { super(lang, ext, i); }

  /**
   * Everything Java/Scala's compileExo() reads out of one SourceFile's {@code correction}: computed once, eagerly,
   * right after step 1 (see {@code ExerciseTemplated.newSourceFromFile()}), rather than lazily on first compile.
   * Python has its own shape (its {@code template} comes from a nested {@code (template, bodySource)} pair, and it
   * has no {@code rawImports} field at all), so it keeps its own record.
   *
   * @param remote         the guessed RemoteXxx universe, or null if it couldn't be guessed ({@link #checkRemoteOrFail}
   *                       turns that into a compile failure)
   * @param rawImports     the content of any BEGIN/END IMPORT section(s), as split out by the entity parser, NOT the full $imports replacement
   *                       compileExo() builds (which also injects packageNameCache-qualified lines)
   * @param correctionBody see {@link LanguageExtraction#correctionBody()}
   */
  public record JvmExtraction(String remote, String runFunction, String dependency, String rawImports, String template, String correctionBody)
      implements LanguageExtraction
  {
  }

  private static int countOccurrences(String haystack, String needle)
  {
    int count = 0;
    for (int i = haystack.indexOf(needle); i != -1; i = haystack.indexOf(needle, i + needle.length()))
      count++;
    return count;
  }

  /**
   * A correction is allowed to omit '/* BEGIN TEMPLATE *\/' / '/* END TEMPLATE *\/' entirely: that legally means an
   * empty template, as if those markers sat immediately before '/* BEGIN SOLUTION *\/' and immediately after
   * '/* END SOLUTION *\/'. Both validateTemplateWellFormedness() and getCorrectedTemplate() need the exact same
   * [begin, endExclusive) span, whichever pair of markers it actually comes from -- factored here once instead of
   * duplicated in both.
   *
   * @return {beginTemplateIndexRaw, endTemplateIndex, endTemplateIndexEnd}, or null if neither marker pair is present.
   */
  private static int[] effectiveTemplateSpan(String correction)
  {
    String begin = correction.contains("/* BEGIN TEMPLATE */") ? "/* BEGIN TEMPLATE */" : "/* BEGIN SOLUTION */";
    String end   = correction.contains("/* BEGIN TEMPLATE */") ? "/* END TEMPLATE */" : "/* END SOLUTION */";
    if (!correction.contains(begin))
      return null;

    int beginTemplateIndexRaw = correction.indexOf(begin);
    int endTemplateIndex      = correction.indexOf(end);
    int endTemplateIndexEnd   = endTemplateIndex + end.length();
    return new int[] {beginTemplateIndexRaw, endTemplateIndex, endTemplateIndexEnd};
  }

  private static void checkMarkerPair(String correction, String begin, String end) throws PLMCompilerException
  {
    int beginCount = countOccurrences(correction, begin);
    int endCount   = countOccurrences(correction, end);

    if (beginCount == 0 && endCount == 0)
      return;

    if (beginCount != endCount) {
      throw new PLMCompilerException("'" + begin + "' appears " + beginCount + " time(s) but '" + end + "' appears " + endCount +
                                     " time(s): they must be paired one-to-one.");
    }
    if (beginCount > 1) {
      throw new PLMCompilerException("'" + begin + "' / '" + end + "' appear " + beginCount + " times; exactly one pair (or none, for '" + begin +
                                     "') is supported.");
    }
    if (correction.indexOf(begin) > correction.indexOf(end))
      throw new PLMCompilerException("'" + begin + "' appears after '" + end + "': they must appear in that order.");
  }

  /**
   * Refuses to guess when a correction's markup is ambiguous or incomplete -- picking a plausible-looking template shape
   * anyway is exactly what silently duplicated a primitive call in welcome.Environment earlier this session (BEGIN
   * TEMPLATE overlapping run() in a way that doesn't match any of the 3 supported cases, so the "disjoint" fallback
   * fired even though the whole solution was already nested inside an existing run()). Every check here exists because
   * some real correction file, if not rejected, would make getCorrectedTemplate() produce code that compiles but
   * behaves wrong, not code that fails to compile -- the worse failure mode, since nothing points the author at the
   * actual problem. A missing BEGIN/END TEMPLATE pair is not such a case: see effectiveTemplateSpan().
   *
   * @param runKeyword     the run() declaration's exact prefix ("void run(" for Java, "def run(" for Scala)
   * @param exampleRunDecl a realistic full run() declaration, shown in the "no run() found" error message
   */
  private static void validateTemplateWellFormedness(String correction, String runKeyword, String exampleRunDecl) throws PLMCompilerException
  {
    int runCount = countOccurrences(correction, runKeyword);
    if (runCount == 0)
      throw new PLMCompilerException("No '" + runKeyword + "' found in the correction. Every exercise must define exactly one run() method"
                                     + " (e.g. \"" + exampleRunDecl + "\").");
    if (runCount > 1)
      throw new PLMCompilerException("Found " + runCount + " occurrences of '" + runKeyword + "' in the correction, expected exactly one."
                                     + " Rename or remove the extra one(s) (this also matches a run() mentioned only in a comment or string).");

    checkMarkerPair(correction, "/* BEGIN TEMPLATE */", "/* END TEMPLATE */");
    checkMarkerPair(correction, "/* BEGIN SOLUTION */", "/* END SOLUTION */");

    int[] span = effectiveTemplateSpan(correction);
    if (span == null) {
      throw new PLMCompilerException("Neither '/* BEGIN TEMPLATE */' nor '/* BEGIN SOLUTION */' markers found, although run() exists. Add at least"
                                     + " '/* BEGIN SOLUTION */' / '/* END SOLUTION */' around the templated portion -- e.g. right after the run()"
                                     + " declaration and right before its closing \"}\" if the whole run() body is templated, or around a"
                                     + " separate method if run() itself should stay untouched.");
    }
    int beginTemplateIndexRaw = span[0], endTemplateIndex = span[1], endTemplateIndexEnd = span[2];
    int runFunctionI = correction.indexOf(runKeyword);
    int[] runSpan    = extractRunSpan(correction, runKeyword);

    boolean case1 = beginTemplateIndexRaw <= runFunctionI && runFunctionI <= endTemplateIndex;
    boolean case2 = runSpan[0] <= beginTemplateIndexRaw && endTemplateIndexEnd <= runSpan[1];
    boolean case3 = endTemplateIndexEnd <= runSpan[0] || runSpan[1] <= beginTemplateIndexRaw;

    if (!case1 && !case2 && !case3) {
      throw new PLMCompilerException("The '/* BEGIN TEMPLATE */' / '/* END TEMPLATE */' region partially overlaps run() in a way that cannot be"
                                     + " safely interpreted. It must either: (1) contain run()'s own declaration entirely, (2) sit entirely inside"
                                     + " run()'s body, or (3) be entirely disjoint from run() (a separate method). Adjust the marker placement to"
                                     + " match one of these exactly.");
    }
  }

  /**
   * Shared Java/Scala per-compile template rebuild: picks one of three class/object-body shapes depending on whether
   * the correction's templated region (BEGIN/END TEMPLATE, or BEGIN/END SOLUTION when TEMPLATE is absent) contains
   * run()'s own declaration, sits fully inside run()'s braces, or is disjoint from run() -- see
   * {@link #extractRunSpan}. Throws (via {@link #validateTemplateWellFormedness}) rather than guess when the
   * correction's markup doesn't unambiguously match one of these three shapes.
   *
   * @param runKeyword     the run() declaration's exact prefix ("void run(" for Java, "def run(" for Scala)
   * @param exampleRunDecl a realistic full run() declaration, shown in the "no run() found" error message
   * @param wrapperHeader  the package+imports+class/object-opening boilerplate, up to and including the opening "{"
   *                       and its trailing "\n" (contains exactly one "$imports" placeholder)
   * @param runDeclLine    the run() declaration line used to re-wrap the "nested" case ("public void run(){" for
   *                       Java, "def run(): Unit = {" for Scala)
   */
  protected static String getCorrectedTemplate(String correction, String runKeyword, String exampleRunDecl, String wrapperHeader, String runDeclLine)
      throws PLMCompilerException
  {
    validateTemplateWellFormedness(correction, runKeyword, exampleRunDecl);
    int[] span                = effectiveTemplateSpan(correction);
    int beginTemplateIndexRaw = span[0], endTemplateIndex = span[1], endTemplateIndexEnd = span[2];
    int runFunctionI = correction.indexOf(runKeyword);
    int[] runSpan    = extractRunSpan(correction, runKeyword);

    if (beginTemplateIndexRaw <= runFunctionI && runFunctionI <= endTemplateIndex)
      // run()'s own declaration falls inside the templated region: the templated text IS run() (signature included).
      return wrapperHeader + "$dependency\n\t\n$body\n}";
    if (runSpan[0] <= beginTemplateIndexRaw && endTemplateIndexEnd <= runSpan[1])
      // The templated region sits fully inside run()'s braces, but run()'s own declaration line is outside it.
      return wrapperHeader + "$dependency\n\t" + runDeclLine + "\n$body\t}\n}";
    // Genuinely disjoint (validated above): a separate templated method, run() itself untouched.
    return wrapperHeader + "$dependency\n$run\n\t\n$body\n}";
  }

  /**
   * Run "jar cfm &lt;jarFile&gt; &lt;manifest declaring Main-Class: mainClassDotPath&gt; &lt;classFiles...&gt;" from
   *  workDir, throwing if the tool reports anything on stderr.
   */
  protected static void runJarTool(File workDir, File jarFile, String mainClassDotPath, Set<String> classFiles, DiagnosticCollector<JavaFileObject> diagnostic)
      throws PLMCompilerException
  {
    File manifestFile = new File(jarFile.getParentFile(), "MANIFEST.MF");
    try {
      Files.writeString(manifestFile.toPath(), "Main-Class: " + mainClassDotPath + "\n");

      ArrayList<String> args = new ArrayList<>();
      args.add("jar");
      args.add("cfm");
      args.add(jarFile.toPath().toString());
      args.add(manifestFile.toPath().toString());
      args.addAll(classFiles);

      Process proc = Runtime.getRuntime().exec(args.toArray(String[] ::new), new String[] {}, workDir);

      BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
      BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

      String rtStdout = stdInput.lines().collect(Collectors.joining("\n"));
      String rtStderr = stdError.lines().collect(Collectors.joining("\n"));

      if (!rtStderr.isEmpty())
        throw new PLMCompilerException(rtStderr, new HashSet<>(classFiles), new Error(), diagnostic);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Shared skeleton of Java/Scala's ExternalPrimitiveLanguage generator: computing the type declarations and method
   * implementations, and assembling them into a file, is identical between the two -- only the exact syntax produced
   * (getLanguageType, getParameter, getPrototype, getImplementation, the file's header/wrapper
   * and extension) differs, and is left to subclasses. getReturning() happens to be syntactically identical in both
   * (same getAnswerXxx() wire-side method names), so it is implemented once here.
   */
  public abstract static class JvmExternalPrimitiveGenerator implements ExternalPrimitiveLanguage {

    abstract String getLanguageType(Class<?> type);
    abstract String getTypeDeclaration(Class<?> type);
    abstract String getParameter(PrimitiveParameter parameter);
    abstract String getPrototype(PrimitiveMethod method);
    abstract String getImplementation(PrimitiveMethod method);

    /** File extension including the dot, e.g. ".java" or ".scala". */
    abstract String fileExtension();
    /**
     * Wrap the generated body (type declarations + method implementations + extra code) into the full file content:
     *  header comment, imports, and the language's class/object wrapper syntax.
     */
    abstract String wrapCode(String name, String body);

    String getReturning(Class<?> type)
    {
      if (type == null)
        return "";

      if (type == String.class)
        return "getAnswerString()";
      if (type == Double.class || type == double.class)
        return "getAnswerDouble()";
      if (type == Character.class || type == char.class)
        return "getAnswerChar()";
      if (type == Color.class)
        return "getAnswerColor()";
      if (type == Direction.class)
        return "getAnswerInt()";
      if (type == Point.class)
        return "(Point)getAnswerObject()";
      if (type == Point[].class)
        return "(Point[])getAnswerObject()";
      if (type == Integer.class || type == int.class)
        return "getAnswerInt()";
      if (type == Boolean.class || type == boolean.class)
        return "getAnswerBoolean()";
      if (type == void.class || type == Void.class)
        return "";

      throw new IllegalStateException("Unknown type: " + type);
    }

    @Override public void generate(File folder, String name, List<PrimitiveMethod> methods) throws IOException { generate(folder, name, methods, ""); }

    @Override public void generate(File folder, String name, List<PrimitiveMethod> methods, String extraCode) throws IOException
    {
      Set<Class<?>> involved = ExternalPrimitiveLanguage.involved(methods);

      final String type_declarations = involved.stream().map(this::getTypeDeclaration).filter(o -> !o.isBlank()).collect(Collectors.joining("\n\n"));
      final String implementations   = methods.stream().map(this::getImplementation).collect(Collectors.joining("\n\n"));

      String body = "\n" + type_declarations + "\n" + implementations;
      if (!extraCode.isBlank())
        body += "\n" + extraCode;

      Files.writeString(new File(folder, name + fileExtension()).toPath(), wrapCode(name, body));
    }
  }
}
