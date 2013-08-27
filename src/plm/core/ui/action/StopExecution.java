package plm.core.ui.action;

import java.awt.event.ActionEvent;
import java.util.Locale;

import javax.swing.ImageIcon;

import plm.core.HumanLangChangesListener;
import plm.core.model.Game;


public class StopExecution extends AbstractGameAction implements HumanLangChangesListener {

	private static final long serialVersionUID = -4563140493957678216L;
	
	public StopExecution(Game game, String text, ImageIcon icon) {
		super(game, text, icon);
		game.addHumanLangListener(this);
	}
	@Override
	public void currentHumanLanguageHasChanged(Locale newLang) {
		setDescription(i18n.tr("Stop your code"), 
				i18n.tr("No execution to stop right now"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.game.stopExerciseExecution();		
	}
}
