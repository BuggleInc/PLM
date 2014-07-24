package plm.core.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import plm.core.model.Game;
import plm.core.model.User;
import plm.core.model.UserAbortException;

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
		User chosenUser = (User) JOptionPane.showInputDialog(this.parent, 
				Game.i18n.tr("Please choose the user you want to use"), 
				Game.i18n.tr("Switch user"),
				JOptionPane.OK_CANCEL_OPTION, null, possibilities, possibilities[0]);

		if (chosenUser == null) 
			return;

		if (chosenUser.equals(game.getUsers().getCurrentUser())) {
			JOptionPane.showMessageDialog(parent, Game.i18n.tr("User {0} already selected.", chosenUser));			
		} else {
			try {
				game.getUsers().switchToUser(chosenUser);
				JOptionPane.showMessageDialog(parent, Game.i18n.tr("User {0} now selected.", chosenUser));			
			} catch (UserAbortException e1) {
				System.out.println(Game.i18n.tr("Operation canceled by user request"));
				JOptionPane.showMessageDialog(parent, Game.i18n.tr("Operation canceled as requested by user."));
			}
		}
	}

}
