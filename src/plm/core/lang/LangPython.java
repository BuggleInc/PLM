package plm.core.lang;

import javax.script.ScriptException;

import org.python.core.PyException;
import org.xnap.commons.i18n.I18n;

import plm.core.model.lesson.ExecutionProgress;
import plm.universe.Entity;

public class LangPython extends ScriptingLanguage {

	public LangPython(boolean isDebugEnabled) {
		super("Python", "py", isDebugEnabled);
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
	
	public boolean handleLangException (ScriptException e,Entity ent,ExecutionProgress progress, I18n i18n) {
		if (!(e.getCause() instanceof org.python.core.PyException)) { // This seems to be the ancestor of all exceptions raised by jython
			return false; // not for us
		}
		ExecutionProgress.outcomeKind errorKind = ExecutionProgress.outcomeKind.FAIL;
		
		org.python.core.PyException cause = (PyException) e.getCause();

		StringBuffer msg = new StringBuffer();

		if (cause.type.toString().equals("<type 'exceptions.SyntaxError'>")) {
			msg.append(i18n.tr("Syntax error: {0}\nLine {1}: {2}\n" +
					"In doubt, check your indentation, and that you don't mix tabs and spaces\n",
					cause.value.__findattr__("msg"),
					((cause.value.__findattr__("lineno").asInt())-ent.getScriptOffset(this)+1),
					cause.value.__findattr__("text")
					));
			errorKind = ExecutionProgress.outcomeKind.COMPILE;

		} else if (cause.type.toString().equals("<type 'exceptions.IndentationError'>")) {
			msg.append(i18n.tr("Indentation error: {0}\nline {1}: {2}\n" +
					"Please, check that you did not mix tabs and spaces. Use the TAB and shift-TAB keys to clean your indentation.\n",
					cause.value.__findattr__("msg"),
					((cause.value.__findattr__("lineno").asInt())-ent.getScriptOffset(this)+1),
					cause.value.__findattr__("text")));
			errorKind = ExecutionProgress.outcomeKind.COMPILE;

		} else if (cause.type.toString().equals("<type 'java.lang.ThreadDeath'>")) {
			msg.append(i18n.tr("You interrupted the execution, did you fall into an infinite loop ?\n" +
					"Your program must stop by itself to successfully pass the exercise.\n"));

		} else { /* It makes sense to display a backtrace for any errors but syntax ones */

			if (cause.type.toString().equals("<type 'exceptions.NameError'>")) {
				msg.append(i18n.tr("NameError raised: You seem to use a non-existent identifier; Please check for typos\n"));
				msg.append(cause.value+"\n");
				errorKind = ExecutionProgress.outcomeKind.COMPILE;
			} else if (cause.type.toString().equals("<type 'exceptions.TypeError'>")) {
				msg.append(i18n.tr("TypeError raised: you are probably misusing a function or something.\n"));
				msg.append(cause.value+"\n");
				errorKind = ExecutionProgress.outcomeKind.COMPILE;
			} else if (cause.type.toString().equals("<type 'exceptions.UnboundLocalError'>")) {
				msg.append(i18n.tr("UnboundLocalError raised: you are probably using a global variable that is not declared as such.\n"));
				msg.append(cause.value+"\n");
				errorKind = ExecutionProgress.outcomeKind.COMPILE;


				/* FIXME: how could we factorize the world's error? */ 
			} else if (cause.type.toString().equals("<type 'plm.universe.bugglequest.exception.NoBaggleUnderBuggleException'>")) {
				msg.append(i18n.tr("Error: there is no baggle to pickup under the buggle"));
			} else if (cause.type.toString().equals("<type 'plm.universe.bugglequest.exception.AlreadyHaveBaggleException'>")) {
				msg.append(i18n.tr("Error: a buggle cannot carry more than one baggle at the same time"));
			} else if (cause.type.toString().equals("<type 'plm.universe.bugglequest.exception.BuggleInOuterSpaceException'>")) {
				msg.append(i18n.tr("Error: your buggle just teleported to the outer space..."));
			} else if (cause.type.toString().equals("<type 'plm.universe.bugglequest.exception.BuggleWallException'>")) {
				msg.append(i18n.tr("Error: your buggle just hit a wall. That hurts."));

			} else {
				msg.append(i18n.tr("Unknown error (please report): {0}\nIts value is: {1}",
						cause.type.toString(),cause.value+"\n"));

			}


			/* The following is very inspired from <jython>/src/org/python/core/PyTraceback.java, 
			 * even if we cannot reuse directly this implementation since we want to change all linenos on the fly. 
			 */
			org.python.core.PyTraceback tb = cause.traceback;
			while (tb != null) {
				tb.tb_lineno-= ent.getScriptOffset(this);
				if (tb.tb_frame == null || tb.tb_frame.f_code == null) {
					msg.append(String.format("  (no code object) at line %s\n", tb.tb_lineno));
				} else {
					msg.append(String.format("  File \"%.500s\", line %d, in %.500s\n",
							tb.tb_frame.f_code.co_filename, tb.tb_lineno, tb.tb_frame.f_code.co_name));
				}
				tb = (org.python.core.PyTraceback) tb.tb_next;
			}
		}				

		if (isDebugEnabled()) {
			System.err.println("CAUSE: "+cause.value.toString());
			System.err.println("MSG: "+e.getMessage());
			System.err.println("BT: "+msg);
		}
		
		if (errorKind == ExecutionProgress.outcomeKind.COMPILE)
			progress.setCompilationError(msg.toString());
		else 
			progress.setExecutionError(msg.toString());

		return true; // That was indeed a Python exception
	}

}
