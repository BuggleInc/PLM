package jlm.ui;

import javax.swing.JEditorPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.StyleSheet;

import jlm.core.Game;
import jlm.event.GameListener;

public class MissionPanel extends JEditorPane implements GameListener {

	private static final long serialVersionUID = 3891198543319748064L;
	private Game game;

	public MissionPanel(Game game) {
		super("text/html", "");
		setEditable(false);
		this.setEditorKit(new JlmHtmlEditorKit());

		// setPreferredSize(new Dimension(600,600));

		// load default css
		StyleSheet styles = ((HTMLDocument) getDocument()).getStyleSheet();
		try {
			styles.importStyleSheet(getClass().getResource("/lessons/screen.css"));			
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.game = game;
		this.game.addGameListener(this);
		currentExerciseHasChanged();
	}

	@Override
	public void currentExerciseHasChanged() {
		setText(this.game.getCurrentLesson().getCurrentExercise().getMission());
		setCaretPosition(0);
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
		// don't care
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

