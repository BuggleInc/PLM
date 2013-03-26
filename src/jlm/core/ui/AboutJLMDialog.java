package jlm.core.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jlm.core.model.Game;

public class AboutJLMDialog extends JDialog {

	private static final long serialVersionUID = -1800747039420103759L;
	private static AboutJLMDialog instance = null;
	
	public static AboutJLMDialog getInstance() {
		if (AboutJLMDialog.instance == null)
			AboutJLMDialog.instance = new AboutJLMDialog();
		return AboutJLMDialog.instance;
	}
	
	private AboutJLMDialog() {
		super(MainFrame.getInstance(), "About JLM", true);
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
				"<h3 style=\"color:#666666;margin:0px;padding:0px;\">version "+Game.getProperty("jlm.major.version","internal")+"&nbsp;"+
				"<span style=\"font-size:8px; color:#AAAAAA;margin:0px;padding:0px;\">("+Game.getProperty("jlm.major.version","internal")+"."+Game.getProperty("jlm.minor.version","")+")</span>"+
				"</h3>"+
				"<br/>"+
				"&copy; 2008-2011 Contributors. All rights reserved.<br/>"+
				"<ul style=\"margin-left:20px;\">" +
				"<li>Original idea: <i>L. Turbak (Wellesley College)</i></li>"+
				"<li>Design and code: <i>M. Quinson and G. Oster</i></li>"+
				"<li>Tests: <i>esial students (class '11, '12, '13, '14)</i></li>"+
				"</ul><br/>"+
				"Your code is saved to "+Game.getInstance().getSessionKit().getSavingLocation()+"<br/>"+
				"</html>"
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
