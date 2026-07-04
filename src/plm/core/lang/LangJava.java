package plm.core.lang;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import plm.core.PLMCompilerException;
import plm.core.lang.primitives.CommandArgumentType;
import plm.core.lang.primitives.ExternalPrimitiveLanguage;
import plm.core.lang.primitives.PrimitiveMethod;
import plm.core.lang.primitives.PrimitiveParameter;
import plm.core.model.Game;
import plm.core.model.LogWriter;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.RunOutcome;
import plm.core.model.session.SourceFile;
import plm.core.ui.ResourcesCache;
import plm.core.utils.ColorMapper;
import plm.universe.CommandExecutor;
import plm.universe.Entity;

public class LangJava extends JVMCompiledLang {
	public LangJava() {
		super("Java","java",ResourcesCache.getIcon("img/lang_java.png"));
	}
        @Override public boolean isJava() { return true; }

        /* Language detection logic */
        private static String brokenLanguageMessage;
        @Override public String getBrokenLanguageMessage() { return brokenLanguageMessage; }
        private static BrokenLanguageState brokenLanguageState = BrokenLanguageState.Unitialized;
        @Override public boolean isBrokenLanguage()
        {

          if (brokenLanguageState == BrokenLanguageState.Unitialized) {
            throw new RuntimeException("Unimplemented");
          }
          return brokenLanguageState != BrokenLanguageState.Usable;
        }

        private final CompilerJava compiler = new CompilerJava(Arrays.asList(new String[] {/* no option */ }));
	public Map<String, Class<Object>> compiledClasses = new TreeMap<String, Class<Object>>(); /* list of existing entity classes */


	public void compileExo(Exercise exo, LogWriter out, StudentOrCorrection whatToCompile) throws PLMCompilerException {
		/* Make sure each run generate a new package to avoid that the loader cache prevent the reloading of the newly generated class */
		packageNameSuffix++;
		runtimePatterns.put("\\$package", "package "+packageName()+";import java.awt.Color;");

		
		/* Prepare the source files */
		Map<String, String> sources = new TreeMap<String, String>();
                for (SourceFile sf : exo.getSourceFilesList(this))
                  sources.put(className(sf.getName()), sf.getCompilableContent(runtimePatterns, whatToCompile)); 

		if (sources.isEmpty()) 
			return;

		try {
			DiagnosticCollector<JavaFileObject> errs = new DiagnosticCollector<JavaFileObject>();			
			compiledClasses = compiler.compile(sources, errs);

			if (out != null)
				out.log(errs);
		} catch (PLMCompilerException e) {
			System.err.println(Game.i18n.tr("Compilation error:"));
                        exo.lastResult = RunOutcome.newCompilationError(e.getDiagnostics());
                        if (out != null)
                          out.log(
                              exo.lastResult.compilationError); // display the same error as in the ExerciseFailedDialog

                        if (Game.getInstance().isDebugEnabled())
                          for (SourceFile sf : exo.getSourceFilesList(this))
                            System.out.println("Source file " + sf.getName() + ":" +
                                               sf.getCompilableContent(runtimePatterns, whatToCompile)); 

			throw e;
		}

	}
	@Override
	protected Entity mutateEntity(String newClassName) throws InstantiationException, IllegalAccessException {
		return (Entity) compiledClasses.get(className(newClassName)).newInstance();
	}

}


/**
 * This class provides an adapted interface to the javax.tools compiler.
 * 
 * Its main method is compile(), of prototype: 
 *  INPUT:  Map<String, String>  : a list of named sourcefiles
 *  OUTPUT: Map<String, Class<Object>> : a list of named compiled class 
 * 
 * It is used in {@link plm.core.model.lesson.Exercise}, where the student code gets compiled.
 * 
 * See also "Create dynamic applications with javax.tools", David J. Biesack.
 * http://www.ibm.com/developerworks/java/library/j-jcomp/index.html?ca=dgr-lnxw82jvaxtools&S_TACT=105AGX59&S_CMP=GR
 */

class CompilerJava {
	// Compiler requires source files with a ".java" extension:
	static final String JAVA_EXTENSION = ".java";

