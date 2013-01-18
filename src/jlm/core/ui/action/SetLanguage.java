package jlm.core.ui.action;

import java.awt.event.ActionEvent;

import jlm.core.model.Game;

public class SetLanguage extends AbstractGameAction {

	private static final long serialVersionUID = 5778501209753480269L;

	private String lang;

	public SetLanguage(Game game, String text, String lang) {
		super(game, text, null);
		this.lang = lang;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		game.setLocale(lang);
	}

}
