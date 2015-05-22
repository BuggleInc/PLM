package plm.core.lang;

import javax.script.ScriptException;

import org.xnap.commons.i18n.I18n;

import plm.core.model.lesson.ExecutionProgress;
import plm.universe.Entity;

public class LangRuby extends ScriptingLanguage {

	public LangRuby(boolean isDebugEnabled) {
		super("Ruby", "rb",isDebugEnabled);
	}

	@Override
	protected void setupEntityBindings(Entity ent) {
		// Nothing to do for now
	}
	
	@Override
	protected boolean handleLangException(ScriptException e, Entity ent,
			ExecutionProgress progress, I18n i18n) {
		// For now, we don't know how to decipher Ruby exceptions. 
		return false;
	}
}
