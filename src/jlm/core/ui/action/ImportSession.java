package jlm.core.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import jlm.core.model.Game;
import jlm.core.model.ZipSessionKit;

public class ImportSession extends AbstractGameAction {

	private static final long serialVersionUID = 5778501209753480269L;

	private Component parent;

	public ImportSession(Game game, String text, ImageIcon icon, Component parent) {
		super(game, text, icon);
		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnValue = fc.showOpenDialog(this.parent);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File sessionFileToImportFrom = fc.getSelectedFile();
			
			ZipSessionKit kit = new ZipSessionKit(this.game);
			kit.loadAll(sessionFileToImportFrom);
		}
	}

}
