package plm.core.ui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import plm.core.model.Game;
import plm.core.utils.FileUtils;


public class ProgrammersLearningMachine {
	
	public static void main(String args[]) {
		FileUtils.setLocale(new JFrame().getLocale());
		
		Game.getInstance(); // make sure it's created
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				String exoUrl = Game.getProperty(Game.PROP_CURRENT_EXERCISE, "plm://lessons.welcome/", true);
				Game.getInstance().setCurrentExercise(exoUrl);
				MainFrame.getInstance().setVisible(true);
			}
		});

	}
}
