package jlm.core.ui;

import javax.swing.JFrame;

import jlm.core.model.Game;
import jlm.core.utils.FileUtils;


public class JavaLearningMachine {
	
	public static void main(String args[]) {
		
		if (System.getProperty("os.name").startsWith("Mac")) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "JLM");
		}
		
		
		FileUtils.setLocale(new JFrame().getLocale());
		
		Game.getInstance().loadChooser();
		MainFrame.getInstance().setVisible(false);		
		new LessonChooser();
		
	}
}
