package plm.core.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import plm.core.model.Game;
import plm.core.model.User;

public class RemoveUser extends AbstractGameAction {

	private static final long serialVersionUID = 2522064526968742663L;
	private Component parent;

	public RemoveUser(Game game, String text, ImageIcon icon, Component parent) {
		super(game, text, icon);
		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		List<User> usersList = game.getUsers().getUsersList();
		User[] possibilities = usersList.toArray(new User[usersList.size()]);
		User chosenUser = (User) JOptionPane.showInputDialog(this.parent, Game.i18n.tr("<html>Please choose the user you want to remove from the drop-down menu.</html>"), Game.i18n.tr("Remove user"), JOptionPane.OK_CANCEL_OPTION, null, possibilities, possibilities[0]);

		if (chosenUser == null) {
			return;
		}

		if (!chosenUser.equals(game.getUsers().getCurrentUser())) {
			game.getUsers().removeUser(chosenUser);
		} else {
			JOptionPane.showMessageDialog(this.parent, Game.i18n.tr("Sorry, you can't delete the current user."), Game.i18n.tr("Try again"), JOptionPane.ERROR_MESSAGE);
		}
	}

}
