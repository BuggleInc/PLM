package jlm.core.ui;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.apple.eawt.Application;

import jlm.core.model.Game;
import jlm.core.model.FileUtils;


public class JavaLearningMachine {
	
	public static void main(String args[]) {
		
		if (System.getProperty("os.name").startsWith("Mac")) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "JLM");
			//Dock Logo
			Image img = ResourcesCache.getIcon("resources/logo.png").getImage(); 
			Application app = Application.getApplication();
			app.setDockIconImage(img);
		}
		
		FileUtils.setLocale(new JFrame().getLocale().getLanguage());
		
		Game.getInstance().loadChooser();
		MainFrame.getInstance().setVisible(true);
	}
}
