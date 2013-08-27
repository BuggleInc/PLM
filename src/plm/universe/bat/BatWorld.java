package plm.universe.bat;

import java.util.List;
import java.util.Vector;

import javax.script.ScriptEngine;
import javax.swing.ImageIcon;

import plm.core.model.Game;
import plm.core.model.ProgrammingLanguage;
import plm.core.ui.ResourcesCache;
import plm.core.ui.WorldView;
import plm.universe.World;

public class BatWorld extends World {
	public List<BatTest> tests = new Vector<BatTest>();
	
	public BatWorld(String funName) {
		super(funName);
		
		BatEntity e = new BatEntity();
		addEntity(e);
		e.setWorld(this);
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
				//throw new RuntimeException("Test "+i+" differs: "+tests.get(i)+" != "+other.tests.get(i));
				return false;
			}
		return true;
	}
	@Override
	public WorldView getView() {
		return new BatWorldView(this);
	}
	@Override
	public ImageIcon getIcon() {
		return ResourcesCache.getIcon("img/world_bat.png");
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
			if (foundError && !tests.get(i).isVisible() && !Game.getInstance().isDebugEnabled()) 
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
