package plm.core.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import plm.core.GameListener;
import plm.core.GameStateListener;
import plm.core.HumanLangChangesListener;
import plm.core.ProgLangChangesListener;
import plm.core.model.Game;
import plm.core.model.LessonLoadingException;
import plm.core.model.ProgrammingLanguage;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lecture;
import plm.core.ui.action.AbstractGameAction;
import plm.core.ui.action.ExportSession;
import plm.core.ui.action.HelpMe;
import plm.core.ui.action.ImportSession;
import plm.core.ui.action.PlayDemo;
import plm.core.ui.action.QuitGame;
import plm.core.ui.action.Reset;
import plm.core.ui.action.RevertExercise;
import plm.core.ui.action.SetLanguage;
import plm.core.ui.action.SetProgLanguage;
import plm.core.ui.action.StartExecution;
import plm.core.ui.action.StepExecution;
import plm.core.ui.action.StopExecution;
import plm.core.ui.action.SwitchExo;
import plm.core.utils.FileUtils;
import plm.universe.World;

public class MainFrame extends JFrame implements GameStateListener, GameListener, HumanLangChangesListener {

	private static final long serialVersionUID = -5022279647890315264L;

	private static MainFrame instance = null;

	private ExerciseView exerciseView;
	private JButton startButton;
	private JButton debugButton;
	private JButton stopButton;
	private JButton resetButton;
	private JButton demoButton;
    private JToggleButton helpMeButton;
    private JButton exoChangeButton;
    
    private JMenu menuFile;
    private JMenuItem miFileLoad,miFileSwitch,miFileExercise,miFileConsole=null,miFileCourse,miFileQuit;
    private JMenu menuSession;
    private JMenuItem miSessionRevert, miSessionExport, miSessionImport, miSessionDebug;

    private JMenu menuLanguage, menuLangHuman, menuLangProg;
    private JMenu menuHelp;
    private JMenuItem miHelpFeedback, miHelpLesson,miHelpWorld,miHelpAbout;
        
	private LoggerPanel outputArea;
	private MissionEditorTabs met;
	public I18n i18n = I18nFactory.getI18n(getClass(),"org.jlm.i18n.Messages",getLocale(), I18nFactory.FALLBACK);

	private JSplitPane mainPanel;
	
	private static final String frameTitle = "Java Learning Machine";

	private MainFrame() {
		super(frameTitle);
		FileUtils.setLocale(this.getLocale());
		initComponents(Game.getInstance());
		this.keyListeners(exerciseView);
		Game.getInstance().addHumanLangListener(this);
	}

	public static MainFrame getInstance() {
		if (MainFrame.instance == null)
			MainFrame.instance = new MainFrame();
		return MainFrame.instance;
	}

	private void initComponents(final Game g) {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		getContentPane().setLayout(new BorderLayout());

		JSplitPane logPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
		logPane.setOneTouchExpandable(true);
		double ratio = 0.7;
		logPane.setResizeWeight(ratio);
		logPane.setDividerLocation((int) (768 * ratio));

		mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);

		mainPanel.setOneTouchExpandable(true);
		double weight = 0.6;
		mainPanel.setResizeWeight(weight);
		mainPanel.setDividerLocation((int) (1024 * weight));

		mainPanel.setLeftComponent(met=new MissionEditorTabs());
		exerciseView = new ExerciseView(g);
		mainPanel.setRightComponent(exerciseView);

		logPane.setTopComponent(mainPanel);
		outputArea = new LoggerPanel(g);
		JScrollPane outputScrollPane = new JScrollPane(outputArea);
		logPane.setBottomComponent(outputScrollPane);
		getContentPane().add(logPane, BorderLayout.CENTER);

		initMenuBar(g);
		initToolBar(g);
		initStatusBar(g);
		currentExerciseHasChanged(g.getCurrentLesson().getCurrentExercise()); 

