package jlm.core.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import jlm.core.GameStateListener;
import jlm.core.StatusStateListener;
import jlm.core.model.Game;
import jlm.core.model.GameState;

public class StatusBar extends JPanel implements GameStateListener,StatusStateListener {

	private static final long serialVersionUID = 8443305863958273495L;
	private Game game;
	private JLabel statusMessageLabel;
	private JLabel statusAnimationLabel;

	private int busyIconIndex = 0;

	private Timer busyIconTimer;

	public StatusBar(Game game) {
		super();
		this.game = game;
		this.game.addGameStateListener(this);
		game.addStatusStateListener(this);
		initComponents();
	}

	public void initComponents() {
		this.setBorder(BorderFactory.createEtchedBorder());

		// JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		statusMessageLabel = new JLabel("");
		statusAnimationLabel = new JLabel();
		statusAnimationLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusAnimationLabel.setIcon(ResourcesCache.getIcon("resources/busyicons/idle-icon.png"));

		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(Box.createHorizontalStrut(10));
		this.add(statusMessageLabel);
		this.add(Box.createHorizontalGlue());
		this.add(statusAnimationLabel);
		this.add(Box.createHorizontalStrut(20));

		ResourcesCache.loadBusyIconAnimation();

		busyIconTimer = new Timer(30, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				busyIconIndex = (busyIconIndex + 1) % ResourcesCache.getBusyIconsSize();
				statusAnimationLabel.setIcon(ResourcesCache.getBusyIcons(busyIconIndex));
			}
		});

	}

	@Override
	public void stateChanged(GameState type) {
		switch (type) {
		case LOADING:
			statusMessageLabel.setText("Loading");
			busyIconTimer.start();
			break;
		case SAVING:
			statusMessageLabel.setText("Saving");
			busyIconTimer.start();
			break;
		case COMPILATION_STARTED:
			statusMessageLabel.setText("Compiling");
			busyIconTimer.start();
			break;
		case LOADING_DONE:
		case SAVING_DONE:
		case COMPILATION_ENDED:
		case DEMO_ENDED:
		case EXECUTION_ENDED:
			game.statusRootSet("");
			game.statusArgEmpty();
			busyIconTimer.stop();
			statusAnimationLabel.setIcon(ResourcesCache.getIcon("resources/busyicons/idle-icon.png"));
			break;
		case EXECUTION_STARTED:
			game.statusRootSet("Running ");
			busyIconTimer.start();
			break;
		case DEMO_STARTED:
			game.statusRootSet("Playing demo ");
			busyIconTimer.start();
			break;		
		default:
			statusMessageLabel.setText("");
			statusAnimationLabel.setIcon(ResourcesCache.getIcon("resources/busyicons/idle-icon.png"));
		}
	}

	@Override
	public void stateChanged(String txt) {
		statusMessageLabel.setText(txt);		
	}	
}
