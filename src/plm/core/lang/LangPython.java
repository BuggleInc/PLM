package plm.core.lang;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.python.core.PyException;
import plm.core.lang.primitives.CommandArgumentType;
import plm.core.lang.primitives.ExternalPrimitiveLanguage;
import plm.core.lang.primitives.PrimitiveMethod;
import plm.core.lang.primitives.PrimitiveParameter;
import plm.core.model.Game;
import plm.core.model.lesson.RunOutcome;
import plm.core.ui.ResourcesCache;
import plm.universe.Entity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class LangPython extends ScriptingLanguage {

	public LangPython() {
		super("Python","py",ResourcesCache.getIcon("img/lang_python.png"));
	}
        @Override public boolean isPython() { return true; }

        /* Language detection logic */
        private static String brokenLanguageMessage;
        @Override public String getBrokenLanguageMessage() { return brokenLanguageMessage; }
        private static BrokenLanguageState brokenLanguageState = BrokenLanguageState.Unitialized;
        @Override public boolean isBrokenLanguage()
        {
          if (brokenLanguageState == BrokenLanguageState.Unitialized) {
            brokenLanguageState = BrokenLanguageState.Usable;

            brokenLanguageMessage = canResolve("/org/python/jsr223/PyScriptEngineFactory", "jython.jar");
            if (!brokenLanguageMessage.isEmpty()) {
              System.err.println("Error while resolving the jython symbols. Is jython.jar installed?" +
                                 brokenLanguageMessage);
              brokenLanguageState = BrokenLanguageState.NotUsable;
            }

            ScriptEngineManager manager = new ScriptEngineManager();
            if (manager.getEngineByName("python") == null) {
              brokenLanguageMessage = Game.i18n.tr(
                  "Cannot retrieve the python ScriptEngine. Are jython.jar and its dependencies in the classpath?");
              brokenLanguageState = BrokenLanguageState.NotUsable;
            }
          }
          return brokenLanguageState != BrokenLanguageState.Usable;
        }

        protected void setupEntityBindings(Entity ent) {
		ent.setScriptOffset(this, ent.getScriptOffset(this)+11);
		ent.setScript(this, 
				/* that's not really clean to get the output working when we redirect to the graphical console, 
				 * but it works (as long as it's evaluated at the exact same time than the script). */
				"import sys;\n" +
				"import java.lang;\n" +
				"class PLMOut:\n" +
				"  def write(obj,msg):\n" +
				"    java.lang.System.out.print(str(msg))\n" +
				"sys.stdout = PLMOut()\n" +
				"sys.stderr = PLMOut()\n" +
				/* getParam is in every Entity, so put it here to not request the universe to call super.setupBinding() */
				"def getParam(i):\n"+
				"  return entity.getParam(i)\n" +
				"def isSelected():\n" +
				"  return entity.isSelected()\n"+
				ent.getScript(this));
        }

        public boolean handleLangException(ScriptException e, Entity ent, RunOutcome progress)
        {
          if (!(e.getCause() instanceof
                org.python.core.PyException)) { // This seems to be the ancestor of all exceptions raised by jython
            return false;                       // not for us
          }
          RunOutcome.kind errorKind = RunOutcome.kind.FAIL;

          org.python.core.PyException cause = (PyException)e.getCause();

          StringBuffer msg = new StringBuffer();

          if (cause.type.toString().equals("<type 'exceptions.SyntaxError'>")) {
            msg.append(Game.i18n.tr("Syntax error: {0}\nLine {1}: {2}\n"
                                        + "In doubt, check your indentation, and that you don't mix tabs and spaces\n",
                                    cause.value.__findattr__("msg"),
                                    ((cause.value.__findattr__("lineno").asInt()) - ent.getScriptOffset(this) + 1),
                                    cause.value.__findattr__("text")));
            errorKind = RunOutcome.kind.COMPILE;

          } else if (cause.type.toString().equals("<type 'exceptions.IndentationError'>")) {
            msg.append(Game.i18n.tr("Indentation error: {0}\nline {1}: {2}\n"
                                        + "Please, check that you did not mix tabs and spaces. Use the TAB and "
                                        + "shift-TAB keys to clean your indentation.\n",
                                    cause.value.__findattr__("msg"),
                                    ((cause.value.__findattr__("lineno").asInt()) - ent.getScriptOffset(this) + 1),
                                    cause.value.__findattr__("text")));
            errorKind = RunOutcome.kind.COMPILE;

          } else if (cause.type.toString().equals("<type 'java.lang.ThreadDeath'>")) {
            msg.append(Game.i18n.tr("You interrupted the execution, did you fall into an infinite loop ?\n"
                                    + "Your program must stop by itself to successfully pass the exercise.\n"));

          } else { /* It makes sense to display a backtrace for any errors but syntax ones */

            if (cause.type.toString().equals("<type 'exceptions.NameError'>")) {
              msg.append(Game.i18n.tr(
                  "NameError raised: You seem to use a non-existent identifier; Please check for typos.\n"));
              msg.append(cause.value + "\n");
              errorKind = RunOutcome.kind.COMPILE;
            } else if (cause.type.toString().equals("<type 'exceptions.TypeError'>")) {
              msg.append(Game.i18n.tr("TypeError raised: you are probably misusing a function or something.\n"));
              msg.append(cause.value + "\n");
              errorKind = RunOutcome.kind.COMPILE;
            } else if (cause.type.toString().equals("<type 'exceptions.UnboundLocalError'>")) {
              msg.append(Game.i18n.tr("UnboundLocalError raised: you are probably using a global variable that is "
                                      + "not declared as such.\n"));
              msg.append(cause.value + "\n");
              errorKind = RunOutcome.kind.COMPILE;

              /* FIXME: how could we factorize the world's error? */
            } else if (cause.type.toString().equals(
                           "<type 'plm.universe.bugglequest.exception.NoBaggleUnderBuggleException'>")) {
              msg.append(Game.i18n.tr("Error: there is no baggle to pickup under the buggle."));
            } else if (cause.type.toString().equals(
                           "<type 'plm.universe.bugglequest.exception.AlreadyHaveBaggleException'>")) {
              msg.append(Game.i18n.tr("Error: a buggle cannot carry more than one baggle at the same time."));
            } else if (cause.type.toString().equals(
                           "<type 'plm.universe.bugglequest.exception.BuggleInOuterSpaceException'>")) {
              msg.append(Game.i18n.tr("Error: your buggle just teleported to the outer space..."));
            } else if (cause.type.toString().equals(
                           "<type 'plm.universe.bugglequest.exception.BuggleWallException'>")) {
              msg.append(Game.i18n.tr("Error: your buggle just hit a wall. That hurts."));

            } else {
              msg.append(Game.i18n.tr("Unknown error (please report): {0}\nIts value is: {1}", cause.type.toString(),
                                      cause.value + "\n"));
            }

            /* The following is very inspired from <jython>/src/org/python/core/PyTraceback.java,
             * even if we cannot reuse directly this implementation since we want to change all linenos on the fly.
             */
            org.python.core.PyTraceback tb = cause.traceback;
            while (tb != null) {
              tb.tb_lineno -= ent.getScriptOffset(this);
              if (tb.tb_frame == null || tb.tb_frame.f_code == null) {
                msg.append(String.format("  (no code object) at line %s\n", tb.tb_lineno));
              } else {
                msg.append(String.format("  File \"%.500s\", line %d, in %.500s\n", tb.tb_frame.f_code.co_filename,
                                         tb.tb_lineno, tb.tb_frame.f_code.co_name));
              }
              tb = (org.python.core.PyTraceback)tb.tb_next;
            }
          }

          if (Game.getInstance().isDebugEnabled()) {
            System.err.println("CAUSE: " + cause.value.toString());
            System.err.println("MSG: " + e.getMessage());
            System.err.println("BT: " + msg);
          }

          if (errorKind == RunOutcome.kind.COMPILE)
            progress.setCompilationError(msg.toString());
          else
            progress.setExecutionError(msg.toString());

          return true; // That was indeed a Python exception
        }

        public static class LangPythonExternalPrimitiveGenerator implements ExternalPrimitiveLanguage {

          String getLanguageType(CommandArgumentType<?> type) {
            if (type == CommandArgumentType.COLOR) return "int";
            if (type == CommandArgumentType.DIRECTION) return "int";
            if (type == CommandArgumentType.DOUBLE) return "float";
            if (type == CommandArgumentType.INT) return "int";
            if (type == CommandArgumentType.STRING) return "str";
            if (type == CommandArgumentType.CHAR) return "str";
            if (type == CommandArgumentType.BOOLEAN)
              return "bool";

            throw new IllegalStateException("Unknown type: " + type);
          }

          String getTypeDeclaration(CommandArgumentType<?> type) {
            if (type == CommandArgumentType.DIRECTION) {
              return "NORTH = 0\n" +
                      "EAST = 1\n" +
                      "SOUTH = 2\n" +
                      "WEST = 3\n";
            }
            if (type == CommandArgumentType.COLOR) {
              return  "white = 0\n" +
                      "black = 1\n" +
                      "blue = 2\n" +
                      "cyan = 3\n" +
                      "darkGray = 4\n" +
                      "gray = 5\n" +
                      "green = 6\n" +
                      "lightGray = 7\n" +
                      "magenta = 8\n" +
                      "orange = 9\n" +
                      "pink = 10\n" +
                      "red = 11\n" +
                      "yellow = 12\n";
            }
            return "";
          }

          String getParameter(PrimitiveParameter parameter) {
            return parameter.name()+": "+getLanguageType(parameter.type());
          }

          String getPrototype(PrimitiveMethod method) {
            String name = method.name();
            List<PrimitiveParameter> parameters = method.parameters();
            CommandArgumentType<?> output = method.output();


            final String outputString = Optional.ofNullable(output).map(this::getLanguageType).orElse("None");

            return "def " + name + "(" + parameters.stream().map(this::getParameter).collect(Collectors.joining(", ")) + ") -> "+outputString+":";
          }

          String getReturning(CommandArgumentType<?> type) {
            if (type == null)
              return "";

            if (type == CommandArgumentType.STRING) return "get_answer_string()";
            if (type == CommandArgumentType.DOUBLE) return "get_answer_double()";
            if (type == CommandArgumentType.CHAR) return "get_answer_char()";
            if (type == CommandArgumentType.COLOR) return "get_answer_int()";
            if (type == CommandArgumentType.DIRECTION) return "get_answer_int()";
            if (type == CommandArgumentType.INT) return "get_answer_int()";
            if (type == CommandArgumentType.BOOLEAN)
              return "get_answer_int()";

            throw new IllegalStateException("Unknown type: " + type);
          }

          String getTemplatingForType(CommandArgumentType<?> type) {
            if (type == CommandArgumentType.STRING) return "%s";
            if (type == CommandArgumentType.DOUBLE) return "%f";
            if (type == CommandArgumentType.CHAR) return "%s";
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

            String command = "\tsend_command(\"" + id + " " +
                    formats
                    + name
                    + "\"" + method.parameters().stream().map(PrimitiveParameter::name).map(s -> ", "+s).collect(Collectors.joining()) + ")";

            String returning = method.output() != null ? "\treturn " + getReturning(method.output()) : "";

            return prototype + "\n" + command + "\n" + returning + "\n";
          }

          @Override
          public void generate(File folder, String name, List<PrimitiveMethod> methods) throws IOException {
            Set<CommandArgumentType<?>> involved = ExternalPrimitiveLanguage.involved(methods);

            final String type_declarations = involved.stream().map(this::getTypeDeclaration)
                    .filter(o -> !o.isBlank()).collect(Collectors.joining("\n\n"));

            final String implementations = methods.stream().map(this::getImplementation).collect(Collectors.joining("\n\n"));

            final String code = ("from Remote import *\n\n" + type_declarations + implementations).replace("\t", " ".repeat(4));

            // System.err.println("XXX Generating "+folder+name+".c");
            Files.writeString(new File(folder, name + ".py").toPath(), code);
          }
        }
}
