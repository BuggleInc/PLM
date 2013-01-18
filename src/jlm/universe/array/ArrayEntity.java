package jlm.universe.array;

import jlm.universe.Entity;
import jlm.universe.World;

public class ArrayEntity extends Entity {

	public int result = Integer.MIN_VALUE;
	
	public ArrayEntity() {
		super();
	}
	
	public ArrayEntity(String name, World w) {
		super(name,w);
	}
	
	public ArrayEntity(ArrayEntity other) {
		super();
		copy(other);
	}

	@Override
	public Entity copy() {
		return new ArrayEntity(this);
	}
	
	
	@Override
	public void copy(Entity o) {
		super.copy(o);
		ArrayEntity other = (ArrayEntity) o;
		this.result = other.result;
	}
	

	public int getResult() {
		return this.result;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ArrayEntity)) {
			return false;
		}
		ArrayEntity other = (ArrayEntity) o;
		return (this.result == other.result);
	}
	
	@Override
	public void run() {
		// Child implement this
	}


}
