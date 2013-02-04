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
	protected BaseballField field;
	
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
	@Override
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
	 * Give the index of the hole
	 * @return the index of the hole
	 */
	public int findMissingPlayer() {
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
	@Override
	public String getBindings(ProgrammingLanguage lang)
	{
		throw new RuntimeException("No binding of BaseballWorld for "+lang);
	}
	
	/**
	 * Give the index of the base with only one player
	 * @return the index of the base with only one player
	 */
	public int getHoleContainer()
	{
		return this.field.getHoleContainer();
	}
	
	/** 
	 * Return a component able of displaying the world
	 * @return a component able of displaying the world
	 */
	@Override
	public WorldView[] getView()
	{
		return new WorldView[] { new BaseballWorldView(this) } ;
	}

	/**
	 * Tell if everyone is home
	 * @return TRUE if the field is okay <br>FALSE else
	 */
	public boolean isSorted()
	{
		return this.field.isSorted();
	}
	
	/**
	 * Move a player to the hole
	 * @param base : index of the base you want to pick a player
	 * @param player : index (1 or 2) of the player in the base you want to move
	 */
	public void move(int base, int player)
	{
		this.field.move(base,player);
	}
	
	/** 
	 * Reset the state of the current world to the one passed in argument
	 * @param the world which must be the new start of your current world
	 */
	@Override
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
