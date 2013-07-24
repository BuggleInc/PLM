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
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import jlm.core.HumanLangChangesListener;
import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;
import jlm.core.utils.FileUtils;

public class LessonChooser extends JFrame implements HumanLangChangesListener {
	private static final long serialVersionUID = 1L;
	
	private String WELCOME_TEXT = "<table border=\"0\" align=\"center\"><tr>\n" +
			"<td valign=\"center\"><img src=\"img/world_buggle.png\" /></td>\n" +
			"<td valign=\"center\">&nbsp;&nbsp;<font size=\"+2\">Welcome to the Java Learning Machine</font>&nbsp;&nbsp;</td>\n" +
			"<td valign=\"center\"><img src=\"img/world_buggle.png\" /></td>\n" +
			"</tr></table>\n" +
			"\n" +
			"<p><font size=\"+1\">The JLM is a Learning Management System (LMS) aiming at teaching the art of computer " +
			"programming through interactive exercises. It offers an extensive set of varied " +
			"exercises, allowing you to practice at your own pace.</font></p><br/>";

	public LessonChooser() {
		super("Choose your lesson");
		FileUtils.setLocale(this.getLocale());
		initComponents(Game.getInstance());
		Game.getInstance().addHumanLangListener(this);
	}

	private void initComponents(Game g) {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

		setBackground(Color.white);
		setLayout(new BorderLayout());

		JEditorPane blurb = new JEditorPane("text/html", "");
		blurb.setEditable(false);
		blurb.setEditorKit(new JlmHtmlEditorKit());
		blurb.setText(Game.i18n.tr(WELCOME_TEXT));

		LessonOverview overview = new LessonOverview(this);
		
		LessonMatrix matrix = new LessonMatrix(overview, new String[][] {
				{"lessons/welcome","lessons/maze", "lessons/bat/string1"},
				{"lessons/sort", "lessons/sort/pancake", "lessons/smn/baseball"},
				{"lessons/recursion" },
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

	@Override
	public void currentHumanLanguageHasChanged(Locale newLang) {
		setTitle(Game.i18n.tr("Choose your lesson"));
		
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

class LessonOverview extends JPanel implements HumanLangChangesListener {
	private static final long serialVersionUID = 1L;
	
	private String PICK_TEXT = "<h1>Please pick a lesson</h1>";

	private JButton btGo;
	private String path;
	private JEditorPane desc;
	public LessonOverview(final LessonChooser lc) {
		setLayout(new BorderLayout());
		
		setBackground(Color.white);
		desc = new JEditorPane("text/html", "");
		desc.setEditable(false);
		desc.setEditorKit(new JlmHtmlEditorKit());
		desc.setText(Game.i18n.tr(PICK_TEXT));

		JScrollPane descScrol = new JScrollPane(desc);
		JPanel descPanel = new JPanel(new BorderLayout());
		descPanel.add(descScrol,BorderLayout.CENTER);
		descPanel.setBackground(Color.white);
		descPanel.setSize(new Dimension(27, 15));
		descPanel.doLayout();
		add(descPanel,BorderLayout.CENTER);
		
		btGo = new JButton(Game.i18n.tr("Go"));
		btGo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Game.getInstance().switchLesson(path.replaceAll("/", "."));
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
			sb = new StringBuffer(Game.i18n.tr("<p>(unable to display the lesson's short description: file {0}.html not found)</p>",filename));
		}

		sb.append(Game.i18n.tr("<p><b>Your score:</b> "));
		String id = path.replaceAll("/", ".").replaceAll("^lessons\\.", "");
		boolean foundOne = false;
		for (ProgrammingLanguage lang:Game.programmingLanguages) {
			int possible = Game.getInstance().studentWork.getPossibleExercises(id, lang);
			int passed = Game.getInstance().studentWork.getPassedExercises(id, lang);
			if (possible>0) {
				if (lang == Game.LIGHTBOT) 
					sb.append(" "+Game.i18n.tr("{0} out of {1} exercises passed.",passed,possible));
				else {
					sb.append("<br/>");
					sb.append("&nbsp;&nbsp;&nbsp;&nbsp;<img src=\"img/lang_"+lang.getLang().toLowerCase()+".png\">&nbsp;&nbsp;");
					sb.append(Game.i18n.tr("{0} out of {1} exercises passed in {2}.",passed,possible,lang.getLang()));
				}
				foundOne = true;
			}
		}
		if (!foundOne) 
			sb.append(Game.i18n.tr("You never attempted this lesson."));
		sb.append("</p>");
		
		desc.setText(sb.toString());
		desc.setCaretPosition(0);
	}

	@Override
	public void currentHumanLanguageHasChanged(Locale newLang) {
		if (!btGo.isEnabled()) {
			// no lesson selected yet
			desc.setText(Game.i18n.tr(PICK_TEXT));
		} else {
			setPath(path);
		}
		btGo.setText(Game.i18n.tr("Go"));
		
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