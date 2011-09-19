package jlm.core.ui.action;

import java.awt.event.ActionEvent;

import jlm.core.ProgLangChangesListener;
import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;

public class SetProgLanguage extends AbstractGameAction implements ProgLangChangesListener {

	private static final long serialVersionUID = 5778501209753480269L;

	private ProgrammingLanguage lang;

	public SetProgLanguage(Game game, ProgrammingLanguage lang) {
		super(game, lang.toString(), null);
		game.addProgLangListener(this);
		this.lang = lang;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		game.setProgramingLanguage(lang);
	}

	@Override
	public void currentProgrammingLanguageHasChanged(ProgrammingLanguage newLang) {
		setEnabled( newLang.equals(lang) );
	}
}
