package plm.core.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.CellRendererPane;
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
import plm.core.ui.action.AddUser;
import plm.core.ui.action.ExportSession;
import plm.core.ui.action.HelpMe;
import plm.core.ui.action.ImportSession;
import plm.core.ui.action.LinkUser;
import plm.core.ui.action.PlayDemo;
import plm.core.ui.action.QuitGame;
import plm.core.ui.action.RemoveUser;
import plm.core.ui.action.Reset;
import plm.core.ui.action.RevertExercise;
import plm.core.ui.action.SetLanguage;
import plm.core.ui.action.SetProgLanguage;
import plm.core.ui.action.StartExecution;
import plm.core.ui.action.StepExecution;
import plm.core.ui.action.StopExecution;
import plm.core.ui.action.SwitchExo;
import plm.core.ui.action.SwitchUser;
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
    private JMenuItem miFileSavePicture,miExoLoad,miExoSwitch,miExoExercise,miExoCourse,miFileQuit;
    private JMenu menuExercise;
    private JMenuItem miExoRevert, miExoDebug, miExoCreative;

    private JMenu menuSession;
    private JMenuItem miSessionExport, miSessionImport, miAddUser, miSwitchUser, miRemoveUser, miLinkIdentity;

    private JMenu menuLanguage, menuLangHuman, menuLangProg;
    private JMenu menuHelp;
    private JMenuItem miHelpFeedback, miHelpLesson,miHelpWorld,miHelpAbout;
        
	private LoggerPanel outputArea;
	private MissionEditorTabs met;
	
	StatusBar statusBar;
	
	public I18n i18n = I18nFactory.getI18n(getClass(),"org.plm.i18n.Messages",getLocale(), I18nFactory.FALLBACK);

	private JSplitPane mainPanel;
	
	private static final String frameTitle = "Programmer's Learning Machine";

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
	
	public static void doDispose() {
		if (MainFrame.instance == null)
			return;
		MainFrame.instance.dispose();
		MainFrame.instance = null;

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
		if (g.getCurrentLesson() != null)
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

		miFileSavePicture = new JMenuItem(new AbstractGameAction(g, i18n.tr("Save a picture"), null, KeyEvent.VK_S) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileNameExtensionFilter(i18n.tr("PNG Image Files"), "png"));
				fc.setDialogType(JFileChooser.OPEN_DIALOG);
				fc.showOpenDialog(MainFrame.getInstance());
				File selectedFile = fc.getSelectedFile();

				if (selectedFile != null) {
					if (selectedFile.exists()) {
						int dialogResult = JOptionPane.showConfirmDialog (null, 
								Game.i18n.tr("Do you want to overwrite {0}?",selectedFile.getName()),
								Game.i18n.tr("{0} exists",selectedFile.getName()),JOptionPane.YES_NO_OPTION);
						if (dialogResult != JOptionPane.YES_OPTION)
							return;
					}
					int iconSize = 500;

					WorldView wv = game.getSelectedWorld().getView();
					wv.setSize(new Dimension(iconSize, iconSize));
					wv.doLayout();
					wv.setVisible(true);

			        BufferedImage img = new BufferedImage(wv.getWidth(), wv.getHeight(), BufferedImage.TYPE_INT_RGB);

			        CellRendererPane crp = new CellRendererPane();
			        crp.add(wv);
			        crp.paintComponent(img.createGraphics(), wv, crp, wv.getBounds());    
			        
					try {
						ImageIO.write(img, "png", selectedFile);
						JOptionPane.showMessageDialog(MainFrame.this, Game.i18n.tr("Image saved into {0}.",selectedFile.getName()));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		menuFile.add(miFileSavePicture);       
        
        miFileQuit = new JMenuItem(new QuitGame(g, i18n.tr("Quit"), null,  KeyEvent.VK_Q));
        miFileQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));

		menuFile.add(miFileQuit);

		menuBar.add(menuFile);

		menuExercise = new JMenu(i18n.tr("Exercise"));
		menuExercise.setMnemonic(KeyEvent.VK_E);
		menuBar.add(menuExercise);
		menuExercise.setEnabled(true);
		miExoLoad = new JMenuItem(new AbstractGameAction(g, i18n.tr("Load lesson"), null, KeyEvent.VK_L) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileNameExtensionFilter(i18n.tr("PLM lesson files"), "plm"));
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
		menuExercise.add(miExoLoad);
		
		
		miExoSwitch = new JMenuItem(new AbstractGameAction(g, i18n.tr("Switch lesson"), null, KeyEvent.VK_L) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				new ChooseLessonDialog();
				MainFrame.getInstance().setVisible(false);		
			}
		});
		miExoSwitch.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		menuExercise.add(miExoSwitch);
		
		miExoExercise = new JMenuItem(new AbstractGameAction(g, i18n.tr("Switch exercise"), null, KeyEvent.VK_E) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				new ChooseLectureDialog();
			}
		});
		miExoExercise.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		menuExercise.add(miExoExercise);
		        
        // Menu item to change the current Course
        miExoCourse = new JMenuItem(new AbstractGameAction(g, i18n.tr("Choose your course")) {

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

        miExoCourse.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
        menuExercise.add(miExoCourse);

		miExoRevert = new JMenuItem(new RevertExercise(g, i18n.tr("Revert Exercise"), null));
		menuExercise.add(miExoRevert);
		menuExercise.addSeparator();
		
		miExoDebug = new JCheckBoxMenuItem(new AbstractGameAction(g, i18n.tr("Debug mode"), null, KeyEvent.VK_D) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				game.switchDebug();

			}
		});
		menuExercise.add(miExoDebug);

		miExoCreative = new JCheckBoxMenuItem(new AbstractGameAction(g, i18n.tr("Creative mode"), null, KeyEvent.VK_C) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				game.switchCreative();

			}
		});
		menuExercise.add(miExoCreative);


		
		/* === Session menu === */
		menuSession = new JMenu(i18n.tr("Session"));
		menuSession.setMnemonic(KeyEvent.VK_S);
		menuBar.add(menuSession);
		menuSession.setEnabled(true);
		

		miSessionExport = new JMenuItem(new ExportSession(g, i18n.tr("Export Session Cache"),	null, this));
		menuSession.add(miSessionExport);

		miSessionImport = new JMenuItem(new ImportSession(g, i18n.tr("Import Session Cache"),
				null, this));
		menuSession.add(miSessionImport);

		miAddUser = new JMenuItem(new AddUser(g, i18n.tr("Add user"), null, this));
		menuSession.add(miAddUser);
		
		miSwitchUser = new JMenuItem(new SwitchUser(g, i18n.tr("Switch user"), null, this));
		menuSession.add(miSwitchUser);

		miRemoveUser = new JMenuItem(new RemoveUser(g, i18n.tr("Remove user"), null, this));
		menuSession.add(miRemoveUser);
		
		miLinkIdentity = new JMenuItem(new LinkUser(g, i18n.tr("Link identity"), null, this));
		menuSession.add(miLinkIdentity);

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
			miHelpAbout = new JMenuItem(new AbstractGameAction(g, i18n.tr("About PLM"), null) {
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e) {
					AboutPLMDialog.getInstance().setVisible(true);
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
        if (Game.getProperty(Game.PROP_PROGRESS_APPENGINE,"false",true).equals("false")) // Turned off
        	helpMeButton.setEnabled(false);

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
		statusBar = new StatusBar(g);
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
						AboutPLMDialog.getInstance().setVisible(true);
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
					System.err.println("Request to add the programming language '"+l+"' to exercise "+exo.getName()+" ignored. Fix your exercise or upgrade your PLM.");
			}
		} else {
			hideWorldView();
		}
	}

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
				System.out.println("Key F1 pressed" );
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
		miFileSavePicture.setText(i18n.tr("Save a picture"));
        miFileQuit.setText(i18n.tr("Quit"));

		menuExercise.setText(i18n.tr("Exercise"));
        miExoLoad.setText(i18n.tr("Load lesson"));
        miExoSwitch.setText(i18n.tr("Switch lesson"));
        miExoExercise.setText(i18n.tr("Switch exercise"));
        miExoCourse.setText(i18n.tr("Choose your course"));
        miExoRevert.setText(i18n.tr("Revert Exercise"));
        miExoDebug.setText(i18n.tr("Debug mode"));
        miExoCreative.setText(i18n.tr("Creative mode"));
        
		menuSession.setText(i18n.tr("Session"));
		menuSession.getAccessibleContext().setAccessibleDescription(i18n.tr("Lesson related functions"));
		miSessionExport.setText(i18n.tr("Export Session Cache"));
		miSessionImport.setText(i18n.tr("Import Session Cache"));

		
		miAddUser.setText(i18n.tr("Add user"));
		miSwitchUser.setText(i18n.tr("Switch user"));
		miRemoveUser.setText(i18n.tr("Remove user"));

		
		menuLanguage.setText(i18n.tr("Language"));
		menuLangHuman.setText(i18n.tr("Human"));
		menuLangProg.setText(i18n.tr("Computer"));
		
		menuHelp.setText(i18n.tr("Help"));
		miHelpFeedback.setText(i18n.tr("Provide feedback"));
		miHelpLesson.setText(i18n.tr("About this lesson"));
		miHelpWorld.setText(i18n.tr("About this world"));
		if (miHelpAbout != null)
			miHelpAbout.setText(i18n.tr("About PLM"));



	}
}

class ProgLangSubMenu extends JMenu implements ProgLangChangesListener, GameListener {
	private static final long serialVersionUID = 1L;

	public ProgLangSubMenu(String name) {
		super(name);
		Game.getInstance().addGameListener(this);
		Game.getInstance().addProgLangListener(this);
		if (Game.getInstance().getCurrentLesson() != null)
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
	public void selectedWorldHasChanged(World w) {   /* don't care */ }
	@Override
	public void selectedEntityHasChanged() {  /* don't care */ }
	@Override
	public void selectedWorldWasUpdated() {   /* don't care */ }

}