		g.addGameStateListener(this);
		g.addGameListener(this);

		pack();
		setSize(1024, 768);
		setVisible(false);
	}

	private void initMenuBar(Game g) {
		JMenuBar menuBar = new JMenuBar();
		
		
		/* === FILE menu === */
		// for now: leave the calls to i18n.tr: that way one is sure to get all the localized strings...
		menuFile = new JMenu(i18n.tr("File"));
		menuFile.setMnemonic(KeyEvent.VK_F);
		menuFile.getAccessibleContext().setAccessibleDescription(i18n.tr("File related functions"));

		
		miFileLoad = new JMenuItem(new AbstractGameAction(g, i18n.tr("Load lesson"), null, KeyEvent.VK_L) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileNameExtensionFilter(i18n.tr("JLM lesson files"), "jlm"));
				fc.setDialogType(JFileChooser.OPEN_DIALOG);
				fc.showOpenDialog(MainFrame.getInstance());
				File selectedFile = fc.getSelectedFile();

				try {
					if (selectedFile != null)
						game.loadLessonFromJAR(fc.getSelectedFile());
				} catch (LessonLoadingException lle) {
					JOptionPane.showMessageDialog(null, lle.getMessage(), i18n.tr("Error"), JOptionPane.ERROR_MESSAGE); 
				}
			}
		});
		menuFile.add(miFileLoad);
		
		
		miFileSwitch = new JMenuItem(new AbstractGameAction(g, i18n.tr("Switch lesson"), null, KeyEvent.VK_L) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				new ChooseLessonDialog();
				MainFrame.getInstance().setVisible(false);		
			}
		});
		miFileSwitch.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		menuFile.add(miFileSwitch);
		
		miFileExercise = new JMenuItem(new AbstractGameAction(g, i18n.tr("Switch exercise"), null, KeyEvent.VK_E) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				new ChooseLectureDialog();
			}
		});
		miFileExercise.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		menuFile.add(miFileExercise);
		
		// Teacher console menu item (shown only if defined in the JLM properties)
        if(Game.getProperty("jlm.configuration.teacher").equals("true")) {
            miFileConsole = new JMenuItem(new AbstractGameAction(g,i18n.tr("Teacher Console")) {

				private static final long serialVersionUID = 1L;
				private TeacherConsoleDialog dialog = null;

                @Override
                public void actionPerformed(ActionEvent e) {
                    // launch teacher console
					if (dialog == null) {
						dialog = new TeacherConsoleDialog();
					}
                    dialog.setVisible(true);
                }
            });
            miFileConsole.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
            menuFile.add(miFileConsole);
        }
        
        // Menu item to change the current Course
        miFileCourse = new JMenuItem(new AbstractGameAction(g, i18n.tr("Choose your course")) {

			private static final long serialVersionUID = 1L;
			private ChooseCourseDialog dialog = null;

            @Override
            public void actionPerformed(ActionEvent e) {
                // launch a dialog to choose the course
                if (dialog == null) {
					dialog = new ChooseCourseDialog();
				}
				dialog.setVisible(true);
            }
        });

        miFileCourse.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
        menuFile.add(miFileCourse);
       
        
        miFileQuit = new JMenuItem(new QuitGame(g, i18n.tr("Quit"), null,  KeyEvent.VK_Q));
        miFileQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));

		menuFile.add(miFileQuit);

		menuBar.add(menuFile);

		/* === Edit menu === */
		menuSession = new JMenu(i18n.tr("Session"));
		menuSession.setMnemonic(KeyEvent.VK_S);
		menuBar.add(menuSession);
		menuSession.setEnabled(true);
		
		miSessionRevert = new JMenuItem(new RevertExercise(g, i18n.tr("Revert Exercise"), null));
		menuSession.add(miSessionRevert);

		miSessionExport = new JMenuItem(new ExportSession(g, i18n.tr("Export Session Cache"),	null, this));
		menuSession.add(miSessionExport);

		miSessionImport = new JMenuItem(new ImportSession(g, i18n.tr("Import Session Cache"),
				null, this));
		menuSession.add(miSessionImport);

		miSessionDebug = new JCheckBoxMenuItem(new AbstractGameAction(g, i18n.tr("Debug mode"), null, KeyEvent.VK_D) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				game.switchDebug();

			}
		});
		menuSession.add(miSessionDebug);


		/* === Language menu === */
		menuLanguage = new JMenu(i18n.tr("Language"));
		menuLanguage.setMnemonic(KeyEvent.VK_L);
		menuBar.add(menuLanguage);

		/* === Programming language changing === */
		menuLangHuman = new JMenu(i18n.tr("Human"));
		menuLanguage.add(menuLangHuman);

		ButtonGroup group = new ButtonGroup();

		for (String[] lang : Game.humanLangs) {
			JMenuItem item = new JRadioButtonMenuItem(new SetLanguage(g, lang[0], new Locale(lang[1])));
			if (lang[1].equals(FileUtils.getLocale().getLanguage())) 
				item.setSelected(true);
			group.add(item);
			menuLangHuman.add(item);		
		}

		
		menuLangProg = new ProgLangSubMenu(i18n.tr("Computer"));
		menuLanguage.add( menuLangProg );

		/* === Help menu === */
		menuHelp = new JMenu(i18n.tr("Help"));
		menuHelp.setMnemonic(KeyEvent.VK_H);
		menuBar.add(menuHelp);

		miHelpFeedback = new JMenuItem(new AbstractGameAction(g, i18n.tr("Provide feedback")) {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				FeedbackDialog.getInstance().setVisible(true);
			}			
		});
		miHelpFeedback.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, ActionEvent.CTRL_MASK));
		menuHelp.add(miHelpFeedback);
		
		miHelpLesson = new JMenuItem(new AbstractGameAction(g, i18n.tr("About this lesson")) {
			private static final long serialVersionUID = 1L;
			private AbstractAboutDialog dialog = null;

			public void actionPerformed(ActionEvent arg0) {
				if (this.dialog == null) 
					this.dialog = new AboutLessonDialog(MainFrame.getInstance());

				this.dialog.setVisible(true);
			}			
		});
		menuHelp.add(miHelpLesson);

		miHelpWorld = new JMenuItem(new AbstractGameAction(g, i18n.tr("About this world"), null) {
			private static final long serialVersionUID = 1L;

			private AbstractAboutDialog dialog = null;

			public void actionPerformed(ActionEvent arg0) {
				if (this.dialog == null) {
					this.dialog = new AboutWorldDialog(MainFrame.getInstance());
				}
				this.dialog.setVisible(true);
			}
		});
		miHelpWorld.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
		menuHelp.add(miHelpWorld);

		if (!System.getProperty("os.name").startsWith("Mac")) {
			miHelpAbout = new JMenuItem(new AbstractGameAction(g, i18n.tr("About JLM"), null) {
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e) {
					AboutJLMDialog.getInstance().setVisible(true);
				}
			});
			menuHelp.add(miHelpAbout);

		} else {
			try {
				OSXAdapter.setQuitHandler(this, getClass().getDeclaredMethod("quit", (Class[]) null));
				OSXAdapter.setAboutHandler(this, getClass().getDeclaredMethod("about", (Class[]) null));
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}

		setJMenuBar(menuBar);
	}
	
	public void appendToTitle(String addendum) {
		this.setTitle(MainFrame.frameTitle +"      "+ addendum);
	}

	private void initToolBar(Game g) {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(true);
		toolBar.setBorder(BorderFactory.createEtchedBorder());

		ImageIcon ii = ResourcesCache.getIcon("img/btn-start.png");
		startButton = new PropagatingButton(new StartExecution(g, i18n.tr("Run"), ii));
		//shortcut ctrl-r
		startButton.setMnemonic(KeyEvent.VK_R);

		debugButton = new PropagatingButton(new StepExecution(g, i18n.tr("Step"), 
				ResourcesCache.getIcon("img/btn-debug.png")));
		//shortcut ctrl-b
		debugButton.setMnemonic(KeyEvent.VK_B);

		stopButton = new PropagatingButton(new StopExecution(g, i18n.tr("Stop"), 
				ResourcesCache.getIcon("img/btn-stop.png")));
		//shortcut ctrl-s
		stopButton.setMnemonic(KeyEvent.VK_S);
		stopButton.setEnabled(false);

		resetButton = new PropagatingButton(new Reset(g, i18n.tr("Reset"), 
				ResourcesCache.getIcon("img/btn-reset.png")));
		//shortcut ctrl-z
		resetButton.setMnemonic(KeyEvent.VK_Z);
		resetButton.setEnabled(true);

		demoButton = new PropagatingButton(new PlayDemo(g, i18n.tr("Demo"), 
				ResourcesCache.getIcon("img/btn-demo.png")));
		//shortcut ctrl-d
		demoButton.setMnemonic(KeyEvent.VK_D);
		demoButton.setEnabled(true);

        helpMeButton = new PropagatingToggleButton(new HelpMe(g, i18n.tr("Call for Help"),
                ResourcesCache.getIcon("img/btn-alert-off.png")));

		toolBar.add(startButton);
		toolBar.add(debugButton);
		toolBar.add(stopButton);
		toolBar.add(resetButton);
		toolBar.add(demoButton);
        toolBar.add(helpMeButton);

        toolBar.addSeparator();
        
        exoChangeButton = new PropagatingButton(new SwitchExo(g, i18n.tr("Switch exercise"), ResourcesCache.getIcon("img/btn-switch-exo.png")));
        toolBar.add(exoChangeButton);
        
		getContentPane().add(toolBar, BorderLayout.NORTH);
	}

	private void initStatusBar(Game g) {
		StatusBar statusBar = new StatusBar(g);
		getContentPane().add(statusBar, BorderLayout.SOUTH);
	}

	@Override
	public void stateChanged(Game.GameState type) {
		switch (type) {
		case LOADING:
		case SAVING:
			startButton.setEnabled(false);
			debugButton.setEnabled(false);
			resetButton.setEnabled(false);
			demoButton.setEnabled(false);
			exerciseView.setEnabledControl(false);
			break;
		case COMPILATION_STARTED:
			if (!Game.getInstance().isDebugEnabled())
				outputArea.clear();
			startButton.setEnabled(false);
			debugButton.setEnabled(false);
			resetButton.setEnabled(false);
			demoButton.setEnabled(false);
			exerciseView.setEnabledControl(false);
			break;
		case LOADING_DONE:
		case SAVING_DONE:
			startButton.setEnabled(true);
			debugButton.setEnabled(true);
			resetButton.setEnabled(true);
			demoButton.setEnabled(true);
			exerciseView.setEnabledControl(true);
			break;
		case COMPILATION_ENDED:
			// startButton.setEnabled(true);
			// exerciseView.setEnabledControl(true);
			break;
		case EXECUTION_STARTED:
			exerciseView.selectWorldPane();
			if (Game.getInstance().stepModeEnabled()) {
				debugButton.setEnabled(true);
				startButton.setEnabled(true);
				debugButton.setText(i18n.tr("Next"));
				debugButton.setIcon(ResourcesCache.getIcon("img/btn-debug-step.png"));
			} else {
				startButton.setEnabled(false);
				debugButton.setEnabled(false);				
			}
			resetButton.setEnabled(false);
			demoButton.setEnabled(false);
			stopButton.setEnabled(true);
			exerciseView.setEnabledControl(false);
			break;
		case EXECUTION_ENDED:
			stopButton.setEnabled(false);
			startButton.setEnabled(true);
			debugButton.setEnabled(true);
			debugButton.setText(i18n.tr("Step"));
			debugButton.setIcon(ResourcesCache.getIcon("img/btn-debug.png"));
			resetButton.setEnabled(true);
			demoButton.setEnabled(true);
			exerciseView.setEnabledControl(true);
			break;
		case DEMO_STARTED:
			exerciseView.selectObjectivePane();
			startButton.setEnabled(false);
			debugButton.setEnabled(false);
			resetButton.setEnabled(false);
			demoButton.setEnabled(false);
			stopButton.setEnabled(true);
			// exerciseView.setEnabledControl(false);
			break;
		case DEMO_ENDED:
			stopButton.setEnabled(false);
			startButton.setEnabled(true);
			debugButton.setEnabled(true);
			resetButton.setEnabled(true);
			demoButton.setEnabled(true);
			exerciseView.setEnabledControl(true);
			break;
		default:
		}

	}

	public void hideWorldView() {
		mainPanel.getBottomComponent().setVisible(false);
		mainPanel.setDividerSize(0);
		validate();
	}
	public void showWorldView() {
		mainPanel.getBottomComponent().setVisible(true);
		mainPanel.setDividerSize(10);

		validate();
	}
	public void lessonChooser() {

	}


	public void quit() {
		MainFrame.getInstance().dispose();
		Game.getInstance().quit();
	}

	public void about() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						AboutJLMDialog.getInstance().setVisible(true);
					}
				});
			}
		});
	}

	@Override
	public void currentExerciseHasChanged(Lecture lecture) {
		Game g = Game.getInstance();
		if (lecture instanceof Exercise) {
			showWorldView();
			Exercise exo = (Exercise) lecture;
			for (ProgrammingLanguage l:exo.getProgLanguages()) {
				if (!g.isValidProgLanguage(l)) 
					System.err.println("Request to add the programming language '"+l+"' to exercise "+exo.getName()+" ignored. Fix your exercise or upgrade your JLM.");
			}
		} else {
			hideWorldView();
		}
	}

	@Override
	public void currentLessonHasChanged() { /* don't care */ }

	@Override
	public void selectedEntityHasChanged() { /* don't care */ }

	@Override
	public void selectedWorldHasChanged(World w) { /* don't care */ }

	@Override
	public void selectedWorldWasUpdated() { /* don't care */ }

	/** Simple JButton which pass the enabled signals to their action */
	class PropagatingButton extends JButton {
		private static final long serialVersionUID = 1L;
		public PropagatingButton(AbstractGameAction act) {
			super(act);
			setBorderPainted(false);
		}
		@Override
		public void setEnabled(boolean enabled) {
			getAction().setEnabled(enabled);
			super.setEnabled(enabled);
		}
	}

    /** Simple JToggleButton which pass the enabled signals to their action */
	class PropagatingToggleButton extends JToggleButton {
		private static final long serialVersionUID = 1L;
		public PropagatingToggleButton(AbstractGameAction act) {
			super(act);
			setBorderPainted(false);
		}
		@Override
		public void setEnabled(boolean enabled) {
			getAction().setEnabled(enabled);
			super.setEnabled(enabled);
		}
	}

	public void keyListeners(ExerciseView e){
		//CTLR PAGEUP
		this.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP,0 ), null );
		this.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN,0), null );
		this.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP,0), "action pageup" );
		this.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN,0 ), "action pagedown" );
		this.getRootPane().getActionMap().put("action pageup", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent ae) {
				int index=(met.getSelectedIndex()==0?met.getTabCount()-1:met.getSelectedIndex()-1);
				met.setSelectedIndex(index);
			}
		});
		this.getRootPane().getActionMap().put("action pagedown", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent ae) {
				int index=(met.getSelectedIndex()==met.getTabCount()-1?0:met.getSelectedIndex()+1);
				met.setSelectedIndex(index);
			}
		});
		this.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("left"), "action left" );
		
		//F1
		this.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("F1" ), "action F1" );
		this.getRootPane().getActionMap().put("action F1", new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent ae) {
				System.out.println("touche F1 press��e" );
			}
		}
				);
	}

	@Override
	public void currentHumanLanguageHasChanged(Locale newLang) {
		i18n.setLocale(newLang);
		//Buttons
		startButton.setText(i18n.tr("Run"));
		debugButton.setText(i18n.tr("Step"));
		stopButton.setText(i18n.tr("Stop"));
		resetButton.setText(i18n.tr("Reset"));
		demoButton.setText(i18n.tr("Demo"));
        helpMeButton.setText(i18n.tr("Call for Help"));
        exoChangeButton.setText(i18n.tr("Switch exercise"));

        // Menus
		menuFile.setText(i18n.tr("File"));
		miFileLoad.setText(i18n.tr("Load lesson"));
		miFileSwitch.setText(i18n.tr("Switch lesson"));
		miFileExercise.setText(i18n.tr("Switch exercise"));
		if (miFileConsole != null)
			miFileConsole.setText(i18n.tr("Teacher Console"));
		miFileCourse.setText(i18n.tr("Choose your course"));
        miFileQuit.setText(i18n.tr("Quit"));

		menuSession.setText(i18n.tr("Session"));
		menuSession.getAccessibleContext().setAccessibleDescription(i18n.tr("Lesson related functions"));
		miSessionRevert.setText(i18n.tr("Revert Exercise"));
		miSessionExport.setText(i18n.tr("Export Session Cache"));
		miSessionImport.setText(i18n.tr("Import Session Cache"));
		miSessionDebug.setText(i18n.tr("Debug mode"));

		
		menuLanguage.setText(i18n.tr("Language"));
		menuLangHuman.setText(i18n.tr("Human"));
		menuLangProg.setText(i18n.tr("Computer"));
		
		menuHelp.setText(i18n.tr("Help"));
		miHelpFeedback.setText(i18n.tr("Provide feedback"));
		miHelpLesson.setText(i18n.tr("About this lesson"));
		miHelpWorld.setText(i18n.tr("About this world"));
		if (miHelpAbout != null)
			miHelpAbout.setText(i18n.tr("About JLM"));



	}
}

