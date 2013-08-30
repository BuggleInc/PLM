package plm.core;

import javax.script.ScriptException;

import org.python.core.PyException;

import plm.core.model.Game;
import plm.core.model.lesson.ExecutionProgress;
import plm.universe.Entity;

public class PythonExceptionDecipher {

	public static boolean isPythonException(ScriptException e) {
		return (e.getCause() instanceof PyException);
	}
	
	public static void handlePythonException (ScriptException e,Entity ent,ExecutionProgress progress) {
		if (e.getCause() instanceof org.python.core.PyException) { // This seem to be all exceptions raised by python
			org.python.core.PyException cause = (org.python.core.PyException) e.getCause();

			StringBuffer msg = new StringBuffer();

			if (cause.type.toString().equals("<type 'exceptions.SyntaxError'>")) {
				msg.append(Game.i18n.tr("Syntax error at line {0}: {1}\n" +
						"In doubt, check your indentation, and that you don't mix tabs and spaces\n",
						((cause.value.__findattr__("lineno").asInt())-ent.getScriptOffset(Game.PYTHON)),
						cause.value.__findattr__("msg")));

			} else { /* It makes sense to display a backtrace for any errors but syntax ones */

				if (cause.type.toString().equals("<type 'exceptions.NameError'>")) {
					msg.append(Game.i18n.tr("NameError raised: You seem to use a non-existent identifier; Please check for typos\n"));
					msg.append(cause.value+"\n");
				} else if (cause.type.toString().equals("<type 'exceptions.TypeError'>")) {
					msg.append(Game.i18n.tr("TypeError raised: you are probably misusing a function or something.\n"));
					msg.append(cause.value+"\n");
				} else if (cause.type.toString().equals("<type 'exceptions.UnboundLocalError'>")) {
					msg.append(Game.i18n.tr("UnboundLocalError raised: you are probably using a global variable that is not declared as such.\n"));
					msg.append(cause.value+"\n");


					/* FIXME: how could we factorize the world's error? */ 
				} else if (cause.type.toString().equals("<type 'plm.universe.bugglequest.exception.NoBaggleUnderBuggleException'>")) {
					msg.append(Game.i18n.tr("Error: there is no baggle to pickup under the buggle"));
				} else if (cause.type.toString().equals("<type 'plm.universe.bugglequest.exception.AlreadyHaveBaggleException'>")) {
					msg.append(Game.i18n.tr("Error: a buggle cannot carry more than one baggle at the same time"));
				} else if (cause.type.toString().equals("<type 'plm.universe.bugglequest.exception.BuggleInOuterSpaceException'>")) {
					msg.append(Game.i18n.tr("Error: your buggle just teleported to the outer space..."));
				} else if (cause.type.toString().equals("<type 'plm.universe.bugglequest.exception.BuggleWallException'>")) {
					msg.append(Game.i18n.tr("Error: your buggle just hit a wall. That hurts."));

				} else {
					msg.append(Game.i18n.tr("Unknown error (please report): {0}\nIts value is: {1}",
							cause.type.toString(),cause.value+"\n"));

				}


				/* The following is very inspired from <jython>/src/org/python/core/PyTraceback.java, 
				 * even if we cannot reuse directly this implementation since we want to change all linenos on the fly. 
				 */
				org.python.core.PyTraceback tb = cause.traceback;
				while (tb != null) {
					tb.tb_lineno-= ent.getScriptOffset(Game.PYTHON);
					if (tb.tb_frame == null || tb.tb_frame.f_code == null) {
						msg.append(String.format("  (no code object) at line %s\n", tb.tb_lineno));
					} else {
						msg.append(String.format("  File \"%.500s\", line %d, in %.500s\n",
								tb.tb_frame.f_code.co_filename, tb.tb_lineno, tb.tb_frame.f_code.co_name));
					}
					tb = (org.python.core.PyTraceback) tb.tb_next;
				}
			}				

			if (Game.getInstance().isDebugEnabled()) {
				System.err.println("CAUSE: "+cause.value.toString());
				System.err.println("MSG: "+e.getMessage());
				System.err.println("BT: "+msg);
			}

			progress.setCompilationError(msg.toString());
		}
	}
}
