package jlm.universe.smn.pancake.raw;

import jlm.universe.Entity;
import jlm.universe.World;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 * @see PancakeWorld
 * @see Entity
 */
public class PancakeEntity extends Entity {

	/**
	 * Must exist too. Calling PancakeEntity("dummy name") is ok
	 * Part of the copy process 
	 * Must call super(name)
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
	 * @param name : the name of the entity
	 * @return A new instance of PancakeEntity
	 */
	public PancakeEntity(String name) {
		super(name);
	}
	
	/** 
	 * Instantiation Constructor (used by exercises to setup the world) 
	 * Must call super(name, world). If you had fields to setup, you'd  have to add more parameters
	 * @param name : the name of the entity
	 * @param world : a world
	 * @return A new instance of PancakeEntity
	 */
	public PancakeEntity(String name, World world) {
		super(name,world);
	}

	/** 
	 * A copy method needed by the JLM
	 * @return a new PancakeEntity with the same name as the caller
	 */
	@Override
	public Entity copy() {
		return new PancakeEntity(this.name);
	}

	/**
	 * Flip a certain amount of pancakes in the stack
	 * @param numberOfPancakes : the number of pancakes, 
	 * 			beginning from the top of the stack, that you want to flip.
	 * @throws InvalidPancakeNumber : in case you ask to flip less than one or 
	 * 									more than the total amount of pancakes
	 */
	public void flip(int numberOfPancakes) throws InvalidPancakeNumber {
		((PancakeWorld) world).flip(numberOfPancakes);
		stepUI();
	}
	
	/**
	 * Give the radius of a specific pancake among others
	 * @param pancakeNumber : the number of the pancake, beginning from the top of the stack, that you want to get.
	 * @return The size of the expected pancake
	 * @throws InvalidPancakeNumber : in case you ask an invalid pancake number
	 */
	public int getPancakeRadius(int pancakeNumber) throws InvalidPancakeNumber{
		return ((PancakeWorld) world).getPancakeSize(pancakeNumber);
	}

	/**
	 * Give the size of the stack of pancakes
	 * @return The number of pancakes in the stack
	 */
	public int getStackSize() {
		return ((PancakeWorld) world).getStackSize();
	}
	
	/**
	 * Tell if the stack of pancakes is correctly sorted according to the control freak pancake seller
	 * @return TRUE if the stack is okay <br>FALSE else
	 */
	public boolean isSorted() {
		return ((PancakeWorld) world).isSorted();
	}
	
	/** Must exist so that exercises can instantiate the entity (Entity is abstract) 
	 * @throws InvalidPancakeNumber : in case you ask to flip less than one or more than the total amount of pancakes
	 */
	@Override
	public void run() throws InvalidPancakeNumber {
	}
	
	/**
	 * Return a string representation of the world
	 * @return A string representation of the world
	 */
	public String toString(){
		return "PancakeEntity (" + this.getClass().getName() + ")";
	}
}
