package jlm.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import jlm.core.Game;
import jlm.core.GameState;
import jlm.event.GameStateListener;
import jlm.ui.action.AbstractGameAction;
import jlm.ui.action.CleanUpSession;
import jlm.ui.action.PlayDemo;
import jlm.ui.action.QuitGame;
import jlm.ui.action.Reset;
import jlm.ui.action.RevertExercise;
import jlm.ui.action.StartExecution;
import jlm.ui.action.StopExecution;


public class MainFrame extends JFrame implements GameStateListener {

	private static final long serialVersionUID = -5022279647890315264L;

	private static JFrame instance = null;

	private ExerciseView exerciseView;
	private JButton startButton;
	private JButton stopButton;
	private JButton resetButton;
	private JButton demoButton;
	private LoggerPanel outputArea;

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
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Game.getInstance().quit();
				System.exit(0);
			}

			@Override
			public void windowClosed(WindowEvent e) {
				Game.getInstance().quit();
				System.exit(0);
			}
		});

		getContentPane().setLayout(new BorderLayout());

		JSplitPane logPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
		logPane.setOneTouchExpandable(true);
		double ratio = 0.7;
		logPane.setResizeWeight(ratio);
		logPane.setDividerLocation((int) (768 * ratio));

		JSplitPane mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);

		JTabbedPane tabPane = new JTabbedPane();
		mainPanel.setOneTouchExpandable(true);
		double weight = 0.6;
		mainPanel.setResizeWeight(weight);
		mainPanel.setDividerLocation((int) (1024 * weight));

		JEditorPane missionPanel = new MissionPanel(Game.getInstance());
		JScrollPane scrollPane = new JScrollPane(missionPanel);
		tabPane.add("Mission", scrollPane);

		CodePanel codePanel = new CodePanel(Game.getInstance());
		tabPane.add("Source Code", codePanel);

		mainPanel.setLeftComponent(tabPane);

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

		menuItem = new JMenuItem(new QuitGame(Game.getInstance(), "Quit", null, "Quit the application", KeyEvent.VK_Q));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));

		menu.add(menuItem);

		menuBar.add(menu);

		/* === Edit menu === */
		menu = new JMenu("Edit");
		menu.setMnemonic(KeyEvent.VK_G);
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

		if (!System.getProperty("os.name").startsWith("Mac")) {
			menu = new JMenu("Help");
			menuBar.add(menu);

			menu.add(new JMenuItem(new AbstractGameAction(Game.getInstance(), "About this lesson", null){
				private static final long serialVersionUID = 1L;
				public void actionPerformed(ActionEvent arg0) {
					AboutLessonDialog.getInstance().setVisible(true);
				}				
			}));
			menu.add(new JMenuItem(new AbstractGameAction(Game.getInstance(), "About this world", null){
				private static final long serialVersionUID = 1L;
				public void actionPerformed(ActionEvent arg0) {
					AboutWorldDialog.getInstance().setVisible(true);
				}				
			}));
			menu.add(new JMenuItem(new AbstractGameAction(Game.getInstance(), "About JLM", null){
				private static final long serialVersionUID = 1L;
				public void actionPerformed(ActionEvent e) {
					AboutJLMDialog.getInstance().setVisible(true);
				}				
			}));
			
		} else {

			if (System.getProperty("os.name").startsWith("Mac")) {								
                try {
					OSXAdapter.setQuitHandler(this, getClass().getDeclaredMethod("quit", (Class[])null));
					OSXAdapter.setAboutHandler(this, getClass().getDeclaredMethod("about", (Class[])null));
                } catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		}

		setJMenuBar(menuBar);

	}

	private void initToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(true);
		toolBar.setBorder(BorderFactory.createEtchedBorder());

		startButton = new JButton(new StartExecution(Game.getInstance(), "Start", ResourcesCache
				.getIcon("resources/start.png")));
		startButton.setBorderPainted(false);

		stopButton = new JButton(new StopExecution(Game.getInstance(), "Stop", ResourcesCache
				.getIcon("resources/stop.png")));
		stopButton.setBorderPainted(false);
		stopButton.setEnabled(false);

		resetButton = new JButton(new Reset(Game.getInstance(), "Reset", ResourcesCache.getIcon("resources/reset.png")));
		resetButton.setBorderPainted(false);
		resetButton.setEnabled(true);

		demoButton = new JButton(new PlayDemo(Game.getInstance(), "Demo", ResourcesCache.getIcon("resources/demo.png")));
		demoButton.setBorderPainted(false);
		demoButton.setEnabled(true);

		LessonComboListAdapter lessonAdapter = new LessonComboListAdapter(Game.getInstance());
		JComboBox lessonComboBox = new JComboBox(lessonAdapter);
		lessonComboBox.setRenderer(new LessonCellRenderer());

		ExerciseComboListAdapter exerciseAdapter = new ExerciseComboListAdapter(Game.getInstance());
		JComboBox exerciseComboBox = new JComboBox(exerciseAdapter);
		exerciseComboBox.setRenderer(new ExerciseCellRenderer());

		toolBar.add(startButton);
		toolBar.add(stopButton);
		toolBar.add(resetButton);
		toolBar.add(demoButton);
		// toolBar.add(viewObjectivesButton);
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
			resetButton.setEnabled(false);
			demoButton.setEnabled(false);
			exerciseView.setEnabledControl(false);
			break;
		case COMPILATION_STARTED:
			outputArea.clear();
			startButton.setEnabled(false);
			resetButton.setEnabled(false);
			demoButton.setEnabled(false);
			exerciseView.setEnabledControl(false);
			break;
		case LOADING_DONE:
		case SAVING_DONE:
			startButton.setEnabled(true);
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
			startButton.setEnabled(false);
			resetButton.setEnabled(false);
			demoButton.setEnabled(false);
			stopButton.setEnabled(true);
			exerciseView.setEnabledControl(false);
			break;
		case EXECUTION_ENDED:
			stopButton.setEnabled(false);
			startButton.setEnabled(true);
			resetButton.setEnabled(true);
			demoButton.setEnabled(true);
			exerciseView.setEnabledControl(true);
			break;
		case DEMO_STARTED:
			exerciseView.selectObjectivePane();
			startButton.setEnabled(false);
			resetButton.setEnabled(false);
			demoButton.setEnabled(false);
			stopButton.setEnabled(false);

			// exerciseView.setEnabledControl(false);
			break;
		case DEMO_ENDED:
			stopButton.setEnabled(false);
			startButton.setEnabled(true);
			resetButton.setEnabled(true);
			demoButton.setEnabled(true);
			exerciseView.setEnabledControl(true);
			break;
		default:
		}

	}

	
	public void quit() {
		MainFrame.getInstance().dispose();
		Game.getInstance().quit();
		//event.setHandled(true);
	}
	
	public void about() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						AboutJLMDialog.getInstance().setVisible(true);
					}
				});
				//event.setHandled(true);
			}
		});
	}	
}
