package plm.core.lang;

import java.awt.Color;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import org.checkerframework.checker.nullness.qual.NonNull;
import plm.core.PLMCompilerException;
import plm.core.lang.primitives.ExternalPrimitiveLanguage;
import plm.core.lang.primitives.PrimitiveMethod;
import plm.core.lang.primitives.PrimitiveParameter;
import plm.core.model.Game;
import plm.core.model.LogWriter;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.RunOutcome;
import plm.core.model.session.SourceFile;
import plm.core.ui.ResourcesCache;
import plm.universe.Direction;
import plm.universe.Point;

/**
 * Remote execution for Scala, mirroring LangJava's architecture as closely as possible on purpose (see the class-level
 * comment there): an external "java" process is spawned per run, running compiled Scala bytecode that talks back to
 * CommandExecutor over a UNIX domain socket. Compilation is done by driving the SAME scala-compiler.jar the previously
 * embedded in-JVM compiler used (scala.tools.nsc.Main), but as a *separate* "java -cp <scala jars> scala.tools.nsc.Main"
 * process, to avoid assuming a standalone "scalac" binary is installed on the machine.
 *
 * Not yet factored with LangJava (structure kept close on purpose to make that factoring easy later); several private
 * helpers below are near-verbatim ports of LangJava's, adapted to Scala syntax where the generated code shape differs.
 */
public class LangScala extends TemplatedRemoteLang {
  /**
   * Extra source files to be copied alongside the student's code
   */
  private static final Map<String, List<String>> remoteExtraSourceFiles = Map.of("RemoteCons", List.of("src/lessons/recursion/cons/universe/RecList.java"));
  /* Language detection logic */
  private static String brokenLanguageMessage;
  private static BrokenLanguageState brokenLanguageState = BrokenLanguageState.Unitialized;
  File tempFolder                                        = new File(System.getProperty("java.io.tmpdir"), "plm_scala_toremove");

  public LangScala() { super("Scala", "scala", ResourcesCache.getIcon("img/lang_scala.png")); }
  @Override public boolean isScala() { return true; }

  @Override public String getBrokenLanguageMessage() { return brokenLanguageMessage; }
  @SuppressWarnings({"rawtypes", "unchecked"}) @Override public boolean isBrokenLanguage()
  {
    if (brokenLanguageState == BrokenLanguageState.Unitialized) {
      String[] resources = new String[] {"/scala/tools/nsc/Interpreter", "/scala/Unit", "/scala/reflect/io/AbstractFile"};
      String[] hints     = new String[] {"scala-compiler.jar", "scala-library.jar", "scala-reflect.jar"};
      for (int i = 0; i < resources.length; i++) {
        brokenLanguageMessage = canResolve(resources[i], hints[i]);
        if (!brokenLanguageMessage.isEmpty()) {
          System.err.println(brokenLanguageState);
          brokenLanguageState = BrokenLanguageState.NotUsable;
          return false;
        }
      }

      String version = "";
      try {
        Class props = Class.forName("scala.util.Properties");
        Method meth = props.getMethod("versionString", new Class[] {});
        version     = (String)meth.invoke(props);
      } catch (Exception e) {
        brokenLanguageMessage = Game.i18n.tr("Error {0} while retrieving the Scala version: {1}", e.getClass().getName(), e.getLocalizedMessage());
        System.err.println(brokenLanguageMessage);
        brokenLanguageState = BrokenLanguageState.NotUsable;
        return false;
      }

      if (version.contains("version 2.12") || version.contains("version 2.13")) {
        brokenLanguageState = BrokenLanguageState.Usable;
      } else {
        brokenLanguageMessage = Game.i18n.tr("Scala is too ancient. Found {0} while I need 2.12 or higher.", version);
        System.err.println(brokenLanguageMessage);
        brokenLanguageState = BrokenLanguageState.NotUsable;
        return false;
      }
    }
    return brokenLanguageState != BrokenLanguageState.Usable;
  }

