package plm.core.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lecture;
import plm.core.model.session.SourceFile;
import plm.core.model.session.SourceFileRevertable;

public class RevertExercise extends AbstractGameAction {

	private static final long serialVersionUID = -1509545929438458599L;

	public RevertExercise(Game game, String text, ImageIcon icon) {
		super(game, text, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object[] options = { Game.i18n.tr("OK"), Game.i18n.tr("Cancel") };
		int choice = 
			JOptionPane.showOptionDialog(null, 
					Game.i18n.tr("Reverting this exercise will erase all your work and cannot be undone.\n Are you sure that you want to proceed?"), 
					Game.i18n.tr("Warning"),
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
				null, options, options[0]);
		if (choice != 0) {
			System.out.println(Game.i18n.tr("Revert canceled on user request -- your work was preserved."));
			return;
		}

		Lecture lect = game.getCurrentLesson().getCurrentExercise();
		if (! (lect instanceof Exercise)) 
			return;

		Exercise ex = (Exercise) lect;
		for (ProgrammingLanguage lang: ex.getProgLanguages())
			for (int i=0; i<ex.getSourceFileCount(lang); i++) {
				SourceFile sf = ex.getSourceFile(lang,i);
				if (sf instanceof SourceFileRevertable)
					((SourceFileRevertable) sf).revert();
			}
		for (ProgrammingLanguage pl:Game.programmingLanguages) 
			Game.getInstance().studentWork.setPassed(ex, pl, false);
		
		System.out.println(Game.i18n.tr("Exercise reverted"));
	}

}
