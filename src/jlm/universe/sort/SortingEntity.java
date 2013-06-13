package jlm.universe.sort;

import jlm.universe.Entity;
import jlm.universe.World;

public class SortingEntity extends Entity {

	public SortingEntity() {
		super("Sorting Entity");
	}

	/** Part of the copy process; Must call super(name) */
	public SortingEntity(String name) {
		super(name);
	}
	
	/** Instantiation Constructor (used by exercises to setup the world) */
	public SortingEntity(String name, World world) {
		super(name,world);
	}

	/** A copy constructor needed by the JLM */
	public Entity copy() {
		return new SortingEntity(this.name);
	}

	public void copy(int from,int to) {
		((SortingWorld) this.world).copy(from,to);
		stepUI();
	}

	public int getValue(int i) {
		return ((SortingWorld) this.world).getValue(i);
	}
	
	public int getValueCount() {
		return ((SortingWorld) this.world).getValueCount();
	}
	
	public boolean isSmaller(int i, int j) {
		return ((SortingWorld) this.world).isSmaller(i,j);
	}
	
	public boolean isSmallerThan(int i, int val) {
		return ((SortingWorld) this.world).isSmallerThan(i,val);
	}
	
	public void run() {
		// Child implement this
	}
	
	public void setValue(int i,int val) {
		((SortingWorld) this.world).setValue(i,val);
		stepUI();
	}
	
	public void swap(int i, int j) {
		((SortingWorld) this.world).swap(i,j);
		stepUI();
	}

}
