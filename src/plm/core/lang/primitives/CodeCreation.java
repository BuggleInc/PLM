package plm.core.lang.primitives;

import dotty.tools.dotc.Driver;
import dotty.tools.dotc.reporting.Reporter;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import plm.core.lang.LangC;
import plm.core.lang.LangJava;
import plm.core.lang.LangPython;
import plm.core.lang.LangScala;
import plm.universe.Entity;

/**
 * Build-time generator of the RemoteXxx glue files of every language, run by maven after the compilation of the PLM. The Java and
 * Scala glue are then compiled in-process, together with the helper sources they need, into plm-entities-java.jar and
 * plm-entities-scala.jar.
 */
public class CodeCreation {

  /** A glue file to generate: its name and the entity whose primitives it exposes. */
  public record Remote(String name, Class<? extends Entity> entity)
  {
    /** Whether its hand-written glue wraps the generated functions (see Glue.wrapped()) */
    public boolean isWrapped()
    {
      return entity.getAnnotation(Glue.class).wrapped();
    }

    /** The classes of the JDK that its hand-written Java glue replaces (see Glue.replaces()) */
    public List<String> replaces()
    {
      return List.of(entity.getAnnotation(Glue.class).replaces());
    }

    /** The glue of that name, or null if there is none */
    public static Remote named(String name)
    {
      return REMOTES.stream().filter(remote -> remote.name().equals(name)).findFirst().orElse(null);
    }

    /**
     * Its hand-written glue for the generated file of that extension (e.g. ".h"): the content of the file &lt;name&gt;Glue&lt;extension&gt;
     * next to the sources of its entity (e.g. RemoteConsGlue.py), or "" if there is none.
     */
    public String extraGlue(String extension)
    {
      Path file = Path.of("src", entity.getPackageName().replace('.', '/'), name + "Glue" + extension);
      try {
        return Files.exists(file) ? Files.readString(file, StandardCharsets.UTF_8) : "";
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }
  }

  /** The glue files to generate, one per entity class annotated with @Glue, by name */
  public static final List<Remote> REMOTES = new Reflections("plm", "lessons", Scanners.TypesAnnotated)
                                                 .getTypesAnnotatedWith(Glue.class, true)
                                                 .stream()
                                                 .map(entity -> new Remote(entity.getAnnotation(Glue.class).value(), entity.asSubclass(Entity.class)))
                                                 .sorted(Comparator.comparing(Remote::name))
                                                 .toList();

  /** A language to generate the glue of: its folder under resources/langages and its generator. */
  private record Target(String dir, ExternalPrimitiveLanguage generator) {}

  /** Sources shared with the PLM, compiled alongside the Java glue in the package "generated". */
  public static final List<String> JAVA_HELPER_SOURCES =
      List.of("src/plm/core/ValueSerializer.java", "src/plm/universe/Point.java", "src/lessons/recursion/cons/universe/RecList.java");

  public static void main(String[] args) throws Exception
  {
    File folder = new File("target/classes/resources/langages/");
    generateGlue(folder);
    buildEntitiesJar(new File(folder, "java"), ".java", new File(folder, "plm-entities-java.jar"));
    buildEntitiesJar(new File(folder, "scala"), ".scala", new File(folder, "plm-entities-scala.jar"));
  }

  /** Writes the glue of every universe (its RemoteXxx files) in every language, in the subfolder of folder named after the language */
  public static void generateGlue(File folder) throws IOException
  {
    Map<String, Map<String, String>> aliases      = PrimitiveAliases.load(PrimitiveAliases.PO_DIRECTORY);
    Map<Remote, List<PrimitiveMethod>> primitives = new LinkedHashMap<>();
    for (Remote remote : REMOTES)
      primitives.put(remote, PrimitiveAliases.withAliases(remote.name(), PrimitiveRegistration.getMaximalPrimitiveForEntity(remote.entity()), aliases));

    List<Target> targets =
        List.of(new Target("java", new LangJava.LangJavaExternalPrimitiveGenerator()), new Target("scala", new LangScala.LangScalaExternalPrimitiveGenerator()),
                new Target("python", new LangPython.LangPythonExternalPrimitiveGenerator()), new Target("c", new LangC.LangCExternalPrimitiveGenerator()));

    for (Target target : targets) {
      File targetFolder = new File(folder, target.dir());
      targetFolder.mkdirs(); // the Scala glue has no hand-written file, so its folder only exists once generated
      System.err.print("Generating " + target.dir() + " entities in " + targetFolder + ":");
      for (Map.Entry<Remote, List<PrimitiveMethod>> entry : primitives.entrySet()) {
        Remote remote = entry.getKey();
        System.err.print(" " + remote.name());
        target.generator().generate(targetFolder, remote, entry.getValue());
      }
      System.err.println(".");
    }
  }

