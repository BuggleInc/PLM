package jlm.core.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import jlm.core.GameListener;
import jlm.core.GameStateListener;
import jlm.core.model.Game;
import jlm.core.model.GameState;
import jlm.core.model.Reader;
import jlm.core.ui.action.AbstractGameAction;
import jlm.core.ui.action.CleanUpSession;
import jlm.core.ui.action.ExportSession;
import jlm.core.ui.action.ImportSession;
import jlm.core.ui.action.PlayDemo;
import jlm.core.ui.action.QuitGame;
import jlm.core.ui.action.Reset;
import jlm.core.ui.action.RevertExercise;
import jlm.core.ui.action.SetLanguage;
import jlm.core.ui.action.ShowHint;
import jlm.core.ui.action.StartExecution;
import jlm.core.ui.action.StepExecution;
import jlm.core.ui.action.StopExecution;

public class MainFrame extends JFrame implements GameStateListener, GameListener {

	private static final long serialVersionUID = -5022279647890315264L;

	private static JFrame instance = null;

	private ExerciseView exerciseView;
	private JButton startButton;
	private JButton debugButton;
	private JButton stopButton;
	private JButton resetButton;
	private JButton hintButton;
	private JButton demoButton;
	private LoggerPanel outputArea;

	private JComboBox lessonComboBox;
	private JComboBox exerciseComboBox;
	
	private MainFrame() {
		super("Java Learning Machine");
		initComponents();
	}

	public static JFrame getInstance() {
		if (MainFrame.instance == null)
			MainFrame.instance = new MainFrame();
		return MainFrame.instance;
	}

