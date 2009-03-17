package jlm.ui;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;

import jlm.core.Game;
import jlm.event.GameListener;
import jlm.universe.EntityControlPanel;
import net.miginfocom.swing.MigLayout;

public class ExerciseView extends JPanel implements GameListener {

	private static final long serialVersionUID = 6649968807663790018L;
	private Game game;
	private WorldView[] worldView;
	private WorldView[] objectivesView;

	private JComboBox entityComboBox;
	private JComboBox worldComboBox;
	private EntityControlPanel buttonPanel;
	private JTabbedPane tabPane;
	private JPanel controlPane;
	private JSlider speedSlider;

	public ExerciseView(Game game) {
		super();
		this.game = game;
		this.game.addGameListener(this);
		this.initComponents();
		currentExerciseHasChanged();
	}

	public void setEnabledControl(boolean enabled) {
		// worldComboBox.setEnabled(enabled);
		entityComboBox.setEnabled(enabled);
		if (buttonPanel != null)
			buttonPanel.setEnabledControl(enabled);
	}

	public void initComponents() {
		// TODO: add key shortcuts
		setLayout(new MigLayout("fill"));

		worldComboBox = new JComboBox(new WorldComboListAdapter(Game.getInstance()));
		worldComboBox.setRenderer(new WorldCellRenderer());
		worldComboBox.setEditable(false);
		add(worldComboBox, "span,growx,wrap");

		// TODO: logarithmic slider ?
		speedSlider = new JSlider(new DelayBoundedRangeModel(Game.getInstance()));
		speedSlider.setOrientation(JSlider.HORIZONTAL);
		speedSlider.setMajorTickSpacing(50);
		speedSlider.setMinorTickSpacing(10);
		speedSlider.setPaintTicks(true);
		speedSlider.setPaintLabels(true);
		add(speedSlider, "growx,wrap");

		tabPane = new JTabbedPane();
		worldView = Game.getInstance().getSelectedWorld().getView();
		tabPane.add("World", worldView[0]);

		objectivesView = Game.getInstance().getAnswerOfSelectedWorld().getView();
		tabPane.add("Objective", objectivesView[0]);
		for (int i=1;i<worldView.length;i++)
			tabPane.add(worldView[i].getTabName(), worldView[i]);

		add(tabPane, "span,grow,wrap");

		entityComboBox = new JComboBox(new EntityComboListAdapter(Game.getInstance()));
		entityComboBox.setRenderer(new EntityCellRenderer());
		entityComboBox.setEditable(false);
		add(entityComboBox, "span,alignx center");

		/*
		 * FIXME: strange behavior on OSX, if you click on long time on the
		 * selected entity item then it tries to edit it and throw an exception.
		 * Even if the editable property is set to false
		 */

		buttonPanel = Game.getInstance().getSelectedWorld().getEntityControlPanel();
		controlPane = new JPanel();
		controlPane.setLayout(new MigLayout("fill"));
		controlPane.add(buttonPanel, "grow");
		add(controlPane, "span,growx,wrap");
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
		if (worldView[0].isWorldCompatible(this.game.getSelectedWorld())) {
			for (WorldView w:worldView)
				w.setWorld(this.game.getSelectedWorld());
			for (WorldView w:objectivesView)
				w.setWorld(this.game.getAnswerOfSelectedWorld());
		} else {
			tabPane.removeAll();
			worldView = Game.getInstance().getSelectedWorld().getView();
			tabPane.add("World", worldView[0]);

			objectivesView = Game.getInstance().getAnswerOfSelectedWorld().getView();
			tabPane.add("Objective", objectivesView[0]);
			
			for (int i=1;i<worldView.length;i++)
				tabPane.add(worldView[i].getTabName(), worldView[i]);

			controlPane.removeAll();
			buttonPanel = Game.getInstance().getSelectedWorld().getEntityControlPanel();
			controlPane.add(buttonPanel, "grow");
		}
	}

	@Override
	public void selectedEntityHasChanged() {
		// don't care
	}

	@Override
	public void selectedWorldWasUpdated() {
		// don't care
	}
}
