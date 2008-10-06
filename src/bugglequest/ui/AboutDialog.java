package bugglequest.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AboutDialog extends JDialog {

	private static final long serialVersionUID = -1800747039420103759L;
	private static AboutDialog instance = null;
	
	public static AboutDialog getInstance() {
		if (AboutDialog.instance == null)
			AboutDialog.instance = new AboutDialog();
		return AboutDialog.instance;
	}
	
	private AboutDialog() {
		super(MainFrame.getInstance(), "About BuggleQuest", true);
		initComponent();
	}
	
	
	public void initComponent() {
		
		JPanel upperPane = new JPanel();
		upperPane.setLayout(new BorderLayout());
		JLabel icon = new JLabel(ResourcesCache.getIcon("resources/BuggleQuestBack.png"));
		upperPane.add(BorderLayout.NORTH, icon);
		JLabel iconTitle = new JLabel(ResourcesCache.getIcon("resources/BuggleQuestBETA.png"));
		upperPane.add(BorderLayout.CENTER, iconTitle);
		
		JLabel text = new JLabel(
				"<html>"+
				"<h3 style=\"color:#666666;margin:0px;padding:0px;\">version 0.9</h3>"+
				"<br/>"+
				"&copy; 2008 Contributors. All rights reserved.<br/>"+
				"<ul style=\"margin-left:20px;\">" +
				"<li>Original idea: <i>L. Turbak (Wellesley College)</i></li>"+
				"<li>Design and code: <i>M. Quinson and G. Oster</i></li>"+
				"<li>Tests: <i>esial students (class '11)</i></li>"+
				"</ul></html>"
		);
		text.setBackground(Color.white);
		text.setOpaque(true);
		upperPane.add(BorderLayout.SOUTH, text);
		
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		pane.add(BorderLayout.CENTER, upperPane);
		
		JButton close = new JButton("Close");
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		pane.add(BorderLayout.SOUTH, close);
		
		setContentPane(pane);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		pack();
		setResizable(false);
				
		setLocationRelativeTo(getParent());
	}
	
}
