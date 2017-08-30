package plm.core.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.LessonLoadingException;
import plm.core.utils.FileUtils;

public class ChooseLessonDialog extends JFrame {
	private static final long serialVersionUID = 1L;
	I18n i18n = I18nFactory.getI18n(getClass(),"org.plm.i18n.Messages",getLocale(), I18nFactory.FALLBACK);

	public ChooseLessonDialog() {
		super();
		Game.getInstance().setCaptureOutput(false);
		setTitle(i18n.tr("Choose your lesson"));
		FileUtils.setLocale(this.getLocale());
		initComponents(Game.getInstance());
	}

	private void initComponents(Game g) {		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent event) {
	        	MainFrame.getInstance().quit();	        			
	        }
	    });
		
		setBackground(Color.white);
		setLayout(new BorderLayout());

		JEditorPane blurb = new JEditorPane("text/html", "");
		blurb.setEditable(false);
		blurb.setEditorKit(new PlmHtmlEditorKit());
		blurb.setText(i18n.tr("<table border=\"0\" align=\"center\"><tr>\n" +
				"<td valign=\"center\"><img src=\"img/world_buggle.png\" /></td>\n" +
				"<td valign=\"center\">&nbsp;&nbsp;<font size=\"+2\">Welcome to the Programmer's Learning Machine</font>&nbsp;&nbsp;</td>\n" +
				"<td valign=\"center\"><img src=\"img/world_buggle.png\" /></td>\n" +
				"</tr></table>\n" +
				"\n" +
				"<p><font size=\"+1\">The PLM is a Learning Management System (LMS) aiming at teaching the art of computer " +
				"programming through interactive exercises. It offers an extensive set of varied " +
				"exercises, allowing you to practice at your own pace.</font></p><br/>"));

		LessonOverview overview = new LessonOverview(this);
		
		LessonMatrix matrix = new LessonMatrix(overview, new String[][] { // WARNING, keep ExoTest.lessons synchronized
				{"lessons/welcome", "lessons/maze", "lessons/turmites", "lessons/turtleart"},
				{"lessons/sort/basic", "lessons/sort/dutchflag", "lessons/sort/baseball", "lessons/sort/pancake"},
				{"lessons/recursion/cons", "lessons/recursion/lego", "lessons/recursion/hanoi" },
				{"lessons/lightbot", "lessons/bat/string1", "lessons/lander" },
		    }); 
	

		add(blurb,BorderLayout.NORTH);

		JSplitPane mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);

		mainPane.setBackground(Color.white);
		mainPane.setLeftComponent(matrix);
		mainPane.setRightComponent(overview);
		
		add(mainPane, BorderLayout.CENTER);

		pack();
		setSize(780, 500);
		setVisible(true);
		setResizable(false);
	}
}


class LessonMatrix extends JPanel {
	private I18n i18n = I18nFactory.getI18n(getClass(),"org.plm.i18n.Messages",getLocale(), I18nFactory.FALLBACK);

	private static final long serialVersionUID = 1L;

	public LessonMatrix(LessonOverview overview, String[][] lessons) {
		setBackground(Color.white);
		GridBagLayout gl = new GridBagLayout(); 
		setLayout(gl);
		
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 3, 3, 3);
        c.gridwidth = 1;

        int maxCol=0;
        for (int row = 0; row < lessons.length; row++) {
        	for (int col=0; col < lessons[row].length; col++) {
        		LessonButton btLesson = new LessonButton(overview, lessons[row][col]);
        		
        		c.gridy = row;
        		c.gridx = col;
        		gl.setConstraints(btLesson, c);
        		add(btLesson);
        	}
        	if (row < lessons.length) {
        		if (lessons[row].length>maxCol)
        			maxCol = lessons[row].length-1;
        	} else if (lessons[row].length>maxCol) // React correctly to when the last line is longer than the others
    			maxCol = lessons[row].length;
        }
        /* add a load lesson button */
        JButton btLoadLesson = new JButton();
        btLoadLesson.setIcon(ResourcesCache.getIcon("img/bt-load-lesson.png")); 
		btLoadLesson.setSize(50,50);
		btLoadLesson.setBackground(Color.white);
		btLoadLesson.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileNameExtensionFilter(i18n.tr("PLM lesson files"), "plm"));
				fc.setDialogType(JFileChooser.OPEN_DIALOG);
				fc.showOpenDialog(MainFrame.getInstance());
				File selectedFile = fc.getSelectedFile();

				try {
					if (selectedFile != null)
						Game.getInstance().loadLessonFromJAR(fc.getSelectedFile());
				} catch (LessonLoadingException lle) {
					JOptionPane.showMessageDialog(null, lle.getMessage(), i18n.tr("Error"), JOptionPane.ERROR_MESSAGE); 
				}
			}
		});
		c.gridy = lessons.length-1;
		c.gridx = maxCol;
		gl.setConstraints(btLoadLesson, c);
		add(btLoadLesson);
	}
	
}

class LessonOverview extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JButton btGo;
	private String path;
	private JEditorPane desc;
	private I18n i18n = I18nFactory.getI18n(getClass(),"org.plm.i18n.Messages",getLocale(), I18nFactory.FALLBACK);

	public LessonOverview(final ChooseLessonDialog lc) {
		setLayout(new BorderLayout());
		
		setBackground(Color.white);
		desc = new JEditorPane("text/html", "");
		desc.setEditable(false);
		desc.setEditorKit(new PlmHtmlEditorKit());
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
				if(Game.getInstance().switchLesson(path.replaceAll("/", "."),false)!=null) {
					MainFrame.getInstance().setVisible(true);
					Game.getInstance().setCaptureOutput(true);
					lc.dispose();
				}
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
		btGo.setEnabled(true);
		btGo.requestFocusInWindow();
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
	
	public void goOnDoubleClick(){
		btGo.doClick();
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
		addMouseListener(new MouseAdapter() {
			String filename = path.replace('.', File.separatorChar)
					+ "/short_desc";

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					overview.setPath(path);
					overview.goOnDoubleClick();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {

				try {
					BufferedReader buffer = FileUtils.newFileReader(filename,
							"html", true);
					String line = buffer.readLine();
					while (!line.contains("<h3>")) {
						line = buffer.readLine();
					}

					String title = line.replaceAll("<h3>|</h3>", "");

					setToolTipText(title);
					buffer.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}

			}
		});
	}
}
