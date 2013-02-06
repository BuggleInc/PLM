package jlm.universe.smn.baseball;

import jlm.core.model.ProgrammingLanguage;
import jlm.core.ui.WorldView;
import jlm.universe.World;

/**
 * @author Julien BASTIAN & Geoffrey HUMBERT
 * @see BaseballField
 */

public class BaseballWorld extends World
{
	protected BaseballField field; // the field of the game
	
	/**
	 * Constructor of the class BaseballWorld
	 * @param world : a world
	 * @return A new BaseballWorld
	 */
	public BaseballWorld(BaseballWorld world)
	{
		super(world);
	}
	
	/**
	 * Constructor of the class BaseballWorld
	 * @param name : the name of the world
	 * @param numberOfBases : the amount of bases in the field
	 * @return A new BaseballWorld
	 */
	public BaseballWorld(String name, int numberOfBases)
	{
		super(name);
		setDelay(200); // Delay (in ms) in default animations
		this.field = BaseballField.create(numberOfBases);
	}

	/**
	 * Make a textual description of the differences between the caller and world
	 * @param world : the world with which you want to compare your world
	 * @return A textual description of the differences between the caller and world
	 */
	public String diffTo(World world)
	{
		String s ;
		if (world == null || !(world instanceof BaseballWorld))
		{
			s="This is not a baseball world ='(...";
		}
		else
		{
			BaseballWorld other = (BaseballWorld) world;
			s = this.field.diffTo(other.field);
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
		if (o == null || !(o instanceof BaseballWorld))
		{
			sw=false;
		}
		else
		{
			BaseballWorld other = (BaseballWorld) o;
			sw = this.field.equals(other.field);
		}
		return sw;
	}
	
	/**
	 * Give the index of the hole and the index of the base with the hole
	 * @return  the index of the hole and the index of the base with the hole
	 */
	public int[] findMissingPlayer() {
		return this.field.findMissingPlayer();
	}
	
	/**
	 * Give the number of bases on your field
	 * @return the number of bases on your field
	 */
	public int getAmountOfBases()
	{
		return this.field.getAmountOfBases();
	}
	
	/**
	 * Return the script except that must be injected within the environment before running user code 
	 * It should pass all order to the java entity, which were injected independently  
	 * @return  the script except that must be injected within the environment before running user code 
	 * @param the programming language used
	 */
	public String getBindings(ProgrammingLanguage lang)
	{
		throw new RuntimeException("No binding of BaseballWorld for "+lang);
	}
	
	/** 
	 * Return a component able of displaying the world
	 * @return a component able of displaying the world
	 */
	public WorldView[] getView()
	{
		return new WorldView[] { new BaseballWorldView(this) } ;
	}
	
	/**
	 * Return the index of the base which have only one player on the field
	 * @return the index of the base which has only one player on the field
	 */
	public int indexOfBaseWithOnePlayer()
	{
		return this.field.indexOfBaseWithOnePlayer();
	}
	
	/**
	 * Tell if everyone is at home
	 * @param baseIndex the index of the base that we want to check
	 * @return TRUE if the field is okay <br>FALSE else
	 */
	public boolean isBaseSorted( int baseIndex)
	{
		return this.field.isBaseSorted(baseIndex);
	}
	
	/**
	 * Move the player one of baseSrc to the hole
	 * @param base : index of the base you want to pick a player
	 * @throws InvalidMoveException in case baseSrc is not near the hole
	 */
	public void movePlayerOne(int baseSrc) throws InvalidMoveException
	{
		this.field.move(baseSrc, 1);
	}

	/**
	 * Move the player two of baseSrc to the hole
	 * @param base : index of the base you want to pick a player
	 * @throws InvalidMoveException in case baseSrc is not near the hole
	 */
	public void movePlayerTwo(int baseSrc) throws InvalidMoveException
	{
		this.field.move(baseSrc, 2);
	}
	
	/** 
	 * Reset the state of the current world to the one passed in argument
	 * @param the world which must be the new start of your current world
	 */
	public void reset(World world)
	{
		BaseballWorld other = (BaseballWorld) world;
		this.field = other.field.copy();
		super.reset(world);		
	}

	/**
	 * Return a string representation of the world
	 * @return A string representation of the world
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("BaseballWorld "+getName()+": ");
		sb.append(this.field.toString());
		return sb.toString();
	}
	
}
