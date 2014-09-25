package plm.test.simple;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.swing.ImageIcon;

import plm.core.lang.ProgrammingLanguage;
import plm.universe.World;

public class SimpleWorld extends World {

	boolean pass = false;
	
	public SimpleWorld(String name) {
		super(name);
	}
	public SimpleWorld(String name, boolean pass) {
		super(name);
		this.pass = pass;
	}
	
	public SimpleWorld(SimpleWorld w) {
		super(w);
		this.pass = w.pass;
	}
	
	public SimpleWorld copy(SimpleWorld w) {
		SimpleWorld res = new SimpleWorld(w);
		return res;
	}
	
	@Override
	public ImageIcon getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setupBindings(ProgrammingLanguage lang, ScriptEngine engine)
			throws ScriptException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String diffTo(World world) {
		// TODO Auto-generated method stub
		return null;
	}

}
