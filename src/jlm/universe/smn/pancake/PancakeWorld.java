package jlm.universe.smn.pancake;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;
import jlm.core.ui.WorldView;
import jlm.universe.EntityControlPanel;
import jlm.universe.World;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 * @see PancakesStack
 * @see World
 */
public class PancakeWorld extends World {

	private PancakesStack stack; // the stack of pancakes
	private int lastModifiedPancake;
	private boolean burnedWorld ;
	
	/**
	 * Constructor of the class PancakeWorld
	 * @param world : a world
	 * @return A new PancakeWorld
	 */
	public PancakeWorld(PancakeWorld world) {
		super(world);
	}
	
	/**
	 * Constructor of the class PancakeWorld
	 * @param name : the name of the world
	 * @param amountOfPancakes : the amount of pancakes in the stack
	 * @param burnedPancake : if we take care of the fact that the pancake is burned on one side
	 * @return A new PancakeWorld
	 */
	public PancakeWorld(String name, int amountOfPancakes, boolean burnedPancake) {
		super(name);
		setDelay(200); // Delay (in ms) in default animations
		this.stack =  PancakesStack.create(amountOfPancakes,true);
		this.lastModifiedPancake = 0 ;
		this.burnedWorld = burnedPancake;
	}
	
	/**
	 * Make a textual description of the differences between the caller and world
	 * @param world : the world with which you want to compare your world
	 * @return A textual description of the differences between the caller and world
	 */
	@Override
	public String diffTo(World world) {
		String s ;
		if (world == null || !(world instanceof PancakeWorld))
		{
			s="This is not a world of pancakes ='(...";
		}
		else
		{
			PancakeWorld other = (PancakeWorld) world;
			s=this.stack.diffTo(other.stack);
		}
		return s;
	}

	/**
	 * Indicate whether some other object is "equal to" this one
	 * @return If the two objects are equal
	 * @param Object o: the reference object with which to compare
	 */
	public boolean equals(Object o) {
		boolean sw = true;
		if (o == null || !(o instanceof PancakeWorld))
		{
			sw=false;
		}
		else
		{
			PancakeWorld other = (PancakeWorld) o;
			sw = this.burnedWorld == other.burnedWorld
				&& this.stack.equals(other.stack,this.burnedWorld);
		}
		return sw;
	}
	
	/**
	 * Flip a certain amount of pancakes in the stack
	 * @param numberOfPancakes : the number of pancakes, beginning from the top of the stack, that you want to flip.
	 * @throws InvalidPancakeNumber : in case you ask to flip less than one or more 
	 * 									than the total amount of pancakes
	 */
	public void flip(int numberOfPancakes) throws InvalidPancakeNumber {
		this.stack.flip(numberOfPancakes);
		this.lastModifiedPancake = numberOfPancakes ;
	}
	
	/**
	 * Return the panel which let the user to interact dynamically with the world
	 */
	@Override
	public EntityControlPanel getEntityControlPanel() {
		return new PancakeFlipButtonPanel();
	}
	
	/**
	 * Give the index of the last modified pancakes
	 * @return pancakeNumber : the index of the pancake, beginning from the top of the stack, that was modified.
	 */
	public int getLastModifiedPancake() {
		return lastModifiedPancake;
	}
	
	/**
	 * Give the size of a specific pancake among others
	 * @param pancakeNumber : the number of the pancake, beginning from the top of the stack, that you want to get.
	 * @return The radius of the expected pancake
	 * @throws InvalidPancakeNumber : in case you ask an invalid pancake number
	 */
	public int getPancakeSize(int pancakeNumber) throws InvalidPancakeNumber{
		if ( pancakeNumber < 0 || pancakeNumber >= this.stack.getSize())
		{
			throw new InvalidPancakeNumber(
					"The pancake's number you've asked is not between 0 and the stack size -1"
					);
		}
		else
		{
			return this.stack.getPancake(pancakeNumber).getRadius();
		}
	}
	
	/**
	 * Getter for the stack of pancakes
	 * @return return the stack of pancakes
	 */
	protected PancakesStack getStack() {
		return this.stack;
	}

	/**
	 * Give the size of the stack of pancakes
	 * @return The number of pancakes in the stack
	 */
	public int getStackSize() {
		return this.stack.getSize();
	}
	
	/** 
	 * Return a component able of displaying the world
	 * @return a component able of displaying the world
	 */
	@Override
	public WorldView[] getView() {
		return new WorldView[] { new PancakeWorldView(this) } ;
	}

	/**
	 * Tell the value of flipped which is used for graphic purpose only
	 * @return the flipped
	 */
	public boolean isFlipped() {
		return this.stack.isFlipped();
	}

	/**
	 * Tell if a specific pancake, among others, is upside down
	 * @param pancakeNumber : the number of the pancake, beginning from the top of the stack, that you want to get.
	 * @return If the specific pancake is upside down or not
	 * @throws InvalidPancakeNumber : in case you ask an invalid pancake number
	 */
	public boolean isPancakeUpsideDown(int pancakeNumber) throws InvalidPancakeNumber {
		if ( pancakeNumber < 0 || pancakeNumber >= this.stack.getSize())
		{
			throw new InvalidPancakeNumber(
					"The pancake's number you've asked is not between 0 and the stack size -1"
					);
		}
		else
		{
			return this.stack.getPancake(pancakeNumber).isUpsideDown();
		}
	}
	
	/**
	 * Tell if the stack of pancakes is correctly sorted according to the control freak pancake seller
	 * @return TRUE if the stack is okay <br>FALSE else
	 */
	public boolean isSorted() {
		return this.stack.isSorted(this.burnedWorld);
	}
	
	/** 
	 * Reset the state of the current world to the one passed in argument
	 * @param the world which must be the new start of your current world
	 */
	@Override
	public void reset(World world) {
		PancakeWorld other = (PancakeWorld) world;
		this.stack = other.stack.copy();
		this.burnedWorld = other.burnedWorld;
		this.lastModifiedPancake = other.lastModifiedPancake;
		super.reset(world);		
	}

	/**
	 * Return the script except that must be injected within the environment before running user code 
	 * It should pass all order to the java entity, which were injected independently  
	 * @return  the script except that must be injected within the environment before running user code 
	 * @param the programming language used
	 * @throws ScriptException 
	 */
	@Override
	public void setupBindings(ProgrammingLanguage lang, ScriptEngine e) throws ScriptException {
		if (lang.equals(Game.PYTHON)) {
			e.eval(
				"def getStackSize():\n" +
				"  return entity.getStackSize()\n" +
				"def getPancakeSize(pancakeNumber):\n" +
				"  return entity.getPancakeSize(pancakeNumber)\n" +
				"def isPancakeUpsideDown(pancakeNumber):\n"+
				"  return entity.isPancakeUpsideDown(pancakeNumber)\n" +
				"def flip(numberOfPancakes):\n" +
				"  entity.flip(numberOfPancakes)\n"	
				);
		} else {
			throw new RuntimeException("No binding of PancakeWorld for "+lang);
		}
	}

	/**
	 * Return a string representation of the world
	 * @return A string representation of the world
	 */
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("PancakeWorld "+getName()+": ");
		sb.append(this.stack.toString());
		return sb.toString();
	}

	/**
	 * tell if we pay attention to the burned side or not
	 * @return if we pay attention to the burned side or not
	 */
	public boolean isBurnedPancake() {
		return this.burnedWorld;
	}

}
