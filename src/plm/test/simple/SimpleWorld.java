package plm.test.simple;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import plm.core.lang.LangPython;
import plm.core.lang.ProgrammingLanguage;
import plm.universe.World;

public class SimpleWorld extends World {

	private boolean objectif = false;
	
	public SimpleWorld(String name) {
		super(name);
	}
	public SimpleWorld(String name, boolean objectif) {
		super(name);
		this.objectif = objectif;
	}
	
	public SimpleWorld(SimpleWorld w) {
		super(w);
		this.objectif = w.objectif;
	}
	
	@Override
	public void setupBindings(ProgrammingLanguage lang, ScriptEngine engine)
			throws ScriptException {
		if (lang instanceof LangPython) {
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
	public String diffTo(World world) {
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
