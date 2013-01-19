package jlm.universe.pancake;

import jlm.universe.Entity;
import jlm.universe.World;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 * @version 1.2
 */
public class PancakeEntity extends Entity {

	/**
	 * Must exist too. Calling PancakeEntity("dummy name") is ok
	 * Part of the copy process 
	 * Must call super(name)
	 * @version 1.2
	 * @return A new instance of PancakeEntity
	 */
	public PancakeEntity() {
		/* BEGIN HIDDEN */
		super("Pancake Entity");
		/* END HIDDEN */
	}

	/**
	 * Part of the copy process 
	 * Must call super(name)
	 * @version 1.2
	 * @param name : the name of the entity
	 * @return A new instance of PancakeEntity
	 */
	public PancakeEntity(String name) {
		super(name);
	}
	
	
	/** 
	 * Instantiation Constructor (used by exercises to setup the world) 
	 * Must call super(name, world). If you had fields to setup, you'd  have to add more parameters
	 * @version 1.2
	 * @param name : the name of the entity
	 * @param world : a world
	 * @return A new instance of PancakeEntity
	 */
	public PancakeEntity(String name, World world) {
		super(name,world);
	}

	/** 
	 * A copy method needed by the JLM
	 * @version 1.2
	 * @return a new PancakeEntity with the same name as the caller
	 */
	@Override
	public Entity copy() {
		return new PancakeEntity(this.name);
	}

	/**
	 * Flip a certain amount of pancakes in the stack
	 * @version 1.2
	 * @param numberOfPancakes : the number of pancakes, 
	 * 			beginning from the top of the stack, that you want to flip.
	 * @throws PancakeNumberException : in case you ask to flip less than one or 
	 * 									more than the total amount of pancakes
	 */
	public void flip(int numberOfPancakes) throws PancakeNumberException {
		((PancakeWorld) world).flip(numberOfPancakes);
		stepUI();
	}

	/**
	 * Give the size of a specific pancake among others
	 * @version 1.2
	 * @param pancakeNumber : the number of the pancake, beginning from the top of the stack, that you want to get.
	 * @return The size of the expected pancake
	 */
	public int getPancakeSize(int pancakeNumber){
		return ((PancakeWorld) world).getPancakeSize(pancakeNumber);
	}
	
	/**
	 * Give the size of the stack of pancakes
	 * @version 1.2
	 * @return The number of pancakes in the stack
	 */
	public int getStackSize() {
		return ((PancakeWorld) world).getStackSize();
	}
	
	/**
	 * Tell if a specific pancake, among others, is upside down
	 * @version 1.2
	 * @param pancakeNumber : the number of the pancake, beginning from the top of the stack, that you want to get.
	 * @return If the specific pancake is upside down or not
	 */
	public boolean isPancakeUpsideDown(int pancakeNumber) {
		return ((PancakeWorld) world).isPancakeUpsideDown(pancakeNumber);
	}
	
	/** Must exist so that exercises can instantiate the entity (Entity is abstract) 
	 * @version 1.2
	 * @throws PancakeNumberException : in case you ask to flip less than one or more than the total amount of pancakes
	 */
	@Override
	public void run() throws PancakeNumberException {
	}
	
	/**
	 * Returns a string representation of the world.
	 * @version 1.2
	 * @return A string representation of the world.
	 */
	public String toString(){
		return "PancakeEntity (" + this.getClass().getName() + ")";
	}
}
