package plm.core.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import plm.core.model.Game;

public class LinkUser extends AbstractGameAction {

	private Component parent;

	public LinkUser(Game game, String text, ImageIcon icon, Component parent) {
		super(game, text, icon);
		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			String userUUID = String.valueOf(game.getUsers().getCurrentUser().getUserUUID());
			String userBranch = "";
			try {
				userBranch = sha1(userUUID);
			} catch (NoSuchAlgorithmException ex) {
				//System.err.println(ex.getMessage());
			}
			String url = Game.getProperty("plm.play.server.url") + "link?hashUUID=" + userBranch;
			java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
		} catch (java.io.IOException ex) {
			//System.err.println(ex.getMessage());
		}
	}

	// Helper methods
	private String sha1(String input) throws NoSuchAlgorithmException {
		MessageDigest mDigest = MessageDigest.getInstance("SHA1");
		byte[] result = mDigest.digest(input.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString();
	}
}
