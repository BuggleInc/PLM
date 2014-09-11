package plm.core.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import plm.core.model.Game;

public class LinkUser extends AbstractGameAction {

	private static final long serialVersionUID = 1L;
	public LinkUser(Game game, String text, ImageIcon icon, Component parent) {
		super(game, text, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			String userUUID = String.valueOf(game.getUsers().getCurrentUser().getUserUUID());
			String url = Game.getProperty("plm.play.server.url") + "link?UUID=" + userUUID;
			java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
		} catch (java.io.IOException ex) {
			//System.err.println(ex.getMessage());
		}
	}
}