  /**
   * Compiles in-process the glue of one language (the generated RemoteXxx files of langFolder, of extension ext) together with the Java
   * Remote and the JAVA_HELPER_SOURCES, all in the package "generated", and packs the classes in jarFile. The Scala glue is compiled
   * against the classes of the Java sources. The sources are staged in target/plm-entities/&lt;language&gt;/src to ease debugging.
   */
  public static void buildEntitiesJar(File langFolder, String ext, File jarFile) throws IOException
  {
    Path staging = Path.of("target/plm-entities", langFolder.getName());
    Path sources = staging.resolve("src");
    Path classes = staging.resolve("classes");
    deleteRecursively(staging);
    Files.createDirectories(sources);
    Files.createDirectories(classes);

    List<File> javaSources = new ArrayList<>(), scalaSources = new ArrayList<>();
    List<File> glue = new ArrayList<>(List.of(langFolder.listFiles((dir, name) -> name.endsWith(ext) && !name.equals("Remote.java"))));
    glue.add(new File(langFolder.getParentFile(), "java/Remote.java")); // the Scala glue uses it too
    for (File remote : glue) {
      // Remote and the generated files have no package declaration
      boolean isJava = remote.getName().endsWith(".java");
      File staged    = sources.resolve(remote.getName()).toFile();
      Files.writeString(staged.toPath(), "package generated" + (isJava ? ";" : "") + "\n" + Files.readString(remote.toPath(), StandardCharsets.UTF_8));
      (isJava ? javaSources : scalaSources).add(staged);
    }
    for (String helper : JAVA_HELPER_SOURCES) {
      String content = Files.readString(Path.of(helper), StandardCharsets.UTF_8).replaceFirst("package [^;]*;", "package generated;");
      // Make the helpers refer to each other's copy instead of the original, which would not be on the student's classpath
      for (String other : JAVA_HELPER_SOURCES) {
        String fqcn = other.substring("src/".length(), other.length() - ".java".length()).replace('/', '.');
        content     = content.replace("import " + fqcn + ";", "import generated." + fqcn.substring(fqcn.lastIndexOf('.') + 1) + ";");
      }
      File staged = sources.resolve(Path.of(helper).getFileName()).toFile();
      Files.writeString(staged.toPath(), content);
      javaSources.add(staged);
    }

    compileJava(javaSources, classes);
    if (!scalaSources.isEmpty())
      compileScala(scalaSources, classes);

    try (JarOutputStream jar = new JarOutputStream(Files.newOutputStream(jarFile.toPath())); var walk = Files.walk(classes)) {
      for (Path classFile : walk.filter(Files::isRegularFile).sorted().toList()) {
        jar.putNextEntry(new JarEntry(classes.relativize(classFile).toString().replace(File.separatorChar, '/')));
        Files.copy(classFile, jar);
        jar.closeEntry();
      }
    }
    System.err.println("Built " + jarFile + " from " + (javaSources.size() + scalaSources.size()) + " sources.");
  }

  /** Compiles files into classes with javac. Fails on any diagnostic. */
  private static void compileJava(List<File> files, Path classes) throws IOException
  {
    JavaCompiler compiler                          = ToolProvider.getSystemJavaCompiler();
    DiagnosticCollector<JavaFileObject> diagnostic = new DiagnosticCollector<>();
    try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostic, null, StandardCharsets.UTF_8)) {
      // Empty CLASS_PATH: the glue must not see the PLM's own classes
      fileManager.setLocation(StandardLocation.CLASS_PATH, List.of());
      fileManager.setLocation(StandardLocation.CLASS_OUTPUT, List.of(classes.toFile()));
      // Same release as the maven compilation of the PLM, so that any JVM able to run it can run the glue
      if (!compiler.getTask(null, fileManager, diagnostic, List.of("--release", "17"), null, fileManager.getJavaFileObjectsFromFiles(files)).call())
        throw new IllegalStateException("Cannot compile the Java entities:\n" + diagnostic.getDiagnostics());
    }
  }

  /** Compiles files into classes with the Scala compiler, against the Scala runtime and the classes already there. Fails on any diagnostic. */
  private static void compileScala(List<File> files, Path classes)
  {
    List<String> args = new ArrayList<>(List.of("-classpath", LangScala.scalaCompilerClasspath() + File.pathSeparator + classes.toAbsolutePath(), "-d",
                                                classes.toAbsolutePath().toString(), "-color:never"));
    files.forEach(f -> args.add(f.getAbsolutePath()));
    Reporter reporter = new Driver().process(args.toArray(new String[0]));
    if (reporter.hasErrors() || reporter.hasWarnings())
      throw new IllegalStateException("Cannot compile the Scala entities: " + String.join(" ", args));
  }

  private static void deleteRecursively(Path dir) throws IOException
  {
    if (!Files.exists(dir))
      return;
    try (var walk = Files.walk(dir)) {
      for (Path p : walk.sorted(Comparator.reverseOrder()).toList())
        Files.delete(p);
    }
  }
}
