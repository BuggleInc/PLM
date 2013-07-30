package jlm.core.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;
import jlm.core.utils.FileUtils;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

public class LessonChooser extends JFrame {
	private static final long serialVersionUID = 1L;
	I18n i18n = I18nFactory.getI18n(getClass(),"org.jlm.i18n.Messages",getLocale(), I18nFactory.FALLBACK);

	public LessonChooser() {
		super();
		setTitle(i18n.tr("Choose your lesson"));
		FileUtils.setLocale(this.getLocale());
		initComponents(Game.getInstance());
	}

	private void initComponents(Game g) {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

		setBackground(Color.white);
		setLayout(new BorderLayout());

		JEditorPane blurb = new JEditorPane("text/html", "");
		blurb.setEditable(false);
		blurb.setEditorKit(new JlmHtmlEditorKit());
		blurb.setText(i18n.tr("<table border=\"0\" align=\"center\"><tr>\n" +
				"<td valign=\"center\"><img src=\"img/world_buggle.png\" /></td>\n" +
				"<td valign=\"center\">&nbsp;&nbsp;<font size=\"+2\">Welcome to the Java Learning Machine</font>&nbsp;&nbsp;</td>\n" +
				"<td valign=\"center\"><img src=\"img/world_buggle.png\" /></td>\n" +
				"</tr></table>\n" +
				"\n" +
				"<p><font size=\"+1\">The JLM is a Learning Management System (LMS) aiming at teaching the art of computer " +
				"programming through interactive exercises. It offers an extensive set of varied " +
				"exercises, allowing you to practice at your own pace.</font></p><br/>"));

		LessonOverview overview = new LessonOverview(this);
		
		LessonMatrix matrix = new LessonMatrix(overview, new String[][] { // WARNING, keep ExoTest.lessons synchronized
				{"lessons/welcome","lessons/maze", "lessons/bat/string1"},
				{"lessons/sort", "lessons/sort/baseball", "lessons/sort/pancake"},
				{"lessons/recursion", "lessons/recursion/hanoi" },
				{"lessons/lightbot" },
		    }); 
	

		add(blurb,BorderLayout.NORTH);

		JSplitPane mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);

		mainPane.setBackground(Color.white);
		mainPane.setLeftComponent(matrix);
		mainPane.setRightComponent(overview);
		
		add(mainPane, BorderLayout.CENTER);

		pack();
		setSize(700, 500);
		setVisible(true);
		setResizable(false);
	}
}


class LessonMatrix extends JPanel {
	private static final long serialVersionUID = 1L;

	public LessonMatrix(LessonOverview overview, String[][] lessons) {
		setBackground(Color.white);
		GridBagLayout gl = new GridBagLayout(); 
		setLayout(gl);
		
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 3, 3, 3);
        c.gridwidth = 1;

        for (int row = 0; row < lessons.length; row++) {
        	for (int col=0; col < lessons[row].length; col++) {
        		LessonButton btLesson = new LessonButton(overview, lessons[row][col]);
        		
        		c.gridy = row;
        		c.gridx = col;
        		gl.setConstraints(btLesson, c);
        		add(btLesson);
        	}
        }
	}
}

class LessonOverview extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JButton btGo;
	private String path;
	private JEditorPane desc;
	private I18n i18n = I18nFactory.getI18n(getClass(),"org.jlm.i18n.Messages",getLocale(), I18nFactory.FALLBACK);

	public LessonOverview(final LessonChooser lc) {
		setLayout(new BorderLayout());
		
		setBackground(Color.white);
		desc = new JEditorPane("text/html", "");
		desc.setEditable(false);
		desc.setEditorKit(new JlmHtmlEditorKit());
		desc.setText(i18n.tr("<h1>Please pick a lesson</h1>\n" +
				"<p>Please click on an icon on the left to select a lesson.</p>\n" +
				"<p>Lessons located above are generally simpler than the ones located below.</p>"));

		JScrollPane descScrol = new JScrollPane(desc);
		JPanel descPanel = new JPanel(new BorderLayout());
		descPanel.add(descScrol,BorderLayout.CENTER);
		descPanel.setBackground(Color.white);
		descPanel.setSize(new Dimension(27, 15));
		descPanel.doLayout();
		add(descPanel,BorderLayout.CENTER);
		
		btGo = new JButton(i18n.tr("Go"));
		btGo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Game.getInstance().switchLesson(path.replaceAll("/", "."),false);
				MainFrame.getInstance().setVisible(true);
				lc.dispose();
			}
		});
		btGo.setEnabled(false);
		
        JPanel bottomButtons = new JPanel();
        bottomButtons.setBackground(Color.white);
        bottomButtons.setLayout(new FlowLayout());
        bottomButtons.add(btGo);

        add(bottomButtons,BorderLayout.SOUTH);
	}
	
	public void setPath(String path) {
		// FIXME: give the focus to the btGo
		this.btGo.setEnabled(true);
		this.path = path;
		
		String filename = path.replace('.',File.separatorChar)+"/short_desc";
		StringBuffer sb = null;
		try {
			sb = FileUtils.readContentAsText(filename, "html",true);
		} catch (IOException ex) {
			filename += ".html";
			sb = new StringBuffer(i18n.tr("<p>(unable to display the short description of this lesson: file {0} not found)</p>",filename));
		}

		sb.append(i18n.tr("<p><b>Your score:</b> "));
		String id = path.replaceAll("/", ".").replaceAll("^lessons\\.", "");
		boolean foundOne = false;
		for (ProgrammingLanguage lang:Game.programmingLanguages) {
			int possible = Game.getInstance().studentWork.getPossibleExercises(id, lang);
			int passed = Game.getInstance().studentWork.getPassedExercises(id, lang);
			if (possible>0) {
				if (lang == Game.LIGHTBOT) 
					sb.append(" "+i18n.tr("{0} out of {1} exercises passed.",passed,possible));
				else {
					sb.append("<br/>");
					sb.append("&nbsp;&nbsp;&nbsp;&nbsp;<img src=\"img/lang_"+lang.getLang().toLowerCase()+".png\">&nbsp;&nbsp;");
					sb.append(i18n.tr("{0} out of {1} exercises passed in {2}.",passed,possible,lang.getLang()));
				}
				foundOne = true;
			}
		}
		if (!foundOne) 
			sb.append(i18n.tr("You never attempted this lesson."));
		sb.append("</p>");
		
		desc.setText(sb.toString());
		desc.setCaretPosition(0);
	}	
}

class LessonButton extends JButton {
	private static final long serialVersionUID = 1L;

	public LessonButton(final LessonOverview overview, final String path) {
		super();
		Icon icon = ResourcesCache.getIcon(path+"/icon.png"); 
		setIcon(icon);
		setSize(icon.getIconWidth(), icon.getIconHeight());
		this.setBackground(Color.white);
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				overview.setPath(path);
			}
		});
	}
}