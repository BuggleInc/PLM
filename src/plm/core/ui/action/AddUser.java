package plm.core.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

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
		JTextField username = new JTextField();
		JTextField uuid = new JTextField(UUID.randomUUID().toString());
		uuid.setToolTipText(Game.i18n.tr("If you already have a UUID, paste it here. If not, you can use that randomly generated identifyier"));
		Object[] message = {
		    Game.i18n.tr("Username:"), username,
		    Game.i18n.tr("Secret UUID:"), uuid
		};

		int option = JOptionPane.showConfirmDialog(null, message, Game.i18n.tr("Add user"), JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION) {
			try {
				game.getUsers().addUser(username.getText(),uuid.getText());
				JOptionPane.showMessageDialog(this.parent, 
						Game.i18n.tr("Your new user has been added successfully!\nYou can now switch to that user"));
			} catch (IllegalArgumentException ex) {
				JOptionPane.showMessageDialog(this.parent, 
						Game.i18n.tr("Invalid UUID! Either paste a correct identifyer or use the randomly generated one"));				
			}
		}
	}

}
