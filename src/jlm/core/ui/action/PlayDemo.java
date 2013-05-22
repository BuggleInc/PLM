package jlm.core.ui.action;

import java.awt.event.ActionEvent;
import java.util.Locale;

import javax.swing.ImageIcon;

import jlm.core.HumanLangChangesListener;
import jlm.core.model.Game;


public class PlayDemo extends AbstractGameAction implements HumanLangChangesListener {

	private static final long serialVersionUID = 5113775865916404566L;

	public PlayDemo(Game game, String text, ImageIcon icon) {
		super(game, text, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.game.startExerciseDemoExecution();		
	}
	
	@Override
	public void currentHumanLanguageHasChanged(Locale newLang) {
		setDescription(i18n.tr("Run the demo of what you should code for this exercise"),
				i18n.tr("Impossible to run the demo right now"));		
	}
}
