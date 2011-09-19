package jlm.core.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import jlm.core.model.Game;
import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.SourceFile;
import jlm.core.model.lesson.SourceFileRevertable;

public class RevertExercise extends AbstractGameAction {

	private static final long serialVersionUID = -1509545929438458599L;

	public RevertExercise(Game game, String text, ImageIcon icon) {
		super(game, text, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Exercise ex = game.getCurrentLesson().getCurrentExercise();
		for (String lang: ex.getProgLanguages())
			for (int i=0; i<ex.publicSourceFileCount(lang); i++) {
				SourceFile sf = ex.getPublicSourceFile(lang,i);
				if (sf instanceof SourceFileRevertable)
					((SourceFileRevertable) sf).revert();
			}
	}

}
