package plm.core.lang.primitives;

import dotty.tools.dotc.Driver;
import dotty.tools.dotc.reporting.Reporter;
import java.io.File;
import java.io.IOException;
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
import lessons.lander.universe.LanderEntity;
import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.hanoi.universe.HanoiEntity;
import lessons.sort.baseball.universe.BaseballEntity;
import lessons.sort.dutchflag.universe.DutchFlagEntity;
import lessons.sort.pancake.universe.PancakeEntity;
import lessons.turmites.universe.TurmiteEntity;
import plm.core.lang.LangC;
import plm.core.lang.LangJava;
import plm.core.lang.LangPython;
import plm.core.lang.LangScala;
import plm.test.simple.SimpleExerciseEntity;
import plm.universe.Entity;
import plm.universe.bat.BatEntity;
import plm.universe.bugglequest.AbstractBuggle;
import plm.universe.sort.SortingEntity;
import plm.universe.turtles.Turtle;

/**
 * Build-time generator of the RemoteXxx glue files of every language, run by maven after the compilation of the PLM. The Java and
 * Scala glue are then compiled in-process, together with the helper sources they need, into plm-entities-java.jar and
 * plm-entities-scala.jar.
 */
public class CodeCreation {

  /** A language to generate the glue of: its folder under resources/langages, its generator and the hand-written code to splice in some remotes. */
  private record Target(String dir, ExternalPrimitiveLanguage generator, Map<String, String> extraCode) {}

  /** Sources shared with the PLM, compiled alongside the Java glue in the package "generated". */
  public static final List<String> JAVA_HELPER_SOURCES =
      List.of("src/plm/core/ValueSerializer.java", "src/plm/universe/Point.java", "src/lessons/recursion/cons/universe/RecList.java");

  public static void main(String[] args) throws Exception
  {
    File folder = new File("target/classes/resources/langages/");

    Map<String, Class<? extends Entity>> remoteMap =
        Map.ofEntries(Map.entry("RemoteBat", BatEntity.class), Map.entry("RemoteCons", ConsEntity.class), Map.entry("RemoteBuggle", AbstractBuggle.class),
                      Map.entry("RemoteTurmite", TurmiteEntity.class), Map.entry("RemoteSort", SortingEntity.class), Map.entry("RemoteTurtle", Turtle.class),
                      Map.entry("RemotePancake", PancakeEntity.class), Map.entry("RemoteHanoi", HanoiEntity.class),
                      Map.entry("RemoteBaseball", BaseballEntity.class), Map.entry("RemoteDutchFlag", DutchFlagEntity.class),
                      Map.entry("RemoteSimple", SimpleExerciseEntity.class), Map.entry("RemoteLander", LanderEntity.class));

    Map<String, List<PrimitiveMethod>> primitives = new LinkedHashMap<>();
    for (Map.Entry<String, Class<? extends Entity>> entry : remoteMap.entrySet())
      primitives.put(entry.getKey(), PrimitiveRegistration.getMaximalPrimitiveForEntity(entry.getValue()).values().stream().toList());

    List<Target> targets =
        List.of(new Target("java", new LangJava.LangJavaExternalPrimitiveGenerator(), Map.of("RemoteCons", ConsEntity.JAVA_REMOTE_EXTRA_CODE)),
                new Target("scala", new LangScala.LangScalaExternalPrimitiveGenerator(), Map.of("RemoteCons", ConsEntity.SCALA_REMOTE_EXTRA_CODE)),
                new Target("python", new LangPython.LangPythonExternalPrimitiveGenerator(), Map.of("RemoteCons", ConsEntity.PYTHON_REMOTE_EXTRA_CODE)),
                new Target("c", new LangC.LangCExternalPrimitiveGenerator(), Map.of()));

    for (Target target : targets) {
      File targetFolder = new File(folder, target.dir());
      System.err.print("Generating " + target.dir() + " entities in " + targetFolder + ":");
      for (Map.Entry<String, List<PrimitiveMethod>> entry : primitives.entrySet()) {
        System.err.print(" " + entry.getKey());
        target.generator().generate(targetFolder, entry.getKey(), entry.getValue(), target.extraCode().getOrDefault(entry.getKey(), ""));
      }
      System.err.println(".");
    }

    buildEntitiesJar(new File(folder, "java"), ".java", new File(folder, "plm-entities-java.jar"));
    buildEntitiesJar(new File(folder, "scala"), ".scala", new File(folder, "plm-entities-scala.jar"));
  }

  /**
   * Compiles in-process the glue of one language (Remote and the generated RemoteXxx files of langFolder, of extension ext) together with
   * the JAVA_HELPER_SOURCES, all in the package "generated", and packs the classes in jarFile. The Scala glue is compiled against the
   * classes of the helpers. The sources are staged in target/plm-entities/&lt;language&gt;/src to ease debugging.
   */
  private static void buildEntitiesJar(File langFolder, String ext, File jarFile) throws IOException
  {
    boolean isJava = ext.equals(".java");
    Path staging   = Path.of("target/plm-entities", langFolder.getName());
    Path sources   = staging.resolve("src");
    Path classes   = staging.resolve("classes");
    deleteRecursively(staging);
    Files.createDirectories(sources);
    Files.createDirectories(classes);

    List<File> glue = new ArrayList<>();
    for (File remote : langFolder.listFiles((dir, name) -> name.endsWith(ext))) {
      // Remote and the generated files have no package declaration
      File staged = sources.resolve(remote.getName()).toFile();
      Files.writeString(staged.toPath(), "package generated" + (isJava ? ";" : "") + "\n" + Files.readString(remote.toPath(), StandardCharsets.UTF_8));
      glue.add(staged);
    }
    List<File> helpers = new ArrayList<>();
    for (String helper : JAVA_HELPER_SOURCES) {
      String content = Files.readString(Path.of(helper), StandardCharsets.UTF_8).replaceFirst("package [^;]*;", "package generated;");
      // Make the helpers refer to each other's copy instead of the original, which would not be on the student's classpath
      for (String other : JAVA_HELPER_SOURCES) {
        String fqcn = other.substring("src/".length(), other.length() - ".java".length()).replace('/', '.');
        content     = content.replace("import " + fqcn + ";", "import generated." + fqcn.substring(fqcn.lastIndexOf('.') + 1) + ";");
      }
      File staged = sources.resolve(Path.of(helper).getFileName()).toFile();
      Files.writeString(staged.toPath(), content);
      helpers.add(staged);
    }

    if (isJava) {
      helpers.addAll(glue);
      compileJava(helpers, classes);
    } else {
      compileJava(helpers, classes);
      compileScala(glue, classes);
    }

    try (JarOutputStream jar = new JarOutputStream(Files.newOutputStream(jarFile.toPath())); var walk = Files.walk(classes)) {
      for (Path classFile : walk.filter(Files::isRegularFile).sorted().toList()) {
        jar.putNextEntry(new JarEntry(classes.relativize(classFile).toString().replace(File.separatorChar, '/')));
        Files.copy(classFile, jar);
        jar.closeEntry();
      }
    }
    System.err.println("Built " + jarFile + " from " + (glue.size() + JAVA_HELPER_SOURCES.size()) + " sources.");
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
