package plm.core.ui.action;

import java.awt.event.ActionEvent;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import plm.core.HumanLangChangesListener;
import plm.core.model.Game;
import plm.core.ui.MainFrame;

/** Action launched when the Start button gets hit */
public class StartExecution extends AbstractGameAction implements HumanLangChangesListener {

	private static final long serialVersionUID = -4326617501298324713L;
	
	public StartExecution(Game game, String text, ImageIcon icon) {
		super(game, text, icon);
		game.addHumanLangListener(this);
	}

	static private int asked = 0;
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Property: "+ Game.getProperty("plm.git.track.user"));
		
		if(Game.getProperty("plm.git.track.user").equals("unknown") && (asked--== 0)) {
			asked = 5; /* Ask every 5 runs */
			
			String trackUserProperty = "unknown";
			String message = i18n.tr("The PLM can save your data anonymously on remote servers. That way, you \n"
				    + "can retrieve your session from your anonymous tag from any connected computer.\n"
				    + "This anonymous data also helps scientists understanding how people learn \n"
				    +" programming. No nominative information is stored, only the source code written\n"
				    + "to solve the challenges.\n");
			String title = i18n.tr("Do you want to store your data online?");
			Object[] options = {i18n.tr("Ok, save my data online"), i18n.tr("No, keep everything local"), i18n.tr("Let me decide later")};
			int n = JOptionPane.showOptionDialog(MainFrame.getInstance(),
			    message,
			    title,
			    JOptionPane.YES_NO_CANCEL_OPTION,
			    JOptionPane.QUESTION_MESSAGE,
			    null,
			    options,
			    options[0]);
			if(n == 0) {
				// The user agreed to let us track his activity on PLM
				trackUserProperty = "true";
			} else if(n == 1) {
				trackUserProperty = "false";
			}
			Game.setProperty("plm.git.track.user", trackUserProperty);
		}

		if (game.getState().equals(Game.GameState.EXECUTION_STARTED) && game.stepModeEnabled()) {
			game.disableStepMode();
			game.allowOneStep();
		} else
			this.game.startExerciseExecution();		
	}

	@Override
	public void currentHumanLanguageHasChanged(Locale newLang) {
		setDescription(i18n.tr("Launch the execution of your code"),
				i18n.tr("Cannot launch the execution right now. Wait a bit, or interrupt current activity with the stop button"));		
	}
}
