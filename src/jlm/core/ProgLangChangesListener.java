package jlm.core;

import jlm.core.model.ProgrammingLanguage;

public interface ProgLangChangesListener {
    // when the used programming language has changed
	public void currentProgrammingLanguageHasChanged(ProgrammingLanguage newLang);

}
