package jlm.core.ui;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import jlm.core.GameListener;
import jlm.core.model.Game;
import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.Lecture;
import jlm.universe.EntityControlPanel;
import jlm.universe.World;
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
		initComponents();
		currentExerciseHasChanged(Game.getInstance().getCurrentLesson().getCurrentExercise());
	}

	public void setEnabledControl(boolean enabled) {
		if (entityComboBox == null) 
			return;
		// worldComboBox.setEnabled(enabled);
		entityComboBox.setEnabled(enabled);
		if (buttonPanel != null)
			buttonPanel.setEnabledControl(enabled);
	}

	public void initComponents() {	
		JPanel upperPane = new JPanel();
		
		// TODO: add key shortcuts
				
		upperPane.setLayout(new MigLayout("insets 0 0 0 0,wrap","[fill]"));

		worldComboBox = new JComboBox(new WorldComboListAdapter(Game.getInstance()));
		worldComboBox.setRenderer(new WorldCellRenderer());
		worldComboBox.setEditable(false);
		worldComboBox.setToolTipText("Switch the displayed world");
		upperPane.add(worldComboBox, "growx");

		// TODO: logarithmic slider ?
		speedSlider = new JSlider(new DelayBoundedRangeModel(Game.getInstance()));
		speedSlider.setOrientation(JSlider.HORIZONTAL);
		speedSlider.setMajorTickSpacing(50);
		speedSlider.setMinorTickSpacing(10);
		speedSlider.setPaintTicks(true);
		speedSlider.setPaintLabels(true);
		speedSlider.setToolTipText("Change the speed of execution");
		upperPane.add(speedSlider, "growx");

		tabPane = new JTabbedPane();
		
		if (Game.getInstance().getSelectedWorld() != null) {
			worldView = Game.getInstance().getSelectedWorld().getView();
			for (WorldView wv: worldView) {
				tabPane.addTab("World"+wv.getTabName(), null, wv, 
						"Current world"+wv.getTip());
			}
		}
		if (Game.getInstance().getAnswerOfSelectedWorld() != null) {
			objectivesView = Game.getInstance().getAnswerOfSelectedWorld().getView();
			for (WorldView wv: objectivesView) {
				tabPane.addTab("Objective"+wv.getTabName(), null, wv, 
						"Target world"+wv.getTip());
			}
		}
		
		upperPane.add(tabPane, "grow 100 100,push");

		entityComboBox = new JComboBox(new EntityComboListAdapter(Game.getInstance()));
		entityComboBox.setRenderer(new EntityCellRenderer());
		entityComboBox.setEditable(false);
		entityComboBox.setToolTipText("Switch the entity");
		upperPane.add(entityComboBox, "alignx center");

		/*
		 * FIXME: strange behavior on OSX, if you click on long time on the
		 * selected entity item then it tries to edit it and throw an exception.
		 * Even if the editable property is set to false
		 */

		
		controlPane = new JPanel();
		controlPane.setLayout(new MigLayout("insets 0 0 0 0, fill"));
		if (Game.getInstance().getSelectedWorld()!=null) {
			buttonPanel = Game.getInstance().getSelectedWorld().getEntityControlPanel();
			controlPane.add(buttonPanel, "grow");
		}
		//add(controlPane, "span,growx,wrap");
		
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperPane, controlPane);
		splitPane.setBorder(null);
		splitPane.setOneTouchExpandable(true);
		splitPane.setResizeWeight(1.0);
		splitPane.setDividerLocation(420);
		
		this.setLayout(new MigLayout("insets 0 0 0 0, fill"));
		this.add(splitPane, "grow");
		
		Lecture lect = this.game.getCurrentLesson().getCurrentExercise();
		worldComboBox.setVisible(lect instanceof Exercise && ((Exercise) lect).worldCount() > 1);
		entityComboBox.setVisible(lect instanceof Exercise && ((Exercise) lect).getCurrentWorld().get(0).getEntityCount() > 1); 
	}

	public void selectObjectivePane() {
		tabPane.setSelectedIndex(1);
	}

	public void selectWorldPane() {
		tabPane.setSelectedIndex(0);
	}

	@Override
	public void currentExerciseHasChanged(Lecture lect) {
		if (worldComboBox != null)
			worldComboBox.setVisible(lect instanceof Exercise && ((Exercise) lect).worldCount() > 1);
	}

	@Override
	public void currentLessonHasChanged() { /* don't care */ }

	@Override
	public void selectedWorldHasChanged(World newWorld) {
		if (worldView != null && worldView[0].isWorldCompatible(this.game.getSelectedWorld())) {
			for (WorldView w:worldView)
				w.setWorld(this.game.getSelectedWorld());
			for (WorldView w:objectivesView)
				w.setWorld(this.game.getAnswerOfSelectedWorld());
		} else {
			tabPane.removeAll();
			
			worldView = Game.getInstance().getSelectedWorld().getView();
			for (WorldView wv: worldView) {
				tabPane.addTab("World"+wv.getTabName(), null, wv, 
						       "Current world"+wv.getTip());
			}
			objectivesView = Game.getInstance().getAnswerOfSelectedWorld().getView();
			for (WorldView wv: objectivesView) {
				tabPane.addTab("Objective"+wv.getTabName(), null, wv, 
						       "Target world"+wv.getTip());
			}
			
			controlPane.removeAll();
			buttonPanel = Game.getInstance().getSelectedWorld().getEntityControlPanel();
			controlPane.add(buttonPanel, "grow");
		}
		
		// 
		Lecture lect = this.game.getCurrentLesson().getCurrentExercise();
		entityComboBox.setVisible(lect instanceof Exercise && ((Exercise) lect).getCurrentWorld().get(0).getEntityCount() > 1); 
		worldComboBox.setVisible(lect instanceof Exercise && ((Exercise) lect).worldCount() > 1);		
	}

	@Override
	public void selectedEntityHasChanged() { /* don't care */ }

	@Override
	public void selectedWorldWasUpdated() { /* don't care */ }
}
