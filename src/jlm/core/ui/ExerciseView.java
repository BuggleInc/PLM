package jlm.core.ui;

import java.awt.AWTKeyStroke;
import java.awt.KeyboardFocusManager;
import java.util.HashSet;
import java.util.Locale;
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
import jlm.core.HumanLangChangesListener;
import jlm.core.model.Game;
import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.Lecture;
import jlm.core.model.lesson.Exercise.WorldKind;
import jlm.universe.EntityControlPanel;
import jlm.universe.World;
import net.miginfocom.swing.MigLayout;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;


public class ExerciseView extends JPanel implements GameListener, HumanLangChangesListener {

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
		this.game.addHumanLangListener(this);
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

	@Override
	public void currentHumanLanguageHasChanged(Locale newLang) {
		// TODO Auto-generated method stub
		i18n.setLocale(newLang);
		
		worldComboBox.setToolTipText(i18n.tr("Switch the displayed world"));
		speedSlider.setToolTipText(i18n.tr("Change the speed of execution"));
		entityComboBox.setToolTipText(i18n.tr("Switch the entity"));
	}
	 
}