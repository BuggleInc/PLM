package plm.core.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import plm.core.GameListener;
import plm.core.GameStateListener;
import plm.core.ProgLangChangesListener;
import plm.core.StatusStateListener;
import plm.core.model.Game;
import plm.core.model.ProgrammingLanguage;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lecture;
import plm.core.ui.action.SetProgLanguage;
import plm.universe.World;

public class StatusBar extends JPanel implements GameListener,GameStateListener,StatusStateListener, ProgLangChangesListener {

	private static final long serialVersionUID = 8443305863958273495L;
	private Game game;
	private JLabel statusMessageLabel;
	private JLabel statusAnimationLabel;
	private JLabel progLangLabel;
	private JPopupMenu popup = new JPopupMenu();
	
	private int busyIconIndex = 0;

	private Timer busyIconTimer;

	public I18n i18n = I18nFactory.getI18n(getClass(),"org.plm.i18n.Messages",getLocale(), I18nFactory.FALLBACK);

	public StatusBar(Game game) {
		super();
		this.game = game;
		this.game.addGameStateListener(this);
		game.addStatusStateListener(this);
		game.addProgLangListener(this);
		game.addGameListener(this);
		initComponents();
	}
	
	public void initComponents() {
		this.setBorder(BorderFactory.createEtchedBorder());

		// JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		statusMessageLabel = new JLabel("");
		statusAnimationLabel = new JLabel();
		statusAnimationLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusAnimationLabel.setIcon(ResourcesCache.getIcon("img/busyicon/idle.png"));
		
		setupLanguages(Game.getInstance().getCurrentLesson().getCurrentExercise());
		progLangLabel = new JLabel();
		progLangLabel.setHorizontalAlignment(SwingConstants.LEFT);
		progLangLabel.setIcon(Game.getProgrammingLanguage().getIcon());
		progLangLabel.addMouseListener(new MouseListener() {			
			public void mouseReleased(MouseEvent e) {
				maybeShowPopup(e);
			}
			public void mousePressed(MouseEvent e) {
				maybeShowPopup(e);
			}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {
                popup.show(e.getComponent(),
                        e.getX(), e.getY());
			}
			void maybeShowPopup(MouseEvent e) {
				if (e.isPopupTrigger())
					mouseClicked(e);
			}
		});

		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(Box.createHorizontalStrut(10));
		this.add(statusMessageLabel);
		this.add(Box.createHorizontalGlue());
		this.add(statusAnimationLabel);
		this.add(Box.createHorizontalStrut(20));
		this.add(progLangLabel);
		this.add(Box.createHorizontalStrut(10));

		ResourcesCache.loadBusyIconAnimation();

		busyIconTimer = new Timer(30, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				busyIconIndex = (busyIconIndex + 1) % ResourcesCache.getBusyIconsSize();
				statusAnimationLabel.setIcon(ResourcesCache.getBusyIcons(busyIconIndex));
			}
		});

	}

	@Override
	public void stateChanged(Game.GameState type) {
		switch (type) {
		case SAVING:
			statusMessageLabel.setText(i18n.tr("Saving"));
			busyIconTimer.start();
			break;
		case COMPILATION_STARTED:
			statusMessageLabel.setText(i18n.tr("Compiling"));
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
			statusAnimationLabel.setIcon(ResourcesCache.getIcon("img/busyicon/idle.png"));
			break;
		case EXECUTION_STARTED:
			game.statusRootSet(i18n.tr("Running "));
			busyIconTimer.start();
			break;
		case DEMO_STARTED:
			game.statusRootSet(i18n.tr("Playing demo "));
			busyIconTimer.start();
			break;		
		case LOADING:
			game.statusRootSet(i18n.tr("Loading "));
			busyIconTimer.start();
			break;
		default:
			statusMessageLabel.setText("");
			statusAnimationLabel.setIcon(ResourcesCache.getIcon("img/busyicon/idle.png"));
		}
	}

	@Override
	public void stateChanged(final String txt) {
		statusMessageLabel.setText(txt);
		//RepaintManager.currentManager(statusMessageLabel).markCompletelyDirty(statusMessageLabel);
		//RepaintManager.currentManager(statusMessageLabel).paintDirtyRegions();
	}

	public void setupLanguages(Lecture lecture) {
		popup.removeAll();
		Game g = Game.getInstance();
		if (lecture instanceof Exercise) {
			Exercise exo = (Exercise) lecture;
			for (ProgrammingLanguage pl : exo.getProgLanguages()) {
				JMenuItem item = new JMenuItem(pl.getIcon());
			    item.addActionListener(new SetProgLanguage(g,pl));
				popup.add(item);
			}
		}
	}
	@Override
	public void currentProgrammingLanguageHasChanged(ProgrammingLanguage newLang) {
		progLangLabel.setIcon(newLang.getIcon());
	}
	@Override
	public void currentExerciseHasChanged(Lecture lecture) {
		setupLanguages(lecture);
	}
	@Override
	public void currentLessonHasChanged() {
		setupLanguages(Game.getInstance().getCurrentLesson().getCurrentExercise());
	}
	@Override
	public void selectedWorldHasChanged(World newWorld) { /* don't care */ }
	@Override
	public void selectedEntityHasChanged() { /* tell your mum */ }
	@Override
	public void selectedWorldWasUpdated() { /* go away */ }
}
