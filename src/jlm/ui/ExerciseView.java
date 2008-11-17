package jlm.ui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import jlm.core.Game;
import jlm.event.GameListener;
import universe.bugglequest.ui.BuggleButtonPanel;
import universe.bugglequest.ui.BuggleCellRenderer;
import universe.bugglequest.ui.WorldCellRenderer;
import universe.bugglequest.ui.WorldComboListAdapter;


public class ExerciseView extends JPanel implements GameListener {

	private static final long serialVersionUID = 6649968807663790018L;
	private Game game;
	private WorldView worldView;
	private WorldView objectivesView;

	private JComboBox buggleComboBox; 
	private JComboBox worldComboBox;
	private BuggleButtonPanel buttonPanel;
	private JTabbedPane tabPane;
	
	public ExerciseView(Game game) {
		super();
		this.game = game;
		this.game.addGameListener(this);
		this.initComponents();
		currentExerciseHasChanged();
	}

	public void setEnabledControl(boolean enabled) {
		worldComboBox.setEnabled(enabled);
		buggleComboBox.setEnabled(enabled);
		if (buttonPanel == null) {
			System.out.println("button panel is null");
			Thread.currentThread().getStackTrace().toString();
		}
		buttonPanel.setEnabledControl(enabled);
	}
	
	public void initComponents() {
		// TODO: add key shortcuts
		setLayout(new BorderLayout());

		JPanel mapsPanel = new JPanel();
		mapsPanel.setBorder(BorderFactory.createEtchedBorder());
		mapsPanel.setLayout(new BorderLayout());

		worldComboBox = new JComboBox(new WorldComboListAdapter(Game.getInstance()));
		worldComboBox.setRenderer(new WorldCellRenderer());
		worldComboBox.setEditable(false);
		mapsPanel.add(worldComboBox, BorderLayout.NORTH);

		tabPane = new JTabbedPane();
		worldView = Game.getInstance().getSelectedWorld().getView();
		tabPane.add("World", worldView);

		objectivesView = Game.getInstance().getAnswerOfSelectedWorld().getView();
		tabPane.add("Objective", objectivesView);
		mapsPanel.add(tabPane, BorderLayout.CENTER);

		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BorderLayout());
		controlPanel.setBorder(BorderFactory.createEtchedBorder());

		buggleComboBox = new JComboBox(new EntityComboListAdapter(Game.getInstance()));
		buggleComboBox.setRenderer(new BuggleCellRenderer());
		buggleComboBox.setEditable(false);
		/*
		 * FIXME: strange behavior on OSX, if you click on long time on the
		 * selected buggle item then it tries to edit it and throw an exception.
		 * Even if the editable property is set to false
		 */

		buttonPanel = new BuggleButtonPanel(buggleComboBox, this.game);
		controlPanel.add(buttonPanel, BorderLayout.CENTER);

		add(mapsPanel, BorderLayout.CENTER);
		add(controlPanel, BorderLayout.SOUTH);
	}

	public void selectObjectivePane() {
		tabPane.setSelectedIndex(1);
	}
	public void selectWorldPane() {
		tabPane.setSelectedIndex(0);		
	}
	
	@Override
	public void currentExerciseHasChanged() {
		// don't care
	}

	@Override
	public void currentLessonHasChanged() {
		// don't care
	}

	@Override
	public void lessonsChanged() {
		// don't care
	}

	@Override
	public void selectedWorldHasChanged() {
		worldView.setWorld(this.game.getSelectedWorld());
		objectivesView.setWorld(this.game.getAnswerOfSelectedWorld());
		//TODO KILLME brushButton.setSelected(game.getSelectedBuggle().isBrushDown());
	}

	@Override
	public void selectedEntityHasChanged() {
		// don't care
		//TODO KILLME brushButton.setSelected(game.getSelectedBuggle().isBrushDown());
	}

	@Override
	public void selectedWorldWasUpdated() {
		// don't care
	}
}
