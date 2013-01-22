package jlm.universe.pancake.burned;

import jlm.core.model.ProgrammingLanguage;
import jlm.core.ui.WorldView;
import jlm.universe.World;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 * @version 1.2
 * @see PancakesStack
 */
public class PancakeWorld extends World {

	protected PancakesStack stack; // the stack of pancakes
	
	/**
	 * Constructor of the class WorldPancake
	 * @version 1.2
	 * @param name : the name of the world
	 * @param amountOfPancakes : the amount of pancake in the stack
	 * @param mixed : if the stack shall be mixed or not
	 * @return A new PancakeWorld
	 */
	public PancakeWorld(String name, int amountOfPancakes, boolean mixed) {
		super(name);
		setDelay(200); // Delay (in ms) in default animations
		this.stack =  PancakesStack.create(amountOfPancakes,mixed);
	}
	
	/**
	 * Constructor of the class WorldPancake
	 * @version 1.2
	 * @param world : a world
	 * @return A new PancakeWorld
	 */
	public PancakeWorld(PancakeWorld world) {
		super(world);
	}

	/**
	 * Make a textual description of the differences between the caller and world
	 * @version 1.2
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
	 * Indicates whether some other object is "equal to" this one. 
	 * @version 1.2
	 * @return If the two objects are equals
	 * @param Object o: the reference object with which to compare.
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
			sw = this.stack.equals(other.stack);
		}
		return sw;
	}
	
	/**
	 * Flip a certain amount of pancakes in the stack
	 * @version 1.2
	 * @param numberOfPancakes : the number of pancakes, beginning from the top of the stack, that you want to flip.
	 * @throws InvalidPancakeNumber : in case you ask to flip less than one or more 
	 * 									than the total amount of pancakes
	 */
	public void flip(int numberOfPancakes) throws InvalidPancakeNumber {
		this.stack.flip(numberOfPancakes);
	}
	
	/**
	 * Returns the script except that must be injected within the environment before running user code 
	 * It should pass all order to the java entity, which were injected independently  
	 * @return  the script except that must be injected within the environment before running user code 
	 * @version 1.2
	 * @param the programming language used
	 */
	@Override
	public String getBindings(ProgrammingLanguage lang) {
		throw new RuntimeException("No binding of PancakeWorld for "+lang);
	}
	
	/**
	 * Give the size of a specific pancake among others
	 * @version 1.2
	 * @param pancakeNumber : the number of the pancake, beginning from the top of the stack, that you want to get.
	 * @return The size of the expected pancake
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
			return this.stack.getPancake(pancakeNumber).getSize();
		}
	}

	/**
	 * Give the size of the stack of pancakes
	 * @version 1.2
	 * @return The number of pancakes in the stack
	 */
	public int getStackSize() {
		return this.stack.getSize();
	}
	
	/** 
	 * Return a component able of displaying the world
	 * @return a component able of displaying the world
	 * @version 1.2
	 */
	@Override
	public WorldView[] getView() {
		return new WorldView[] { new PancakeWorldView(this) } ;
	}

	/**
	 * Tell if a specific pancake, among others, is upside down
	 * @version 1.2
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
	 * Reset the state of the current world to the one passed in argument
	 * @version 1.2
	 * @param the world which must be the new start of your current world
	 */
	@Override
	public void reset(World world) {
		PancakeWorld other = (PancakeWorld) world;
		this.stack = other.stack.copy();
		super.reset(world);		
	}

	/**
	 * Sort the stack of pancakes correctly, according to the control freak pancake seller
	 * @version 1.2
	 * @throws InvalidPancakeNumber : in case you ask to flip less than one or more than the total amount of pancakes
	 */
	public void solve() throws InvalidPancakeNumber {
		this.stack.solve();
	}
	
	/**
	 * Getter for the stack of pancakes
	 * @version 1.2
	 * @return return the stack of pancakes
	 */
	protected PancakesStack getStack() {
		return this.stack;
	}
	
	/**
	 * Returns a string representation of the world.
	 * @version 1.2
	 * @return A string representation of the world.
	 */
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("PancakeWorld "+getName()+": ");
		sb.append(this.stack.toString());
		return sb.toString();
	}

	/**
	 * Tell if the stack of pancakes is correctly sorted according to the control freak pancake seller
	 * @version 1.2
	 * @return TRUE if the stack is okay <br>FALSE else
	 */
	public boolean isSorted() {
		return this.stack.isSorted();
	}

}
