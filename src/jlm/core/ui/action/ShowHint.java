package jlm.core.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import jlm.core.model.Game;
import jlm.core.ui.AbstractAboutDialog;
import jlm.core.ui.MainFrame;

public class ShowHint extends AbstractGameAction {

	private static final long serialVersionUID = -1509545929438458599L;

	public ShowHint(Game game, String text, ImageIcon icon) {
		super(game, text, icon,"Show an hint","There is no more hint for this exercise");
	}

	private AbstractAboutDialog dialog = null;

	public void actionPerformed(ActionEvent arg0) {
		if (this.dialog == null) {
			this.dialog = new ShowHintDialog(MainFrame.getInstance());
		}
		this.dialog.setVisible(true);
	}
	class ShowHintDialog extends AbstractAboutDialog {
		protected ShowHintDialog(JFrame parent) {
			super(parent);
			currentExerciseHasChanged();
		}

		private static final long serialVersionUID = 1L;
		@Override
		public void currentExerciseHasChanged() {
			setTitle("Hint for the exercise "
					+ Game.getInstance().getCurrentLesson().getCurrentExercise().getName());
			String hint = Game.getInstance().getCurrentLesson().getCurrentExercise().hint;
			area.setText(hint!=null?hint:"There is no hint for this exercise, sorry.");
			area.setCaretPosition(0);
		}
		
	}
}
