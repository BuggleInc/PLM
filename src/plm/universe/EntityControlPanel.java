package plm.universe;

import java.util.Locale;

import javax.swing.JPanel;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import plm.core.HumanLangChangesListener;
import plm.core.model.Game;

public abstract class EntityControlPanel extends JPanel implements HumanLangChangesListener {
	private static final long serialVersionUID = 1L;
	public abstract void setEnabledControl(boolean enabled);

	public I18n i18n = I18nFactory.getI18n(getClass(),"org.plm.i18n.Messages",Game.getInstance().getLocale(), I18nFactory.FALLBACK);
	
	public void currentHumanLanguageHasChanged(Locale newLang) {
		if (newLang!=null)
			i18n = I18nFactory.getI18n(getClass(),"org.plm.i18n.Messages",newLang, I18nFactory.FALLBACK);
	}
	
	/** Do a textual output corresponding to the fact that a button was pressed. This is a direct help */
	public static void echo(String name) {
		System.out.println(name+(Game.getProgrammingLanguage()==Game.JAVA?";":""));
	}
	
	public void dispose() {
		
	}
}
