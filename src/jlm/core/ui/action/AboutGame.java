package jlm.core.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import jlm.core.model.Game;
import jlm.core.ui.AboutLessonDialog;
import jlm.core.ui.AboutWorldDialog;
import jlm.core.ui.AbstractAboutDialog;
import jlm.core.ui.MainFrame;

public class AboutGame extends AbstractGameAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AbstractAboutDialog dialog = null;
	private boolean world = false;

	public AboutGame(Game game, String text, boolean bool) {
		super(game, text);
		world=bool;
	}

	public AboutGame(Game game, String text, ImageIcon icon, boolean bool) {
		super(game, text, icon);
		world=bool;
	}

	public AboutGame(Game game, String text, ImageIcon icon,
			String descEnabled, String descDisabled) {
		super(game, text, icon, descEnabled, descDisabled);
		// TODO Auto-generated constructor stub
	}

	public AboutGame(Game game, String text, ImageIcon icon,
			String descEnabled, String descDisabled, Integer mnemonic) {
		super(game, text, icon, descEnabled, descDisabled, mnemonic);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (this.dialog == null){
			if(!world)
				this.dialog = new AboutLessonDialog(MainFrame.getInstance());
			else
				this.dialog = new AboutWorldDialog(MainFrame.getInstance());
		}

		this.dialog.setVisible(true);
	}

	public AbstractAboutDialog getDialog() {
		return dialog;
	}

	public void setDialog(AbstractAboutDialog dialog) {
		this.dialog = dialog;
	}

	
}
