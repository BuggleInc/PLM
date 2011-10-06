package jlm.core.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import jlm.core.GameListener;
import jlm.core.GameStateListener;
import jlm.core.ProgLangChangesListener;
import jlm.core.model.Game;
import jlm.core.model.GameState;
import jlm.core.model.ProgrammingLanguage;
import jlm.core.model.Reader;
import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.Lecture;
import jlm.core.ui.action.AbstractGameAction;
import jlm.core.ui.action.CleanUpSession;
import jlm.core.ui.action.ExportSession;
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
import jlm.universe.World;

public class MainFrame extends JFrame implements GameStateListener, GameListener {

	private static final long serialVersionUID = -5022279647890315264L;

	private static MainFrame instance = null;

	private ExerciseView exerciseView;
	private JButton startButton;
	private JButton debugButton;
	private JButton stopButton;
	private JButton resetButton;
	private JButton demoButton;
	private LoggerPanel outputArea;
	
	private JSplitPane mainPanel;
	
	private MainFrame() {
		super("Java Learning Machine");
		Reader.setLocale(this.getLocale().getLanguage());
		initComponents(Game.getInstance());
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
				g.quit();
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

		mainPanel.setLeftComponent(new MissionEditorTabs());

		exerciseView = new ExerciseView(g);
		mainPanel.setRightComponent(exerciseView);

		logPane.setTopComponent(mainPanel);
		outputArea = new LoggerPanel(g);
		JScrollPane outputScrollPane = new JScrollPane(outputArea);
		logPane.setBottomComponent(outputScrollPane);
		getContentPane().add(logPane, BorderLayout.CENTER);
		// g.setOutputWriter(outputArea);

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

		/* === FILE menu === */
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menu.getAccessibleContext().setAccessibleDescription("File related functions");

		menuItem = new JMenuItem(new AbstractGameAction(g, "Switch exercise") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new LessonNavigatorDialog(MainFrame.getInstance()).setVisible(true);
			}			
		});
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		menu.add(menuItem);
		
		menuItem = new JMenuItem(new AbstractGameAction(g, "Switch lesson", null, "Go to another lesson",  "Cannot switch lesson now", KeyEvent.VK_L) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				Game.getInstance().changeLesson("lessons.chooser");
			}
		});
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		menu.add(menuItem);
		
		menuItem = new JMenuItem(new QuitGame(g, "Quit", null,  KeyEvent.VK_Q));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));

		menu.add(menuItem);

		menuBar.add(menu);

		/* === Edit menu === */
		menu = new JMenu("Session");
		menu.setMnemonic(KeyEvent.VK_S);
		menu.getAccessibleContext().setAccessibleDescription("Lesson related functions");
		menuBar.add(menu);
		menu.setEnabled(true);

		JMenuItem revertExerciseCodeSource = new JMenuItem(new RevertExercise(g, "Revert Exercise",
				null));
		menu.add(revertExerciseCodeSource);

		JMenuItem exportSessionMenuItem = new JMenuItem(new ExportSession(g, "Export Session Cache",
				null, this));
		menu.add(exportSessionMenuItem);

		JMenuItem importSessionMenuItem = new JMenuItem(new ImportSession(g, "Import Session Cache",
				null, this));
		menu.add(importSessionMenuItem);


		/* === Language menu === */
		menu = new JMenu("Language");
		menu.setMnemonic(KeyEvent.VK_L);
		menuBar.add(menu);
		
		/* === Programming language changing === */
		JMenu textLangSubMenu = new JMenu("Human");
		menu.add(textLangSubMenu);
		ButtonGroup group = new ButtonGroup();
		
		for (String[] lang : new String[][] { {"Francais","fr"}, {"English","en"}}) {
			JMenuItem item = new JRadioButtonMenuItem(new SetLanguage(g, lang[0], lang[1]));
			if (lang[1].equals(Reader.getLocale())) 
				item.setSelected(true);
			group.add(item);
			textLangSubMenu.add(item);		
		}
		
		menu.add(new ProgLangSubMenu());

		/* === Help menu === */
		menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_H);
		menuBar.add(menu);

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
		menuItem = new JMenuItem(new AbstractGameAction(g, "About this lesson") {
			private static final long serialVersionUID = 1L;
			private AbstractAboutDialog dialog = null;

			public void actionPerformed(ActionEvent arg0) {
				if (this.dialog == null) 
					this.dialog = new AboutLessonDialog(MainFrame.getInstance());
				
				this.dialog.setVisible(true);
			}			
		});
		menu.add(menuItem);
		
		menuItem = new JMenuItem(new AbstractGameAction(g, "About this world", null) {
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
		
		if (!System.getProperty("os.name").startsWith("Mac")) {
			menu.add(new JMenuItem(new AbstractGameAction(g, "About JLM", null) {
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

	private void initToolBar(Game g) {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(true);
		toolBar.setBorder(BorderFactory.createEtchedBorder());

		ImageIcon ii = ResourcesCache.getIcon("resources/start.png");
		startButton = new PropagatingButton(new StartExecution(g, "Run", ii));

		debugButton = new PropagatingButton(new StepExecution(g, "Step", 
				ResourcesCache.getIcon("resources/debug.png")));
		
		stopButton = new PropagatingButton(new StopExecution(g, "Stop", 
				ResourcesCache.getIcon("resources/stop.png")));
		stopButton.setEnabled(false);

		resetButton = new PropagatingButton(new Reset(g, "Reset", 
				ResourcesCache.getIcon("resources/reset.png")));
		resetButton.setEnabled(true);

		demoButton = new PropagatingButton(new PlayDemo(g, "Demo", 
				ResourcesCache.getIcon("resources/demo.png")));
		demoButton.setEnabled(true);
		

		toolBar.add(startButton);
		toolBar.add(debugButton);
		toolBar.add(stopButton);
		toolBar.add(resetButton);
		toolBar.add(demoButton);

		getContentPane().add(toolBar, BorderLayout.NORTH);
	}

	private void initStatusBar(Game g) {
		StatusBar statusBar = new StatusBar(g);
		getContentPane().add(statusBar, BorderLayout.SOUTH);
	}

	@Override
	public void stateChanged(GameState type) {
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
				debugButton.setText("Next");
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
			debugButton.setText("Step");
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
		// event.setHandled(true);
	}

	public void about() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						AboutJLMDialog.getInstance().setVisible(true);
					}
				});
				// event.setHandled(true);
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
}

class ProgLangSubMenu extends JMenu implements ProgLangChangesListener, GameListener {
	private static final long serialVersionUID = 1L;
	
	public ProgLangSubMenu() {
		super("Computer");
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