class ProgLangSubMenu extends JMenu implements ProgLangChangesListener, GameListener {
	private static final long serialVersionUID = 1L;

	public ProgLangSubMenu(String name) {
		super(name);
		Game.getInstance().addGameListener(this);
		Game.getInstance().addProgLangListener(this);
		currentExerciseHasChanged(Game.getInstance().getCurrentLesson().getCurrentExercise());
	}

	@Override
	public void currentProgrammingLanguageHasChanged(ProgrammingLanguage newLang) {
		currentExerciseHasChanged(Game.getInstance().getCurrentLesson().getCurrentExercise());		
	}
	@Override
	public void currentExerciseHasChanged(Lecture lecture) {
		Game g = Game.getInstance();
		if (lecture instanceof Exercise) {
			setEnabled(true);
			Exercise exo = (Exercise) lecture;
			removeAll();
			for (ProgrammingLanguage pl : exo.getProgLanguages()) {
				ButtonGroup group = new ButtonGroup();
				JMenuItem item = new JRadioButtonMenuItem(new SetProgLanguage(g,pl));
				if (pl.equals(Game.getProgrammingLanguage()))
					item.setSelected(true);
				group.add(item);
				add(item);
			}
		} else {
			setEnabled(false);
		}
	}

	@Override
	public void currentLessonHasChanged() {   /* don't care */ }
	@Override
	public void selectedWorldHasChanged(World w) {   /* don't care */ }
	@Override
	public void selectedEntityHasChanged() {  /* don't care */ }
	@Override
	public void selectedWorldWasUpdated() {   /* don't care */ }

}
