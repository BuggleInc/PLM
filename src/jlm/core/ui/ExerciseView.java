package jlm.core.ui;

import java.awt.AWTKeyStroke;
import java.awt.KeyboardFocusManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import jlm.core.GameListener;
import jlm.core.model.Game;
import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lecture;
import jlm.universe.EntityControlPanel;
import jlm.universe.World;
import jlm.universe.bugglequest.mapeditor.Editor;
import jlm.universe.bugglequest.mapeditor.MapEditorPanel;
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
	private JPanel upperPane;
	private JSplitPane splitPane;
	private boolean admin;
	private static boolean ok = false; 

	private static ExerciseView instance = null;

	public ExerciseView(Game game) {
		super();
		this.game = game;
		this.game.addGameListener(this);
		initComponents();
		addComponents();
		currentExerciseHasChanged(Game.getInstance().getCurrentLesson().getCurrentExercise());
		instance=this;
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
		upperPane = new JPanel();

		// TODO: add key shortcuts

		upperPane.setLayout(new MigLayout("insets 0 0 0 0,wrap","[fill]"));

		worldComboBox = new JComboBox(new WorldComboListAdapter(Game.getInstance()));
		worldComboBox.setRenderer(new WorldCellRenderer());
		worldComboBox.setEditable(false);
		worldComboBox.setToolTipText("Switch the displayed world");

		// TODO: logarithmic slider ?
		speedSlider = new JSlider(new DelayBoundedRangeModel(Game.getInstance()));
		speedSlider.setOrientation(JSlider.HORIZONTAL);
		speedSlider.setMajorTickSpacing(50);
		speedSlider.setMinorTickSpacing(10);
		speedSlider.setPaintTicks(true);
		speedSlider.setPaintLabels(true);
		speedSlider.setToolTipText("Change the speed of execution");

		tabPane = new JTabbedPane();
		removeControlPage(tabPane);
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


		entityComboBox = new JComboBox(new EntityComboListAdapter(Game.getInstance()));
		entityComboBox.setRenderer(new EntityCellRenderer());
		entityComboBox.setEditable(false);
		entityComboBox.setToolTipText("Switch the entity");

		/*
		 * FIXME: strange behavior on OSX, if you click on long time on the
		 * selected entity item then it tries to edit it and throw an exception.
		 * Even if the editable property is set to false
		 */


		controlPane = new JPanel();
		controlPane.setLayout(new MigLayout("insets 0 0 0 0, fill"));
		//add(controlPane, "span,growx,wrap");

		if (Game.getInstance().getSelectedWorld()!=null) {
			buttonPanel = Game.getInstance().getSelectedWorld().getEntityControlPanel();
			controlPane.add(buttonPanel, "grow");
		}



		this.setLayout(new MigLayout("insets 0 0 0 0, fill"));

		Lecture lect = this.game.getCurrentLesson().getCurrentExercise();
		worldComboBox.setVisible(lect instanceof Exercise && ((Exercise) lect).worldCount() > 1);
		entityComboBox.setVisible(lect instanceof Exercise && ((Exercise) lect).getCurrentWorld().get(0).getEntityCount() > 1); 
	}

	public void addComponents(){
		this.removeAll();
		upperPane.removeAll();

		upperPane.add(worldComboBox, "growx");

		if(!Global.admin) upperPane.add(speedSlider, "growx");

		upperPane.add(tabPane, "grow 100 100,push");

		if(!Global.admin) upperPane.add(entityComboBox, "alignx center");

		if(!Global.admin) {
			splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperPane, controlPane);
			splitPane.setBorder(null);
			splitPane.setOneTouchExpandable(true);
			splitPane.setResizeWeight(1.0);
			splitPane.setDividerLocation(420);
			this.add(splitPane, "grow");
		}
		else
			this.add(upperPane, "grow");
		admin=Global.admin;
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
		setOk(true);
		if(Global.admin!=admin)
			addComponents();
		
		if (worldView != null && worldView[0].isWorldCompatible(this.game.getSelectedWorld())) {
			for (WorldView w:worldView)
				w.setWorld(this.game.getSelectedWorld());
			for (WorldView w:objectivesView)
				w.setWorld(this.game.getAnswerOfSelectedWorld());
		}

		tabPane.removeAll();

		worldView = Game.getInstance().getSelectedWorld().getView();
		objectivesView = Game.getInstance().getAnswerOfSelectedWorld().getView();
		if(!Global.admin){
			for (WorldView wv: worldView) {
				tabPane.addTab("World"+wv.getTabName(), null, wv, 
						"Current world"+wv.getTip());
			}
			for (WorldView wv: objectivesView) {
				tabPane.addTab("Objective"+wv.getTabName(), null, wv, 
						"Target world"+wv.getTip());
			}
		}
		else{
			for (WorldView wv: worldView) {
				String map_path = "src/"+Game.getInstance().getCurrentLesson().getCurrentExercise().getMissionMarkDownFilePath()+".map";
				
				Editor edit= new Editor();
				edit.loadMap(new File(map_path));
				tabPane.addTab("World"+wv.getTabName(), null, new MapEditorPanel(edit), 
						"Current world"+wv.getTip());
			}
		}

		controlPane.removeAll();
		buttonPanel = Game.getInstance().getSelectedWorld().getEntityControlPanel();
		controlPane.add(buttonPanel, "grow");

		// 
		Lecture lect = this.game.getCurrentLesson().getCurrentExercise();
		entityComboBox.setVisible(lect instanceof Exercise && ((Exercise) lect).getCurrentWorld().get(0).getEntityCount() > 1); 
		worldComboBox.setVisible(lect instanceof Exercise && ((Exercise) lect).worldCount() > 1);		
	}

	@Override
	public void selectedEntityHasChanged() { /* don't care */ }

	@Override
	public void selectedWorldWasUpdated() { /* don't care */ }

	private static void removeControlPage(JComponent comp)
	{
		KeyStroke ctrlTab = KeyStroke.getKeyStroke("ctrl TAB");
		KeyStroke ctrlShiftTab = KeyStroke.getKeyStroke("ctrl shift TAB");

		// Remove ctrl-tab from normal focus traversal
		Set<AWTKeyStroke> forwardKeys = new HashSet<AWTKeyStroke>(comp.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
		forwardKeys.remove(ctrlTab);
		comp.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys);

		// Remove ctrl-shift-tab from normal focus traversal
		Set<AWTKeyStroke> backwardKeys = new HashSet<AWTKeyStroke>(comp.getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));
		backwardKeys.remove(ctrlShiftTab);
		comp.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardKeys);

		// Add keys to the tab's input map
		InputMap inputMap = comp.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		inputMap.put(ctrlTab, "navigateNext");
		inputMap.put(ctrlShiftTab, "navigatePrevious");
	}

	public JTabbedPane getTabPane() {
		return tabPane;
	}

	public static ExerciseView getInstance() {
		return instance;
	}

	public static boolean isOk() {
		return ok;
	}

	public static void setOk(boolean ok) {
		ExerciseView.ok = ok;
	}
}