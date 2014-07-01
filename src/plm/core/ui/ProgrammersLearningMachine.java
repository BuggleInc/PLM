package plm.core.ui;

import javax.swing.JFrame;

import plm.core.model.Game;
import plm.core.utils.FileUtils;


public class ProgrammersLearningMachine {
	
	public static void main(String args[]) {
		
		if (System.getProperty("os.name").startsWith("Mac")) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "PLM");
		}
		
		
		FileUtils.setLocale(new JFrame().getLocale());
		
		Game.getInstance(); // make sure it's created
		MainFrame.getInstance().setVisible(false);		
		ChooseLessonDialog cld = new ChooseLessonDialog();
		cld.setVisible(true);
	}
}
