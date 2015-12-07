package plm.universe.bat;

import java.util.List;
import java.util.Vector;

import javax.script.ScriptEngine;

import org.xnap.commons.i18n.I18n;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.universe.World;

public class BatWorld extends World {
	public List<BatTest> tests = new Vector<BatTest>();
	
	public BatWorld(Game game, String funName) {
		super(game, funName);
		
		addEntity(new BatEntity());
	}
	public BatWorld(BatWorld w2) {
		super(w2);
		this.tests = new Vector<BatTest>();
		for (BatTest t:w2.tests) 
			tests.add(t.copy());
	}
	
	@Override
	public void reset(World w) {
		BatWorld anotherWorld = (BatWorld) w;
		this.tests = new Vector<BatTest>();
		for (BatTest t:anotherWorld.tests) 
			tests.add(t.copy());
		super.reset(anotherWorld);		
	}
	@Override
	public boolean equals(Object o){
		if (!(o instanceof BatWorld)) {
			return false;
		}
		BatWorld other = (BatWorld) o;
		if (other.tests.size() != tests.size()) {
			//getGame().getLogger().log("Amount of tests differ between worlds: "+tests.size()+" != "+other.tests.size());
			return false;
		}
		for (int i=0;i<tests.size();i++)
			if (!tests.get(i).equals(other.tests.get(i))) {
				//throw new RuntimeException("Test "+i+" differs: "+tests.get(i)+" != "+other.tests.get(i));
				return false;
			}
		return true;
	}
	
	/* So that the view can display them */
	public List<BatTest> getTests() {
		return tests;
	}

	/* World logic */
	public void addTest(boolean visible, Object...params) {
		tests.add(new BatTest(getGame(), getName(),visible, params));
	}
	@Override
	public void setupBindings(ProgrammingLanguage lang, ScriptEngine e) {
		if (lang == Game.PYTHON) {
			e.put("batTests", tests);
		}
	}
	@Override
	public String diffTo(World w, I18n i18n) {
		BatWorld other = (BatWorld) w;
		StringBuffer sb = new StringBuffer();
		boolean foundError = false;
		for (int i=0;i<tests.size();i++) {
			if (foundError && !tests.get(i).isVisible() && !getGame().isDebugEnabled()) 
				return sb.toString();
					
			if (!tests.get(i).equals(other.tests.get(i))) { 
				sb.append(other.tests.get(i).getName()+" returned "+other.tests.get(i).getResult()+
						                               " while "+         tests.get(i).getResult()+" were expected.\n");
				foundError = true;
			}
		}
		return sb.toString();
	}
}
