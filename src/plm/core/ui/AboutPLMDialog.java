package plm.core.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

import plm.core.model.Game;
import plm.core.utils.FileUtils;

public class AboutPLMDialog extends JDialog {

	private static final long serialVersionUID = -1800747039420103759L;
	private static AboutPLMDialog instance = null;
	
	
	public static AboutPLMDialog getInstance() {
		if (AboutPLMDialog.instance == null)
			AboutPLMDialog.instance = new AboutPLMDialog();
		return AboutPLMDialog.instance;
	}
	
	private AboutPLMDialog() {
		super(MainFrame.getInstance(), "About PLM", true);
		this.setTitle(Game.i18n.tr("About PLM"));
		initComponent();
	}
	
	
	public void initComponent() {
		JTabbedPane tabs = new JTabbedPane();
		
		JPanel aboutPane = new JPanel();
		aboutPane.setBackground(Color.white);	
		aboutPane.setLayout(new BorderLayout());
		JLabel icon = new JLabel(ResourcesCache.getIcon("img/BuggleQuestBack.png"));
		aboutPane.add(BorderLayout.NORTH, icon);
		JLabel iconTitle = new JLabel(ResourcesCache.getIcon("img/BuggleQuestBETA.png"));
		aboutPane.add(BorderLayout.CENTER, iconTitle);
		
		JTextPane text = new JTextPane();
		text.setContentType("text/html");
		text.setEditable(false);
		text.setBorder(null);
		text.setBackground(Color.white);
		text.setOpaque(true);
		text.setText(Game.i18n.tr(
				"<html>"+
				"<h3 style=\"color:#666666;margin:0px;padding:0px;\">version {0}&nbsp;"+
				"<span style=\"font-size:8px; color:#AAAAAA;margin:0px;padding:0px;\">({1})</span>"+
				"</h3>"+
				"<br/>"+
				"&copy; 2008-2011 Contributors. All rights reserved.<br/>"+
				"<ul style=\"margin-left:20px;\">" +
				"<li>Original idea: <i>L. Turbak (Wellesley College)</i></li>"+
				"<li>Design and code: <i>M. Quinson and G. Oster</i></li>"+
				"<li>Tests: <i>Telecom Nancy students (all classes since ''11)</i></li>"+
				"</ul><br/>"+
				"Your code is saved to {2}<br/>"+
				"Your secret session ID is {3}<br/>"+
				"</html>",
				Game.getProperty("plm.major.version","internal",false),
				Game.getProperty("plm.major.version","internal",false)+"."+Game.getProperty("plm.minor.version","",false),
				Game.getSavingLocation(),
				Game.getInstance().getUsers().getCurrentUser().getUserUUID())
		);
		aboutPane.add(BorderLayout.SOUTH, text);
		
		tabs.add(Game.i18n.tr("About"),aboutPane);
		
		JEditorPane changelogArea = new JEditorPane("text/html","");
		changelogArea.setEditorKit(new PlmHtmlEditorKit());
		changelogArea.setEditable(false);
		try {
			StringBuffer ctn = new StringBuffer();
			Pattern itemPattern = Pattern.compile("^ +[*].*");
			Pattern subItemPattern = Pattern.compile("^ +[-].*");
			boolean inSubItem = false;
			for (String s:FileUtils.readContentAsText("ChangeLog",null,false).toString().split("\n")) {
				if (s.startsWith("20")) {// all releases are done in this century
					if (inSubItem)
						ctn.append("</ul>");
					inSubItem = false;
					ctn.append("</ul><h2>"+s+"</h2><ul>\n");
				} else if (itemPattern.matcher(s).matches()) { 
					if (inSubItem)
						ctn.append("</ul>");
					inSubItem = false;
					ctn.append(s.replaceFirst("[*]", "<li>")+"\n");
				} else if (subItemPattern.matcher(s).matches()) { 
					if (!inSubItem)
						ctn.append("<ul>");
					ctn.append(s.replaceFirst("[-]", "<li>")+"\n");
					inSubItem = true;
				} else {
					ctn.append(s+"\n");
				}
			}
			changelogArea.setText(ctn.toString());
			changelogArea.setCaretPosition(0);
		} catch (Exception e) {
			System.out.println("ChangeLog not found. Broken jarfile.");
		}
		JScrollPane sp  = new JScrollPane(changelogArea);
		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		tabs.add(Game.i18n.tr("Changes"),sp);
		
		JEditorPane licenseArea = new JEditorPane("text/html","");
		licenseArea.setEditorKit(new PlmHtmlEditorKit());
		licenseArea.setEditable(false);
		licenseArea.setText(Game.i18n.tr(
				  "<h2>PLM is proudly Free Software</h2>"
				+ ""
				+ "<p>Copyright (c) 2008-2013.<br/> "
				+ "The PLM Team: Martin Quinson, Gérald Oster and others.<br/> "
				+ "(see the git logs for the exact authorship of each file).</p> "
				+ ""
				+ "<p>The PLM software was written internally by the team. This software is "
				+ "distributed under the GNU general public license version 3 (GPLv3).</p> "
				+ ""
				+ "<p>The pedagogical material distributed with the PLM is covered both by "
				+ "the GPLv3 license (mainly for the included code) and by the CC-BY-SA "
				+ "license (mainly for the other media files). But the exact boundary "
				+ "here between the source code and the media is left as an exercise to "
				+ "the reader, so this material is distributed under both licenses for "
				+ "sake of simplicity.</p>"
				+ ""
				+ "<p>The GPLv3 license can be found in the archive under the name "
				+ "LICENSE-GPL-3. The CC-BY-SA license can be found online: "
				+ "http://creativecommons.org/licenses/by-sa/3.0/</p>"
				+ ""
				+ "<p>The whole artwork is free content, licenced under CC-BY-SA, GPL, LGPL "
				+ "and/or CC0-public domain. Some parts were done by us, other parts were "
				+ "borowed from free collections. The specific license and origin of each "
				+ "files is listed in the COPYING file distributed with the archive.</p>"
				));
		licenseArea.setCaretPosition(0);
		tabs.add(Game.i18n.tr("Copying"),new JScrollPane(licenseArea));
		
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		pane.add(BorderLayout.CENTER, tabs);
		
		JButton close = new JButton(Game.i18n.tr("Close"));
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		pane.add(BorderLayout.SOUTH, close);
		
		setContentPane(pane);
		pane.setPreferredSize(new Dimension(400, MainFrame.getInstance().getSize().height));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		pack();
		setResizable(true);
				
		close.requestFocusInWindow();
		setLocationRelativeTo(getParent());
	}
	
}
