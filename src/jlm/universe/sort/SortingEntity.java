package jlm.universe.sort;

import jlm.universe.Entity;
import jlm.universe.World;

public class SortingEntity extends Entity {

	/**
	 * Must exist too. Calling SortingEntity("dummy name") is ok
	 * Part of the copy process 
	 * Must call super(name)
	 * @return A new instance of SortingEntity
	 */
	public SortingEntity() {
		super("Sorting Entity");
	}

	/**
	 * Part of the copy process 
	 * Must call super(name)
	 * @param name : the name of the entity
	 * @return A new instance of SortingEntity
	 */
	public SortingEntity(String name) {
		super(name);
	}
	
	/** 
	 * Instantiation Constructor (used by exercises to setup the world) 
	 * Must call super(name, world). If you had fields to setup, you'd  have to add more parameters
	 * @param name : the name of the entity
	 * @param world : a world
	 * @return A new instance of SortingEntity
	 */
	public SortingEntity(String name, World world) {
		super(name,world);
	}

	/** 
	 * A copy method needed by the JLM
	 * @return a new SortingEntity with the same name as the caller
	 */
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
