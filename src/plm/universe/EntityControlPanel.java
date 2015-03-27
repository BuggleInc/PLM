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
	private Game game;
	
	public EntityControlPanel(Game game) {
		this.game = game;
	}
	
	public void currentHumanLanguageHasChanged(Locale newLang) {
	}
	
	/** Do a textual output corresponding to the fact that a button was pressed. This is a direct help */
	public static void echo(String name) {
		System.out.println(name+(Game.getProgrammingLanguage()==Game.JAVA?";":""));
	}
	
	public Game getGame() {
		return game;
	}
	
	public void dispose() {
		
	}
}
