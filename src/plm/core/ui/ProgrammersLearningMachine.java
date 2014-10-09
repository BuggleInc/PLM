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
				ChooseLessonDialog cld = new ChooseLessonDialog();
				cld.setVisible(true);
			}
		});

	}
}
