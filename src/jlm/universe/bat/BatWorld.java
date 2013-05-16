package jlm.universe.bat;

import java.util.List;
import java.util.Vector;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;
import jlm.core.ui.WorldView;
import jlm.universe.Entity;
import jlm.universe.World;

public class BatWorld extends World {
	public List<BatTest> tests = new Vector<BatTest>();
	
	public BatWorld(String funName) {
		super(funName);
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
			//System.out.println("Amount of tests differ between worlds: "+tests.size()+" != "+other.tests.size());
			return false;
		}
		for (int i=0;i<tests.size();i++)
			if (!tests.get(i).equals(other.tests.get(i))) {
				//System.out.println("Test "+i+" differs: "+tests.get(i)+" != "+other.tests.get(i));
				return false;
			}
		return true;
	}
	@Override
	public WorldView[] getView() {
		return new WorldView[] { new BatWorldView(this) };
	}
	
	/* So that the view can display them */
	protected List<BatTest> getTests() {
		return tests;
	}

	/* World logic */
	public void addTest(boolean visible, Object...params) {
		tests.add(new BatTest(getName(),visible, params));
	}
	@Override
	public void setupBindings(ProgrammingLanguage lang, ScriptEngine e) {
		/* No need of any binding for this world */
	}
	@Override
	public String diffTo(World w) {
		BatWorld other = (BatWorld) w;
		StringBuffer sb = new StringBuffer();
		boolean foundError = false;
		for (int i=0;i<tests.size();i++) {
			if (foundError && !tests.get(i).isVisible()) 
				return sb.toString();
					
			if (!tests.get(i).equals(other.tests.get(i))) { 
				sb.append(other.tests.get(i).getName()+" returned "+other.tests.get(i).getResult()+ " while "+tests.get(i).getResult()+" were expected."+"\n");
				foundError = true;
			}
		}
		return sb.toString();
	}
	@Override 
	public void runEntity(Entity ent) {
		ProgrammingLanguage pl = Game.getProgrammingLanguage();
		if (pl.equals(Game.JAVA)) {
			super.runEntity(ent);
		} else if (pl.equals(Game.PYTHON)) {
			ScriptEngine engine ;

			ScriptEngineManager manager = new ScriptEngineManager();       
			engine = manager.getEngineByName("python");
			if (engine==null) 
				throw new RuntimeException("Failed to start an interpreter for python");
			
			try {
				engine.eval(
						"import java.lang.System.err\n"+
						"def log(a):\n"+
						"  java.lang.System.err.print(\"%s: %s\" %(entity.getName(),a))\n");
				engine.eval(ent.getScript(Game.PYTHON));
			} catch (ScriptException e1) {
				e1.printStackTrace();
			}									

			for (BatTest t:((BatWorld) ent.getWorld()).getTests())
				try {
					engine.put("thetest",t);
					
					engine.eval("thetest.setResult("+t.getName()+")");
				} catch (Exception e) {
					t.setResult("this test raised an exception: "+e.getMessage());
				}

		}
	}
}
