package plm.core.ui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import plm.core.model.Game;
import plm.core.model.User;
import plm.core.utils.FileUtils;


public class ProgrammersLearningMachine {
	
	public static void main(String args[]) {
		FileUtils.setLocale(new JFrame().getLocale());
		
		Game.getInstance(); // make sure it's created
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				User user = Game.getInstance().getUsers().getCurrentUser();
				Game.getInstance().setCurrentExercise(user.getCurrentExercise());
				
				MainFrame.getInstance().setVisible(true);
				Game.getInstance().setCaptureOutput(true);
			}
		});

	}
}
