package jlm.core.ui;


public class JavaLearningMachine {
	static ChooserFrame chooser;
	
	public static void main(String args[]) {
		
		if (System.getProperty("os.name").startsWith("Mac")) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "JLM");
		}
		
		chooser = new ChooserFrame();
		chooser.setVisible(true);
	}
}
