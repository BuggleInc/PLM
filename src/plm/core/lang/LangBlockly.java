package plm.core.lang;

import javax.script.ScriptException;

import plm.core.model.lesson.ExecutionProgress;
import plm.universe.Entity;

public class LangBlockly extends ScriptingLanguage {

	public LangBlockly(boolean isDebugEnabled) {
		super("Blockly","blockly", isDebugEnabled);
	}

	protected void setupEntityBindings(Entity ent) {
	}
	
	public boolean handleLangException (ScriptException e,Entity ent,ExecutionProgress progress) {
		return true;
	}

}
