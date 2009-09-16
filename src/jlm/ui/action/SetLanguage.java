package jlm.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import jlm.core.Game;

public class SetLanguage extends AbstractGameAction {

	private static final long serialVersionUID = 5778501209753480269L;

	private String lang;

	public SetLanguage(Game game, String text, ImageIcon icon, Component parent, String lang) {
		super(game, text, icon);
		this.lang = lang;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Game.getInstance().setLocale(lang);
	}

}
