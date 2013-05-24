package jlm.core.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

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

import jlm.core.GameListener;
import jlm.core.GameStateListener;
import jlm.core.HumanLangChangesListener;
import jlm.core.ProgLangChangesListener;
import jlm.core.model.FileUtils;
import jlm.core.model.Game;
import jlm.core.model.LessonLoadingException;
import jlm.core.model.ProgrammingLanguage;
import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.Lecture;
import jlm.core.ui.action.AbstractGameAction;
import jlm.core.ui.action.ExportSession;
import jlm.core.ui.action.HelpMe;
import jlm.core.ui.action.ImportSession;
import jlm.core.ui.action.PlayDemo;
import jlm.core.ui.action.QuitGame;
import jlm.core.ui.action.Reset;
import jlm.core.ui.action.RevertExercise;
import jlm.core.ui.action.SetLanguage;
import jlm.core.ui.action.SetProgLanguage;
import jlm.core.ui.action.StartExecution;
import jlm.core.ui.action.StepExecution;
import jlm.core.ui.action.StopExecution;
import jlm.core.ui.action.SwitchExo;
import jlm.universe.World;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

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
    
	List<MenuContainer> menuContainers = new Vector<MenuContainer>();

    
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
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
		    		if (JOptionPane.YES_OPTION == 
		    				JOptionPane.showConfirmDialog(null, 
		    						i18n.tr("Do you really want to quit ?"), 
		    						i18n.tr("Exit the JLM"), JOptionPane.YES_NO_OPTION)) {
		    			g.quit();
		    		}
				}
			});

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
	}

	private void initMenuBar(Game g) {
		JMenuBar menuBar = new JMenuBar();

		JMenu menu;
		JMenuItem menuItem;
		
		MenuContainer menuContainer;
		MenuItemContainer menuItemContainer;

		/* === FILE menu === */
		// for now: leave the calls to i18n.tr: that way one is sure to get all the localized strings...
		menu = new JMenu(i18n.tr("File"));
		menu.setMnemonic(KeyEvent.VK_F);
		menu.getAccessibleContext().setAccessibleDescription(i18n.tr("File related functions"));

		menuContainer = new MenuContainer(menu, "File", "File related functions");

		
		menuItem = new JMenuItem(new AbstractGameAction(g, i18n.tr("Load lesson"), null, KeyEvent.VK_L) {
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
		menu.add(menuItem);
		menuItemContainer = new MenuItemContainer(menuItem, "Load lesson");
		menuContainer.add(menuItemContainer);
		
		
		menuItem = new JMenuItem(new AbstractGameAction(g, i18n.tr("Switch lesson"), null, KeyEvent.VK_L) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				game.switchLesson("lessons.chooser");
			}
		});
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		menu.add(menuItem);
		menuItemContainer = new MenuItemContainer(menuItem, "Switch lesson");
		menuContainer.add(menuItemContainer);
		
		// Teacher console menu item (shown only if defined in the JLM properties)
        if(Game.getProperty("jlm.configuration.teacher").equals("true")) {
            menuItem = new JMenuItem(new AbstractGameAction(g,i18n.tr("Teacher Console")) {

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
            menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
            menu.add(menuItem);
            menuItemContainer = new MenuItemContainer(menuItem, "Teacher Console");
            menuContainer.add(menuItemContainer);
        }
        
        // Menu item to change the current Course
        menuItem = new JMenuItem(new AbstractGameAction(g, i18n.tr("Choose your course")) {

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

        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
        menu.add(menuItem);
        menuItemContainer = new MenuItemContainer(menuItem, "Choose your course");
        menuContainer.add(menuItemContainer);
       
        
		menuItem = new JMenuItem(new QuitGame(g, i18n.tr("Quit"), null,  KeyEvent.VK_Q));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));

		menu.add(menuItem);
        menuItemContainer = new MenuItemContainer(menuItem, "Quit");
        menuContainer.add(menuItemContainer);

		menuBar.add(menu);
		menuContainers.add(menuContainer);

		/* === Edit menu === */
		menu = new JMenu(i18n.tr("Session"));
		menu.setMnemonic(KeyEvent.VK_S);
		menu.getAccessibleContext().setAccessibleDescription(i18n.tr("Lesson related functions"));
		menuBar.add(menu);
		menu.setEnabled(true);
		
		menuContainer = new MenuContainer(menu, "Session", "Lesson related functions");

		JMenuItem revertExerciseCodeSource = new JMenuItem(new RevertExercise(g, i18n.tr("Revert Exercise"),
				null));
		menu.add(revertExerciseCodeSource);
        menuItemContainer = new MenuItemContainer(revertExerciseCodeSource, "Revert Exercise");
        menuContainer.add(menuItemContainer);

		JMenuItem exportSessionMenuItem = new JMenuItem(new ExportSession(g, i18n.tr("Export Session Cache"),
				null, this));
		menu.add(exportSessionMenuItem);
        menuItemContainer = new MenuItemContainer(exportSessionMenuItem, "Export Session Cache");
        menuContainer.add(menuItemContainer);

		JMenuItem importSessionMenuItem = new JMenuItem(new ImportSession(g, i18n.tr("Import Session Cache"),
				null, this));
		menu.add(importSessionMenuItem);
        menuItemContainer = new MenuItemContainer(importSessionMenuItem, "Import Session Cache");
        menuContainer.add(menuItemContainer);

		JMenuItem switchDebug = new JCheckBoxMenuItem(new AbstractGameAction(g, i18n.tr("Debug mode"), null, KeyEvent.VK_D) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				game.switchDebug();

			}
		});
		menu.add(switchDebug);
	    menuItemContainer = new MenuItemContainer(switchDebug, "Debug mode");
	    menuContainer.add(menuItemContainer);
		menuContainers.add(menuContainer);


		/* === Language menu === */
		menu = new JMenu(i18n.tr("Language"));
		menu.setMnemonic(KeyEvent.VK_L);
		menuBar.add(menu);
		menuContainer = new MenuContainer(menu, "Language");
		menuContainers.add(menuContainer);

		/* === Programming language changing === */
		JMenu textLangSubMenu = new JMenu(i18n.tr("Human"));
		menu.add(textLangSubMenu);
	    menuItemContainer = new MenuItemContainer(textLangSubMenu, "Human");
	    menuContainer.add(menuItemContainer);

		ButtonGroup group = new ButtonGroup();

		for (String[] lang : new String[][] { {"Francais","fr"}, {"English","en"}}) {
			JMenuItem item = new JRadioButtonMenuItem(new SetLanguage(g, lang[0], new Locale(lang[1])));
			if (lang[1].equals(FileUtils.getLocale().getLanguage())) 
				item.setSelected(true);
			group.add(item);
			textLangSubMenu.add(item);		
		}

		
		JMenu progLangSubMenu = new ProgLangSubMenu(i18n.tr("Computer"));
		
		menu.add(progLangSubMenu);
	    menuItemContainer = new MenuItemContainer(progLangSubMenu, "Computer");
	    menuContainer.add(menuItemContainer);

		/* === Help menu === */
		menu = new JMenu(i18n.tr("Help"));
		menu.setMnemonic(KeyEvent.VK_H);
		menuBar.add(menu);
		menuContainer = new MenuContainer(menu, "Help");
		menuContainers.add(menuContainer);

/*	
 TODO: forum to be fixed	
		menu.add(new JMenuItem(new AbstractGameAction(g, "Join the forum", null) {
			private static final long serialVersionUID = 1L;

			private JDialog dialog = null;

			public void actionPerformed(ActionEvent arg0) {
				if (this.dialog == null) {
					//this.dialog = new JLMForumDialog();
					this.dialog = new XMPPDialog();
				}
				this.dialog.setVisible(true);
			}
		}));
*/
		menuItem = new JMenuItem(new AbstractGameAction(g, i18n.tr("About this lesson")) {
			private static final long serialVersionUID = 1L;
			private AbstractAboutDialog dialog = null;

			public void actionPerformed(ActionEvent arg0) {
				if (this.dialog == null) 
					this.dialog = new AboutLessonDialog(MainFrame.getInstance());

				this.dialog.setVisible(true);
			}			
		});
		menu.add(menuItem);
	    menuItemContainer = new MenuItemContainer(menuItem, "About this lesson");
	    menuContainer.add(menuItemContainer);

		menuItem = new JMenuItem(new AbstractGameAction(g, i18n.tr("About this world"), null) {
			private static final long serialVersionUID = 1L;

			private AbstractAboutDialog dialog = null;

			public void actionPerformed(ActionEvent arg0) {
				if (this.dialog == null) {
					this.dialog = new AboutWorldDialog(MainFrame.getInstance());
				}
				this.dialog.setVisible(true);
			}
		});
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
		menu.add(menuItem);
	    menuItemContainer = new MenuItemContainer(menuItem, "About this world");
	    menuContainer.add(menuItemContainer);

		if (!System.getProperty("os.name").startsWith("Mac")) {
			menu.add(new JMenuItem(new AbstractGameAction(g, i18n.tr("About JLM"), null) {
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e) {
					AboutJLMDialog.getInstance().setVisible(true);
				}
			}));

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

		ImageIcon ii = ResourcesCache.getIcon("resources/start.png");
		startButton = new PropagatingButton(new StartExecution(g, "RunButton", ii));
		//shortcut ctrl-r
		startButton.setMnemonic(KeyEvent.VK_R);

		debugButton = new PropagatingButton(new StepExecution(g, "StepButton", 
				ResourcesCache.getIcon("resources/debug.png")));
		//shortcut ctrl-b
		debugButton.setMnemonic(KeyEvent.VK_B);

		stopButton = new PropagatingButton(new StopExecution(g, "StopButton", 
				ResourcesCache.getIcon("resources/stop.png")));
		//shortcut ctrl-s
		stopButton.setMnemonic(KeyEvent.VK_S);
		stopButton.setEnabled(false);

		resetButton = new PropagatingButton(new Reset(g, "ResetButton", 
				ResourcesCache.getIcon("resources/reset.png")));
		//shortcut ctrl-z
		resetButton.setMnemonic(KeyEvent.VK_Z);
		resetButton.setEnabled(true);

		demoButton = new PropagatingButton(new PlayDemo(g, "DemoButton", 
				ResourcesCache.getIcon("resources/btn-demo.png")));
		//shortcut ctrl-d
		demoButton.setMnemonic(KeyEvent.VK_D);
		demoButton.setEnabled(true);

        helpMeButton = new PropagatingToggleButton(new HelpMe(g, "HelpButton",
                ResourcesCache.getIcon("resources/btn-alert-off.png")));

		toolBar.add(startButton);
		toolBar.add(debugButton);
		toolBar.add(stopButton);
		toolBar.add(resetButton);
		toolBar.add(demoButton);
        toolBar.add(helpMeButton);

        toolBar.addSeparator();
        
        exoChangeButton = new PropagatingButton(new SwitchExo(g, "ExerciseButton", ResourcesCache.getIcon("resources/btn-switch-exo.png")));
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
		// TODO Look at creating a sries of helper classes modelled on MenuContainer et al.
		
		//System.out.println("MainFrame : human languange changed to: "+newLang);

		i18n.setLocale(newLang);
		//Buttons
		startButton.setText(i18n.tr("Run"));
		debugButton.setText(i18n.tr("Step"));
		stopButton.setText(i18n.tr("Stop"));
		resetButton.setText(i18n.tr("Reset"));
		demoButton.setText(i18n.tr("Demo"));
        helpMeButton.setText(i18n.tr("Call for Help"));
        exoChangeButton.setText(i18n.tr("Switch exercise"));

        //Menus
        JMenu menu;
        JMenuItem menuItem;
        for (MenuContainer mC : menuContainers){
        	menu = mC.menu;
        	menu.setText(i18n.tr(mC.descriptor));
         	if (mC.toolDescriptor!="") {
        		menu.getAccessibleContext().setAccessibleDescription(i18n.tr(mC.toolDescriptor));
        	}
        	for (MenuItemContainer mI : mC.menuItemContainers) {
        		menuItem = mI.menuItem;
         		menuItem.setText(i18n.tr(mI.descriptor));
        	}
        }
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

// Helper classes for language switching inside menus
// Might be folded into one with subclasses to handle every visible item...
// TODO Look at that refactoring.
// TODO work on a better encapsulation.
class MenuContainer {
	JMenu menu;
	String descriptor;
	String toolDescriptor;
	List<MenuItemContainer> menuItemContainers;
	
	public MenuContainer(JMenu m, String d) {
		this(m, d, "");
	}

	public MenuContainer(JMenu m, String d, String tD) {
		this.menu = m;
		this.descriptor = d;
		this.toolDescriptor = tD;
		this.menuItemContainers =  new Vector<MenuItemContainer>();
		this.menuItemContainers.clear();
		//System.out.println("Menu "+d+" ("+tD+")");
	}

	public void add(MenuItemContainer mc) {
		menuItemContainers.add(mc);
	}
}

class MenuItemContainer {
	JMenuItem menuItem;
	String descriptor;
	
	public MenuItemContainer(JMenuItem mi, String d) {
		this.menuItem = mi;
		this.descriptor = d;
		//System.out.println(mi.getText()+" : "+d);
	}
}
