package jlm.ui;


public class JavaLearningMachine {

	public static void main(String args[]) {
		
		if (System.getProperty("os.name").startsWith("Mac")) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "JLM");
		}
		
		MainFrame.getInstance().setVisible(true);
	}
}