	private void initComponents() {
		Reader.setLocale(this.getLocale().getLanguage());
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Game.getInstance().quit();
			}
		});

		getContentPane().setLayout(new BorderLayout());

		JSplitPane logPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
		logPane.setOneTouchExpandable(true);
		double ratio = 0.7;
		logPane.setResizeWeight(ratio);
		logPane.setDividerLocation((int) (768 * ratio));

		JSplitPane mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);

		mainPanel.setOneTouchExpandable(true);
		double weight = 0.6;
		mainPanel.setResizeWeight(weight);
		mainPanel.setDividerLocation((int) (1024 * weight));

		mainPanel.setLeftComponent(new MissionEditorTabs());

		exerciseView = new ExerciseView(Game.getInstance());
		mainPanel.setRightComponent(exerciseView);

		logPane.setTopComponent(mainPanel);
		outputArea = new LoggerPanel(Game.getInstance());
		JScrollPane outputScrollPane = new JScrollPane(outputArea);
		logPane.setBottomComponent(outputScrollPane);
		getContentPane().add(logPane, BorderLayout.CENTER);
		// Game.getInstance().setOutputWriter(outputArea);

		initMenuBar();
		initToolBar();
		initStatusBar();

		Game.getInstance().addGameStateListener(this);
		Game.getInstance().addGameListener(this);

		pack();
		setSize(1024, 768);
	}

	private void initMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu menu;
		JMenuItem menuItem;

		/* === FILE menu === */
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menu.getAccessibleContext().setAccessibleDescription("File related functions");

		menuItem = new JMenuItem(new QuitGame(Game.getInstance(), "Quit", null,  KeyEvent.VK_Q));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));

		menu.add(menuItem);

		menuBar.add(menu);

		/* === Edit menu === */
		menu = new JMenu("Session");
		menu.setMnemonic(KeyEvent.VK_S);
		menu.getAccessibleContext().setAccessibleDescription("Lesson related functions");
		menuBar.add(menu);
		menu.setEnabled(true);

		// JMenu lessonSubmenu = new JMenu("Change lesson...");
		// JMenu exerciceSubmenu = new JMenu("Change exercise...");

		JMenuItem revertExerciseCodeSource = new JMenuItem(new RevertExercise(Game.getInstance(), "Revert Exercise",
				null));
		menu.add(revertExerciseCodeSource);

		JMenuItem cleanUpSessionMenuItem = new JMenuItem(new CleanUpSession(Game.getInstance(), "Clear Session Cache",
				null));
		menu.add(cleanUpSessionMenuItem);

		JMenuItem exportSessionMenuItem = new JMenuItem(new ExportSession(Game.getInstance(), "Export Session Cache",
				null, this));
		menu.add(exportSessionMenuItem);

		JMenuItem importSessionMenuItem = new JMenuItem(new ImportSession(Game.getInstance(), "Import Session Cache",
				null, this));
		menu.add(importSessionMenuItem);


		/* === Language menu === */
		menu = new JMenu("Language");
		menu.setMnemonic(KeyEvent.VK_L);
		menuBar.add(menu);
		JMenuItem setLanguageToFrench = new JMenuItem(new SetLanguage(Game.getInstance(), "Francais", null, this,"fr"));
		menu.add(setLanguageToFrench);
		JMenuItem setLanguageToEnglish = new JMenuItem(new SetLanguage(Game.getInstance(), "English", null, this,"en"));
		menu.add(setLanguageToEnglish);
		
		/* === Help menu === */
		menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_H);
		menuBar.add(menu);

		menu.add(new JMenuItem(new AbstractGameAction(Game.getInstance(), "Join the forum", null) {
			private static final long serialVersionUID = 1L;

			private JDialog dialog = null;

			public void actionPerformed(ActionEvent arg0) {
				if (this.dialog == null) {
					this.dialog = new JLMForumDialog();
				}
				this.dialog.setVisible(true);
			}
		}));
		menuItem = new JMenuItem(new AbstractGameAction(Game.getInstance(), "About this lesson") {
			private static final long serialVersionUID = 1L;
			private AbstractAboutDialog dialog = null;

			public void actionPerformed(ActionEvent arg0) {
				if (this.dialog == null) 
					this.dialog = new AboutLessonDialog(MainFrame.getInstance());
				
				this.dialog.setVisible(true);
			}			
		});
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		menu.add(menuItem);
		menuItem = new JMenuItem(new AbstractGameAction(Game.getInstance(), "Navigate this lesson") {
			private static final long serialVersionUID = 1L;
			private LessonNavigatorDialog dialog = null;

			public void actionPerformed(ActionEvent arg0) {
				if (this.dialog == null) {
					this.dialog = new LessonNavigatorDialog(MainFrame.getInstance());
				}
				this.dialog.setVisible(true);
			}			
		});
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		menu.add(menuItem);
		
		menu.add(new JMenuItem(new AbstractGameAction(Game.getInstance(), "About this world", null) {
			private static final long serialVersionUID = 1L;

			private AbstractAboutDialog dialog = null;

			public void actionPerformed(ActionEvent arg0) {
				if (this.dialog == null) {
					this.dialog = new AboutWorldDialog(MainFrame.getInstance());
				}
				this.dialog.setVisible(true);
			}
		}));

		if (!System.getProperty("os.name").startsWith("Mac")) {
			menu.add(new JMenuItem(new AbstractGameAction(Game.getInstance(), "About JLM", null) {
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

	private void initToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(true);
		toolBar.setBorder(BorderFactory.createEtchedBorder());

		ImageIcon ii = ResourcesCache.getIcon("resources/start.png");
		startButton = new PropagatingButton(new StartExecution(Game.getInstance(), "Run", ii));

		debugButton = new PropagatingButton(new StepExecution(Game.getInstance(), "Step", 
				ResourcesCache.getIcon("resources/debug.png")));
		
		stopButton = new PropagatingButton(new StopExecution(Game.getInstance(), "Stop", 
				ResourcesCache.getIcon("resources/stop.png")));
		stopButton.setEnabled(false);

		resetButton = new PropagatingButton(new Reset(Game.getInstance(), "Reset", 
				ResourcesCache.getIcon("resources/reset.png")));
		resetButton.setEnabled(true);

		hintButton = new PropagatingButton(new ShowHint(Game.getInstance(), "Hint", 
				ResourcesCache.getIcon("resources/step.png")));
		hintButton.setEnabled(Game.getInstance().getCurrentLesson().getCurrentExercise().hint != null);
		
		demoButton = new PropagatingButton(new PlayDemo(Game.getInstance(), "Demo", 
				ResourcesCache.getIcon("resources/demo.png")));
		demoButton.setEnabled(true);
		
		LessonComboListAdapter lessonAdapter = new LessonComboListAdapter(Game.getInstance());
		lessonComboBox = new JComboBox(lessonAdapter);
		lessonComboBox.setRenderer(new LessonCellRenderer());
		lessonComboBox.setToolTipText("Switch the lesson");

		ExerciseComboListAdapter exerciseAdapter = new ExerciseComboListAdapter(Game.getInstance());
		exerciseComboBox = new JComboBox(exerciseAdapter);
		exerciseComboBox.setRenderer(new ExerciseCellRenderer());
		exerciseComboBox.setToolTipText("Switch the exercise");

		toolBar.add(startButton);
		toolBar.add(debugButton);
		toolBar.add(stopButton);
		toolBar.add(resetButton);
		toolBar.add(hintButton);
		toolBar.add(demoButton);
		toolBar.add(new JSeparator(SwingConstants.VERTICAL));
		toolBar.add(Box.createHorizontalGlue());
		toolBar.add(new JLabel("Lesson:"));
		toolBar.add(lessonComboBox);
		toolBar.add(Box.createHorizontalStrut(10));
		toolBar.add(new JLabel("Exercise:"));
		toolBar.add(exerciseComboBox);
		// toolBar.add(new JSeparator(SwingConstants.VERTICAL));
		toolBar.add(Box.createHorizontalGlue());

		getContentPane().add(toolBar, BorderLayout.NORTH);
	}

	private void initStatusBar() {
		StatusBar statusBar = new StatusBar(Game.getInstance());
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
			hintButton.setEnabled(false);
			exerciseView.setEnabledControl(false);
			break;
		case COMPILATION_STARTED:
			outputArea.clear();
			startButton.setEnabled(false);
			debugButton.setEnabled(false);
			resetButton.setEnabled(false);
			demoButton.setEnabled(false);
			hintButton.setEnabled(false);
			exerciseView.setEnabledControl(false);
			lessonComboBox.setEnabled(false);
			exerciseComboBox.setEnabled(false);		
			break;
		case LOADING_DONE:
		case SAVING_DONE:
			startButton.setEnabled(true);
			debugButton.setEnabled(true);
			resetButton.setEnabled(true);
			hintButton.setEnabled(Game.getInstance().getCurrentLesson().getCurrentExercise().hint != null);
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
			hintButton.setEnabled(false);
			stopButton.setEnabled(true);
			exerciseView.setEnabledControl(false);
			lessonComboBox.setEnabled(false);
			exerciseComboBox.setEnabled(false);		
			break;
		case EXECUTION_ENDED:
			stopButton.setEnabled(false);
			startButton.setEnabled(true);
			debugButton.setEnabled(true);
			debugButton.setText("Step");
			resetButton.setEnabled(true);
			demoButton.setEnabled(true);
			exerciseView.setEnabledControl(true);
			hintButton.setEnabled(Game.getInstance().getCurrentLesson().getCurrentExercise().hint != null);
			lessonComboBox.setEnabled(true);
			exerciseComboBox.setEnabled(true);		
			break;
		case DEMO_STARTED:
			exerciseView.selectObjectivePane();
			startButton.setEnabled(false);
			debugButton.setEnabled(false);
			resetButton.setEnabled(false);
			hintButton.setEnabled(false);
			demoButton.setEnabled(false);
			stopButton.setEnabled(true);
			lessonComboBox.setEnabled(false);
			exerciseComboBox.setEnabled(false);		
			// exerciseView.setEnabledControl(false);
			break;
		case DEMO_ENDED:
			stopButton.setEnabled(false);
			startButton.setEnabled(true);
			debugButton.setEnabled(true);
			resetButton.setEnabled(true);
			demoButton.setEnabled(true);
			exerciseView.setEnabledControl(true);
			hintButton.setEnabled(Game.getInstance().getCurrentLesson().getCurrentExercise().hint != null);
			lessonComboBox.setEnabled(true);
			exerciseComboBox.setEnabled(true);		
			break;
		default:
		}

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
	public void currentExerciseHasChanged() {
		hintButton.setEnabled(Game.getInstance().getCurrentLesson().getCurrentExercise().hint != null);
	}

	@Override
	public void currentLessonHasChanged() {
		hintButton.setEnabled(Game.getInstance().getCurrentLesson().getCurrentExercise().hint != null);
	}

	@Override
	public void lessonsChanged() {
		// don't care
	}

	@Override
	public void selectedEntityHasChanged() {
		// don't care
	}

	@Override
	public void selectedWorldHasChanged() {
		// don't care
	}

	@Override
	public void selectedWorldWasUpdated() {
		// don't care
	}
	
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
