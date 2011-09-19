package jlm.universe.bat;

import java.util.List;
import java.util.Vector;

import jlm.core.ui.WorldView;
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
	public String getBindings(String lang) {
		throw new RuntimeException("No binding of BatWorld for "+lang);
	}
}
