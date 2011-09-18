package jlm.core.ui.action;

import java.awt.event.ActionEvent;

import jlm.core.model.Game;

public class SetProgLanguage extends AbstractGameAction {

	private static final long serialVersionUID = 5778501209753480269L;

	private String lang;

	public SetProgLanguage(Game game, String lang) {
		super(game, lang, null);
		this.lang = lang;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		game.setProgramingLanguage(lang);
	}
}
