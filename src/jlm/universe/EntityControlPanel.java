package jlm.universe;

import java.util.Locale;

import javax.swing.JPanel;

import jlm.core.HumanLangChangesListener;
import jlm.core.model.Game;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

public abstract class EntityControlPanel extends JPanel implements HumanLangChangesListener {
	private static final long serialVersionUID = 1L;
	public abstract void setEnabledControl(boolean enabled);

	public I18n i18n = I18nFactory.getI18n(getClass(),"org.jlm.i18n.Messages",Game.getInstance().getLocale(), I18nFactory.FALLBACK);
	
	public void currentHumanLanguageHasChanged(Locale newLang) {
		i18n = I18nFactory.getI18n(getClass(),"org.jlm.i18n.Messages",newLang, I18nFactory.FALLBACK);
	}
}
