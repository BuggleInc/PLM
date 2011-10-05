package jlm.universe.array;

import java.util.Arrays;

import jlm.core.model.ProgrammingLanguage;
import jlm.core.ui.WorldView;
import jlm.universe.World;

public class ArrayWorld extends World {

	protected int[] values;

	public ArrayWorld(String name) {
		super(name);
	}

	public ArrayWorld(String name, int size) {
		super(name);
		setDelay(50);
		this.values = new int[size];
	}	
	
	public ArrayWorld(ArrayWorld anotherWorld) {
		super(anotherWorld);
		//reset(anotherWorld);
	}

	@Override
	public void reset(World w) {
		ArrayWorld anotherWorld = (ArrayWorld) w;
		this.values = new int[anotherWorld.values.length];
		for (int i=0; i<anotherWorld.values.length; i++ ) {
			this.values[i] = anotherWorld.values[i];
		}
		super.reset(anotherWorld);		
	}
	
	@Override
	public WorldView[] getView() {
		return new WorldView[] { new ArrayWorldView(this) };
	}
	
	@Override
	public boolean equals(Object o) {
		if (! (o instanceof ArrayWorld)) {
			return false;
		}
		ArrayWorld anotherWorld = (ArrayWorld) o;
		return Arrays.equals(this.values, anotherWorld.values) && super.equals(o);
	}
	
	public int[] getValues() {
		return this.values;
	}
	
	public void setValues(int[] newValues) {
		this.values = newValues;
	}
	@Override
	public String getBindings(ProgrammingLanguage lang) {
		throw new RuntimeException("No binding of ArrayWorld for "+lang);
	}

	@Override
	public String diffTo(World wrong) {
		return null;
	}
	
}
