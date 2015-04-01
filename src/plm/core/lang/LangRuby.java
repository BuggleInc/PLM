package plm.core.lang;

import javax.script.ScriptException;

import plm.core.model.lesson.ExecutionProgress;
import plm.universe.Entity;

public class LangRuby extends ScriptingLanguage {

	public LangRuby(boolean isDebugEnabled) {
		super("Ruby","rb",isDebugEnabled);
	}

	@Override
	protected void setupEntityBindings(Entity ent) {
		// Nothing to do for now
	}
	
	@Override
	protected boolean handleLangException(ScriptException e, Entity ent,
			ExecutionProgress progress) {
		// For now, we don't know how to decipher Ruby exceptions. 
		return false;
	}
}
