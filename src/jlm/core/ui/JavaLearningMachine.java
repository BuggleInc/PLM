package jlm.core.ui;

import javax.swing.JFrame;

import jlm.core.model.Game;
import jlm.core.model.Reader;


public class JavaLearningMachine {
	
	public static void main(String args[]) {
		
		if (System.getProperty("os.name").startsWith("Mac")) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "JLM");
		}
		
		Reader.setLocale(new JFrame().getLocale().getLanguage());
		
		Game.getInstance().loadLesson("lessons.chooser");
		MainFrame.getInstance().setVisible(true);
	}
}
