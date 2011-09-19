package jlm.core.ui.action;

import java.awt.event.ActionEvent;

import jlm.core.ProgLangChangesListener;
import jlm.core.model.Game;

public class SetProgLanguage extends AbstractGameAction implements ProgLangChangesListener {

	private static final long serialVersionUID = 5778501209753480269L;

	private String lang;

	public SetProgLanguage(Game game, String lang) {
		super(game, lang, null);
		game.addProgLangListener(this);
		this.lang = lang;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		game.setProgramingLanguage(lang);
	}

	@Override
	public void currentProgrammingLanguageHasChanged(String newLang) {
		setEnabled( newLang.equals(lang) );
	}
}
