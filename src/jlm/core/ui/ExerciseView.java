package jlm.core.ui;

import java.awt.AWTKeyStroke;
import java.awt.KeyboardFocusManager;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BoundedRangeModel;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import jlm.core.GameListener;
import jlm.core.model.Game;
import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.Lecture;
import jlm.core.model.lesson.Exercise.WorldKind;
import jlm.universe.EntityControlPanel;
import jlm.universe.World;
import net.miginfocom.swing.MigLayout;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;


public class ExerciseView extends JPanel implements GameListener {

	private static final long serialVersionUID = 6649968807663790018L;
	private Game game;
	private WorldView worldView;
	private WorldView objectivesView;

	private JComboBox entityComboBox;
	private JComboBox worldComboBox;
	private EntityControlPanel buttonPanel;
	private JTabbedPane tabPane;
	private JPanel controlPane;
	private JSlider speedSlider;

	public I18n i18n = I18nFactory.getI18n(getClass(),"org.jlm.i18n.Messages",getLocale(), I18nFactory.FALLBACK);

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
		worldComboBox.setToolTipText(i18n.tr("Switch the displayed world"));
		upperPane.add(worldComboBox, "growx");

		// TODO: logarithmic slider ?
		speedSlider = new JSlider(new DelayBoundedRangeModel(Game.getInstance()));
		speedSlider.setOrientation(JSlider.HORIZONTAL);
		speedSlider.setMajorTickSpacing(50);
		speedSlider.setMinorTickSpacing(10);
		speedSlider.setPaintTicks(true);
		speedSlider.setPaintLabels(true);
		speedSlider.setToolTipText(i18n.tr("Change the speed of execution"));
		upperPane.add(speedSlider, "growx");

		tabPane = new JTabbedPane();
		removeControlPage(tabPane);
		if (Game.getInstance().getSelectedWorld() != null) {
			worldView = Game.getInstance().getSelectedWorld().getView();
			tabPane.addTab(i18n.tr("World")+worldView.getTabName(), null, worldView, 
					i18n.tr("Current world")+worldView.getTip());
		}
		if (Game.getInstance().getAnswerOfSelectedWorld() != null) {
			objectivesView = Game.getInstance().getAnswerOfSelectedWorld().getView();
			tabPane.addTab(i18n.tr("Objective")+objectivesView.getTabName(), null, objectivesView, 
					i18n.tr("Target world")+objectivesView.getTip());
		}
		
		upperPane.add(tabPane, "grow 100 100,push");

		entityComboBox = new JComboBox(new EntityComboListAdapter(Game.getInstance()));
		entityComboBox.setRenderer(new EntityCellRenderer());
		entityComboBox.setEditable(false);
		entityComboBox.setToolTipText(i18n.tr("Switch the entity"));
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
		worldComboBox.setVisible(lect instanceof Exercise && ((Exercise) lect).getWorldCount() > 1);
		entityComboBox.setVisible(lect instanceof Exercise && ((Exercise) lect).getWorlds(WorldKind.CURRENT).get(0).getEntityCount() > 1); 
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
			worldComboBox.setVisible(lect instanceof Exercise && ((Exercise) lect).getWorldCount() > 1);
	}

	@Override
	public void currentLessonHasChanged() { /* don't care */ }

	@Override
	public void selectedWorldHasChanged(World newWorld) {
		if (worldView != null && worldView.isWorldCompatible(this.game.getSelectedWorld())) {
			worldView.setWorld(this.game.getSelectedWorld());
			objectivesView.setWorld(this.game.getAnswerOfSelectedWorld());
		} else {
			tabPane.removeAll();
			worldView = Game.getInstance().getSelectedWorld().getView();
			tabPane.addTab(i18n.tr("World")+worldView.getTabName(), null, worldView, 
					i18n.tr("Current world")+worldView.getTip());
			objectivesView = Game.getInstance().getAnswerOfSelectedWorld().getView();
			tabPane.addTab(i18n.tr("Objective")+objectivesView.getTabName(), null, objectivesView, 
					i18n.tr("Target world")+objectivesView.getTip());
		}
		// To refresh the controlPane in any case ( else the SortingWorldPanel is not refreshed )
		controlPane.removeAll();
		buttonPanel = Game.getInstance().getSelectedWorld().getEntityControlPanel();
		controlPane.add(buttonPanel, "grow");
		
		Lecture lect = this.game.getCurrentLesson().getCurrentExercise();
		entityComboBox.setVisible(lect instanceof Exercise && ((Exercise) lect).getWorlds(WorldKind.CURRENT).get(0).getEntityCount() > 1); 
		worldComboBox.setVisible(lect instanceof Exercise && ((Exercise) lect).getWorldCount() > 1);		
	}

	// To refresh the controlPane in the BDR & BDR2 exercise from welcome
	@Override
	public void selectedEntityHasChanged() { 
		controlPane.removeAll();
		buttonPanel = Game.getInstance().getSelectedWorld().getEntityControlPanel();
		controlPane.add(buttonPanel, "grow");
	}

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
	 
}

