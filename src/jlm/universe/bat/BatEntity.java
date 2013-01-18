package jlm.universe.bat;

import jlm.universe.Entity;
import jlm.universe.World;

public class BatEntity extends Entity {
	
	public BatEntity() {
		super();
	}
	
	public BatEntity(String name, World w) {
		super(name,w);
	}
	
	public BatEntity(BatEntity other) {
		super();
		copy(other);
	}

	@Override
	public Entity copy() {
		return new BatEntity(this);
	}
	
	
	@Override
	public void copy(Entity o) {
		super.copy(o);
	}
	

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof BatEntity)) {
			return false;
		}
		return (super.equals(o));
	}
	
	@Override
	public void run() {
		for (BatTest t:((BatWorld) world).getTests())
			try {
				run(t);
			} catch (Exception e) {
				t.setResult("this test raised an exception: "+e.getMessage());
			}
	}

	protected void run(BatTest t) {
		// To be overriden by child classes
	}
	
}