	private final ClassLoaderImpl classLoader;

	// The compiler instance that this facade uses.
	private JavaCompiler compiler;

	// The compiler options (such as "-target" "1.6").
	private final List<String> options;

	// collect compiler diagnostics in this instance.
	private DiagnosticCollector<JavaFileObject> diagnostics;

	// The FileManager which will store source and class "files".
	private final FileManagerImpl javaFileManager;

	/**
	 * Construct a new instance which delegates to the named class loader.
	 * 
	 * @param options
	 *            The compiler options (such as "-target" "1.5"). See the usage
	 *            for javac
	 * @throws IllegalStateException
	 *             if the Java compiler cannot be loaded.
	 */
	public CompilerJava(Iterable<String> options) {
		final ClassLoader loader = getClass().getClassLoader();
		
		compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler == null) {
			try {
				compiler = (JavaCompiler) Class.forName("com.sun.tools.javac.api.JavacTool").newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (compiler == null) {
			throw new IllegalStateException("Cannot find the system Java compiler. "
					+ "Please use a java SDK instead of a java runtime or check the class path.");
		}

		classLoader = (ClassLoaderImpl) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
			public ClassLoader run() {
				return new ClassLoaderImpl(loader);
			}
		});

		diagnostics = new DiagnosticCollector<JavaFileObject>();
		final JavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
		javaFileManager = new FileManagerImpl(fileManager, classLoader);

		this.options = new ArrayList<String>();
		if (options != null) {
			for (String option : options) {
				this.options.add(option);
			}
		}

		// piece of code dedicated to webstart (jnlp) support
		// (might be improved one day...)
		List<URL> classpath = new ArrayList<URL>();

