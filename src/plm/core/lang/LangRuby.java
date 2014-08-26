package plm.core.lang;

import plm.core.ui.ResourcesCache;

public class LangRuby extends ScriptingLanguage {

	public LangRuby() {
		super("Ruby","rb",ResourcesCache.getIcon("img/lang_ruby.png"));
	}
}