class DelayBoundedRangeModel implements BoundedRangeModel, GameListener {

	public static int MIN_DELAY = 0;
	public static int DEFAULT_DELAY = 100;
	public static int MAX_DELAY = 500;

	protected EventListenerList listenerList = new EventListenerList();
	protected transient ChangeEvent changeEvent = null;

	private int extent = 0;
	private int min = MIN_DELAY;
	private int max = MAX_DELAY;
	private boolean isAdjusting = false;

	private Game game;

	public DelayBoundedRangeModel(Game game) {
		this.game = game;
		this.game.addGameListener(this);
	}

	@Override
	public int getMaximum() {
		return this.max;
	}

	@Override
	public void setMaximum(int newMaximum) {
		this.max = newMaximum;
	}

	@Override
	public int getMinimum() {
		return this.min;
	}

	@Override
	public void setMinimum(int newMinimum) {
		this.min = newMinimum;
	}

	@Override
	public int getValue() {
		return game.getSelectedWorld() == null? 0: game.getSelectedWorld().getDelay();
	}

	@Override
	public void setValue(int n) {
		if (game.getSelectedWorld() != null) {
			n = Math.min(n, Integer.MAX_VALUE - extent);

			int newValue = Math.max(n, min);
			if (newValue + extent > max) {
				newValue = max - extent;
			}
			setRangeProperties(newValue, extent, min, max, isAdjusting);
		}
	}

	@Override
	public boolean getValueIsAdjusting() {
		return isAdjusting;
	}

	@Override
	public void setValueIsAdjusting(boolean b) {
		setRangeProperties(game.getSelectedWorld().getDelay(), extent, min, max, b);
	}

	@Override
	public int getExtent() {
		return this.extent;
	}

	@Override
	public void setExtent(int n) {
        int newExtent = Math.max(0, n);
        int value = game.getSelectedWorld().getDelay();
        if(value + newExtent > max) {
            newExtent = max - value;
        }
        setRangeProperties(value, newExtent, min, max, isAdjusting);
	}

	@Override
	public void setRangeProperties(int newValue, int newExtent, int newMin, int newMax, boolean adjusting) {
		if (newMin > newMax) {
			newMin = newMax;
		}
		if (newValue > newMax) {
			newMax = newValue;
		}
		if (newValue < newMin) {
			newMin = newValue;
		}

		/*
		 * Convert the addends to long so that extent can be Integer.MAX_VALUE
		 * without rolling over the sum. A JCK test covers this, see bug
		 * 4097718.
		 */
		if (((long) newExtent + (long) newValue) > newMax) {
			newExtent = newMax - newValue;
		}

		if (newExtent < 0) {
			newExtent = 0;
		}

		boolean isChange = (newValue != game.getSelectedWorld().getDelay()) || (newExtent != extent)
				|| (newMin != min) || (newMax != max) || (adjusting != isAdjusting);

		if (isChange) {
			for (World w:game.getSelectedWorlds())
				w.setDelay(newValue);
			extent = newExtent;
			min = newMin;
			max = newMax;
			isAdjusting = adjusting;

			fireStateChanged();
		}
	}

	@Override
	public void addChangeListener(ChangeListener x) {
		listenerList.add(ChangeListener.class, x);
	}

	@Override
	public void removeChangeListener(ChangeListener x) {
		listenerList.remove(ChangeListener.class, x);
	}

	protected void fireStateChanged() {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				if (changeEvent == null) {
					changeEvent = new ChangeEvent(this);
				}
				((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
			}
		}
	}

	@Override
	public void currentExerciseHasChanged(Lecture lect) { /* don't care */ }

	@Override
	public void currentLessonHasChanged() { /* don't care */ }

	@Override
	public void selectedWorldHasChanged(World w) {
		fireStateChanged();
	}

	@Override
	public void selectedEntityHasChanged() { /* don't care */ }

	@Override
	public void selectedWorldWasUpdated() {
		fireStateChanged();
	}
}
