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
   * Run "jar cfm &lt;jarFile&gt; &lt;manifest declaring Main-Class: mainClassDotPath&gt; &lt;classFiles...&gt;" from
   *  workDir, throwing if the tool reports anything on stderr.
   */
  protected static void runJarTool(File workDir, File jarFile, String mainClassDotPath, Set<String> classFiles, DiagnosticCollector<JavaFileObject> diagnostic)
      throws PLMCompilerException
  {
    File manifestFile = new File(workDir, "MANIFEST.MF");
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
