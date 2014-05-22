package plm.core.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import plm.core.model.Game;
import plm.core.model.User;

public class SwitchUser extends AbstractGameAction {

	private static final long serialVersionUID = 2522064526968742663L;
	private Component parent;

	public SwitchUser(Game game, String text, ImageIcon icon, Component parent) {
		super(game, text, icon);
		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		List<User> usersList = game.getUsers().getUsersList();
		User[] possibilities = usersList.toArray(new User[usersList.size()]);
		// TODO: translate the text
		User chosenUser = (User) JOptionPane.showInputDialog(this.parent, "Please choose the user you want to use:\n", "Switch user", JOptionPane.OK_CANCEL_OPTION, null, possibilities, possibilities[0]);

		if (chosenUser == null) {
			return;
		}

		// TODO: exit the PLM after the change
		if (!chosenUser.equals(game.getUsers().getCurrentUser())) {
			game.getUsers().switchToUser(chosenUser);
		}
	}

}
