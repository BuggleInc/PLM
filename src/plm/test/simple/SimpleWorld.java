package plm.test.simple;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.xnap.commons.i18n.I18n;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.universe.World;

public class SimpleWorld extends World {

	private boolean objectif = false;
	
	public SimpleWorld(Game game, String name) {
		super(game, name);
	}
	public SimpleWorld(Game game, String name, boolean objectif) {
		super(game, name);
		this.objectif = objectif;
	}
	
	public SimpleWorld(SimpleWorld w) {
		super(w);
		this.objectif = w.objectif;
	}
	
	@Override
	public void setupBindings(ProgrammingLanguage lang, ScriptEngine engine)
			throws ScriptException {
		if (lang.equals(Game.PYTHON)) {
			engine.put("w", this);
		}
	}

	@Override
	public boolean equals(Object o){
		if (!(o instanceof SimpleWorld)) {
			return false;
		}
		SimpleWorld other = (SimpleWorld) o;
		if(this.objectif != other.objectif) {
			return false;
		}
		return true;
	}
	
	@Override
	public String diffTo(World world, I18n i18n) {
		SimpleWorld other = (SimpleWorld) world;
		String s = "No diff";
		if(this.objectif != other.objectif) {
			s = "Returned "+other.objectif+" while "+objectif+" was expected...";
		}
		return s;
	}

	public void setObjectif(boolean val) {
		objectif = val;
	}
	
	public boolean getObjectif() {
		return objectif;
	}
	
}