  /**
   * e.g. "src/lessons/recursion/cons/universe/RecList.java" -> "lessons.recursion.cons.universe.RecList"
   */
  private static String fqcnFromSourcePath(String sourcePath)
  {
    String withoutSrcPrefix = sourcePath.startsWith("src/") ? sourcePath.substring("src/".length()) : sourcePath;
    String withoutExtension = withoutSrcPrefix.replaceFirst("\\.(java|scala)$", "");
    return withoutExtension.replace('/', '.');
  }

  /**
   * e.g. "src/lessons/recursion/cons/universe/RecList.java" -> "RecList"
   */
  private static String fileNameWithoutExtension(String path)
  {
    String name = new File(path).getName();
    int dot     = name.lastIndexOf('.');
    return dot < 0 ? name : name.substring(0, dot);
  }

  private static final String RUN_KEYWORD = "def run(";

  private static int countOccurrences(String haystack, String needle)
  {
    int count = 0;
    for (int i = haystack.indexOf(needle); i != -1; i = haystack.indexOf(needle, i + needle.length()))
      count++;
    return count;
  }

  /**
   * Refuses to guess when a correction's markup is ambiguous or incomplete -- picking a plausible-looking template shape
   * anyway is exactly what silently duplicated a primitive call in welcome.Environment earlier this session (no BEGIN
   * TEMPLATE markers at all, so the "disjoint" fallback fired even though the whole solution was already nested inside
   * an existing run()). Every check here exists because some real correction file, if not rejected, would make
   * getCorrectedTemplate() produce code that compiles but behaves wrong, not code that fails to compile -- the worse
   * failure mode, since nothing points the author at the actual problem.
   */
  private static void validateTemplateWellFormedness(String correction) throws PLMCompilerException
  {
    int runCount = countOccurrences(correction, RUN_KEYWORD);
    if (runCount == 0)
      throw new PLMCompilerException("No '" + RUN_KEYWORD + "' found in the correction. Every Scala exercise must define exactly one run() method"
                                     + " (e.g. \"def run(): Unit = { ... }\").");
    if (runCount > 1)
      throw new PLMCompilerException("Found " + runCount + " occurrences of '" + RUN_KEYWORD + "' in the correction, expected exactly one."
                                     + " Rename or remove the extra one(s) (this also matches a run() mentioned only in a comment or string).");

    checkMarkerPair(correction, "/* BEGIN TEMPLATE */", "/* END TEMPLATE */");
    checkMarkerPair(correction, "/* BEGIN SOLUTION */", "/* END SOLUTION */");

    boolean hasTemplateMarkers = correction.contains("/* BEGIN TEMPLATE */");
    if (!hasTemplateMarkers) {
      throw new PLMCompilerException("No '/* BEGIN TEMPLATE */' / '/* END TEMPLATE */' markers found, although run() exists. Add them explicitly around"
                                     + " the templated portion -- e.g. right after \"def run() {\" and right before its closing \"}\" if the whole run()"
                                     + " body is templated, or around a separate method if run() itself should stay untouched.");
    }

    int beginTemplateIndexRaw = correction.indexOf("/* BEGIN TEMPLATE */");
    int endTemplateIndex      = correction.indexOf("/* END TEMPLATE */");
    int endTemplateIndexEnd   = endTemplateIndex + "/* END TEMPLATE */".length();
    int runFunctionI          = correction.indexOf(RUN_KEYWORD);
    int[] runSpan             = ExerciseTemplated.extractRunSpan(correction, RUN_KEYWORD);

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

  private static void checkMarkerPair(String correction, String begin, String end) throws PLMCompilerException
  {
    int beginCount = countOccurrences(correction, begin);
    int endCount   = countOccurrences(correction, end);

    if (beginCount == 0 && endCount == 0)
      return;

    if (beginCount != endCount) {
      throw new PLMCompilerException("'" + begin + "' appears " + beginCount + " time(s) but '" + end + "' appears " + endCount
                                     + " time(s): they must be paired one-to-one.");
    }
    if (beginCount > 1) {
      throw new PLMCompilerException("'" + begin + "' / '" + end + "' appear " + beginCount + " times; exactly one pair (or none, for '"
                                     + begin + "') is supported.");
    }
    if (correction.indexOf(begin) > correction.indexOf(end))
      throw new PLMCompilerException("'" + begin + "' appears after '" + end + "': they must appear in that order.");
  }

  /**
   * Scala counterpart of LangJava's getCorrectedTemplate(), but built on ExerciseTemplated.extractRunSpan() (the shared,
   * containment-based utility -- see its javadoc) instead of duplicating that logic locally.
   *
   * The LangJava version is separated for now (TBD) and computes the offset to ensure that the presented error messages match the code.
   */
  public static @NonNull String getCorrectedTemplate(String correction) throws PLMCompilerException
  {
    validateTemplateWellFormedness(correction);
    int beginTemplateIndexRaw = correction.indexOf("/* BEGIN TEMPLATE */");
    int endTemplateIndex      = correction.indexOf("/* END TEMPLATE */");
    int endTemplateIndexEnd   = endTemplateIndex + "/* END TEMPLATE */".length();
    int runFunctionI          = correction.indexOf(RUN_KEYWORD);

    int[] runSpan = ExerciseTemplated.extractRunSpan(correction, RUN_KEYWORD);

    String template;
    if (beginTemplateIndexRaw <= runFunctionI && runFunctionI <= endTemplateIndex) {
      // run()'s own declaration falls inside the templated region: the templated text IS run() (signature included).
      template = "$package\n\n$imports\n\nobject Entity {\n$dependency\n\t\n$body\n}";
    } else if (runSpan[0] <= beginTemplateIndexRaw && endTemplateIndexEnd <= runSpan[1]) {
      // The templated region sits fully inside run()'s braces, but run()'s own declaration line is outside it.
      template = "$package\n\n$imports\n\nobject Entity {\n$dependency\n\tdef run(): Unit = {\n$body\t}\n}";
    } else {
      // Genuinely disjoint (validated above): a separate templated method, run() itself untouched.
      template = "$package\n\n$imports\n\nobject Entity {\n$dependency\n$run\n\t\n$body\n}";
    }
    return template;
  }

  private static String extractRunFunction(String code) { return ExerciseTemplated.extractRunFunction(code, RUN_KEYWORD); }

  private static String extractRunDependency(String code) { return extractMarkedSection(code, "/* BEGIN DEPENDENCY */", "/* END DEPENDENCY */"); }

  private static String extractImportDependency(String code) { return extractMarkedSection(code, "/* BEGIN IMPORT */", "/* END IMPORT */"); }

  /**
   * Absolute path of the jar a given class was loaded from -- used to locate scala-library.jar/scala-compiler.jar/
   * scala-reflect.jar on disk (already proven present as PLM dependencies by isBrokenLanguage() above), so the external
   * "java" processes below can be given an explicit classpath without assuming any standalone "scalac"/"scala" binary is
   * installed on the machine.
   */
  private static String jarPathFor(Class<?> cls)
  {
    try {
      return new File(cls.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath();
    } catch (Exception e) {
      throw new RuntimeException("Cannot locate the jar providing " + cls.getName() + ". Is Scala properly installed?", e);
    }
  }

  private static String scalaCompilerClasspath()
  {
    return String.join(File.pathSeparator, jarPathFor(scala.tools.nsc.Main.class), jarPathFor(scala.collection.immutable.List.class),
                       jarPathFor(scala.reflect.io.AbstractFile.class));
  }

  private static String scalaLibraryJar() { return jarPathFor(scala.collection.immutable.List.class); }

  private static void compileScalaFiles(DiagnosticCollector<JavaFileObject> diagnostic, File packageFolder, File... files) throws PLMCompilerException
  {
    List<File> javaFiles  = Arrays.asList(files).stream().filter(f -> f.getName().endsWith(".java")).toList();
    List<File> scalaFiles = Arrays.asList(files).stream().filter(f -> f.getName().endsWith(".scala")).toList();
    List<String> paths    = Arrays.asList(files).stream().map(s -> s.toPath().toString()).toList();

    if (!javaFiles.isEmpty()) {
      ArrayList<String> javacArgs = new ArrayList<>();
      javacArgs.add("javac");
      javacArgs.add("-d");
      javacArgs.add(".");
      javaFiles.forEach(f -> javacArgs.add(f.getName()));
      runToolProcess(diagnostic, packageFolder, paths, javacArgs);
    }

    if (!scalaFiles.isEmpty()) {
      ArrayList<String> scalacArgs = new ArrayList<>();
      scalacArgs.add("java");
      scalacArgs.add("-cp");
      scalacArgs.add(scalaCompilerClasspath());
      scalacArgs.add("scala.tools.nsc.Main");
      scalacArgs.add("-classpath");
      // "." : packageFolder itself, where javac (above, if any) just wrote the already-compiled Java classes.
      scalacArgs.add(scalaCompilerClasspath() + File.pathSeparator + ".");
      scalacArgs.add("-d");
      scalacArgs.add(".");
      scalaFiles.forEach(f -> scalacArgs.add(f.getName()));
      runToolProcess(diagnostic, packageFolder, paths, scalacArgs);
    }
  }

  private static void runToolProcess(DiagnosticCollector<JavaFileObject> diagnostic, File packageFolder, List<String> paths, List<String> args)
      throws PLMCompilerException
  {
    try {
      String[] params = args.toArray(String[] ::new);
      Process proc    = Runtime.getRuntime().exec(params, new String[] {}, packageFolder);

      BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
      BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

      String rtStdout = stdInput.lines().collect(Collectors.joining("\n"));
      String rtStderr = stdError.lines().collect(Collectors.joining("\n"));

      int retcode = -1;
      try {
        retcode = proc.waitFor();
      } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
      }

      if (retcode != 0 || !rtStderr.isBlank()) {
        String msg = "The following command failed: " + String.join(" ", params);
        msg += rtStderr.isBlank() ? rtStdout : rtStderr;

        throw new PLMCompilerException(msg.toString(), new HashSet<>(paths), new Error(), diagnostic);
      }
    } catch (IOException e) {
      throw new PLMCompilerException(e.getMessage(), new HashSet<>(paths), new Error(), diagnostic);
    }
  }

  /**
   * Builds a runnable jar for the compiled classes, with a Class-Path manifest entry pointing at scala-library.jar so
   * "java -jar" alone (no external -cp needed at run time, mirroring LangJava's runEntity() as closely as possible) can
   * run compiled Scala bytecode.
   */
  private static void createJarFile(DiagnosticCollector<JavaFileObject> diagnostic, File packageFolder, File jarFile, String mainClassDotPath,
                                    Set<String> classFiles) throws PLMCompilerException
  {
    File manifestFile = new File(packageFolder, "MANIFEST.MF");
    try {
      Files.writeString(manifestFile.toPath(), "Main-Class: " + mainClassDotPath + "\n");

      ArrayList<String> args = new ArrayList<>();
      args.add("jar");
      args.add("cfm");
      args.add(jarFile.toPath().toString());
      args.add(manifestFile.toPath().toString());
      args.addAll(classFiles);

      Process proc = Runtime.getRuntime().exec(args.toArray(String[] ::new), new String[] {}, packageFolder);

      BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
      BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

      String rtStdout = stdInput.lines().collect(Collectors.joining("\n"));
      String rtStderr = stdError.lines().collect(Collectors.joining("\n"));

      if (!rtStderr.isEmpty()) {
        throw new PLMCompilerException(rtStderr, new HashSet<>(classFiles), new Error(), diagnostic);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String getRemoteScalaFile(String remoteName, String packageName)
  {
    String remote;
    if (remoteName == null || remoteName.isEmpty())
      remote = "Remote.scala";
    else
      remote = remoteName;

    if (!remote.startsWith("Remote"))
      remote = "Remote" + remote;
    if (!remote.endsWith(".scala"))
      remote = remote + ".scala";

    String path = "resources/langages/scala/" + remote;

    InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
    if (stream == null) {
      throw new IllegalArgumentException("Remote '" + path + "' do not exist (argument passed: '" + remoteName + "').");
    }

    String packageDeclaration = "package " + packageName;
    String remoteCode         = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n"));

    if (remoteCode.startsWith("package")) {
      remoteCode = remoteCode.replaceFirst("package .*", packageDeclaration);
    } else {
      remoteCode = packageDeclaration + "\n" + remoteCode;
    }

    return remoteCode;
  }

  private String copyFile(String path, String packageName) throws IOException
  {
    String content = Files.readString(new File(path).toPath(), StandardCharsets.UTF_8);
    // Java source files copied in verbatim (e.g. RecList.java, ValueSerializer.java, Point.java) use "package x.y.z;",
    // Scala's own generated files use "package x.y.z" (no semicolon) -- replaceFirst matches either.
    content = content.replaceFirst("package [^;\\n]*;?", "package " + packageName + (path.endsWith(".java") ? ";" : ""));
    return content;
  }

  @Override public void compileExo(Exercise exo, LogWriter out, StudentOrCorrection whatToCompile) throws PLMCompilerException
  {
    /* Make sure each run generate a new package to avoid that the loader cache prevent the reloading of the newly generated class */
    packageNameSuffix++;
    String packageNameCache = packageName();

    Map<String, String> runtimePatterns = new TreeMap<String, String>();
    runtimePatterns.put("\\$package", "package " + packageNameCache + ";");

    String mainRemoteContent =
        getRemoteScalaFile(null, packageNameCache).replace("import ValueSerializer._", "import " + packageNameCache + ".ValueSerializer._");

    DiagnosticCollector<JavaFileObject> diagnostic = new DiagnosticCollector<JavaFileObject>();
    try {
      for (SourceFile sf : exo.getSourceFilesList(this)) {
        String key = packageNameCache + "." + sf.getName();

        String correction = sf.getCorrection();

        String remote = getRemote(correction);
        if (remote == null) {
          PLMCompilerException e = new PLMCompilerException("This universe is not implemented in Scala.", null, diagnostic);
          exo.lastResult         = RunOutcome.newCompilationError(e.getMessage());
          throw e;
        }

        String runFunction = extractRunFunction(correction);
        String dependency  = extractRunDependency(correction);
        String imports     = extractImportDependency(correction);

        runtimePatterns.put("\\$run", runFunction);
        runtimePatterns.put("\\$dependency", dependency);
        runtimePatterns.put("\\$imports", ("import " + packageNameCache + ".ValueSerializer._; "
                                           + "import java.awt.Color; "
                                           + "import " + packageNameCache + ".Remote._; "
                                           + "import " + packageNameCache + "." + remote + "._; " + imports)
                                              .replace('\n', ' '));

        String template = getCorrectedTemplate(correction);
        sf.setTemplate(template);

        String entityCode = sf.getCompilableContent(runtimePatterns, whatToCompile);
        entityCode        = Pattern.compile("([^a-zA-Z])(Direction)([^a-zA-Z.])").matcher(entityCode).replaceAll("$1Int$3");
        entityCode        = Pattern.compile("this\\.").matcher(entityCode).replaceAll("");
        // Scala's "override" needs a real supertype member to override, but Entity is a flat `object` extending nothing
        // so we strip "override"s just as LangJava strips "@Override" there for the exact same reason.
        entityCode = Pattern.compile("\\boverride\\b").matcher(entityCode).replaceAll("");

        File workspace = new File(tempFolder, key.substring(0, key.lastIndexOf('.')).replace('.', '/'));
        workspace.mkdirs();

        File mainRemote = new File(workspace, "Remote.scala");

        String entityRemoteContent = getRemoteScalaFile(remote, packageNameCache).replace("import Remote._", "import " + packageNameCache + ".Remote._");
        File entityRemote          = new File(workspace, remote + ".scala");

        File entityFile = new File(workspace, "Entity.scala");
        File mainFile   = new File(workspace, "Main.scala");

        String mainContent = "package " + packageNameCache + "\n"
                             + "\n"
                             + "object Main {\n"
                             + "  def main(args: Array[String]): Unit = {\n"
                             + "    try {\n"
                             + "      Remote.connect(args(0))\n"
                             + "      Entity.run()\n"
                             + "    } catch {\n"
                             + "      case e: Exception =>\n"
                             + "        e.printStackTrace()\n"
                             + "        System.exit(1)\n"
                             + "    }\n"
                             + "    System.exit(0)\n"
                             + "  }\n"
                             + "}\n";

        try {
          File valueSerializer = new File(workspace, "ValueSerializer.java");

          List<String> extraSourcePaths = new ArrayList<>(List.of("src/plm/universe/Point.java"));
          extraSourcePaths.addAll(remoteExtraSourceFiles.getOrDefault(remote, List.of()));

          // See LangJava.compileExo()'s identical comment: same reasoning, same fix, applied here too.
          java.util.function.UnaryOperator<String> rewriteExtraImports = content ->
          {
            for (String sourcePath : extraSourcePaths) {
              String originalFqcn = fqcnFromSourcePath(sourcePath);
              String simpleName   = fileNameWithoutExtension(sourcePath);
              content             = content.replace("import " + originalFqcn + ";", "import " + packageNameCache + "." + simpleName + ";");
            }
            return content;
          };

          entityCode = rewriteExtraImports.apply(entityCode);
          Files.writeString(valueSerializer.toPath(), rewriteExtraImports.apply(copyFile("src/plm/core/ValueSerializer.java", packageNameCache)));

          List<File> extraFiles = new ArrayList<>();
          for (String sourcePath : extraSourcePaths) {
            File extraFile = new File(workspace, new File(sourcePath).getName());
            Files.writeString(extraFile.toPath(), rewriteExtraImports.apply(copyFile(sourcePath, packageNameCache)));
            extraFiles.add(extraFile);
          }

          Files.writeString(new File(workspace, "Template.txt").toPath(), template);
          Files.writeString(new File(workspace, "Correction.txt").toPath(), correction);
          Files.writeString(mainRemote.toPath(), mainRemoteContent);
          Files.writeString(entityRemote.toPath(), entityRemoteContent);
          Files.writeString(entityFile.toPath(), entityCode);
          Files.writeString(mainFile.toPath(), mainContent);

          List<File> filesToCompile = new ArrayList<>(List.of(mainFile, mainRemote, entityRemote, entityFile, valueSerializer));
          filesToCompile.addAll(extraFiles);
          compileScalaFiles(diagnostic, workspace, filesToCompile.toArray(File[] ::new));

          // Scala compiles "object Main" to Main.class (plus a Main$.class holding the singleton); the manifest only
          // needs the former as its Main-Class entry point, exactly like a Java class with a static main().
          File jarFile = new File(workspace, "Code.jar");

          // Relative to workspace itself (where scalac actually wrote these under their package-name subdirectories),
          // not to its parent: this is what must end up as each entry's name inside the jar.
          // For example "plm/runtime4/Main.class" needs "-cp jarfile plm.runtime4.Main" to resolve.
          Set<String> classFiles = new HashSet<>();
          try (var walk = Files.walk(workspace.toPath())) {
            walk.filter(p -> p.toString().endsWith(".class")).forEach(p -> classFiles.add(workspace.toPath().relativize(p).toString()));
          }

          createJarFile(diagnostic, workspace, jarFile, packageNameCache + ".Main", classFiles);

          sf.meta.put("SCALA", jarFile.toPath().toString() + "|" + packageNameCache + ".Main");

        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    } catch (PLMCompilerException e) {
      System.err.println(Game.i18n.tr("Compilation error:"));
      exo.lastResult = RunOutcome.newCompilationError(e.getDiagnostics());
      System.err.println(e.getMessage());
      if (out != null)
        out.log(exo.lastResult.compilationError);

      if (Game.getInstance().isDebugEnabled())
        for (SourceFile sf : exo.getSourceFilesList(this))
          System.out.println("Source file " + sf.getName() + ":" + sf.getCompilableContent(runtimePatterns, whatToCompile));

      throw e;
    }
  }

  /**
   * Runs "java -cp &lt;jarPath&gt;:&lt;scala-library.jar&gt; &lt;mainClass&gt; &lt;socketPath&gt;", executable being the
   * "jarPath|mainClass" pair stored by compileExo() (see its sf.meta.put("SCALA", ...)). We cannot use "java -jar" alone
   * because a jar's Class-Path manifest attribute is only reliably resolved for relative paths, while the path of
   * scala-library.jar is probably absolute, leading to silent failures at startup.
   */
  @Override protected ProcessBuilder buildProcess(String executable, Path socketPath) throws IOException
  {
    String[] parts   = executable.split("\\|", 2);
    String jarPath   = parts[0];
    String mainClass = parts.length > 1 ? parts[1] : null;
    if (mainClass == null)
      throw new RuntimeException("Malformed script reference (missing main class): " + executable);

    File exec = new File(jarPath);
    if (!exec.exists())
      throw new RuntimeException(Game.i18n.tr("Error, please recompile the exercise: {0} does not exist", exec.getName()));

    return new ProcessBuilder("java", "-cp", jarPath + File.pathSeparator + scalaLibraryJar(), mainClass, socketPath.toString());
  }

  public static class LangScalaExternalPrimitiveGenerator implements ExternalPrimitiveLanguage {

    String getLanguageType(Class<?> type)
    {
      if (type == Color.class)
        return "Color";
      if (type == Direction.class)
        return "Int";
      if (type == Point.class)
        return "Point";
      if (type == Point[].class)
        return "Array[Point]";
      if (type == Double.class || type == double.class)
        return "Double";
      if (type == Integer.class || type == int.class)
        return "Int";
      if (type == String.class)
        return "String";
      if (type == Character.class || type == char.class)
        return "Char";
      if (type == Boolean.class || type == boolean.class)
        return "Boolean";
      if (type == void.class || type == Void.class)
        return "Unit";

      throw new IllegalStateException("Unknown type: " + type);
    }

    String getTypeDeclaration(Class<?> type)
    {
      if (type == Direction.class) {
        return "object Direction {\n"
            + "\tval NORTH = 0\n"
            + "\tval EAST = 1\n"
            + "\tval SOUTH = 2\n"
            + "\tval WEST = 3\n"
            + "}";
      }
      return "";
    }

    String getParameter(PrimitiveParameter parameter) { return parameter.name() + ": " + getLanguageType(parameter.type()); }

    String getPrototype(PrimitiveMethod method)
    {
      String name                         = method.name();
      List<PrimitiveParameter> parameters = method.parameters();
      Class<?> output                     = method.output();

      final String outputString = Optional.ofNullable(output).map(this::getLanguageType).orElse("Unit");

      return "def " + name + "(" + parameters.stream().map(this::getParameter).collect(Collectors.joining(", ")) + "): " + outputString;
    }

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
        return "getAnswerObject().asInstanceOf[Point]";
      if (type == Point[].class)
        return "getAnswerObject().asInstanceOf[Array[Point]]";
      if (type == Integer.class || type == int.class)
        return "getAnswerInt()";
      if (type == Boolean.class || type == boolean.class)
        return "getAnswerBoolean()";
      if (type == void.class || type == Void.class)
        return "";

      throw new IllegalStateException("Unknown type: " + type);
    }

    String getArgumentExpression(PrimitiveParameter parameter) { return parameter.name(); }

    String getImplementation(PrimitiveMethod method)
    {
      String prototype = getPrototype(method);

      int id      = method.id();
      String name = method.name();

      String command =
          "\tsendCommand(\"" + id + "\", \"" + name + "\"" +
          method.parameters().stream().map(this::getArgumentExpression).map(s -> ", " + s + ".asInstanceOf[Object]").collect(Collectors.joining()) + ")";

      String returning = method.hasReturn() ? "\t" + getReturning(method.output()) : "";

      return prototype + " = {\n" + command + "\n" + returning + "\n}";
    }

    @Override public void generate(File folder, String name, List<PrimitiveMethod> methods) throws IOException { generate(folder, name, methods, ""); }

    @Override public void generate(File folder, String name, List<PrimitiveMethod> methods, String extraCode) throws IOException
    {
      Set<Class<?>> involved = ExternalPrimitiveLanguage.involved(methods);

      final String type_declarations = involved.stream().map(this::getTypeDeclaration).filter(o -> !o.isBlank()).collect(Collectors.joining("\n\n"));

      final String implementations = methods.stream().map(this::getImplementation).collect(Collectors.joining("\n\n"));

      String body = "\n" + type_declarations + "\n" + implementations;
      if (!extraCode.isBlank())
        body += "\n" + extraCode;

      final String code =
          "/* THIS FILE IS GENERATED. DO NOT EDIT */\nimport Remote._\nimport java.awt.Color\n\nobject " + name + " {" + body.replace("\n", "\n\t") + "\n}";

      Files.writeString(new File(folder, name + ".scala").toPath(), code);
    }
  }
}
