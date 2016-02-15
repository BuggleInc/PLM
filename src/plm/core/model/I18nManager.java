package plm.core.model;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

public class I18nManager {

	private static final Map<String, I18n> i18ns = new HashMap<String, I18n>();

	public static void addI18n(String lang) {
		I18n i18n = I18nFactory.getI18n(I18nManager.class, "org.plm.i18n.Messages", new Locale(lang), I18nFactory.FALLBACK);
		i18ns.put(lang, i18n);
	}

	public static I18n getI18n(Locale locale) {
		final String lang = locale.toString();
		if(!i18ns.containsKey(lang)) {
			addI18n(lang);	
		}
		return i18ns.get(lang);
	}
}
