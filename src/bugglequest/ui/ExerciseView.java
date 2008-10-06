package bugglequest.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import bugglequest.core.AbstractBuggle;
import bugglequest.core.Game;
import bugglequest.event.GameListener;
import bugglequest.exception.BuggleWallException;

public class ExerciseView extends JPanel implements GameListener {

	private static final long serialVersionUID = 6649968807663790018L;
	private Game game;
	private WorldView worldView;
	private WorldView objectivesView;

	private JComboBox buggleComboBox; 
	private JToolBar buttonsPanel;
	private JComboBox worldComboBox;
	private JButton fButton;
	private JButton bButton;
	private JButton rButton;
	private JButton lButton;
	private JToggleButton brushButton;
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
		fButton.setEnabled(enabled);
		bButton.setEnabled(enabled);
		lButton.setEnabled(enabled);
		rButton.setEnabled(enabled);
		brushButton.setEnabled(enabled);
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
		worldView = new WorldView(Game.getInstance().getSelectedWorld());
		tabPane.add("World", worldView);

		objectivesView = new WorldView(Game.getInstance().getAnswerOfSelectedWorld());
		tabPane.add("Objective", objectivesView);
		mapsPanel.add(tabPane, BorderLayout.CENTER);

		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BorderLayout());
		controlPanel.setBorder(BorderFactory.createEtchedBorder());

		buggleComboBox = new JComboBox(new BuggleComboListAdapter(Game.getInstance()));
		buggleComboBox.setRenderer(new BuggleCellRenderer());
		buggleComboBox.setEditable(false);
		/*
		 * FIXME: strange behavior on OSX, if you click on long time on the
		 * selected buggle item then it tries to edit it and throw an exception.
		 * Even if the editable property is set to false
		 */

		buttonsPanel = new JToolBar(); // for the nice jbutton style
		buttonsPanel.setFloatable(false);

		fButton = new JButton("forward");
		fButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					game.getSelectedBuggle().forward();
				} catch (BuggleWallException e) {
					// e.printStackTrace();
					game.getOutputWriter().log(e);
				}
			}
		});
		fButton.setMnemonic(KeyEvent.VK_UP);

		bButton = new JButton("backward");
		bButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					game.getSelectedBuggle().backward();
				} catch (BuggleWallException e) {
					// e.printStackTrace();
					game.getOutputWriter().log(e);
				}
			}
		});
		bButton.setMnemonic(KeyEvent.VK_DOWN);

		lButton = new JButton("turn left");
		lButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				game.getSelectedBuggle().turnLeft();
			}
		});
		lButton.setMnemonic(KeyEvent.VK_LEFT);

		rButton = new JButton("turn right");
		rButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				game.getSelectedBuggle().turnRight();
			}
		});
		rButton.setMnemonic(KeyEvent.VK_RIGHT);

		brushButton = new JToggleButton("mark");
		brushButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				AbstractBuggle b = game.getSelectedBuggle();
				if (b.isBrushDown()) {
					b.brushUp();
				} else {
					b.brushDown();
				}
			}
		});
		brushButton.setMnemonic(KeyEvent.VK_SPACE);
		brushButton.setSelected(game.getSelectedBuggle().isBrushDown());
		
		
		GridBagLayout gdLayout = new GridBagLayout();
		buttonsPanel.setLayout(gdLayout);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);

		c.gridy = 0;
		c.gridx = 0;
		c.gridwidth = 3;
		gdLayout.setConstraints(buggleComboBox, c);
		buttonsPanel.add(buggleComboBox);

		c.gridy = 1;
		c.gridx = 1;
		c.gridwidth = 1;
		gdLayout.setConstraints(fButton, c);
		buttonsPanel.add(fButton);

		c.gridy = 2;
		c.gridx = 0;
		gdLayout.setConstraints(lButton, c);
		buttonsPanel.add(lButton);

		c.gridy = 2;
		c.gridx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		gdLayout.setConstraints(brushButton, c);
		buttonsPanel.add(brushButton);

		c.gridy = 2;
		c.gridx = 2;
		gdLayout.setConstraints(rButton, c);
		buttonsPanel.add(rButton);

		c.gridy = 3;
		c.gridx = 1;
		gdLayout.setConstraints(bButton, c);
		buttonsPanel.add(bButton);
		controlPanel.add(buttonsPanel, BorderLayout.CENTER);

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
		brushButton.setSelected(game.getSelectedBuggle().isBrushDown());
	}

	@Override
	public void selectedBuggleHasChanged() {
		// don't care
		brushButton.setSelected(game.getSelectedBuggle().isBrushDown());
	}

	@Override
	public void selectedWorldWasUpdated() {
		// don't care
	}
}
