package jlm.core.ui;

import java.net.URLClassLoader;

import javax.swing.JFrame;

import jlm.core.GameListener;
import jlm.core.model.Course;
import jlm.core.model.CourseAppEngine;
import jlm.core.model.Game;
import jlm.core.model.JLMClassLoader;
import jlm.core.model.Reader;
import jlm.core.model.lesson.Lesson;


public class JavaLearningMachine {
	
	public static void main(String args[]) {
		
		if (System.getProperty("os.name").startsWith("Mac")) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "JLM");
		}
		
		Reader.setLocale(new JFrame().getLocale().getLanguage());
		
		Game.getInstance().loadChooser();
		MainFrame.getInstance().setVisible(true);
		
		CourseAppEngine c = new CourseAppEngine();
	}
}