		if (loader instanceof URLClassLoader) {
			for (URL url : ((URLClassLoader) loader).getURLs()) {
				if (url.toString().startsWith("http:")) {
					try {
						String jarFilename = url.getFile();
						jarFilename = jarFilename.substring(jarFilename.lastIndexOf('/'));
						File tempFile = new File(System.getProperty("java.io.tmpdir"), jarFilename);
						// if the compiler was a singleton, we could have used
						// File.createTempFile("cached-",".jar");
						if (!tempFile.exists()) {
							// we have to re-download .jar since
							// JarURLConnection is not properly working
							download(url, tempFile);
							tempFile.deleteOnExit();
						}
						URL jarLocation = new URL("file:" + tempFile.getAbsolutePath());
						classpath.add(jarLocation);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					classpath.add(url);
				}
			}
		}
		String classPath = System.getProperty("java.class.path");
		for (String path : classPath.split(File.pathSeparator)) {
			try {
				classpath.add(new URL("file:" + path));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		StringBuffer sb = new StringBuffer();
		for (URL jar : classpath) {
			sb.append(jar.getPath());
			sb.append(File.pathSeparatorChar);
		}
		this.options.add("-cp");
		this.options.add(sb.toString());
	}

	private static void download(URL url, File localFileName) {
		OutputStream out = null;
		InputStream in = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(localFileName));
			URLConnection conn = url.openConnection();
			in = conn.getInputStream();
			byte[] buffer = new byte[1024 * 512];
			int numRead;
			while ((numRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, numRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (IOException ioe) {
				// ioe.printStackTrace();
			}
		}
	}

	/**
	 * Compile Java source in <var>javaSource</var> and return the resulting
	 * class.
	 * <p>
	 * Thread safety: this method is thread safe if the <var>javaSource</var>
	 * and <var>diagnosticsList</var> are isolated to this thread.
	 * 
	 * @param qualifiedClassName
	 *            The fully qualified class name.
	 * @param javaSource
	 *            Complete java source, including a package statement and a
	 *            class, interface, or annotation declaration.
	 * @param diagnosticsList
	 *            Any diagnostics generated by compiling the source are added to
	 *            this collector.
	 * @param types
	 *            zero or more Class objects representing classes or interfaces
	 *            that the resulting class must be assignable (castable) to.
	 * @return a Class which is generated by compiling the source
	 * @throws CharSequenceCompilerException
	 *             if the source cannot be compiled - for example, if it
	 *             contains syntax or semantic errors or if dependent classes
	 *             cannot be found.
	 * @throws ClassCastException
	 *             if the generated class is not assignable to all the optional
	 *             <var>types</var>.
	 */
	public synchronized Class<Object> compile(final String qualifiedClassName, final String javaSource,
			final DiagnosticCollector<JavaFileObject> diagnosticsList, final Class<?>... types)
			throws PLMCompilerException, ClassCastException {
		if (diagnosticsList != null)
			diagnostics = diagnosticsList;
		else
			diagnostics = new DiagnosticCollector<JavaFileObject>();
		Map<String, String> classes = new HashMap<String, String>(1);
		classes.put(qualifiedClassName, javaSource);
		Map<String, Class<Object>> compiled = compile(classes, diagnosticsList);
		Class<Object> newClass = compiled.get(qualifiedClassName);
		return castable(newClass, types);
	}

	/**
	 * Compile multiple Java source strings and return a Map containing the
	 * resulting classes.
	 * <p>
	 * Thread safety: this method is thread safe if the <var>classes</var> and
	 * <var>diagnosticsList</var> are isolated to this thread.
	 * 
	 * @param classes
	 *            A Map whose keys are qualified class names and whose values
	 *            are the Java source strings containing the definition of the
	 *            class. A map value may be null, indicating that compiled class
	 *            is expected, although no source exists for it (it may be a
	 *            non-public class contained in one of the other strings.)
	 * @param diagnosticsList
	 *            Any diagnostics generated by compiling the source are added to
	 *            this list.
	 * @return A mapping of qualified class names to their corresponding
	 *         classes. The map has the same keys as the input
	 *         <var>classes</var>; the values are the corresponding Class
	 *         objects.
	 * @throws CharSequenceCompilerException
	 *             if the source cannot be compiled
	 */
	public synchronized Map<String, Class<Object>> compile(final Map<String, String> classes,
			final DiagnosticCollector<JavaFileObject> diagnosticsList) throws PLMCompilerException {

		if (diagnosticsList != null)
			diagnostics = diagnosticsList;
		else
			diagnostics = new DiagnosticCollector<JavaFileObject>();

		List<JavaFileObject> sources = new ArrayList<JavaFileObject>();
		for (Entry<String, String> entry : classes.entrySet()) {
			String qualifiedClassName = entry.getKey();
			String javaSource = entry.getValue();
			if (javaSource != null) {
				final int dotPos = qualifiedClassName.lastIndexOf('.');
				final String className = dotPos == -1 ? qualifiedClassName : qualifiedClassName.substring(dotPos + 1);
				final String packageName = dotPos == -1 ? "" : qualifiedClassName.substring(0, dotPos);
				final JavaFileObjectImpl source = new JavaFileObjectImpl(className, javaSource);
				sources.add(source);
				// Store the source file in the FileManager via package/class
				// name.
				// For source files, we add a .java extension
				javaFileManager.putFileForInput(StandardLocation.SOURCE_PATH, packageName, className + JAVA_EXTENSION,
						source);
			}
		}
		// Get a CompliationTask from the compiler and compile the sources
		final CompilationTask task = compiler.getTask(null, javaFileManager, diagnostics, options, null, sources);
		final Boolean result = task.call();
		if (result == null || !result.booleanValue()) {
			/*
			 * FIXME: provide a way to debug
			 * when templates are broken
			 */
			 //for (String n:classes.keySet()) 
			 // System.out.println("File "+n+":\n"+classes.get(n));
			 
			throw new PLMCompilerException(Game.i18n.tr("Compilation failed."), classes.keySet(), diagnostics);
		}
		try {
			// For each class name in the input map, get its compiled
			// class and put it in the output map
			Map<String, Class<Object>> compiled = new HashMap<String, Class<Object>>();
			for (String qualifiedClassName : classes.keySet()) {
				final Class<Object> newClass = loadClass(qualifiedClassName);
				compiled.put(qualifiedClassName, newClass);
			}
			return compiled;
		} catch (ClassNotFoundException e) {
			throw new PLMCompilerException(classes.keySet(), e, diagnostics);
		} catch (IllegalArgumentException e) {
			throw new PLMCompilerException(classes.keySet(), e, diagnostics);
		} catch (SecurityException e) {
			throw new PLMCompilerException(classes.keySet(), e, diagnostics);
		}
	}

	/**
	 * Load a class that was generated by this instance or accessible from its
	 * parent class loader. Use this method if you need access to additional
	 * classes compiled by
	 * {@link #compile(String, CharSequence, DiagnosticCollector, Class...)
	 * compile()}, for example if the primary class contained nested classes or
	 * additional non-public classes.
	 * 
	 * @param qualifiedClassName
	 *            the name of the compiled class you wish to load
	 * @return a Class instance named by <var>qualifiedClassName</var>
	 * @throws ClassNotFoundException
	 *             if no such class is found.
	 */
	@SuppressWarnings("unchecked")
	public Class<Object> loadClass(final String qualifiedClassName) throws ClassNotFoundException {
		return (Class<Object>) classLoader.loadClass(qualifiedClassName);
	}

	/**
	 * Check that the <var>newClass</var> is a subtype of all the type
	 * parameters and throw a ClassCastException if not.
	 * 
	 * @param types
	 *            zero of more classes or interfaces that the
	 *            <var>newClass</var> must be castable to.
	 * @return <var>newClass</var> if it is castable to all the types
	 * @throws ClassCastException
	 *             if <var>newClass</var> is not castable to all the types.
	 */
	private Class<Object> castable(Class<Object> newClass, Class<?>... types) throws ClassCastException {
		for (Class<?> type : types)
			if (!type.isAssignableFrom(newClass)) {
				throw new ClassCastException(type.getName());
			}
		return newClass;
	}

	/**
	 * Converts a String to a URI.
	 * 
	 * @param name
	 *            a file name
	 * @return a URI
	 */
	static URI toURI(String name) {
		try {
			return new URI(name);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
}

/**
 * A JavaFileManager which manages Java source and classes. This FileManager
 * delegates to the JavaFileManager and the ClassLoaderImpl provided in the
 * constructor. The sources are all in memory CharSequence instances and the
 * classes are all in memory byte arrays.
 */
final class FileManagerImpl extends ForwardingJavaFileManager<JavaFileManager> {
	// the delegating class loader (passed to the constructor)
	private final ClassLoaderImpl classLoader;

	// Internal map of filename URIs to JavaFileObjects.
	private final Map<URI, JavaFileObject> fileObjects = new HashMap<URI, JavaFileObject>();

	/**
	 * Construct a new FileManager which forwards to the <var>fileManager</var>
	 * for source and to the <var>classLoader</var> for classes
	 * 
	 * @param fileManager
	 *            another FileManager that this instance delegates to for
	 *            additional source.
	 * @param classLoader
	 *            a ClassLoader which contains dependent classes that the
	 *            compiled classes will require when compiling them.
	 */
	public FileManagerImpl(JavaFileManager fileManager, ClassLoaderImpl classLoader) {
		super(fileManager);
		this.classLoader = classLoader;
	}

	/**
	 * @return the class loader which this file manager delegates to
	 */
	public ClassLoader getClassLoader() {
		return classLoader;
	}

	/**
	 * For a given file <var>location</var>, return a FileObject from which the
	 * compiler can obtain source or byte code.
	 * 
	 * @param location
	 *            an abstract file location
	 * @param packageName
	 *            the package name for the file
	 * @param relativeName
	 *            the file's relative name
	 * @return a FileObject from this or the delegated FileManager
	 * @see javax.tools.ForwardingJavaFileManager#getFileForInput(javax.tools.JavaFileManager.Location,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException {
		FileObject o = fileObjects.get(uri(location, packageName, relativeName));
		if (o != null)
			return o;
		return super.getFileForInput(location, packageName, relativeName);
	}

	/**
	 * Store a file that may be retrieved later with
	 * {@link #getFileForInput(javax.tools.JavaFileManager.Location, String, String)}
	 * 
	 * @param location
	 *            the file location
	 * @param packageName
	 *            the Java class' package name
	 * @param relativeName
	 *            the relative name
	 * @param file
	 *            the file object to store for later retrieval
	 */
	public void putFileForInput(StandardLocation location, String packageName, String relativeName, JavaFileObject file) {
		fileObjects.put(uri(location, packageName, relativeName), file);
	}

	/**
	 * Convert a location and class name to a URI
	 */
	private URI uri(Location location, String packageName, String relativeName) {
		return CompilerJava.toURI(location.getName() + '/' + packageName + '/' + relativeName);
	}

    public static class LangJavaExternalPrimitiveGenerator implements ExternalPrimitiveLanguage {

        String getLanguageType(CommandArgumentType<?> type) {
            if (type == CommandArgumentType.COLOR) return "int";
            if (type == CommandArgumentType.DIRECTION) return "int";
            if (type == CommandArgumentType.DOUBLE) return "double";
            if (type == CommandArgumentType.INT) return "int";
            if (type == CommandArgumentType.STRING) return "String";
            if (type == CommandArgumentType.CHAR) return "char";
            if (type == CommandArgumentType.BOOLEAN)
                return "boolean";

            throw new IllegalStateException("Unknown type: " + type);
        }

        String getTypeDeclaration(CommandArgumentType<?> type) {
            if (type == CommandArgumentType.DIRECTION) {
                return "public static class Direction {\n" +
                        "\tstatic final int NORTH = 0;\n" +
                        "\tstatic final int EAST = 1;\n" +
                        "\tstatic final int SOUTH = 2;\n" +
                        "\tstatic final int WEST = 3;\n" +
                        "}";
            }
            if (type == CommandArgumentType.COLOR) {
                return "public static class Color {\n" +
                        "\tstatic final int white = "+ ColorMapper.color2int(Color.white) +";\n" +
                        "\tstatic final int WHITE = "+ColorMapper.color2int(Color.WHITE)+";\n" +
                        "\tstatic final int black = "+ColorMapper.color2int(Color.black)+";\n" +
                        "\tstatic final int BLACK = "+ColorMapper.color2int(Color.BLACK)+";\n" +
                        "\tstatic final int blue = "+ColorMapper.color2int(Color.blue)+";\n" +
                        "\tstatic final int BLUE = "+ColorMapper.color2int(Color.BLUE)+";\n" +
                        "\tstatic final int cyan = "+ColorMapper.color2int(Color.cyan)+";\n" +
                        "\tstatic final int CYAN = "+ColorMapper.color2int(Color.CYAN)+";\n" +
                        "\tstatic final int darkGray = "+ColorMapper.color2int(Color.darkGray)+";\n" +
                        "\tstatic final int DARK_GRAY = "+ColorMapper.color2int(Color.DARK_GRAY)+";\n" +
                        "\tstatic final int gray = "+ColorMapper.color2int(Color.gray)+";\n" +
                        "\tstatic final int GRAY = "+ColorMapper.color2int(Color.GRAY)+";\n" +
                        "\tstatic final int green = "+ColorMapper.color2int(Color.green)+";\n" +
                        "\tstatic final int GREEN = "+ColorMapper.color2int(Color.GREEN)+";\n" +
                        "\tstatic final int lightGray = "+ColorMapper.color2int(Color.lightGray)+";\n" +
                        "\tstatic final int LIGHT_GRAY = "+ColorMapper.color2int(Color.LIGHT_GRAY)+";\n" +
                        "\tstatic final int magenta = "+ColorMapper.color2int(Color.magenta)+";\n" +
                        "\tstatic final int MAGENTA = "+ColorMapper.color2int(Color.MAGENTA)+";\n" +
                        "\tstatic final int orange = "+ColorMapper.color2int(Color.orange)+";\n" +
                        "\tstatic final int ORANGE = "+ColorMapper.color2int(Color.ORANGE)+";\n" +
                        "\tstatic final int pink = "+ColorMapper.color2int(Color.pink)+";\n" +
                        "\tstatic final int PINK = "+ColorMapper.color2int(Color.PINK)+";\n" +
                        "\tstatic final int red = "+ColorMapper.color2int(Color.red)+";\n" +
                        "\tstatic final int RED = "+ColorMapper.color2int(Color.RED)+";\n" +
                        "\tstatic final int yellow = "+ColorMapper.color2int(Color.yellow)+";\n" +
                        "\tstatic final int YELLOW = "+ColorMapper.color2int(Color.YELLOW)+";\n" +
                        "}";
            }
            return "";
        }

        String getParameter(PrimitiveParameter parameter) {
            return getLanguageType(parameter.type()) + " " + parameter.name();
        }

        String getPrototype(PrimitiveMethod method) {
            String name = method.name();
            List<PrimitiveParameter> parameters = method.parameters();
            CommandArgumentType<?> output = method.output();


            final String outputString = Optional.ofNullable(output).map(this::getLanguageType).orElse("void");

            return "public static " + outputString + " " + name + "(" + parameters.stream().map(this::getParameter).collect(Collectors.joining(", ")) + ")";
        }

        String getReturning(CommandArgumentType<?> type) {
            if (type == null)
                return "";

            if (type == CommandArgumentType.STRING) return "getAnswerString()";
            if (type == CommandArgumentType.DOUBLE) return "getAnswerDouble()";
            if (type == CommandArgumentType.CHAR) return "getAnswerChar()";
            if (type == CommandArgumentType.COLOR) return "getAnswerInt()";
            if (type == CommandArgumentType.DIRECTION) return "getAnswerInt()";
            if (type == CommandArgumentType.INT) return "getAnswerInt()";
            if (type == CommandArgumentType.BOOLEAN)
                return "getAnswerBoolean()";

            throw new IllegalStateException("Unknown type: " + type);
        }

        String getTemplatingForType(CommandArgumentType<?> type) {
            if (type == CommandArgumentType.STRING) return "%s";
            if (type == CommandArgumentType.DOUBLE) return "%f";
            if (type == CommandArgumentType.CHAR) return "%c";
            if (type == CommandArgumentType.COLOR) return "%d";
            if (type == CommandArgumentType.DIRECTION) return "%d";
            if (type == CommandArgumentType.INT) return "%d";
            if (type == CommandArgumentType.BOOLEAN)
                return "%d";

            throw new IllegalStateException("Unknown type: " + type);
        }

        String getImplementation(PrimitiveMethod method) {
            String prototype = getPrototype(method);

            int id = method.id();
            String name = method.name();
            String formats = method.parameters().stream().map(PrimitiveParameter::type)
                    .map(this::getTemplatingForType).map(s -> s + " ").collect(Collectors.joining());

            String command = "\tsendCommand(\"" + id + " " +
                    formats
                    + name
                    + "\"" + method.parameters().stream().map(PrimitiveParameter::name).map(s -> ", " + s).collect(Collectors.joining()) + ");";

            String returning = method.output() != null ? "\treturn " + getReturning(method.output()) + ";" : "";

            return prototype + "{\n" + command + "\n" + returning + "\n}";
        }

        @Override
        public void generate(File folder, String name, List<PrimitiveMethod> methods) throws IOException {
            Set<CommandArgumentType<?>> involved = ExternalPrimitiveLanguage.involved(methods);

	/**
	 * Add a class name/JavaFileObject mapping
	 * 
	 * @param qualifiedClassName
	 *            the name
	 * @param javaFile
	 *            the file associated with the name
	 */
	void add(final String qualifiedClassName, final JavaFileObject javaFile) {
		classes.put(qualifiedClassName, javaFile);
	}

	@Override
	public InputStream getResourceAsStream(final String name) {
		if (name.endsWith(".class")) {
			String qualifiedClassName = name.substring(0, name.length() - ".class".length()).replace('/', '.');
			JavaFileObjectImpl file = (JavaFileObjectImpl) classes.get(qualifiedClassName);
			if (file != null) {
				return new ByteArrayInputStream(file.getByteCode());
			}
		}
		return super.getResourceAsStream(name);
	}

}
