package plm.core.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Locale;

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
import plm.core.HumanLangChangesListener;
import plm.core.ProgLangChangesListener;
import plm.core.StatusStateListener;
import plm.core.model.Game;
import plm.core.model.ProgrammingLanguage;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lecture;
import plm.core.ui.action.SetLanguage;
import plm.core.ui.action.SetProgLanguage;
import plm.universe.World;

public class StatusBar extends JPanel implements GameListener,GameStateListener,StatusStateListener,
	ProgLangChangesListener, HumanLangChangesListener {

	private static final long serialVersionUID = 8443305863958273495L;
	private Game game;
	private JLabel statusMessageLabel;
	private JLabel statusAnimationLabel;
	private JLabel progLangLabel;
	private JPopupMenu progLangPopup = new JPopupMenu();
	private JLabel humLangLabel;
	private JPopupMenu humLangPopup = new JPopupMenu();
	
	private int busyIconIndex = 0;

	private Timer busyIconTimer;

	public I18n i18n = I18nFactory.getI18n(getClass(),"org.plm.i18n.Messages",getLocale(), I18nFactory.FALLBACK);

	private Color defaultColor, highlightColor = new Color(255,120,120);
	
	public StatusBar(Game game) {
		super();
		this.game = game;
		this.game.addGameStateListener(this);
		game.addStatusStateListener(this);
		game.addProgLangListener(this);
		game.addHumanLangListener(this);
		game.addGameListener(this);
		initComponents();
		defaultColor = getBackground();
	}
	
	public void initComponents() {
		this.setBorder(BorderFactory.createEtchedBorder());

		// JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		statusMessageLabel = new JLabel("");
		statusAnimationLabel = new JLabel();
		statusAnimationLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusAnimationLabel.setIcon(ResourcesCache.getIcon("img/busyicon/idle.png"));
		
		setupProgLanguages(game.getCurrentLesson().getCurrentExercise());
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
                progLangPopup.show(e.getComponent(),
                        e.getX(), e.getY());
			}
			void maybeShowPopup(MouseEvent e) {
				if (e.isPopupTrigger())
					mouseClicked(e);
			}
		});

		humLangLabel = new JLabel();
		humLangLabel.setHorizontalAlignment(SwingConstants.LEFT);
		humLangLabel.setText(game.getLocale().getLanguage());
		humLangLabel.addMouseListener(new MouseListener() {			
			public void mouseReleased(MouseEvent e) {
				maybeShowPopup(e);
			}
			public void mousePressed(MouseEvent e) {
				maybeShowPopup(e);
			}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {
                humLangPopup.show(e.getComponent(),
                        e.getX(), e.getY());
			}
			void maybeShowPopup(MouseEvent e) {
				if (e.isPopupTrigger())
					mouseClicked(e);
			}
		});

		for (String[] lang : Game.humanLangs) {
			JMenuItem item = new JMenuItem(new SetLanguage(game, lang[0], new Locale(lang[1])));
			humLangPopup.add(item);
		}
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(Box.createHorizontalStrut(10));
		this.add(statusMessageLabel);
		this.add(Box.createHorizontalGlue());
		this.add(statusAnimationLabel);
		this.add(Box.createHorizontalStrut(20));
		this.add(humLangLabel);
		this.add(Box.createHorizontalStrut(5));
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
			setBackground(highlightColor);
			break;
		case COMPILATION_STARTED:
			statusMessageLabel.setText(i18n.tr("Compiling"));
			busyIconTimer.start();
			setBackground(highlightColor);
			break;
		case LOADING_DONE:
		case SAVING_DONE:
		case COMPILATION_ENDED:
		case DEMO_ENDED:
		case EXECUTION_ENDED:
			game.statusRootSet("");
			game.statusArgEmpty();
			busyIconTimer.stop();
			setBackground(defaultColor);
			statusAnimationLabel.setIcon(ResourcesCache.getIcon("img/busyicon/idle.png"));
			break;
		case EXECUTION_STARTED:
			game.statusRootSet(i18n.tr("Running "));
			busyIconTimer.start();
			setBackground(highlightColor);
			break;
		case DEMO_STARTED:
			game.statusRootSet(i18n.tr("Playing demo "));
			busyIconTimer.start();
			setBackground(highlightColor);
			break;		
		case LOADING:
			game.statusRootSet(i18n.tr("Loading "));
			busyIconTimer.start();
			setBackground(highlightColor);
			break;
		default:
			statusMessageLabel.setText("");
			statusAnimationLabel.setIcon(ResourcesCache.getIcon("img/busyicon/idle.png"));
			setBackground(defaultColor);
		}
	}

	@Override
	public void stateChanged(final String txt) {
		statusMessageLabel.setText(txt);
		//RepaintManager.currentManager(statusMessageLabel).markCompletelyDirty(statusMessageLabel);
		//RepaintManager.currentManager(statusMessageLabel).paintDirtyRegions();
	}

	public void setupProgLanguages(Lecture lecture) {
		progLangPopup.removeAll();
		Game g = Game.getInstance();
		if (lecture instanceof Exercise) {
			Exercise exo = (Exercise) lecture;
			for (ProgrammingLanguage pl : exo.getProgLanguages()) {
				JMenuItem item = new JMenuItem(pl.getIcon());
				item.setToolTipText(pl.getLang());
			    item.addActionListener(new SetProgLanguage(g,pl));
				progLangPopup.add(item);
			}
		}
	}
	@Override
	public void currentProgrammingLanguageHasChanged(ProgrammingLanguage newLang) {
		progLangLabel.setIcon(newLang.getIcon());
	}
	@Override
	public void currentExerciseHasChanged(Lecture lecture) {
		setupProgLanguages(lecture);
	}
	@Override
	public void currentLessonHasChanged() {
		setupProgLanguages(game.getCurrentLesson().getCurrentExercise());
	}
	@Override
	public void selectedWorldHasChanged(World newWorld) { /* don't care */ }
	@Override
	public void selectedEntityHasChanged() { /* tell your mum */ }
	@Override
	public void selectedWorldWasUpdated() { /* go away */ }

	@Override
	public void currentHumanLanguageHasChanged(Locale newLang) {
		humLangLabel.setText(newLang.getLanguage());
	}
}
