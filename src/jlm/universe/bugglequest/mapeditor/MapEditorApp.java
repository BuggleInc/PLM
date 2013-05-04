package jlm.universe.bugglequest.mapeditor;

import java.awt.BorderLayout;

import javax.swing.JFrame;


public class MapEditorApp {

	public static void main(String[] args) {
		MapEditorPanel m = new MapEditorPanel(new Editor());
		JFrame frame = new JFrame("BuggleQuest - MapEditor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setSize(800, 600);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.add(m,BorderLayout.CENTER);
		frame.setVisible(true);
		frame.pack();
	}

}
