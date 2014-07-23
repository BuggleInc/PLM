package plm.core.ui.action;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import plm.core.model.Game;
import plm.core.ui.AddExistingUserDialog;

public class AddUserWithUUID extends AbstractGameAction {

	private static final long serialVersionUID = 1L;
	private Component parent;

	public AddUserWithUUID(Game game, String text, ImageIcon icon, Component parent) {
		super(game, text, icon);
		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		AddExistingUserDialog dialog = new AddExistingUserDialog((Frame) this.parent, true);
		String[] newUser = dialog.showDialog();

		if (newUser[0] == null || newUser[0].equals("")) {
			return;
		}

		try {
			int usersCount = game.getUsers().getUsersList().size();
			game.getUsers().addUserWithUUID(newUser[0], newUser[1]);
			if (usersCount == game.getUsers().getUsersList().size()) {
				JOptionPane.showMessageDialog(this.parent, Game.i18n.tr("Your uuid is already used by a local user."),
						Game.i18n.tr("Error"), JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this.parent, Game.i18n.tr("Your new user has been added successfully!"));
			}
		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this.parent, Game.i18n.tr("Your uuid is malformed."),
					Game.i18n.tr("Error"), JOptionPane.ERROR_MESSAGE);

		}
	}

}
