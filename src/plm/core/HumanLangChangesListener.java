package plm.core;

import java.util.Locale;

public interface HumanLangChangesListener {
    // when the used human language (English, French, etc) has changed
	public void currentHumanLanguageHasChanged(Locale newLang);

}
