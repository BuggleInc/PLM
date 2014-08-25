package plm.core;

import plm.core.lang.ProgrammingLanguage;

public interface ProgLangChangesListener {
    // when the used programming language has changed
	public void currentProgrammingLanguageHasChanged(ProgrammingLanguage newLang);

}
