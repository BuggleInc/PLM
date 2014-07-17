package plm.core.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import plm.core.model.Game;

public class AddUser extends AbstractGameAction {

	private static final long serialVersionUID = 2522064526968742663L;
	private Component parent;

	public AddUser(Game game, String text, ImageIcon icon, Component parent) {
		super(game, text, icon);
		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String newUser = JOptionPane.showInputDialog(this.parent, Game.i18n.tr("<html>Enter a name for your new user.<br/>You'll have to user the \"Switch user\" menu to use your new user.</html>"), Game.i18n.tr("Add user"), JOptionPane.PLAIN_MESSAGE);

		if (newUser == null) {
			return;
		}

		game.getUsers().addUser(newUser);
		JOptionPane.showMessageDialog(this.parent, Game.i18n.tr("Your new user has been added successfully!"));
	}

}
