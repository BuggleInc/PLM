package plm.core.lang;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import org.checkerframework.checker.nullness.qual.NonNull;
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
    /* Language detection logic */
    private static String brokenLanguageMessage;
    private static BrokenLanguageState brokenLanguageState = BrokenLanguageState.Unitialized;
    File tempFolder = new File(System.getProperty("java.io.tmpdir"), "plm_java_toremove");

    public LangJava() {
        super("Java", "java", ResourcesCache.getIcon("img/lang_java.png"));
    }

    private static @NonNull String getCorrectedTemplate(String correction) {

        int runFunctionI = correction.indexOf("void run(");

        int beginTemplateIndex = correction.indexOf("/* BEGIN TEMPLATE */") + "/* BEGIN TEMPLATE */".length();
        int endTemplateIndex = correction.indexOf("/* END TEMPLATE */");
        int beginSolutionIndex = correction.indexOf("/* BEGIN SOLUTION */");

        String template;
        if (beginSolutionIndex == beginTemplateIndex + 3)
            template = "$package\n\n$imports\n\npublic class Entity {\n$dependency\n\t public void run(){\n$body}\n}";
        else {
            if (beginTemplateIndex < runFunctionI && runFunctionI < endTemplateIndex)
                template = "$package\n\n$imports\n\npublic class Entity {\n$dependency\n\t\n$body\n}";
            else
                template = "$package\n\n$imports\n\npublic class Entity {\n$dependency\n$run\n\t\n$body\n}";
        }
        return template;
    }

    private static String extractRunFunction(String code) {
        int startRun = code.indexOf("void run(");
        int beginOfRunLine = code.substring(0, startRun).lastIndexOf('\n');
        if (beginOfRunLine == -1) beginOfRunLine = 0;

        int i = code.indexOf('{', startRun) + 1;
        int bracket = 1;

        for (; i < code.length() && bracket > 0; i++) {
            if (code.charAt(i) == '{') bracket++;
            if (code.charAt(i) == '}') bracket--;
        }

        String substring = code.substring(beginOfRunLine, i);
        return substring;
    }

    private static String extractRunDependency(String code) {

        StringBuilder section = new StringBuilder();

        for (int i = 0; i < code.length(); i++) {
            if (!code.startsWith("/* BEGIN DEPENDENCY */", i)) continue;

            int begin = i + "/* BEGIN DEPENDENCY */".length();
            int end = code.indexOf("/* END DEPENDENCY */", i);

            section.append(code, begin, end).append("\n");
            i = end + "/* END DEPENDENCY */".length();
        }

        return section.toString();
    }

    private static String extractImportDependency(String code) {

        StringBuilder section = new StringBuilder();

        for (int i = 0; i < code.length(); i++) {
            if (!code.startsWith("/* BEGIN IMPORT */", i)) continue;

            int begin = i + "/* BEGIN IMPORT */".length();
            int end = code.indexOf("/* END IMPORT */", i);

            section.append(code, begin, end).append("\n");
            i = end + "/* END IMPORT */".length();
        }

        return section.toString();
    }

    private static String getRemote(String code) {
      if (code.contains("Langton") || code.contains("Turmite"))
        return null;

      if (code.contains(".bat."))
        return "RemoteBat";
      if (code.contains("Buggle"))
        return "RemoteBuggle";
      if (code.contains("Turtle"))
        return "RemoteTurtle";
      if (code.contains("Flag"))
        return "RemoteFlag";
      if (code.contains("Baseball"))
        return "RemoteBaseball";
      if (code.contains("Pancake"))
        return "RemotePancake";
      if (code.contains("Hanoi"))
        return "RemoteHanoi";
      if (code.contains("Sort"))
        return "RemoteSort";

      return null;
    }

    private static void compileJavaFiles(DiagnosticCollector<JavaFileObject> diagnostic, File packageFolder, File... files) throws PLMCompilerException {
        Runtime rt = Runtime.getRuntime();

        for (File javaFile : files) {

            String path = javaFile.toPath().toString();
            if (!path.endsWith(".java")) {
                throw new PLMCompilerException("Trying to compile a non java file: '" + path + "'", Set.of(path), new Error(), diagnostic);
            }

        }

        List<String> names = Arrays.asList(files).stream().map(s -> s.getName()).toList();
        List<String> paths = Arrays.asList(files).stream().map(s -> s.toPath().toString()).toList();

        ArrayList<String> args = new ArrayList<>();

        args.add("javac");
        args.addAll(names);


        Process proc;
        try {
            proc = rt.exec(args.toArray(String[]::new), new String[]{}, packageFolder);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            String rtStdout = stdInput.lines().collect(Collectors.joining("\n"));

            String rtStderr = stdError.lines().collect(Collectors.joining("\n"));

            if (!rtStderr.isEmpty()) {
                throw new PLMCompilerException(rtStderr, new HashSet<>(paths), new Error(), diagnostic);
            }
        } catch (IOException e) {
            throw new PLMCompilerException(e.getMessage(), new HashSet<>(paths), new Error(), diagnostic);
        }

    }

    private static void createJarFile(DiagnosticCollector<JavaFileObject> diagnostic, File root, File packageFolder, File jarFile, File mainFile, File... files) throws PLMCompilerException {

        Set<File> allFiles = new HashSet<>();
        allFiles.add(mainFile);
        allFiles.addAll(List.of(files));

        Runtime rt = Runtime.getRuntime();

        if (!packageFolder.toPath().toString().startsWith(root.toPath().toString())) {
            throw new PLMCompilerException("Root folder (" + root.toPath() + ") is not above package folder (" + packageFolder.toPath() + ") in file hierarchy.", Set.of(), new Error(), diagnostic);
        }

        String packagePath = packageFolder.toPath().toString().substring(root.toPath().toString().length() + 1);
        String packageName = packagePath.replace('/', '.');

        String mainFileDotPath = packageName + "." + mainFile.getName().substring(0, mainFile.getName().indexOf('.'));

        File manifestFile = new File(packageFolder, "MANIFEST.MF");
        try {
            Files.writeString(manifestFile.toPath(), "Main-Class: " + mainFileDotPath + "\n");


            List<String> classFiles = allFiles.stream().map(s -> {
                String javaPath = s.toPath().toString();
                return javaPath.substring(0, javaPath.lastIndexOf('.')) + ".class";
            }).filter(s -> s.endsWith(".class")).toList();
            classFiles = classFiles.stream().map(s -> s.substring(root.toPath().toString().length() + 1)).toList();

            ArrayList<String> args = new ArrayList<>();

            args.add("jar");
            args.add("cfm");
            args.add(jarFile.toPath().toString());
            args.add(manifestFile.toPath().toString());
            args.addAll(classFiles);

            Process proc = rt.exec(args.toArray(String[]::new), new String[]{}, root);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            String rtStdout = stdInput.lines().collect(Collectors.joining("\n"));

            String rtStderr = stdError.lines().collect(Collectors.joining("\n"));

            if (!rtStderr.isEmpty()) {
                throw new PLMCompilerException(rtStderr, allFiles.stream().map(s -> s.toPath().toString()).collect(Collectors.toSet()), new Error(), diagnostic);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isJava() {
        return true;
    }

    @Override
    public String getBrokenLanguageMessage() {
        return brokenLanguageMessage;
    }

    @Override
    public boolean isBrokenLanguage() {
        if (brokenLanguageState == BrokenLanguageState.Unitialized) {
            throw new RuntimeException("Unimplemented");
        }
        return brokenLanguageState != BrokenLanguageState.Usable;
    }

    public String getRemoteJavaFile(String remoteName, String packageName) {
        String remote;
        if (remoteName == null || remoteName.isEmpty()) remote = "Remote.java";
        else remote = remoteName;

        if (!remote.startsWith("Remote")) remote = "Remote" + remote;
        if (!remote.endsWith(".java")) remote = remote + ".java";

        String path = "resources/langages/java/" + remote;

        InputStream stream = getClass().getClassLoader().getResourceAsStream(path);

        if (stream == null) {
            throw new IllegalArgumentException("Remote '" + path + "' do not exist (argument passed: '" + remoteName + "').");
        }

        String packageDeclaration = "package " + packageName + ";";
        String remoteCode = new BufferedReader(new InputStreamReader(stream))
                .lines().collect(Collectors.joining("\n"));

        if (remoteCode.startsWith("package")) {
            remoteCode = remoteCode.replaceFirst("package .*;", packageDeclaration);
        } else {
            remoteCode = packageDeclaration + "\n" + remoteCode;
        }

        return remoteCode;
    }

    public void compileExo(Exercise exo, LogWriter out, StudentOrCorrection whatToCompile) throws PLMCompilerException {
        /* Make sure each run generate a new package to avoid that the loader cache prevent the reloading of the newly generated class */
        packageNameSuffix++;
        String packageNameCache = packageName();


        Map<String, String> runtimePatterns = new TreeMap<String, String>();
        runtimePatterns.put("\\$package", "package " + packageNameCache + ";");

        String mainRemoteContent = getRemoteJavaFile(null, packageNameCache);

        DiagnosticCollector<JavaFileObject> diagnostic = new DiagnosticCollector<JavaFileObject>();
        try {
            for (SourceFile sf : exo.getSourceFilesList(this)) {
                String key = packageNameCache + "." + sf.getName();

                String correction = sf.getCorrection();

                String remote = getRemote(correction);
                if (remote == null) {
                    PLMCompilerException e = new PLMCompilerException("This universe is not implemented in Java.", null, diagnostic);
                    exo.lastResult = RunOutcome.newCompilationError(e.getMessage());
                    throw e;
                }

                String runFunction = extractRunFunction(correction);
                String dependency = extractRunDependency(correction);
                String imports = extractImportDependency(correction);

                runtimePatterns.put("\\$run", runFunction);
                runtimePatterns.put("\\$dependency", dependency);
                runtimePatterns.put("\\$imports", ("import static " + packageNameCache + ".ValueSerializer.*;\n"
                                                   + "import static " + packageNameCache + ".Remote.*;\n"
                                                   + "import static " + packageNameCache + "." + remote + ".*;\n" + imports)
                                                      .replace('\n', ' '));

                String template = getCorrectedTemplate(correction);
                sf.setTemplate(template);


                String entityCode = sf.getCompilableContent(runtimePatterns, whatToCompile);
                entityCode = Pattern.compile("([^a-zA-Z])(Color)([^a-zA-Z.])").matcher(entityCode).replaceAll("$1int$3");
                entityCode = Pattern.compile("([^a-zA-Z])(Direction)([^a-zA-Z.])").matcher(entityCode).replaceAll("$1int$3");
                entityCode = Pattern.compile("this.").matcher(entityCode).replaceAll("");

                File workspace = new File(tempFolder, key.substring(0, key.lastIndexOf('.')).replace('.', '/'));
                //noinspection ResultOfMethodCallIgnored
                workspace.mkdirs();

                File mainRemote = new File(workspace, "Remote.java");

                String entityRemoteContent = getRemoteJavaFile(remote, packageNameCache)
                        .replace("import static Remote.*;", "import static " + packageNameCache + ".Remote.*;");
                File entityRemote = new File(workspace, remote + ".java");

                File entityFile = new File(workspace, "Entity.java");
                File mainFile = new File(workspace, "Main.java");

                String mainContent = "package " + packageNameCache + ";\n" +
                        "import " + packageNameCache + ".Entity;\n" +
                        "\n" +
                        "public class Main {\n" +
                        "   public static void main(String[] args){\n" +
                        "       new Entity().run();\n" +
                        "   }\n" +
                        "}\n";

                try {

                  File ValueSerializer          = new File(workspace, "ValueSerializer.java");
                  String ValueSerializerContent = Files.readString(new File("src/plm/core/ValueSerializer.java").toPath(), StandardCharsets.UTF_8);
                  ValueSerializerContent        = ValueSerializerContent.replaceFirst("package .*;", "package " + packageNameCache + ";\n");
                  Files.writeString(ValueSerializer.toPath(), ValueSerializerContent);

                  Files.writeString(new File(workspace, "Template.txt").toPath(), template);
                  Files.writeString(new File(workspace, "Correction.txt").toPath(), correction);
                  Files.writeString(mainRemote.toPath(), mainRemoteContent);
                  Files.writeString(entityRemote.toPath(), entityRemoteContent);
                  Files.writeString(entityFile.toPath(), entityCode);
                  Files.writeString(mainFile.toPath(), mainContent);

                  compileJavaFiles(diagnostic, workspace, mainFile, mainRemote, entityRemote, entityFile, ValueSerializer);

                  File jarFile = new File(workspace, "Code.jar");
                  createJarFile(diagnostic, tempFolder, workspace, jarFile, mainFile, entityFile, mainRemote, entityRemote, ValueSerializer,
                                new File(workspace, "ValueSerializer$Parser.class"));

                  sf.meta.put("JAVA", jarFile.toPath().toString());

                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
            }
        } catch (PLMCompilerException e) {
            System.err.println(Game.i18n.tr("Compilation error:"));
            exo.lastResult = RunOutcome.newCompilationError(e.getDiagnostics());
            System.err.println(e.getMessage());
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
    public ArrayList<Entity> mutateEntities(Exercise exo, List<Entity> olds, StudentOrCorrection whatToMutate) throws PLMCompilerException {

        List<SourceFile> sourceFile = exo.getSourceFilesList(this);

        if (sourceFile.size() != 1)
            throw new IllegalStateException("ToBeYetImplemented: Cannot differentiate entity scripts for now.");

        SourceFile source = sourceFile.get(0);

        for (Entity old : olds) {
            String path = source.meta.get("JAVA");
            if (path != null) {
                old.setScript(this, path);
            }
        }

        return new ArrayList<>(olds);
    }

    @Override
    protected Entity mutateEntity(String newClassName) throws InstantiationException, IllegalAccessException {
        throw new RuntimeException("This function should not longer be called, the new implementation do not rely on it.");
    }

    @Override
    public void runEntity(final Entity ent, final RunOutcome progress) {
        final StringBuffer resEvaluationError = new StringBuffer();

        try {

            String executable;
            if (ent.getScript(this) != null) {
                executable = ent.getScript(this);
            } else {
                executable = Game.getInstance().getCurrentLesson().getCurrentExercise().getId();
                throw new IllegalStateException("TOFIX");
            }

            String cmd = executable;
            File exec = new File(cmd);
            if (!exec.exists())
              throw new RuntimeException(Game.i18n.tr("Error, please recompile the exercise: {0} does not exist", exec.getName()));

            ProcessBuilder pb = new ProcessBuilder("java", "-jar", cmd);
            final Process process        = pb.start();
            final BufferedWriter bwriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

            Thread reader = new Thread() {
                public void run() {
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        try {
                            String str;
                            while ((str = reader.readLine()) != null)
                                System.out.println(str);
                        } finally {
                            reader.close();
                        }
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            };

            Thread error = new Thread() {
                public void run() {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    Exception parseError = null;
                    String str = "";
                    try {
                        while ((str = reader.readLine()) != null) {
                          //                            System.out.println("EXECUTING COMMAND: " + str);
                          CommandExecutor.command(ent, str, bwriter);
                          //                            System.out.println("COMMAND EXECUTED");
                        }
                    } catch (Exception e) {
                        parseError = e;
                    }
                    if (parseError != null) {
                        StringBuffer sb = new StringBuffer(str + "\n");
                        try {
                            while ((str = reader.readLine()) != null)
                                sb.append(str + "\n");
                        } catch (IOException ioe) {
                            System.err.println("Exception while handling the exception. Bailing out");
                            parseError.printStackTrace();
                            ioe.printStackTrace();
                        }
                        throw new RuntimeException("Parse error while reading the command: " + sb.toString(), parseError);
                    }
                }
            };

            reader.start();
            error.start();

            process.waitFor();

            reader.join();
            error.join();

            bwriter.close();

            if (resEvaluationError.length() > 0) {
                System.err.println(resEvaluationError.toString());
                progress.setCompilationError(resEvaluationError.toString());
            }

        } catch (Exception e) {
            resEvaluationError.append(e.getMessage());
            progress.setExecutionError(resEvaluationError.toString());
        }
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
                        "\tstatic final int white = " + ColorMapper.color2int(Color.white) + ";\n" +
                        "\tstatic final int WHITE = " + ColorMapper.color2int(Color.WHITE) + ";\n" +
                        "\tstatic final int black = " + ColorMapper.color2int(Color.black) + ";\n" +
                        "\tstatic final int BLACK = " + ColorMapper.color2int(Color.BLACK) + ";\n" +
                        "\tstatic final int blue = " + ColorMapper.color2int(Color.blue) + ";\n" +
                        "\tstatic final int BLUE = " + ColorMapper.color2int(Color.BLUE) + ";\n" +
                        "\tstatic final int cyan = " + ColorMapper.color2int(Color.cyan) + ";\n" +
                        "\tstatic final int CYAN = " + ColorMapper.color2int(Color.CYAN) + ";\n" +
                        "\tstatic final int darkGray = " + ColorMapper.color2int(Color.darkGray) + ";\n" +
                        "\tstatic final int DARK_GRAY = " + ColorMapper.color2int(Color.DARK_GRAY) + ";\n" +
                        "\tstatic final int gray = " + ColorMapper.color2int(Color.gray) + ";\n" +
                        "\tstatic final int GRAY = " + ColorMapper.color2int(Color.GRAY) + ";\n" +
                        "\tstatic final int green = " + ColorMapper.color2int(Color.green) + ";\n" +
                        "\tstatic final int GREEN = " + ColorMapper.color2int(Color.GREEN) + ";\n" +
                        "\tstatic final int lightGray = " + ColorMapper.color2int(Color.lightGray) + ";\n" +
                        "\tstatic final int LIGHT_GRAY = " + ColorMapper.color2int(Color.LIGHT_GRAY) + ";\n" +
                        "\tstatic final int magenta = " + ColorMapper.color2int(Color.magenta) + ";\n" +
                        "\tstatic final int MAGENTA = " + ColorMapper.color2int(Color.MAGENTA) + ";\n" +
                        "\tstatic final int orange = " + ColorMapper.color2int(Color.orange) + ";\n" +
                        "\tstatic final int ORANGE = " + ColorMapper.color2int(Color.ORANGE) + ";\n" +
                        "\tstatic final int pink = " + ColorMapper.color2int(Color.pink) + ";\n" +
                        "\tstatic final int PINK = " + ColorMapper.color2int(Color.PINK) + ";\n" +
                        "\tstatic final int red = " + ColorMapper.color2int(Color.red) + ";\n" +
                        "\tstatic final int RED = " + ColorMapper.color2int(Color.RED) + ";\n" +
                        "\tstatic final int yellow = " + ColorMapper.color2int(Color.yellow) + ";\n" +
                        "\tstatic final int YELLOW = " + ColorMapper.color2int(Color.YELLOW) + ";\n" +
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

            final String type_declarations = involved.stream().map(this::getTypeDeclaration)
                    .filter(o -> !o.isBlank()).collect(Collectors.joining("\n\n"));

            final String implementations = methods.stream().map(this::getImplementation).collect(Collectors.joining("\n\n"));

            final String code = "/* THIS FILE IS GENERATED. DO NOT EDIT */\nimport static Remote.*;\n\npublic class " + name + " {" +
                                ("\n" + type_declarations + "\n" + implementations).replace("\n", "\n\t") + "\n}";

            System.err.println("XXX Generating " + folder + "/" + name + ".java");
            Files.writeString(new File(folder, name + ".java").toPath(), code);
        }
    }

}