package bugglequest.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import bugglequest.core.Game;
import bugglequest.ui.AboutDialog;

public class About extends AbstractGameAction {

	private static final long serialVersionUID = -8041256493299950795L;

	public About(Game game, String text, ImageIcon icon) {
		super(game, text, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		AboutDialog.getInstance().setVisible(true);
	}
}
